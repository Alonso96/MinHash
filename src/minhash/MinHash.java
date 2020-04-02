package minhash;
import java.io.BufferedReader;
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
public class MinHash{

	
	public static ArrayList<String> buildKmer(String path,int  k) throws IOException{
		String genoma = new String(Files.readAllBytes(Paths.get(path)));
		String kmer;
		ArrayList<String> kmers = new ArrayList<String>();
		int n_kmers = genoma.length() - k + 1;
		for (int i=0;i<n_kmers;i++) {
			kmer = genoma.substring(i,i+k);
			kmers.add(kmer);
		}
		
		return kmers;
	}
	public static int hash (String kmer) {
		 //calcola complemento inverso
		String canonicalKmer;
		String rcKmer = makeComplement(kmer);
		  
		    //determina il minore tra il kmero originale e il suo reverse complement
		    if ((kmer.compareTo(rcKmer)) < 0)
		    	canonicalKmer = kmer;
		    else 		    	canonicalKmer = rcKmer;

		     //calculate murmurhash using a hash seed of 42
		    int hash = MurmurHash.hash32(canonicalKmer,0,canonicalKmer.length());
		    if (hash < 0) 
		    	hash += Math.pow(2, 32);
			return hash;

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
    static private double jaccardSimilarity(Set<Integer>s1, Set<Integer>s2) {


        final int sa = s1.size();
        final int sb = s2.size();
        s1.retainAll(s2);
        final int intersection = s1.size();
        return 1d / (sa + sb - intersection) * intersection;
    }
    static private double jaccardDistance(Set<Integer>s1, Set<Integer>s2) {
    	return 1.0 - jaccardSimilarity(s1,s2);

        
    }
	public static void main(String[] args) throws IOException{
		Set<Integer> set1 = new HashSet<Integer>();
		Set<Integer> set2 = new HashSet<Integer>();
		ArrayList<String> seq1 = buildKmer("/home/alfonso/Downloads/Mash-master/test/genome1.fna",21); //path, k 
		ArrayList<String> seq2 = buildKmer("/home/alfonso/Downloads/Mash-master/test/genome2.fna",21); //path, k
		for (String s :seq1 ){
			
			set1.add(hash(s));
		}
		for (String s :seq2 ){
			
			set2.add(hash(s));
		}
		
	
		System.out.println("Indice di Jaccard: "+jaccardSimilarity(set1, set2));
		System.out.println("Distanza di Jaccard: "+jaccardDistance(set1, set2));
	}
}