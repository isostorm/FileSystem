package filesystem;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

import filesystem.exception.*;

/**
 * A class of directories.
 * @invar Each directory must have valid items registered in it.
 *        | hasValidItems()
 * @invar Each directory must have a valid number of items.
 *        | hasValidNbItems()
 */
public class Directory extends DiskItem {

	/**********************************************************
	 * Constructors
	 **********************************************************/
    
    /**
     * Initialize a new root directory with given name and writability.
     * 
     * @param  name
     *         The name of the new directory.
     * @param  writable
     *         The writability of the new directory.
     * @post   The new directory has no items.
     *         | new.getNbItems() == 0
     * @effect The new directory is a disk item with the given
     *         name and writability.
     *         | super(name,writable)
    */
    public Directory(String name, boolean writable) {
        super(name,writable);
    }
    
    /**
     * Initialize a new writable root directory with given name.
     * 
     * @param  name
     *         The name of the new directory.
     * @effect The new root directory is initialized with the given name
     *         and is writable.
     *         | this(name,true)
     */
    public Directory(String name) {
        this(name,true); 
    }
    
    
    /**
     * Initialize a new directory with given parent directory, name and 
     * writability.
     * 
     * @param  parent
     *         The parent directory of the new directory.
     * @param  name
     *         The name of the new directory.
     * @param  writable
     *         The writability of the new directory.
     * @post   The new directory has no items.
     *         | new.getNbItems() == 0
	 * @effect The new directory is a disk item with the given
     *         parent, name and writability.
     *         | super(parent,name,writable)        
     */
    public Directory(Directory parent, String name, boolean writable) 
           throws IllegalArgumentException,
                  DiskItemNotWritableException,
                  IllegalAddException    {
        super(parent,name,writable);    
    }

    /**
     * Initialize a new writable directory with given parent directory
     * and name.
     * 
     * @param  parent
     *         The parent directory of the new directory.
     * @param  name
     *         The name of the new directory.
	 * @effect The new directory is a disk item with the given
     *         parent, name and writability.
     *         | this(parent,name,true)       
     */
    public Directory(Directory parent, String name) 
           throws IllegalArgumentException, 
                  DiskItemNotWritableException,
                  IllegalAddException    {
        this(parent,name,true);    
    }    

	/**********************************************************
	 * delete/termination
	 **********************************************************/

    /**
     * Check whether this directory can be deleted.
     * 
     * @return False if either:
     *         - this directory is already terminated
     *         - this directory is not writable
     *         - this directory is not a root, and its parent
     *           directory is not writable
     *         - this directory is not empty;
     *         True otherwise.
     *       | result == 
     *       |   !( isTerminated() || !isWritable() || getNbItems()>0 ||
     *       |      (!isRoot() && !getParentDirectory().isWritable()) )
     * @see superclass
     */
    public boolean canBeTerminated() {
        return super.canBeTerminated() && getNbItems() == 0;
    }
    
	/**********************************************************
	 * Contents
	 **********************************************************/

	  /**
	   * Return the position at which the given item is registered.
	   *
	   * @param   item
	   *          The item to be searched.
	   * @return  The given item is registered in this directory at the
	   *          resulting position.
	   *          | getItemAt(result)==item
	   * @throws  NoSuchItemException [must]
	   *          The given item is not in the directory
	   *          | ! hasAsItem(item)
	   * @O       Logaritmic in the number of items.
	   *          | log(getNbItems())
	   */
	  public int getIndexOf(DiskItem item)
	             throws NoSuchItemException {
	      try {
	          int index = getIndexOfFirstItemNotBefore(item);
              if (getItemAt(index) == item){
	              return index;
              } else {
	            throw new NoSuchItemException();
              }
	      }  catch(IllegalArgumentException exc) {
       	     // The given item was null, 
	         // or is ordered after all items in this directory
	         throw new NoSuchItemException();
	      }
	  }

