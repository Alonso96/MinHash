package minhash;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class MinHashTest {
	HashMap<String,Integer> seq1 =null;
	HashMap<String,Integer> seq2 =null;
	/*public static String process(byte [] chunk, int length) {
		String a = new String(chunk);
		StringBuilder sb = new StringBuilder();
		int i =0;
		while(i<a.length()) {
			char b = a.charAt(i);
			if (b=='>'){
				while(a.charAt(i)!='\n') i++;
			}
			else sb.append(a.charAt(i));
			i++;
			
		}
		return sb.toString();
	} 
	*/
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if (args.length != 2) {
			System.err.println("Two argument required");
			return;
		}
	
		try {
		long start = System.currentTimeMillis();
		//ArrayList<Long> set1 ;
		//ArrayList<Long> set2 ;
	
	
		System.out.println("Reading 1st genome....");

		TreeSet<Long> seq1 =  new TreeSet<Long> ();
				
		MinHash.makeSketchFromFile(args[0],21,seq1,1000); //path, k 
		int nKmer =0;
		System.out.println("read finished");
		/*
		for (Long entry : seq1.keySet() )
			nKmer+= seq1.get(entry);
		System.out.println("# of kMers 1st genome: "+ nKmer);
		nKmer=0;
		*/
		//System.out.println("Hashing...");
		//hashedKmers1 = MinHash.hash(seq1);
		//seq1.clear();
		//seq1=null;
		System.out.println("Creating sketch");
	//	set1 =MinHash. getMinHash(seq1, 1000); //costruisce sketch di minHash grande 1000
		//seq1.clear();
	//	seq1=null;
		
		
		TreeSet<Long> seq2 =  new TreeSet<Long> ();
		
		MinHash.makeSketchFromFile(args[1],21,seq2,1000); //path, k 
		nKmer =0;
		
		/*for (Long entry : seq2.keySet() )
			nKmer+= seq2.get(entry);
		System.out.println("# of kMers 1st genome: "+ nKmer);
		
		nKmer=0;
		*/
		System.out.println("Hashing...");
		//hashedKmers1 = MinHash.hash(seq1);
		//seq1.clear();
		//seq1=null;
	//	System.out.println("Creating sketch");
	//	set2 =MinHash. getMinHash(seq2, 1000); //costruisce sketch di minHash grande 1000
	//	seq2.clear();
		//seq2=null;
		
		
		System.out.println("Indice di Jaccard: "+MinHash.jaccardSimilarity(seq1, seq2, 1000));
		System.out.println("Distanza di Jaccard: "+MinHash.jaccardDistance(seq1, seq2,1000));
		
		long end = System.currentTimeMillis();
		
		System.out.println("Execution time in ms:"+  (end - start) );
		
		

		}
		catch (Throwable ex) {
				System.err.println("Uncaught exception - " + ex.getMessage());
		        ex.printStackTrace(System.err);
		}
		}


}
