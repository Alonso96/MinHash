package minhash;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

//import org.apache.commons.codec.digest.MurmurHash3;



public class MinHash<T>{

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
		int i=0;
		char [] kmer = new char[k];
		char [] s1 = new char[sequence.length()+lastChar.length];
		for(i=0; i<sequence.length();i++) {//copio tutti i caratteri della sequenza in un char array
			if(sequence.charAt(i)=='N' || sequence.charAt(i)=='n') // se è una sequenza non codificante salta il carattere
				i--;
			else
				s1[i]=sequence.charAt(i);
		}
		cont =i;
		for(i =0; i<lastChar.length;i++) {
			
			s1[i+cont] = lastChar[i];
			
		}
		int n_kmers =s1.length - k +1 ;
        for (i=0;i<n_kmers;i++) {
			for(int j=0;j<k;j++) {
				kmer[j]= s1[j+i];
			}
		
			incrementValue(seq1,hash(kmer));
			
			}
		}


	
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


		   		long[]  hash = MurmurHash3.hash128(canonicalKmer.getBytes(StandardCharsets.UTF_8),0,canonicalKmer.getBytes(StandardCharsets.UTF_8).length,42) ;
		
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
   public static ArrayList<Long> getMinHash(ArrayList<Long>hashedKmers, int s) //s corrisponde alla grandezza del mio sketch
    {  
	  int i =0;
	  Long [] minH = new Long[hashedKmers.size()];
	  for(int j=0;j<hashedKmers.size();j++)
		  minH[j]=hashedKmers.get(j);
	  
	  hashedKmers = null;
	  
	  RadixSort rs = new RadixSort();
	  rs.sort(minH);
//	  Collections.sort(hashedKmers);
	   ArrayList<Long> minHash = new ArrayList<Long>();

	   for (Long entry :minH) {
    	  if(i==s) break; 
    	  else {
    		  	if(minHash.contains(entry)) {
    		  		System.out.println("Collisione con valore:" + entry); 
    		  		continue;
    		  	}
    		  	else {
    		  		minHash.add(entry);
    		  		//System.out.println(entry);
    		  		i++;
    		  	}

    		
    		}
    	  }
        
	   

           
        minH = null;
	   	System.out.println("Minhash size" +minHash.size());
        return  minHash;
      
    }
    

    static double jaccardSimilarity(ArrayList<Long>  set1, ArrayList<Long> set2, int s ) {
    	int n =0;
    	int maxS =0;
    	if (set1.size()>=set2.size())
    			maxS=set1.size();
    	else maxS=set2.size();
    	
    	Set<Long> union = new HashSet<Long>(set1);
    	Set<Long> intersection = new HashSet<Long>(set1);
    	union.addAll(set2);
    	intersection.retainAll(set2);
    /*
    	//union.addAll(intersection);
    	int a = set1.size();
    	int b = set2.size();
 
    	System.out.println("Cardinalità di set1: " +set1.size());
 
    	System.out.println("Cardinalità di set2: "+  set2.size());
    	if (a == 0 || b== 0) return 0;
    	
    	else {
    	
    	for(int k : set1.keySet() ) {
    		
   
    		if (set2.containsKey(k)) identicalMinHashes++;
    		
    	}
    	
    
    	
    //	System.out.println(set1.entrySet());
    //	System.out.println(set2.entrySet());
       */
        
        System.out.println(intersection.size()+"/"+ maxS);
        n= intersection.size() / union.size();
    	// 2.f*  intersection.size()/union.size();
        return  (double) intersection.size() / union.size();
         
     
    }

    	/*if (a.isEmpty() && b.isEmpty()) {
			return 1.0f;
		}
		if (a.isEmpty() || b.isEmpty()) {
			return 0.0f;
		}
    	final long intersection = set1.retainAll(set2)
        final int sa = a.size();
        final int sb = a..size();
        System.out.println("Insieme1 " + a.size());
        System.out.println("Insieme2 " + b.size());
     //   set1.retainAll(set2);
   //     final int intersection = set1.size();
        return 1d / (sa + sb - intersection) * intersection;
       // inter
    /*	if (set1.isEmpty() && set2.isEmpty()) {
			return 1.0f;
		}
		if (set1.isEmpty() || set2.isEmpty()) {
			return 0.0f;
		}
		
		final int intersection = intersection(set1, set2).size();
		
*/
		// ∣a ∩ b∣ / ∣a ∪ b∣
		// Implementation note: The size of the union of two sets is equal to
		// the size of both sets minus the duplicate elements.
		//return intersection / (float) (a.size() + b.size() - intersection);
	
    static double pValue(int sharedKmers,int sketchSize,int maxHash,int hashBit) {
    	double r=0;
    	return r;
    }
    
    static double jaccardDistance(ArrayList<Long>set1, ArrayList<Long> set2, int s) {
    	double jaccard = jaccardSimilarity(set1, set2, s);
  //  	return 1.0 - jaccardSimilarity(set1,set2, s);
    	if(jaccard ==1) return 0;
    	else if(jaccard==0) return 1;
    	else
  //  	return 1 - jaccard;
    		return -Math.log(2 * jaccard / (1. + jaccard)) / 21;
    	
    	
        
    }
    public static void readKmerFromFile(String path,int kSize, Map<Long,Integer> seq1) throws IOException{
    
    	char [] lastChars = new char [kSize-1] ;
 
    		String line = "";
    
    		try {
    			int cont =0;
    		BufferedReader br= new BufferedReader(new FileReader(path));
    		
    		while ((line = br.readLine())!= null) { //Successivamente inserire controllo per saltare le righe che non inziano per alfabeto ACGT
        		
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
        			 buildKmer(lastChars,line.toUpperCase(), kSize,seq1);
        			  
        			 if(line.length()<(kSize -1)) { //se la riga letta ha meno di k-1 caratteri, si verifica solo alla fine del genoma e questi caratteri non verranno mai considerati in quanto
        				 //nella prox iterazione uscirà dal ciclo perchè è arrivato a fine file
        				for(int i=0;i<line.length();i++) {
        					lastChars[i] = line.charAt(i);
        				}
        			 }
        			 else {
        				
        			 	for(int i=0;i<lastChars.length;i++) {
        			 		lastChars[i]= line.charAt(line.length()-(kSize-1)+i);
        			 	}
        			 }
        		//	 System.gc();
        			// System.out.println(String.valueOf(lastChars));
        		  }
        		
        		//  kM=null;
        		  //line="";
        		  
        		  
    		
    	}
    		br.close();
    		}catch(Exception ex) {
    			ex.printStackTrace();
    			
    		}
    	
    	//	is.close();
    		
    	//	br.close();
    		
    	//  return allKmers;
    			
    }
    public static void getHistogram(ArrayList<String> kmers,String path) throws IOException{
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
    
    
	

}