    /**
     * Return the item in this directory with the given name.
     * 
     * @param name
     *        The name of the item to be looked up.
     * @return The resulting item belongs to this directory and has 
     *         the given name.
     *         | hasAsItem(result) && 
     *         | result.getName().equalsIgnoreCase(name) 
     * @throws NoSuchItemException [must]
     *         The directory does not contain an item with the given 
     *         name.
     *         | ! exists(name)
     */
    public DiskItem getItem(String name) throws NoSuchItemException {
        try {
            int index = getIndexOfFirstItemNotBefore(name);
            if (getItemAt(index).getName().equalsIgnoreCase(name))
                return getItemAt(index);
            throw new NoSuchItemException();
        } catch (IllegalArgumentException exc) {
            // The given name was null,
            // or is ordered after all items in this directory
            throw new NoSuchItemException();
        }
    }
	  
    /**
     * Check whether this directory is a direct or indirect subdirectory
     * of the given directory
     * 
     * @param other
     *        The directory to check with.
     * @return False if this directory is a root directory;
     *         Otherwise, true if the parent directory of this
     *         directory is equal to the given directory or if
     *         that parent directory is itself directly or 
     *         indirectly a subdirectory of the given directory;
     *         Otherwise false.
     *         | if (isRoot())
     *         |   then result == false
     *         | else result == (getParentDirectory().equals(other) ||
     *         |   getParentDirectory().
     *         |              isDirectOrIndirectSubdirectoryOf(other)) 
     * @throws IllegalArgumentException [must]
     *         The given directory is not effective.
     *         | other == null
     */
    public boolean isDirectOrIndirectSubdirectoryOf(Directory other) 
                   throws IllegalArgumentException {
        try {
            return (this != other) && 
                   other.equalsOrIsDirectOrIndirectParentOf(this);
        } catch (NullPointerException e) {
            // other is not an effective directory
           throw new IllegalArgumentException();
        }        
    }
    
	/**
	 * Check whether this directory can have the given item at the given
	 * position.
	 *
	 * @param   item
	 *          The item to be checked.
	 * @param   position
	 *          The position to be checked.
	 * @return  False if this directory can not have the given item as
	 *          its item
	 *          | if (!canHaveAsItem(item))
	 *          | result == false
	 * @return  Otherwise, false if the given position is not positive
	 *          or exceeds the number of items (registered in this
	 *          directory) + 1
	 *          | if ((position < 1) || (position > getNbItems()+1))
	 *          | result == false
	 * @return  Otherwise, false if inserting the given item at the
	 *          given position does not yield an ordered sequence of
	 *          items for this directory; True otherwise.
	 *          | result == 
	 *          |   (((position == 1) || 
	 *          |      item.isOrderedAfter(getItemAt(position-1))) &&
	 *          |    ((position == getNbItems()+1) || 
	 *          |      item.isOrderedBefore(getItemAt(position))))
	 */
    public boolean canHaveAsItemAt(@Raw DiskItem item,int position){
    	  if (!canHaveAsItem(item))
    		  return false;
    	  if ((position < 1) || (position > getNbItems()+1))
    		  return false;
    	  return (((position == 1) || 
    			   item.isOrderedAfter(getItemAt(position-1))) &&
    	          ((position == getNbItems()+1) || 
    	        	   item.isOrderedBefore(getItemAt(position))));  
    }

    /**
     * Insert the given item at the given position.
     * 
	 * @param   item
	 *          The item to be added.
	 * @param   position
	 *          The position where the given item must be inserted.
	 * @post    The number of items registered in this directory is
	 *          incremented with 1.
	 *          | new.getNbItems() == getNbItems() + 1   
	 * @post    The given item is inserted at the given position.
	 *          | new.getItemAt(position) == item
	 * @post    All items after the given position are shifted
	 *          one position to the right.
	 *          | for each I in position..getNbItems():
	 *          |   new.getItemAt(I+1) == getItemAt(I)
	 * @throws  IllegalAddException [must]
	 *          This directory can not have the given item as item.
	 *          | !canHaveAsItemAt(item,position)
	 */
    private void setItemAt(@Raw DiskItem item, int position)
                                      throws IllegalAddException,
                                             IllegalArgumentException {
    	  if (!canHaveAsItemAt(item,position))
    		throw new IllegalAddException(this,item);
    	  try {
	    items.add(position-1,item);
	  } catch (IndexOutOfBoundsException e) {
		throw new IllegalArgumentException();
	  }
    }
    
