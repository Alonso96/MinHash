package minhash;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Collections;
import java.util.Arrays;
import org.apache.commons.codec.digest.MurmurHash3;
public class MinHash{

	
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
	public static ArrayList<Long> hash (ArrayList<String> kmer) {
		 //calcola complemento inverso
		ArrayList<Long> minHashes = new ArrayList<Long>();
		for(String s : kmer) {
		//	System.out.println(s);
		String canonicalKmer;
		String rcKmer = makeComplement(s);
		Long min = Long.MAX_VALUE;
		int minl = 0;
		    //determina il minore tra il kmero originale e il suo reverse complement
		    if ((s.compareTo(rcKmer)) < 0)
		    	canonicalKmer = s;
		    else 
		    	canonicalKmer = rcKmer;

		     //calculate murmurhash using a hash seed of 42
		    Long hash = MurmurHash3.hash64(canonicalKmer.getBytes());
		    System.out.println(hash);
		    if (hash < 0) 
		    	hash = (long) ((long)hash + Math.pow(2, 64));
		    if(hash<= min) {
		    	min = hash;
		    	minHashes.add(min);
           // System.out.println("aggiunto min "+ min);
		    }
		}
		  
			return minHashes;

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
  /*  public static Set<Integer> sMinHash(ArrayList<Integer> sketch, int s) //s corrisponde alla grandezza del mio sketch
    {  
    	System.out.println(s);
       Set<Integer> minHashes = new HashSet<Integer>();
        int min = Integer.MAX_VALUE;
        int i =0;
        while(i<s) {
           if(sketch.get(i)<min) {
                    min = sketch.get(i);
                    minHashes.add(min);
                    System.out.println("aggiunto min "+ min);
                    i++;
            }
            
        }
        return (Set<Integer>) minHashes;
    }
    */
    /*	//ArrayList<Integer> temp = new ArrayList<Integer>(sketch);
    	Set<Integer> set = new HashSet<Integer>();
        // Sort the given array arr in reverse order 
        // This method doesn't work with primitive data 
        // types. So, instead of int, Integer type 
        // array will be used 
    	Collections.sort(sketch); 
    	for(Integer s1: sketch) {
    	System.out.println(s1);
    	}
    
  
        // prende i primi s elementi piccoli
        for (int i = 0; i < s; i++) {
        	set.add(sketch.get(s));
        	
        }
      //      set.add(sketch.get(i));
        return set;
    }
    */
    static private double jaccardSimilarity(Set<Long> set1, Set<Long> set2) {


        final int sa = set1.size();
        final int sb = set2.size();
        set1.retainAll(set2);
        final int intersection = set1.size();
        return 1d / (sa + sb - intersection) * intersection;
    }
    static private double jaccardDistance(Set<Long> set1, Set<Long> set2) {
    	return 1.0 - jaccardSimilarity(set1,set2);

        
    }
    public static ArrayList<String> readKmerFromFile(String path,int kSize) throws IOException{
    	ArrayList<String> allKmers = new ArrayList<String>();
    	ArrayList<String> kmers = new ArrayList<String>();
    	File file = new File(path); 
    	BufferedReader br = new BufferedReader(new FileReader(file)); 
    	String st; 
    	//br.readLine(); //salto la prima riga
    	  while ((st = br.readLine()) != null) {
    		  kmers = buildKmer(st, kSize);
    		  for(String s:kmers)
    			  allKmers.add(s);
    	  }
    		  
    	return allKmers;
    			
    }
	public static void main(String[] args) throws IOException{
		Set<Long> set1 = new HashSet<Long>();
		Set<Long> set2 = new HashSet<Long>();
		ArrayList<Long> sketch1 = new ArrayList<Long>();
		ArrayList<Long> sketch2 = new ArrayList<Long>();
		ArrayList<String> seq1 = readKmerFromFile("/home/alfonso/Downloads/Mash-master/test/genome1.fna",21); //path, k 
		ArrayList<String> seq2 = readKmerFromFile("/home/alfonso/Downloads/Mash-master/test/genome2.fna",21); //path, k
		sketch1 = hash(seq1);
		sketch2= hash(seq2);
			//System.out.println(hash(s));
	/*	}
		for (String s :seq2 ){
			sketch2.add(hash(s));
		}
		*/
		for (Long s: sketch1)
			set1.add(s);
		for (Long s: sketch2)
			set2.add(s);
		
	//	set1 = sMinHash(sketch1, sketch1.size());
		//set2 = sMinHash(sketch2, sketch2.size());
		
		System.out.println("Indice di Jaccard: "+jaccardSimilarity(set1, set2));
		System.out.println("Distanza di Jaccard: "+jaccardDistance(set1, set2));
	}
}