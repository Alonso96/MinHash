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
import org.apache.commons.text.similarity.*;


public class MinHash<T>{
	 private int numHashes_ = 0;
	    private int a[];
	    private int b[];
	    private int p[];
	    private Random random_;

	    public MinHash( int numHashes, int seed ) {
	        numHashes_ =  numHashes;
	        random_ =new Random();
	        a = new int[numHashes];
	        b = new int[numHashes];
	        p = new int[numHashes];
	        for(int i = 0; i < numHashes; ++i ) {
	            a[i] = random_.nextInt();
	            b[i] = random_.nextInt();
	            p[i] = random_.nextInt();
	        }
	    }
	
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
	public static ArrayList <Long> hash (ArrayList<String> kmer) {
		 //calcola complemento inverso	
		//Long min = Long.MAX_VALUE;
		//System.out.println(kmer.size());
		
		
		ArrayList<Long> sketch = new  ArrayList<Long>();
		for(String s : kmer) {
		//	System.out.println(s);
		String canonicalKmer;
		String rcKmer = makeComplement(s);
	
		    //determina il minore tra il kmero originale e il suo reverse complement
		    if ((s.compareTo(rcKmer)) < 0)
		    	canonicalKmer = s;
		    else 
		    	canonicalKmer = rcKmer;

		     //calculate murmurhash using a hash seed of 42
		    Long hash = MurmurHash3.hash64(canonicalKmer.getBytes());
		//    System.out.println(hash);
		   if (hash < 0) 
		    	hash = (long) ((long)hash + Math.pow(2, 64));
		    sketch.add(hash);
		    
		    
		   /* if(hash<=min) {
		    	
		    	min = hash;
		    	minHashes.add(min);
           // System.out.println("aggiunto min "+ min);
		    }
		   */
		}
		//	System.out.println(minHashes.size());
			return sketch;

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
   public static Set <Long> getMinHash(ArrayList<Long> hashedKmers, int s) //s corrisponde alla grandezza del mio sketch
    {  
	   System.out.println("sketch size:" + hashedKmers.size());
	  Set<Long> minHash = new HashSet<Long>();
	  /* int scaled =1000;
	   Long max = (long) 0;
	   Long MAX_HASH = (long) Math.pow(2, 64);
	   Long keep_below = MAX_HASH / scaled;
	   ArrayList<Long> sketches = new ArrayList<Long>(hashedKmers);
      ArrayList<Long> minHashes = new ArrayList<Long>();
        Long min = Long.MAX_VALUE;
      */
      Collections.sort(hashedKmers); // ordino gli sketch con hash dal più piccolo al più grande
      
      //  Long max =sketch.get(0); // assegno al massimo il primo elemento dello sketch
  //     max =Long.MAX_VALUE;

      for( int i =0; i<s-1;i++) {
    	  minHash.add(hashedKmers.get(i));
    	
    	}
            
        System.out.println("Minhash size" +minHash.size());
        return  minHash;
      
    }
    

    static private double jaccardSimilarity(Set<Long>  set1, Set<Long> set2 ) {
    
    	System.out.println("Cardinalità di set1: " +set1.size());
    	System.out.println("Cardinalità di set2: "+  set2.size());
    	 Set<Long>intersection = new HashSet<Long>(set1);
         intersection.retainAll(set2);
        
         Set<Long> union = new HashSet<Long>(set1);
         union.addAll(set2);

         if (union.isEmpty()) {
             return 0;
         }
         System.out.println(intersection.size() +" /"+  set1.size());
         return (double) intersection.size() / union.size();
         
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
		Set<Long> set1 = new  HashSet <Long>();
		Set<Long> set2 = new  HashSet <Long>();
		ArrayList<Long> hashedKmers1 = new ArrayList<Long>();
		ArrayList<Long> hashedKmers2 = new ArrayList<Long>();
		ArrayList<String> seq1 = readKmerFromFile("/home/alfonso/Downloads/Mash-master/test/genome1.fna",21); //path, k 
		ArrayList<String> seq2 = readKmerFromFile("/home/alfonso/Downloads/Mash-master/test/genome3.fna",21); //path, k
	/*	Set<String> s1 = new HashSet<String>(seq1);
		System.out.println("l " + seq1.size());
		System.out.println("cardinalita " + s1.size());
		*/
		hashedKmers1 = hash(seq1);
		hashedKmers2= hash(seq2);
			//System.out.println(hash(s));
	/*	}
		for (String s :seq2 ){
			sketch2.add(hash(s));
		}
		*/
		/*for (Long s: sketch1)
			set1.add(s);
		for (Long s: sketch2)
			set2.add(s);
		*/
		set1 = getMinHash(hashedKmers1, 1000); //costruisce sketch di minHash grande 1000
		set2 = getMinHash(hashedKmers2, 1000);
		//JaccardSimilarity js = new JaccardSimilarity();
	
		
		System.out.println("Indice di Jaccard: "+jaccardSimilarity(set1, set2));
	
	System.out.println("Distanza di Jaccard: "+jaccardDistance(set1, set2));
	}
}