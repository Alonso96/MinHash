package minhash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

//import org.apache.commons.codec.digest.MurmurHash3;



public class MinHash<T>{
/*
	public static ArrayList<String> buildKmer(String sequence,int  k) throws IOException{
		//String genoma = new String(Files.readAllBytes(Paths.get(path)));
		String kmer=null;
		
	
		ArrayList<String> kmers = new ArrayList<String>();
		int n_kmers = sequence.length() - k ;
	//	System.out.println("n k-meri" + n_kmers);
	//	System.out.println("l sequenza: "+ sequence.length());
		for (int i=0;i<n_kmers;i++) {
			kmer = sequence.substring(i,i+k);
			
		kmers.add(kmer);
		}
		
		return kmers;
	}
	public static void  buildKmer(char []lastChar,String sequence,int  k, Map<Long,Integer> seq1) throws IOException{
		//String genoma = new String(Files.readAllBytes(Paths.get(path)));
		int cont =0;
		int nFalse=0;
		int i=0;
		int sequenceLength =0;
		char [] kmer = new char[k];
		char [] s1 = new char[sequence.length()+lastChar.length];
		for(i=0; i<sequence.length();i++) {//copio tutti i caratteri della sequenza in un char array
			if(sequence.charAt(i)=='N' || sequence.charAt(i)=='n') { // se è una sequenza non codificante salta il carattere
				nFalse++;
				continue;
			}
			else
				s1[i-nFalse]=Character.toUpperCase(sequence.charAt(i));
			cont++;
		}
		//cont =i;
		for(i =0; i<lastChar.length;i++) {
			if(lastChar[i]==0) break;
			
			s1[i+cont] = Character.toUpperCase(lastChar[i]);
			
		}
		for(sequenceLength=0;sequenceLength<s1.length;sequenceLength++)
			if(s1[sequenceLength]==0) break;
		
		int n_kmers =sequenceLength - k +1 ;
        for (i=0;i<n_kmers;i++) {
			for(int j=0;j<k;j++) {
				kmer[j]= s1[j+i];
			}
		
			incrementValue(seq1,hash(kmer));
			
			}
		}

*/
	
	public static Long hash (char [] kmer) throws UnsupportedEncodingException {
		
				String canonicalKmer;
				String rcKmer =String.valueOf(makeComplement(kmer));
	//	lex1 = s.compareTo(rcKmer);
	//	lex2= rcKmer.compareTo(s);
		    //determina il minore tra il kmero originale e il suo reverse complement
				if (String.valueOf(kmer).compareTo(rcKmer) >0)
					canonicalKmer = rcKmer ;
				else 
					canonicalKmer = String.valueOf(kmer);

				
		   		long [] hash = MurmurHash3.hash128(canonicalKmer.getBytes(StandardCharsets.UTF_8),0,canonicalKmer.getBytes(StandardCharsets.UTF_8).length,42) ;
		   		
		   		if(hash[0]<0)
		   			hash[0]+=Math.pow(2, 64);
		   
		//   	System.out.println(hash[0]);
			return hash[0];

	}
	
	public static<K> void incrementValue(Map<K,Integer> map, K key)
	{
		
		// containsKey() checks if this map contains a mapping for a key
				int count = map.containsKey(key) ? map.get(key) : 0;
				map.merge(key, 1, Integer::sum);
			}
	
    public static char [] makeComplement(char [] dna) {
         char [] revKmer = new char [dna.length];
// partire dalla fine verso inizio
         for(int i=0;i<dna.length;i++){
              
              if(dna[i] == 'T'){
                  revKmer[revKmer.length-i-1]='A';
              }
              if(dna[i] == 'A'){
                  revKmer[revKmer.length-i-1]='T';
              }
              if(dna[i] == 'C'){
                  revKmer[revKmer.length-i-1]='G';
              }
              if(dna[i] == 'G'){
                  revKmer[revKmer.length-i-1]='C';
              }
         }
         return revKmer;
    }
    
 /*
   public static ArrayList<Long> getMinHash(Map<Long,Integer>hashedKmers, int s) //s corrisponde alla grandezza del mio sketch
    {  
	   int i =0;
	   ArrayList<Long> minHash = new ArrayList<Long>();
	   for (Long entry :hashedKmers.keySet()) {
    	  if(i==s) break; 
    	  else {
    		  	if(hashedKmers.get(entry)<3) {
    		 // 		System.out.println("Collisione con valore:" + entry); 
    		  		continue;
    		  	}
    		  	else {
    		  		if (minHash.contains(entry)) continue;
    		  		else {
    		  			minHash.add(entry);
    		  		//	System.out.println(entry);
    		  			i++;
    		  		}
    		  	}

    		
    		}
    	  }
        


           
     //   minH = null;
	   	System.out.println("Minhash size" +minHash.size());
        return  minHash;
      
    }
  */

    static double jaccardSimilarity(TreeSet<Long>  seq1, TreeSet<Long> seq2, int s ) {
    	int n =0;
    	int maxS =0;
    	if (seq1.size()>=seq2.size())
    			maxS=seq1.size();
    	else maxS=seq2.size();
    	
    	Set<Long> union = new HashSet<Long>(seq1);
    	Set<Long> intersection = new HashSet<Long>(seq1);
    	union.addAll(seq2);
    	intersection.retainAll(seq2);
   
        System.out.println(intersection.size()+"/"+ maxS);
        n= intersection.size() / union.size();
    	// 2.f*  intersection.size()/union.size();
        return  (double) intersection.size() / union.size();
         
     
    }