	  /**
	   * Add the given item to the items registered in this directory.
	   *
	   * @param   item
	   *          The item to be added.
	   * @pre     The given item must have an effective name.
	   *          | item.getName() != null
	   * @post    The number of items registered in this directory is
	   *          incremented with 1.
	   *          | new.getNbItems() == getNbItems() + 1   
	   * @post    The given item is added to the items registered
	   *          in this directory.
	   *          | new.hasAsItem(item)
	   * @post    All items registered in this directory, that are 
	   *          ordered after the given item are shifted one position
	   *          to the right.
	   *          | for each I in 1..getNbItems():
	   *          |   if (getItemAt(I).isOrderedAfter(item))
	   *          |     then new.getItemAt(I+1) == getItemAt(I)
	   * @throws  IllegalAddException [must]
	   *          This directory can not have the given item as item.
	   *          | !canHaveAsItem(item)
	   */
	  private void addToItems(@Raw DiskItem item) throws IllegalAddException{
	    try {
	    	    setItemAt(item, getIndexOfFirstItemNotBefore(item));
	    }
	    catch (IllegalArgumentException e) {
	    	  //item.getName() != null is a precondition
	    	  assert item == null;
	    	  throw new IllegalAddException(this,item);
	    }
	  }
	  
	  /**
	   * Add the given item to the items registered in this directory.
	   * Also updates the modification time and enforces writability.
	   *
	   * @param  item
	   *         The item to be added.
	   * @effect The new modification time of this directory is updated.
	   *         | setModificationTime()
	   * @effect The given item is added to the items registered in this
	   *         directory.
	   *         | addToItems(item)        
  	   * @throws DiskItemNotWritableException [must]
	   *         This directory is not writable.
	   *         | !isWritable()
	   */
	  @Model void addToItemsAndUpdateModificationTime(@Raw DiskItem item) 
	       throws DiskItemNotWritableException,IllegalAddException {
	    if (!isWritable())
	        throw new DiskItemNotWritableException(this);
	    addToItems(item);
	    setModificationTime();
	  }	  

	  /**
	   * Remove the given item from this directory.
	   *
	   * @param item
	   *        The item to remove
	   * @effect The item is removed.
	   *         | removeItemAt(getIndexOf(item))
	   * @throws NoSuchItemException [must]
	   *         The given item is not in the directory
	   *         | ! hasAsItem(item)
	   */
	  @Model private void removeFromItems(DiskItem item) throws NoSuchItemException{
	    try {
			removeItemAt(getIndexOf(item));
		} catch (IllegalArgumentException e) {
			//cannot occur : getIndexOf returns a position in the
			//correct range
			assert false;
		}
	  }
	  
	  /**
	   * Remove the given item at the given position from this directory.
	   *
	   * @param position
	   *        The position from the item to remove.
	   * @post  The item is removed, and all elements to its
	   *        right are shifted left by 1 position.
	   *        | for each I in position+1..getNbItems():
	   *        |   new.getItemAt(I-1) == getItemAt(I)
	   * @post  The number of items has decreased.
	   *        | new.getNbItems() == getNbItems() - 1
	   * @throws IllegalArgumentException [must]
	   *         The given position is not positive or exceeds the number
	   *         of items registered in this directory. 
	   *         | (position < 1) || (position > getNbItems())
	   */
	  @Model private void removeItemAt(int position) throws IllegalArgumentException{
		try {
			items.remove(position-1);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException();
		}
	  }
	  
