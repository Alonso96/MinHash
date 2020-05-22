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
	public static void  buildKmer(String lastChar,String sequence,int  k, Map<String,Integer> seq1) throws IOException{
		//String genoma = new String(Files.readAllBytes(Paths.get(path)));
		
		//String kmer =null;
		//StringBuilder st = new StringBuilder();
		ArrayList<String> kmers = new ArrayList<String>();
		//StringBuilder s1 = new StringBuilder();

		String s1 = lastChar+sequence;
	//	char [] km = new char [k];
		StringBuilder sb = new StringBuilder();
	//	s1.append(lastChar);
	//	s1.append(sequence);
		//int n_kmers = sequence.length() - k + 1;
		int n_kmers =s1.length() - k +1 ;
	//	System.out.println("n k-meri" + n_kmers);
	//	System.out.println("l sequenza: "+ sequence.length());
		for (int i=0;i<n_kmers;i++) {
				
			for(int j=0;j<k;j++) {
				sb.append(s1.charAt(j+i));
			}
			//kmer= s1.substring(i,i+k);
		
			if(String.valueOf(sb.toString()).contains("N")) continue;
			else {
				incrementValue(seq1,sb.toString());
				sb.setLength(0);
				//seq1.add(String.valueOf(km));
			//	System.out.println(kmer.length());
			}
		}
		//AL POSTO DI METTERE IN UN ARRAYLIST PERCHE' NON GLI PASSO L'HASHMAP E METTO TUTTO DENTRO ??
	//	return kmers;
	}
	/*
	public static String getMd5(String input) 
    { 
        try { 
  
            // Static getInstance method is called with hashing MD5 
            MessageDigest md = MessageDigest.getInstance("MD5"); 
  
            // digest() method is called to calculate message digest 
            //  of an input digest() return array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
            return hashtext; 
        }  
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            throw new RuntimeException(e); 
        } 
    } 
 public static long hash64(String string) {
	  long h = 1125899906842597L; // prime
	  int len = string.length();

	  for (int i = 0; i < len; i++) {
	    h = 31*h + string.charAt(i);
	  }
	  return h;
	}
	 */
	
	public static ArrayList<Long> hash (Map<String, Integer> seq1) throws UnsupportedEncodingException {
		 //calcola complemento inverso	
		//Long min = Long.MAX_VALUE;
		//System.out.println(kmer.size());
		 // prime
		ArrayList<Long> kmerHashed = new ArrayList<Long>();
		
	//	ArrayList<Long> sketch = new  ArrayList<Long>();
		for(String s : seq1.keySet()) {
			if(seq1.get(s)>4) {		//	System.out.println(s);
				String canonicalKmer;
				String rcKmer = makeComplement(s);
	//	lex1 = s.compareTo(rcKmer);
	//	lex2= rcKmer.compareTo(s);
		    //determina il minore tra il kmero originale e il suo reverse complement
				if (s.compareTo(rcKmer) <=0)
					canonicalKmer = s;
				else 
					canonicalKmer = rcKmer;


				//seq + 16, length - 16, seed, data + 4
		/*		  Long h = 1125899906842597L; //prime
				  int len = canonicalKmer.length();
				  for (int i = 0; i < len; i++) {
				    h = 31*h + canonicalKmer.charAt(i);
				  }
				  */
				//hash64(canonicalKmer);//
		   		long[]  hash = MurmurHash3.hash128(canonicalKmer.getBytes(StandardCharsets.UTF_8),0,canonicalKmer.getBytes(StandardCharsets.UTF_8).length,42) ;
		//    System.out.println(hash);
		   	
		    	
		    
		 //  		kmerHashed.add(hash);
		  
		   	//	else
		   		kmerHashed.add(hash[0]);
		  
		   	}
		   	else continue;
		  
		   
		} //fine for 
		//	System.out.println(minHashes.size());
		//System.out.println(occurenceOfHash);
			return kmerHashed;

	}
	
	/*public static ArrayList<String> hash (HashMap<String, Integer> seq) throws UnsupportedEncodingException {
		 //calcola complemento inverso	
		//Long min = Long.MAX_VALUE;
		//System.out.println(kmer.size());
		
		ArrayList<String> kmerHashed = new ArrayList<String>();
		int i=0;
		int lex1 =0;
		int lex2=0;
	//	ArrayList<Long> sketch = new  ArrayList<Long>();
		for(String s : seq.keySet()) {
			if(seq.get(s)>4) {
		//	System.out.println(s);
				String canonicalKmer;
				String rcKmer = makeComplement(s);
	//	lex1 = s.compareTo(rcKmer);
	//	lex2= rcKmer.compareTo(s);
		    //determina il minore tra il kmero originale e il suo reverse complement
				if (s.compareTo(rcKmer) <=0)
					canonicalKmer = s;
				else 
					canonicalKmer = rcKmer;


		     
		   	
		   		String hash =  getMd5(canonicalKmer);//MurmurHash3.hash64(canonicalKmer.getBytes(StandardCharsets.UTF_8)) ;
		//    System.out.println(hash);
		   // if (hash < 0) 
		    	
		    
		   		kmerHashed.add(hash);
		  // i++;
		   	}
		   	else continue;
		   /* if(hash<=min) {
		    	
		    	min = hash;
		    	minHashes.add(min);
          // System.out.println("aggiunto min "+ min);
		    }
		   
		} //fine for 
		//	System.out.println(minHashes.size());
		//System.out.println(occurenceOfHash);
			return kmerHashed;

	}
	*/
	public static<K> void incrementValue(Map<K,Integer> map, K key)
	{
		
		// containsKey() checks if this map contains a mapping for a key
				int count = map.containsKey(key) ? map.get(key) : 0;
				map.merge(key, 1, Integer::sum);
			}
	
    public static String makeComplement(String dna) {
         StringBuilder builder = new StringBuilder();

         for(int i=0;i<dna.length();i++){
              char c = dna.charAt(i);
              if(dna.charAt(i) == 'T'){
                  builder.append('A');
              }
              if(dna.charAt(i) == 'A'){
                  builder.append('T');
              }
              if(dna.charAt(i) == 'C'){
                  builder.append('G');
              }
              if(dna.charAt(i) == 'G'){
                  builder.append('C');
              }   
         }
         return builder.toString();
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
    public static void readKmerFromFile(String path,int kSize, Map<String,Integer> seq1) throws IOException{
    //	char [] alphabet = "ACGT".toCharArray();
    	//System.out.println(alphabet);
    	
  
    	String lastChars = "" ;
   // 	HashMap<String,Integer> allKmers = new HashMap<String,Integer>();
    //	ArrayList<String>  kM = null ;
    
    	
    	//File file = new File(path); 
    	
    	//int c = 0;             

	//	byte[] buf = new byte[1024];
    //	FileInputStream is = new FileInputStream(path);
    

    		//int i =0;
    		//boolean  first = true;
    		//
    //	String chunk = new String(buf);
    		String line = "";
    		
    	//	Scanner scan = new Scanner(chunk);
    		//Reader inputString = new StringReader(chunk);
    		try {
    		BufferedReader br= new BufferedReader(new FileReader(path));
    		while ((line =  br.readLine()) != null) { //Successivamente inserire controllo per saltare le righe che non inziano per alfabeto ACGT
        		  if(line.contains(">")) { // se è la riga descrittiva del genoma salta
        			//  br.readLine();
        			  lastChars = ""; // se è una nuova sequenza butto gli ultimi k-1 caratteri letti 
        			  continue;
        			  } 
        		  else {
        			 buildKmer(lastChars,line.toUpperCase(), kSize,seq1);
        			  /*for(String entry : kM) {
        			   
                			
            				incrementValue(allKmers,entry);
              		  }
        			  */
        		//	  genome.append(Character.toUpperCase(character));
        			 if(line.length()<(kSize -1)) //se la riga letta ha meno di k-1 caratteri, si verifica solo alla fine del genoma e questi caratteri non verranno mai considerati in quanto
        				 //nella prox iterazione uscirà dal ciclo perchè è arrivato a fine file
        				 lastChars = line;
        			 else {
        				
        			 	lastChars =  line.substring(line.length()-(kSize-1));
        			 }
        		//	 System.gc();
        		  }
        		
        		//  kM=null;
        		  line="";
        		  
        		  
    		
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