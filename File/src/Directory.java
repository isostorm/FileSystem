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
	 * @post   Sub items equals a new empty arraylist is initialized
	 * 		   | new.subItems == new ArrayList<DiskItem>()
	 * @effect This directory is moved to the given parent directory
	 * 		   | move(parent)
	 * @effect Initialize a new disk item with the given name and writable state
	 * 		   | super(name, writable)
	 */
	public Directory(Directory parent, String name, boolean writable){
		super(name, writable);
		subItems = new ArrayList<DiskItem>();
		move(parent); 
	}
	/**
	 * Initializes a new directory with the given name and writable state
	 * 
	 * @param  name
	 * 		   The name of the directory
	 * @param  writable
	 * 		   The writable state of the directory
	 * @effect Initialize a new directory with the given name and writable state 
	 * 		   and null as its parent directory
	 * 		   | this(null, name, writable)
	 */
	public Directory(String name, boolean writable){
		this(null, name, writable);
	
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
		this(name, true);
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
	 * 
	 * @param  index
	 * 		   The index of the sub item to return
	 * @Pre    The given index must be positive and may not 
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
	 * @Pre    The given item must be effective
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
	 * If this directory contains the item, it will be removed.
	 * 
	 * @param item The item to remove.
	 * @effect If the item was in the list of sub item, the given item will be removed.
	 *         | subItems.remove(item)
	 */
	public void removeItem(DiskItem item)
	{
		subItems.remove(item);
	}
	/**
	 * Get the item with the given name in this directory
	 * 
	 * @param  name
	 * 		   The name of the sub item
	 * @return The element with the given name in this directory
	 * 		   |result == getItem(name, subItems, 0, subItems.size()-1)
	 */
	public DiskItem getItem(String name)
	{
		if(getNbItems() == 0)
		{
			return null;
		}
		return getItem(name, subItems, 0, subItems.size()-1);
	}
	/**
	 * A recursive method to get the item with the given name
	 * 
	 * @param  name
	 * 		   The name of the element to search for 
	 * @param  subItems
	 * 		   The arrayList to search in
	 * @param  leftIndex
	 * 		   The left index to start searching
	 * @param  rightIndex
	 * 		   The right index to end searching
	 * @return If the left index exceeds the right index return null
	 * 		   | if(leftIndex > rightIndex)
	 * 				then result == null
	 * 		   If the name of the middle item of this part of the arraylist 
	 * 		   equals the given name, return the middle item
	 * 		   | if(middleItemName.equalsIgnoreCase(name))
	 * 				then result == middleItemName
	 * 		   Else if the name of the middle item precedes the given name
	 * 		   return this method invoked on a part of the arraylist, from the middle element 
	 * 		   added with one to the right index
	 * 			| else if (middleItem.precedes(name))
	 * 				then result == getItem(name, subItems, middleIndex+1, rightIndex)
	 * 		   Else return this method invoked on a part of the arraylist, from the left index 
	 * 		   to the middle element subtracted with one
	 * 			| else if (middleItem.precedes(name))
	 * 				then result == getItem(name, subItems, leftIndex, middleIndex-1)
	 * 			
	 */
	
	@Model
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
		else if (middleItem.precedes(name))
		{
			return getItem(name, subItems, middleIndex+1, rightIndex);
		}
		else
		{
			return getItem(name, subItems, leftIndex, middleIndex-1);
		}
	}
	/**
	 * Adds the given item to the sub items.
	 * 
	 * @param diskItem 
	 * 		  The disk item to add to the sub items.
	 * @post  Each item before the given item precedes the given item
	 * 		  | for each item[i] in 0..i>subItems.indexOf(diskItem)
	 * 				item[i].precedes(diskItem) 
	 * @post  The list of sub items contains the given disk item
	 * 		  | subItems.contains(diskItem)
	 */
	public void addItem(DiskItem diskItem)
	{
		if(getNbItems() == 0)
		{
			subItems.add(diskItem);
		}
		int i = 0;
		while(i < getNbItems() && subItems.get(i).precedes(diskItem.getName())){
			i++;
		}
		subItems.add(i, diskItem);
	}

	/**
	 * Checks whether this directory is a direct or indirect sub directory of a given directory.
	 * 
	 * @param directory
	 * 		  The directory to check for whether the given directory is a sub directory
	 * @return The result of the recursive method isDirectOrIndirectSubdirectoryOfRecursive
	 * 		   | result == isDirectOrIndirectSubdirectoryOfRecursive(directory)
	 */
	public boolean isDirectOrIndirectSubdirectoryOf(Directory directory)
	{
		return isDirectOrIndirectSubdirectoryOfRecursive(directory);
	}
	
	/**
	 * A recursive method that checks whether this directory is a 
	 * direct or indirect sub directory of the given directory
	 * 
	 * @param  directory
	 * 		   The directory to check for whether the given directory is a sub directory
	 * @return If this directory has no parent directory return false
	 * 		   | if(getDirectory() == null)
	 * 				then result == false
	 * 		   Else if the given directory is the parent directory 
	 * 		   of this directory, return true.
	 * 		   | else if (getDirectory() == directory)
	 * 				then result == true
	 * 		   Else invoke this method on the parent directory of this directory 
	 * 		   with the given directory as argument
	 * 		   | else result == getDirectory().isDirectOrIndirectSubdirectoryOfRecursive(directory)
	 * 		   
	 */
	private boolean isDirectOrIndirectSubdirectoryOfRecursive(Directory directory)
	{
		if(getDirectory() == null)
		{
			return false;
		}
		else if (getDirectory() == directory)
		{
			return true;
		}
		else
		{
			return getDirectory().isDirectOrIndirectSubdirectoryOfRecursive(directory);
		}
	}
	/**
	 * Checks if a disk item with the given name exists (case insensitive) inside this directory.
	 * 
	 * @param name The name of the disk item to check.
	 * @return True if there is an item with the given name (case insensitive).
	 *         | result == for some item in subItems
	 *         					(getItem.getName == name)
	 */
	public boolean exists(String name)
	{
		return (getItem(name) != null);
	}

	/**
	 * Checks whether or not this directory can have the given file as a sub file.
	 * 
	 * @return 
	 */
	public boolean canHaveAsFile(File file)
	{
		return file != null && !exists(file.getName());
	}

	/**
	 * Checks whether this directory can be moved to the given directory
	 * 
	 * @return If the target is null and the super method canMoveTo, invoked on the target
	 * 			yields true, return true
	 * 			if(target == null && super.canMoveTo(target))
	 * 				then result == true
	 * 			else if the super method canMoveTo yields true and this directory 
	 * 			doesn't equal the given target
	 * 			 
	 */
	@Override
	public boolean canMoveTo(Directory target)
	{
		if(target == null && super.canMoveTo(target))
		{
			return true;
		}
		return super.canMoveTo(target) && target != this && !target.isDirectOrIndirectSubdirectoryOf(this);
	}
		
}
