package minhash;
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.Collections;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.codec.digest.MurmurHash3;



public class MinHash<T>{

	public static ArrayList<String> buildKmer(String sequence,int  k) throws IOException{
		//String genoma = new String(Files.readAllBytes(Paths.get(path)));
		String kmer;
	
		ArrayList<String> kmers = new ArrayList<String>();
		int n_kmers = sequence.length() - k + 1;
	//	System.out.println("n k-meri" + n_kmers);
	//	System.out.println("l sequenza: "+ sequence.length());
		for (int i=0;i<n_kmers;i++) {
			kmer = sequence.substring(i,i+k);
			
			kmers.add(kmer);
		}
		
		return kmers;
	}
	public static HashMap<Integer, Integer > hash (ArrayList<String> kmer) throws UnsupportedEncodingException {
		 //calcola complemento inverso	
		//Long min = Long.MAX_VALUE;
		//System.out.println(kmer.size());
		
		HashMap<Integer,Integer> occurenceOfHash = new HashMap<Integer,Integer>();
		int lex1 =0;
		int lex2=0;
	//	ArrayList<Long> sketch = new  ArrayList<Long>();
		for(String s : kmer) {
		//	System.out.println(s);
		String canonicalKmer;
		String rcKmer = makeComplement(s);
		lex1 = s.compareTo(rcKmer);
		lex2= rcKmer.compareTo(s);
		    //determina il minore tra il kmero originale e il suo reverse complement
		    if (lex1< lex2)
		    	canonicalKmer = s;
		    else 
		    	canonicalKmer = rcKmer;

		     //calculate murmurhash using a hash seed of 42
		 
		    int  hash = canonicalKmer.hashCode();
		//    System.out.println(hash);
		   if (hash < 0) 
			hash+= Math.pow(2, 32);
		    incrementValue(occurenceOfHash,hash);
		    
		    
		   /* if(hash<=min) {
		    	
		    	min = hash;
		    	minHashes.add(min);
           // System.out.println("aggiunto min "+ min);
		    }
		   */
		}
		//	System.out.println(minHashes.size());
		//System.out.println(occurenceOfHash);
			return occurenceOfHash;

	}
	public static<K> void incrementValue(Map<K,Integer> map, K key)
	{
		
		// containsKey() checks if this map contains a mapping for a key
				Integer count = map.containsKey(key) ? map.get(key) : 0;
				map.put(key, count + 1);
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
   public static HashMap <Integer,Integer> getMinHash(HashMap<Integer, Integer> hashedKmers1, int s) //s corrisponde alla grandezza del mio sketch
    {  
	   int i =0;
	   HashMap<Integer, Integer> minHash = new HashMap<Integer,Integer>();
	   
	/*   long min = Long.MAX_VALUE;
	   //  Collections.sort(hashedKmers); // ordino gli sketch con hash dal più piccolo al più grande

	   ArrayList<Long> valuesList = new ArrayList<Long>(hashedKmers.keySet());
	   
	while(i<s) {
		   int randomIndex = new Random().nextInt(valuesList.size());
		   Long randomValue = valuesList.get( randomIndex);
		   min = randomValue;
		   if(randomValue<min) { // se il valore è minore del max sketch attuale;
			   
			   if(minHash.containsKey(randomValue)) i--;
			   else
				   	minHash.put(randomValue, 1);
			   //min = randomValue;
			   i++;
		   } 
			   
	   }
	   */
	   

	   System.out.println("sketch size:" + hashedKmers1.size());
	
	  /* int scaled =1000;
	   Long max = (long) 0;
	   Long MAX_HASH = (long) Math.pow(2, 64);
	   Long keep_below = MAX_HASH / scaled;
	   ArrayList<Long> sketches = new ArrayList<Long>(hashedKmers);
      ArrayList<Long> minHashes = new ArrayList<Long>();
        Long min = Long.MAX_VALUE;
      */
  //  Collections.sort(hashedKmers); // ordino gli sketch con hash dal più piccolo al più grande
	 TreeMap<Integer , Integer> sorted = new TreeMap<>(); 
	  
      // Copy all data from hashMap into TreeMap 
      sorted.putAll(hashedKmers1); 
    
      // Display the TreeMap which is naturally sorted 
      for (Map.Entry<Integer, Integer> entry : sorted.entrySet()) {
    	  if(i==s) break; 
    	  else {
    		  if(entry.getValue()>1) continue;
    		  else {
    		  minHash.put(entry.getKey(),entry.getValue());
    		 System.out.println(entry.getKey()+ " " +entry.getValue());
    		  i++;
    		  }
    	  }
      }  

    
        System.out.println("Minhash size" +minHash.size());
        return  minHash;
      
    }
    

    static private double jaccardSimilarity(HashMap<Integer,Integer>  set1, HashMap<Integer,Integer> set2, int s ) {
    	int identicalMinHashes =0;
    	
    	
    	int uniqueHash=0;;
    	int i =0;
    	Set<Integer> s1= new HashSet<Integer>(set1.keySet());
    	Set<Integer> s2 = new HashSet<Integer>(set2.keySet());
    	Set<Integer> union = new HashSet<Integer>(s1);
    	Set<Integer> intersection = new HashSet<Integer>(s1);
    	union.addAll(s2);
    	intersection.retainAll(s2);
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
        
        System.out.println(intersection.size()+"/1000");
    	 return ( double) intersection.size()/union.size();
         
     
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
	
    
    static private double jaccardDistance(HashMap<Integer,Integer> set1, HashMap<Integer,Integer> set2, int s) {
    	return 1.0 - jaccardSimilarity(set1,set2, s);
    	//return  (double )-1/21*(Math.log(2*jaccardSimilarity(set1, set2, s)/1+jaccardSimilarity(set1, set2, s)));

        
    }
    public static ArrayList<String> readKmerFromFile(String path,int kSize) throws IOException{
    	ArrayList<String> allKmers = new ArrayList<String>();
    	ArrayList<String> kmers = new ArrayList<String>();
    	StringBuilder genome = new StringBuilder();
    	File file = new File(path); 
    	int nLine=0;
   
    		
    	
    	BufferedReader br = new BufferedReader(new FileReader(file)); 
    	String st;
    	
    
    	  while ((st = br.readLine()) != null) { //Successivamente inserire controllo per saltare le righe che non inziano per alfabeto ACGT
    		  if(st.contains(">")) continue; // se è la prima riga salta
    		  genome.append(st);
    	  }
    	  kmers= buildKmer(genome.toString(), kSize);
    	    System.out.println("kmers size: "+kmers.size());
    	    System.out.println("Genome lenght: "+ genome.toString().length());
    	  allKmers.addAll(buildKmer(genome.toString(), kSize));
    	  
    	  System.out.println("AllKmer size: "+allKmers.size());
    	  System.out.println(nLine);
    	  return allKmers;
    			
    }

	public static void main(String[] args) throws IOException{
		HashMap<Integer,Integer> set1 = new  HashMap <Integer,Integer>();
		HashMap<Integer,Integer> set2 = new  HashMap <Integer,Integer>();
		HashMap<Integer,Integer> hashedKmers1 = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> hashedKmers2 = new HashMap<Integer,Integer>();
		int identicalHash =0;
		ArrayList<String> seq1 = readKmerFromFile("/home/alfonso/Downloads/genome1.fna",21); //path, k 
		ArrayList<String> seq2 = readKmerFromFile("/home/alfonso/Downloads/genome2.fna",21); //path, k
		hashedKmers1 = hash(seq1);
		hashedKmers2= hash(seq2);
		set1 = getMinHash(hashedKmers1, 1000); //costruisce sketch di minHash grande 1000
		set2 = getMinHash(hashedKmers2, 1000);

		System.out.println("Indice di Jaccard: "+jaccardSimilarity(set1, set2, 1000));
	
		System.out.println("Distanza di Jaccard: "+jaccardDistance(set1, set2,1000));
	}
}