package projectOneTest;

import java.util.ArrayList;

public class Itemset implements Comparable<Object> {

	protected ArrayList<String> allItems;
	protected int supportCount;

	public Itemset(String item, int support) {
		this.allItems = new ArrayList<String>();
		this.allItems.add(item);
		this.supportCount = support;
	}
	

//Create Itemset after adding 2 itemset1 and itemset2
	public Itemset(Itemset itemst1, Itemset itemst2) {
		this.allItems = new ArrayList<String>();
		this.allItems.addAll(itemst1.allItems);
		this.allItems.add(itemst2.allItems.get(itemst2.allItems.size()-1));
		this.supportCount = 0;
	}
	
	

	public void setSupport(int support) {
		this.supportCount = support;
	}
	

//This will check two itemsets equality
	@Override
	public int compareTo(Object object) throws ClassCastException {
		if (!(object instanceof Itemset))
			throw new ClassCastException("Itemset Object not found");
		
		Itemset that = (Itemset)object;
		
		if (this.allItems.size() != that.allItems.size())
			throw new IllegalStateException("Two different size itemset/transaction can't be compared");
		
		for (int i = 0; i < this.allItems.size(); i++)
			if (!this.allItems.get(i).equals(that.allItems.get(i)))
				return this.allItems.get(i).compareTo(that.allItems.get(i));
		
		return 0;
	}
	
	
}