	  /**
	   * Move the item at the given position to the correct position,
	   * yielding an ordered list. (The given item is the only item 
	   * out of place.)
	   * 
	   * @param position
	   *        The position of the given item
	   * @pre   The directory must can have the item at the given position
	   *        | canHaveAsItem(getItemAt(position))
	   * @pre   Each item at a position before the given position must be
	   *        a valid item at its position.
	   *        | for each I in 1..(position-1):
	   *        | hasValidItemAt(I)
	   * @pre   Each item at a position at least two positions after the
	   *        given position must be a valid item at its position.
	   *        | for each I in (position+2)..getNbItems():
	   *        | hasValidItemAt(I)
	   * @pre   If the given position is not the last position in the
	   *        directory, the directory must can have the item at the
	   *        position after the given position.
	   *        | if (position < getNbItems())
	   *        | then canHaveAsItem(getItemAt(position+1)
	   * @pre   If the given position is not the first or last position
	   *        in the directory, the item at the position after the 
	   *        given position must not be ordered before the item at
	   *        the position before the given position.
	   *        | if (position > 1) && (position < getNbItems())
	   *        | then (! getItemAt(position+1).
	   *        |               isOrderedBefore(getItemAt(position-1)))
	   * @post  Some items can be at a new position (in fact items
	   *        can be shifted one position to the right or the left).
	   *        | for each I in 1..getNbItems() :
	   *        |   for some J in 1..getNbItems():
	   *        |      new.getItemAt(J) == getItemAt(I)
	   * @post  The directory has a valid (ordered) list of items.
	   *        | new.hasValidItems()       
	   * @throws IllegalArgumentException [must]
	   *         The given position is not positive or exceeds the number
	   *         of items registered in this directory. 
	   *         | (position < 1) || (position > getNbItems())
	   */
	  @Raw public void moveItemAtToSortedPosition(int position)
	                                 throws IllegalArgumentException{
		DiskItem item = getItemAt(position);
	    removeItemAt(position);
		try {
			addToItems(item);
		} catch (IllegalAddException e) {
			//cannot occur since
			//@pre canHaveAsItem(getItemAt(position))
		}
	  }

	/**
	 * Remove the given item from this directory. 
	 * Also updates the modification time and enforces writability.
	 *
	 * @param  item
	 *         The item to remove
	 * @effect Remove the given item from this directory.
	 *         | removeFromItems(item)        
	 * @effect The new modification time of this directory is updated.
	 *         | setModificationTime()
	 * @throws DiskItemNotWritableException [must]
	 *         This directory is not writable.
	 *         | !isWritable()
	 */
	@Model void removeFromItemsAndUpdateModificationTime(@Raw DiskItem item) 
	     throws DiskItemNotWritableException,NoSuchItemException {
	    if (!isWritable())
	        throw new DiskItemNotWritableException(this);
	    removeFromItems(item);
	    setModificationTime();
	}

	  /**
	   * Return the index of the first item in this directory, that is 
	   * not ordered before the given item.
	   *
	   * @param   item
	   *          The item to compare with.
	   * @pre     The given item must have an effective name.
	   *          | item.getName() != null
	   * @return The index of the first item not before the name of 
	   *         the given item.
	   *         | result == getIndexOfFirstItemNotBefore(item.getName())
	   * @throws IllegalArgumentException [must]
	   *         The given item is not effective.
	   *         | item == null
	   * @O      Logaritmic in the number of items.
	   *         | log(getNbItems())
	   */
	  private int getIndexOfFirstItemNotBefore(@Raw DiskItem item)
	              throws IllegalArgumentException {
    	    try {
	      return getIndexOfFirstItemNotBefore(item.getName());
    	    } catch (NullPointerException e){
    	    	  assert item == null;
    	    	  throw new IllegalArgumentException();
    	    } 
	  }

	  /**
	   * Return the index of the first item in this directory,  
	   * ordered not before (an item with) the given name.
	   *
	   * @param   name
	   *          The name to compare with.
	   * @return  The resulting index is in between 1 and the
	   *          number of items registered in this directory plus 1.
	   *          | (result >= 1) && (result <= getNbItems()+1)
	   * @return  The resulting index exceeds the number of items in this
	   *          directory, or the item registered at that index is not
	   *          ordered before the given name.
	   *          | (result == getNbItems()+1) ||
	   *          | (! getItemAt(result).isOrderedBefore(name))
	   * @return  The resulting index is 1 or the item registered in
	   *          this directory at a position before the resulting
	   *          index is ordered before the given name.
	   *          | (result == 1) ||
	   *          | (getItemAt(result-1).isOrderedBefore(name))
	   * @throws IllegalArgumentException [must]
	   *         The given name is not effective.
	   *         | name == null
	   * @O      Logaritmic in the number of items.
	   *         | log(getNbItems())
	   */
	  private int getIndexOfFirstItemNotBefore(String name)
                  throws IllegalArgumentException {
		  if (name == null)
			      throw new IllegalArgumentException();

          // Binary search:
          int low = 1;
	      int high = getNbItems();
	      int middle = 0;
	      while (low <= high) {
	    	    middle = (low+high)/2; 	    
	        DiskItem middleItem = getItemAt(middle);
	        if (middleItem.isOrderedBefore(name)) {
	          low = middle + 1;
	        } else {
	          high = middle - 1;
	        }
	      }
	      return high+1;
	  }
	  
