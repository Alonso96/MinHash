package minhash;
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
	public static TreeMap<Long, Integer> hash (ArrayList<String> kmer) throws UnsupportedEncodingException {
		 //calcola complemento inverso	
		//Long min = Long.MAX_VALUE;
		//System.out.println(kmer.size());
		
		TreeMap<Long,Integer> occurenceOfHash = new TreeMap<Long,Integer>();
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
		 
		    long  hash = MurmurHash3.hash64(canonicalKmer.getBytes("UTF-8")) ;
		//    System.out.println(hash);
		    if (hash < 0) 
		    	hash+= Math.pow(2, 64);
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
   public static HashMap<Long, Integer> getMinHash(TreeMap<Long, Integer> hashedKmers, int s) //s corrisponde alla grandezza del mio sketch
    {  
	  int i =0;
	
	   HashMap<Long, Integer> minHash = new HashMap<Long,Integer>();

	   for (long entry : hashedKmers.keySet()) {
    	  if(i==s) break; 
    	  else {
    		  if(hashedKmers.get(entry)<0) continue; //accept all k-mers
    		  else {
    		//	  System.out.println(entry);
    			  
    			  minHash.put(entry,hashedKmers.get(entry));
    		
    			  i++;
    		  }
    	  }
      }  
	   

           
       
	   	System.out.println("Minhash size" +minHash.size());
        return  minHash;
      
    }
    

    static private double jaccardSimilarity(HashMap<Long, Integer>  s1, HashMap<Long, Integer> s2, int s ) {
    	int n =0;
    	Set<Long> union = new HashSet<Long>(s1.keySet());
    	Set<Long> intersection = new HashSet<Long>(s1.keySet());
    	union.addAll(s2.keySet());
    	intersection.retainAll(s2.keySet());
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
        n= intersection.size() / union.size();
    	// 2.f*  intersection.size()/union.size();
        return  (float) (Math.ceil(n * Math.pow(10, 2)) / Math.pow(10, 2));
         
     
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
	
    
    static private double jaccardDistance(HashMap<Long,Integer> set1, HashMap<Long,Integer> set2, int s) {
    	//double jaccard = jaccardSimilarity(set1, set2, s);
    	return 1.0 - jaccardSimilarity(set1,set2, s);
    //	return -Math.log(2 * jaccard / (1. + jaccard)) / 21;
    	
        
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
    	    //System.out.println("kmers size: "+kmers.size());
    	    //System.out.println("Genome lenght: "+ genome.toString().length());
    	  allKmers.addAll(buildKmer(genome.toString(), kSize));
    	  
    	  //System.out.println("AllKmer size: "+allKmers.size());
    	  //System.out.println(nLine);
    	  return allKmers;
    			
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
    
    
	public static void main(String[] args) throws IOException{
		if (args.length != 2) {
			System.err.println("Required at least 2 argument");
			return;
		}
		HashMap<Long,Integer> set1 = new  HashMap <Long,Integer>();
		HashMap<Long,Integer> set2 = new  HashMap <Long,Integer>();
		TreeMap<Long,Integer> hashedKmers1 = new TreeMap<Long,Integer>();
		TreeMap<Long,Integer> hashedKmers2 = new TreeMap<Long,Integer>();
		
		int identicalHash =0;
		ArrayList<String> seq1 = readKmerFromFile(args[0],21); //path, k 
		ArrayList<String> seq2 = readKmerFromFile(args[1],21); //path, k
		hashedKmers1 = hash(seq1);
		hashedKmers2= hash(seq2);
		set1 = getMinHash(hashedKmers1, 1000); //costruisce sketch di minHash grande 1000
		set2 = getMinHash(hashedKmers2, 1000);
		

		System.out.println("Indice di Jaccard: "+jaccardSimilarity(set1, set2, 1000));
		System.out.println("Distanza di Jaccard: "+jaccardDistance(set1, set2,1000));
		
	//	getHistogram(seq1,"/home/alfonso/Desktop/genome1.txt");
	//	getHistogram(seq2,"/home/alfonso/Desktop/genome2.txt");
	
	}
}