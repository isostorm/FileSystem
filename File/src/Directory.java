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
	
	/**
	 * Check whether this directory contains the given disk item
	 * 
	 * @param  item
	 * 		   The item to check for, it is in this directory
	 * @pre    The given item must be effective
	 * 		   | item != null
	 * @return True if and only if this directory contains the given disk item
	 * 		   | result == subItems.contains(item)
	 */
	public boolean hasAsItem(DiskItem item){
		return subItems.contains(item);
		
	}
	/**
	 * Returns the index of the given diskItem in this 
	 * 
	 * @param  item
	 * 		   The disk item to get the index of
	 * @return The index of the given disk
	 */
	public int getIndexOf(DiskItem item){
		return subItems.indexOf(item)+1;
	}
	/**
	 * 
	 * @param name
	 * @return
	 */
	public DiskItem getItem(String name)
	{
		return getItem(name, subItems, 0, subItems.size()-1);
	}
	private DiskItem getItem(String name, ArrayList<DiskItem> subItems, int leftIndex, int rightIndex)
	{
		int middleIndex = (leftIndex + rightIndex)/2;
		DiskItem middleItem = subItems.get(middleIndex);
		String middleItemName = middleItem.getName();
		System.out.println("Left:" + leftIndex + " Right:" + rightIndex);
		if(leftIndex > rightIndex)
		{
			return null;
		}
		
		if(middleItemName.equalsIgnoreCase(name))
		{
			return middleItem;
		}
		else if (middleItem.precedes(this))
		{
			return getItem(name, subItems, middleIndex+1, rightIndex);
		}
		else
		{
			return getItem(name, subItems, leftIndex, middleIndex-1);
		}
	}
}