    /**
	 * Check whether the item registered at the given position is a
	 * valid item for this directory.
	 *
	 * @param   position
	 *          The position of the item to be checked.
	 * @return  False if this directory can not have the item 
	 *          registered at the given position.
	 *          | if (! canHaveAsItem(getItemAt(position)))
	 *          |   then result == false
	 * @return  Otherwise, false if the given position exceeds 1 and
	 *          the item registered at the given position is ordered
	 *          strictly before the item registered at the position-1;
	 *          True otherwise.
	 *          | else if ( (position > 1) &&
	 *          |           (getItemAt(position).isOrderedBefore
	 *          |               (getItemAt(position-1))) )
	 *          |   then result == false
	 *          |   else result == true
	 * @throws  IllegalArgumentException [must]
	 *          The given position is not positive or exceeds the 
	 *          number of items registered in this directory.
	 *        | (position < 1) || (position > getNbItems())
	 */
    @Raw public boolean hasValidItemAt(int position)
    	                            throws IllegalArgumentException{
    	  if (! canHaveAsItem(getItemAt(position))) {
    		  return false;
    	  }
    	  try {
    		  return !(getItemAt(position).isOrderedBefore(
    					          getItemAt(position-1)));
    	  } catch (IllegalArgumentException e){
    		  assert position == 1;
    		  return true;
    	  }
    } 
  
   /**
	* Check whether an item with the given name is registered 
	* in this directory (ignoring case).
	*
	* @param   name
	*          The name to be checked.
	* @return  True if an item with the given name (ignoring case)
	*          is registered at some position in this directory; 
	*          false otherwise.
	*        | result ==
	*        |   (for some I in 1..getNbItems():
	*        |      getItemAt(I).getName().equalsIgnoreCase(name))
    */
   @Raw public boolean exists(String name) {
	   for (int i=1;i<=getNbItems();i++) {
		   if (getItemAt(i).getName().equalsIgnoreCase(name)){
			   return true;
		   }
	   }
	   return false;
   }
	  /**
	   * Check whether the given item is registered in this directory.
	   * 
  	   * @param item
	   *        The item to be checked.
	   * @return True if an item equal to the given item is registered at some
	   *         position in this directory;
	   *         false otherwise.
	   *         | result == 
	   *         |    for some I in 1..getNbItems():
	   *         |        getItemAt(I) == item
	   */
     @Raw public boolean hasAsItem(@Raw DiskItem item) {
    	   try {
         for (int i=1; i<=getNbItems(); i++) {
    	       if (getItemAt(i)==item)
        	     return true;
         }
        	return false;
       } catch (IllegalArgumentException e) {
			return false;
       }
	 }  
    
	  /**
	   * Check whether this directory has valid items.
	   *
	   * @return  True if all items in this directory are valid
	   *          items for this directory; false otherwise.
	   *          | result ==
	   *          |   for each I in 1..getNbItems():
	   *          |     hasValidItemAt(I)
	   */
	 @Raw public boolean hasValidItems() {
		for (int pos=1;pos <= getNbItems();pos++) {
			if (! hasValidItemAt(pos)){
				return false;
			}
		}
		return true;
	 }    
	 
