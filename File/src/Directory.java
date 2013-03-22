import java.util.ArrayList;

import be.kuleuven.cs.som.annotate.*;


public class Directory extends DiskItem{
	
	/**
	 * Initializes a new directory with the given name and writable state
	 * 
	 * @param  name
	 * 		   The name of the directory
	 * @param  writable
	 * 		   The writable state of the directory
	 * @effect Initialize a new disk item with the given name and writable state
	 * 		   | super(name, writable)
	 */
	public Directory(String name, boolean writable){
		super(name, writable);
	}
	
	/**
	 * Initializes a new directory with the given name
	 * 
	 * @param  name
	 * 		   The name of the directory
	 * @effect Initialize a new disk item with the given name and true as its writable state
	 * 		   | this(name, true)
	 */
	public Directory(String name){
		super(name, true);
	}
	
	private ArrayList<DiskItem> subItems;
	/**
	 * Return the number of sub-items in this directory
	 */
	public int getNbItems(){
		if(subItems == null)
			return 0;
		return subItems.size();
	}
	/**
	 * Return  the item at a given index
	 * @param  index
	 * 		   The index of the sub item to return
	 * @pre    The given index must be positive and may not 
	 * 		   exceed the number of items this directory contains.
	 * 		   | index>=0 && index<=getNbItems
	 * @return The item at the given index
	 */
	@Basic @Raw
	public DiskItem getItemAt(int index){
		return subItems.get(index-1);
	}
	
	
}
