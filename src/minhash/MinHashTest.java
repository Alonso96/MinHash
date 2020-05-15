package minhash;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class MinHashTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if (args.length != 2) {
			System.err.println("Two argument required");
			return;
		}
	/*	 Scanner scan = new Scanner(System.in);  // Create a Scanner object
	  
	 
		 System.out.println("Inserisci il valore k per generare k-meri...(Premi invio per skip e lasciare valore default 21)");
		 int kmer = scan.nextInt();  // Read user input
		 System.out.println("Inserisci la taglia dello sketch...(Premi invio per skip e lasciare valore default 1000");
		 int skSize = scan.nextInt();
		 */
		long start = System.currentTimeMillis();
		ArrayList<Long> set1 ;//= new ArrayList<Long>(); 
		ArrayList<Long> set2 ;//= new ArrayList<Long>();
	//	Long[] hashedKmers1 ;
	//	Long[] hashedKmers2 ;
		ArrayList<Long> hashedKmers1 ;  
		ArrayList<Long>hashedKmers2;  
		HashMap<String,Integer> seq1 = MinHash.readKmerFromFile(args[0],21); //path, k 
		HashMap<String,Integer> seq2 = MinHash.readKmerFromFile(args[1],21); //path, k
		
		hashedKmers1 = MinHash.hash(seq1);
		hashedKmers2= MinHash.hash(seq2);
		set1 =MinHash. getMinHash(hashedKmers1, 1000); //costruisce sketch di minHash grande 1000
		set2 = MinHash.getMinHash(hashedKmers2, 1000);
		System.out.println("Indice di Jaccard: "+MinHash.jaccardSimilarity(set1, set2, 1000));
		System.out.println("Distanza di Jaccard: "+MinHash.jaccardDistance(set1, set2,1000));
		
		long end = System.currentTimeMillis();
		
		System.out.println("Execution time in ms:"+  (end - start));
	}

}