	  /**
	   * Check whether this directory can have the given item as one of
	   * its items.
	   *
	   * @param   item
	   *          The item to be checked.
	   * @return  False if this directory is terminated
	   *          | if (this.isTerminated())
	   *          | then result == false
	   * @return  Otherwise, false if the given item is not effective
	   *          | else if (item == null)
	   *          |  then result == false
	   * @return  Otherwise, false if the given item is terminated
	   *          | else if (item.isTerminated())
	   *          |  then result == false
	   * @return  Otherwise, false if the given item has no effective name
	   *          | else if (item.getName()==null)
	   *          |  then result == false
	   * @return  Otherwise, false if the given item is the same as
	   *          this directory, or if the given disk item is the direct
	   *          or indirect parent directory of this directory
	   *          | else if (item.equalsOrIsDirectOrIndirectParentOf(this))
	   *          |  then result == false
	   * @return  Otherwise, if this directory has the given item as one of
	   *          its items, true if that item has this directory as its
	   *          parent directory, and if that item has a unique name in
	   *          this directory (ignoring case)
	   *          | else if (hasAsItem(item))
	   *          |  then result == 
	   *          |   ((item.getParentDirectory() == this) &&
	   *          |    (for each I in 1..getNbItems:
	   *          |      if ((getItemAt(I) != null) && 
	   *                     (item.getName().equalsIgnoreCase(getItemAt(I).getName())))
	   *                 then getItemAt(I) == item))
	   * @return  Otherwise true if the name of the given item does not
	   *          exist in this directory, and if the given item is a 
	   *          root item or the parent directory of the given disk
	   *          item is writable; False otherwise.
	   *          | else result == !exists(item.getName()) && 
	   *          | (item.isRoot() || item.getParentDirectory().isWritable())
	   */
	  @Raw public boolean canHaveAsItem(@Raw DiskItem item) {
		 if (this.isTerminated())
			 return false;
		 if (item==null)
			 return false;
		 if (item.isTerminated())
			 return false;
		 if (item.getName() == null)
			 return false;
		 if (item.equalsOrIsDirectOrIndirectParentOf(this))
			 return false;
		 if (hasAsItem(item)) {
			 for (int position=1;position<=getNbItems();position++){
				 if ((getItemAt(position) != null) && 
				     (item.getName().equalsIgnoreCase(getItemAt(position).getName())) &&
				     (getItemAt(position) != item))
					 return false;
			 }
			 return item.getParentDirectory()==this;
		 }
		 return (!exists(item.getName())) && 
		        (item.isRoot() || item.getParentDirectory().isWritable());		 
	  }


    /**
     * Check whether this directory has a valid number of registered items.
     * 
     * @return True if the number of items is positive; false otherwise. 
     *         | result == getNbItems() >= 0
     */
	@Raw public boolean hasValidNbItems() {
		return getNbItems() >= 0;
	}

	/**
	 * Return the number of items of this directory.
	 */
	@Raw public int getNbItems() {
		try {
			return items.size();
		} catch (NullPointerException e) {
			assert items == null;
			return 0;
		}
	}
    
	/**
	 * Return the item registered at the given position in this directory.
	 * 
	 * @param position
	 *        The position of the item to be returned.
	 * @throws IllegalArgumentException [must]
	 *         The given position is not positive or exceeds the number
	 *         of items registered in this directory. 
	 *         | (position < 1) || (position > getNbItems())
	 * @O Constant time. | 1
	 */
	@Raw
	public DiskItem getItemAt(int position) throws IllegalArgumentException {
		try {
			return items.get(position - 1);
		} catch (IndexOutOfBoundsException e) {
			// (implicitly) propagating the IndexOutOfBoundsException
			// would be perfectly OK if the assignment didn't prohibit it.
			throw new IllegalArgumentException("Index out of bounds!");
		} catch (NullPointerException e) {
			assert items==null;
			throw new IllegalArgumentException();
		}
	}
    
	/**
	 * Variable referencing a list collecting all items contained by this
	 * directory.
	 * 
	 * @invar items references an effective list. 
	 *        | items != null
	 * @invar Each element in the list references an effective item. 
	 *        | for each I in 0..items.size() - 1:
	 *        |   items.get(I) != null
	 * @invar Each element in the list references a non-terminated item.
	 *        | for each I in 0..items.size() - 1:
	 *        | !items.get(I).isTerminated()
	 * @invar Each element in the list (except the first element)
	 *        references an item that has a name which (ignoring case)
	 *        comes after the name of the immediately preceeding element,
	 *        in lexicographic order. 
	 *        | for each I in 1..items.size() - 1:
	 *        |   items.get(I).isOrderedAfter(items.get(I-1))
	 * @invar Each element in the list references an item that references
	 *        back to this directory.
	 *        | for each I in 1..items.size() - 1:
	 *        | !items.get(I).getParentDirectory() == this
	 */	
	private final List<DiskItem> items = new ArrayList<DiskItem>();
}
