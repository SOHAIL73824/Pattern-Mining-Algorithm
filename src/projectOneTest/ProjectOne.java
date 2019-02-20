package projectOneTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.ArrayList;

public class ProjectOne {
	
	private static Map<String, BitSet> listValue;
	
	private static int transaction_count;   
	private static int supportvalue;
	private static int minNumItems;
	private static String output_file_path;
		
	

	public static void main (String[] args) throws IOException {
		String inputDataPath = args[2];
		supportvalue = Integer.parseInt(args[0]);	
		minNumItems=Integer.parseInt(args[1]);
		output_file_path=args[3];
		listValue = new HashMap<String, BitSet>();
	    transaction_count = getAllItemSets(inputDataPath, listValue);
		frequencySetGeneration();
	}

	
	 //It will generate the Frequency 1 itemset
	 
	public static TreeSet<Itemset> firstItemset() {
		HashMap<String, Integer> firstItem = new HashMap<String, Integer>();
		
		for (String str : listValue.keySet())
			firstItem.put(str, (int)(listValue.get(str).cardinality()));
			TreeSet<Itemset> firstItemsets = new TreeSet<Itemset>();
		
		for (String term : firstItem.keySet())
			if (firstItem.get(term) >= supportvalue)
				firstItemsets.add(new Itemset(term, firstItem.get(term)));
		
		return firstItemsets;
	}


    //Check compatibility of two Itemset to combine
	public static boolean checkIfItemsetCanCombine(Itemset transaction1, Itemset transaction2) {
		int i;
		
		// To check if two transaction/Itemset has first n-2 elements are the same;
		for (i = 0; i < transaction1.allItems.size()-1; i++)
			if (!transaction1.allItems.get(i).equals(transaction2.allItems.get(i))) 
				return false;
		
		//// To check if last element of transaction1 is greater than transaction2
		if (transaction1.allItems.get(i).compareTo(transaction2.allItems.get(i)) >= 0) 
			return false;
		
		return true;
	}
	

	
	//Generating new candidate sets
	public static TreeSet<Itemset> newCandidateSet(TreeSet<Itemset> itemsets) {
		//System.out.println(itemsets+"::");
		TreeSet<Itemset> candidateSets = new TreeSet<Itemset>();
		Iterator<Itemset> allItemset = itemsets.iterator(); 
		Itemset singleCandidate = null;

		while (allItemset.hasNext()) {
			Itemset singleItemset = allItemset.next();
			Iterator<Itemset> newItemsets = itemsets.tailSet(singleItemset).iterator();
			while (newItemsets.hasNext()) {
				Itemset newSingleItemset = newItemsets.next();
				if (checkIfItemsetCanCombine(singleItemset, newSingleItemset)) {
					singleCandidate = new Itemset(singleItemset, newSingleItemset);
					BitSet JoinedItemset = new BitSet((int) transaction_count);
					JoinedItemset.set(0, (int)transaction_count);
					
					for (String str : singleCandidate.allItems)
						JoinedItemset.and(listValue.get(str));
					singleCandidate.supportCount = (int) (JoinedItemset.cardinality());;
						
					if (singleCandidate.supportCount >= supportvalue){
						
						candidateSets.add(singleCandidate);
						}
				}
			}
		}

		return candidateSets;
	}
	
	//To check if all subset of frequency set are present in previous k-1 frequency sets
	public static boolean checkSubsetOfItemset(TreeSet<Itemset> itemsets, Itemset newCandiate) {
		for (int i = 0; i < newCandiate.allItems.size(); i++) {
			String removed = newCandiate.allItems.remove(i);
			if (!itemsets.contains(newCandiate))
				return false;
			newCandiate.allItems.add(i, removed);
		}

		return true;
	}
	

	//Printing output to output_file.txt file
	public static void fileWrite(ArrayList<TreeSet<Itemset>> itemsetList) {
		try {
		    PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(output_file_path, true)));
		
		 
		    for (TreeSet<Itemset> allItemSets : itemsetList){
		    	for (Itemset singleItemset : allItemSets){
		    		if(singleItemset.allItems.size()>=minNumItems){
		    		for (String i : singleItemset.allItems){
		    			output.print(i+" ");}
		    		output.print("("+singleItemset.supportCount+")");output.println();}}}
		    output.println();
		    output.close();
		} catch (IOException e) {System.out.println("In Exception");
		}
	}
	
	

	//It implements the Apriori algorithm to generate frequency sets
	public static void frequencySetGeneration() throws IOException{
	
		File previous_output = new File(output_file_path);
		
		if (previous_output.exists()){previous_output.delete();}
		
		ArrayList<TreeSet<Itemset>> allItemsetsList = new ArrayList<TreeSet<Itemset>>();
		TreeSet<Itemset> lastItemSets;
		
		allItemsetsList.add(firstItemset());
		lastItemSets = allItemsetsList.get(0);

		while(!lastItemSets.isEmpty()) {
			TreeSet<Itemset> candidates = newCandidateSet(lastItemSets);
			TreeSet<Itemset> survivors = new TreeSet<Itemset>();
			for (Itemset newCandidateItemset : candidates)
				if (checkSubsetOfItemset(lastItemSets, newCandidateItemset))
					survivors.add(newCandidateItemset);
	
			lastItemSets = survivors;
			allItemsetsList.add(survivors);
			
		}
		fileWrite(allItemsetsList);
		
		
		
	}
	
	//To get all Itemsubsets from the input file.It will load all the Items of the file  in the Bitset
	public static int getAllItemSets(String data, Map<String, BitSet> listValue) throws IOException {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(data)));
		String line = null;
		int i = 0;
		
		while ((line = buffer.readLine()) != null) {
			String[] terms = line.split(" ");
			for (String str : terms) {
				BitSet bs = listValue.get(str);
				if (bs == null) {
					bs = new BitSet();
					listValue.put(str, bs);
				}
				bs.set(i);
			}
			i++;
		}
		buffer.close();
		
		return i;
	}
}
