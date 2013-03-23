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
		subItems = new ArrayList<DiskItem>();
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
		subItems = new ArrayList<DiskItem>();
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
	/**
	 * Adds the given item to the sub items.
	 * 
	 * @param diskItem The disk item to add to the sub items.
	 * @post subItems.contains(diskItem)
	 */
	public void addItem(DiskItem diskItem)
	{
		subItems.add(diskItem);
	}

	/**
	 * Checks whether this directory is a direct or indirect sub directory of a directory.
	 * 
	 * @param directory
	 * @return True if this directory is a direct or indirect sub directory of the given directory.
	 * MOET GEFIXED WORDEN
	 */
	public boolean isDirectOrIndirectSubdirectoryOf(Directory directory)
	{
		return isDirectOrIndirectSubdirectoryOfRecursive(directory);
	}
	protected boolean isDirectOrIndirectSubdirectoryOfRecursive(Directory directory)
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
	 *         | result == (getItem(name) != null)
	 */
	public boolean exists(String name)
	{
		return (getItem(name) != null);
	}

	@Override
	public void move(Directory directory) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 */
	public boolean canHaveAsFile(File file)
	{
		return file != null && !exists(file.getName());
	}

	/**
	 * 
	 */
	public boolean canMoveTo(Directory target)
	{
		return super.canMoveTo(target) && target != this && isDirectOrIndirectSubdirectoryOf(target) && target.isDirectOrIndirectSubdirectoryOf(this);
	}
		
}