    static double jaccardDistance(TreeSet<Long>set1, TreeSet<Long> set2, int s) {
    	double jaccard = jaccardSimilarity(set1, set2, s);
  //  	return 1.0 - jaccardSimilarity(set1,set2, s);
    	if(jaccard ==1) return 0;
    	else if(jaccard==0) return 1;
    	else
  //  	return 1 - jaccard;
    		return -Math.log(2 * jaccard / (1. + jaccard)) / 21;
    	
    	
        
    }
    public static void makeSketchFromFile(String path,int kSize, TreeSet<Long> seq1,int s) throws IOException{
    
    	char [] lastChars = new char [kSize-1] ;
    	//char []
    	int nFalse=0;
		int i=0;
		int sketchSize=0;
		int sequenceLength =0;
		char [] kmer = new char[kSize];
		int kmerCount=0;
		Long hash =0L;
 
    		String line = "";
    
    		try {
    			int cont =0;
    			
    		BufferedReader br= new BufferedReader(new FileReader(path));
    		
    		while ((line = br.readLine())!= null) { //Successivamente inserire controllo per saltare le righe che non inziano per alfabeto ACGT
        	//	cont++;
    		//	System.out.println(cont);
    			if(line.trim().isEmpty()) { // se è la riga descrittiva del genoma salta
        			//  br.readLine();
    				 System.out.println("fine file");
    				 continue;
        			  
        			  }
        		  else if(line.charAt(0)=='>') {
        			  lastChars =  new char[kSize-1];// se è una nuova sequenza butto gli ultimi k-1 caratteri letti 
        			  continue;
        		  }
        		  else {
        				 cont =0;
        				 nFalse =0;
        				 i=0;
        				char [] s1 = new char[line.length()+lastChars.length];
        				for(i=0; i<line.length();i++) {//copio tutti i caratteri della sequenza in un char array
        					if(line.charAt(i)=='N' || line.charAt(i)=='n') { // se è una sequenza non codificante salta il carattere
        						nFalse++;
        						continue;
        					}
        					else
        						s1[i-nFalse]=Character.toUpperCase(line.charAt(i));
        					cont++;
        				}
        				//cont =i;
        				for(i =0; i<lastChars.length;i++) {
        					if(lastChars[i]==0) break;
        					
        					s1[i+cont] = Character.toUpperCase(lastChars[i]);
        					
        				}
        				for(sequenceLength=0;sequenceLength<s1.length;sequenceLength++)
        					if(s1[sequenceLength]==0) break;
        				
        				int n_kmers =sequenceLength - kSize +1 ;
        		        for (i=0;i<n_kmers;i++) {
        					for(int j=0;j<kSize;j++) {
        						kmer[j]= s1[j+i];
        					}
        				
        					//incrementValue(seq1,hash(kmer));
        					//System.out.println(String.valueOf(kmer));
        					hash = hash(kmer);
        					//System.out.println(hash);
        					kmerCount ++;
        					if(sketchSize == s) {
        						if(hash<seq1.last() && !( seq1.contains(hash))) {
        							  seq1.remove(seq1.last());
        							  seq1.add(hash);
        						 }
        					}else if(sketchSize <s) {
        						if(seq1.contains(hash)) continue;
        						else {
        							seq1.add(hash);
        							sketchSize++;
        						}
        					}
        					
        					}
        					
        			 
        			 if(line.length()<(kSize -1)) { //se la riga letta ha meno di k-1 caratteri, si verifica solo alla fine del genoma e questi caratteri non verranno mai considerati in quanto
        				 //nella prox iterazione uscirà dal ciclo perchè è arrivato a fine file
        				for(i=0;i<line.length();i++) {
        					lastChars[i] = Character.toUpperCase(line.charAt(i));
        				}
        			 }
        			 else {
        				
        			 	for( i=0;i<lastChars.length;i++) {
        			 		lastChars[i]= Character.toUpperCase(line.charAt(line.length()-(kSize-1)+i));
        			 	}
        			 }
        
        		  }
        		
        
        		  
    		
    	}
    		System.out.println("kmercount: "+ kmerCount);
    		System.out.println("sketch size :" + seq1.size());
    		for(Long e : seq1) {
    			System.out.println(e);
    		}
    		br.close();
    		}catch(Exception ex) {
    			ex.printStackTrace();
    			
    		}
    	
    	//	is.close();
    		
    	//	br.close();
    		
    	//  return allKmers;
    			
    }
  /*  public static void getHistogram(ArrayList<String> kmers,String path) throws IOException{
    	HashMap<String,Integer> histogram = new HashMap<String,Integer>();
    	File fout = new File(path);
		FileOutputStream fos = new FileOutputStream(fout);
	 
		OutputStreamWriter osw = new OutputStreamWriter(fos);
    	for(String kmer : kmers) {
    		incrementValue(histogram, kmer);
    	}
    	System.out.println("Writing to file...");
    	for(Map.Entry<String,Integer> entry : histogram.entrySet()) {
    	
    				osw.write(entry.getKey() +" : " + entry.getValue() + "\n");
    				
    	}
    				osw.close();
    				System.out.println("Write complete");
    		
    	}
    
    */
	

}