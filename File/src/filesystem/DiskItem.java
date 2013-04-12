package filesystem;

import java.util.Date;

import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

import filesystem.exception.*;

/**
 * A class of disk items.
 * 
 * @invar	Each disk item must have a properly spelled name.
 * 			| hasValidName()
 * @invar	Each disk item must have a proper creation time.
 *        	| hasValidCreationTime()
 * @invar   	Each disk item must have a proper modification time.
 *          	| hasValidModificationTime()
 * @invar   	Each disk item must have a proper parent directory.
 *          	| hasValidParentDirectory()
 */
public abstract class DiskItem implements DiskItemInterface{

	/**********************************************************
	* Constructors
	**********************************************************/
	    
	/**
	 * Initialize a new root disk item with given name and writability.
	 * 
	 * @param  name
	 *         The name of the new disk item.
	 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX@param  writable
	 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX        The writability of the new disk item.
	 * @post   The new disk item is a root disk item.
	 *         | new.isRoot()
	 * @effect The new disk item has the given name (if legal) or a
	 *         legal name.
	 *         | setName(name) 
	 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXX@effect The new disk item has the given writability.
	 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXX        | setWritability(writable)
	 * @post   The creation time is initialized to some time during 
	 *         constructor execution.
	 *         | (new.getCreationTime().getTime() >= 
	 *         |             System.currentTimeMillis()) &&
	 *         | (new.getCreationTime().getTime() <=
	 *         |             (new System).currentTimeMillis())
	 * @post   The new file has no time of last modification.
	 *         | new.getModificationTime() == null     
	 */
	@Model protected DiskItem(String name/*, /*boolean writable*/) {
		setName(name);
		/*setWritability(writable);*/
	}
	
	protected boolean isRoot()
	{
		return false;
	}
	/**
	 * Initialize a new disk item with given parent directory, name 
	 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX and writability.
	 *   
	 * @param  parent
	 *         The parent directory of the new disk item.
	 * @param  name
	 *         The name of the new disk item.  
	 *XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX @param  writable
	 *XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX         The writability of the new disk item.
	 * @post   The given directory is registered as the parent 
	 *         directory of this item.
	 *         | new.getParentDirectory() == parent
	 * @post   The number of items registered in the given directory 
	 *         is incremented with 1.
	 *         | (new parent).getNbItems() == parent.getNbItems() + 1
	 * @post   The given item is added to the items registered
	 *         in the given directory.
	 *         | (new parent).hasAsItem(this)
	 * @post   All items registered in the given directory, that are 
	 *         ordered after the given item are shifted one position 
	 *         to the right.
	 *         | for each I in 1..parent.getNbItems():
	 *         |   if (parent.getItemAt(I).isOrderedAfter(name))
	 *         |     then (new parent).getItemAt(I+1) == parent.getItemAt(I)
	 * @effect The new modification time of this directory is updated.
	 *         | (new parent).setModificationTime()         
	 * @post   If the given name is a legal name for a disk item
	 *         and the given name does not occur in the given directory,
	 *         the name of this disk item is set to the given name.
	 *         Otherwise the name of this disk item is set to
	 *         a valid name.
	 *         | if (isValidName(name) && !parent.exists(name)
	 *         |      then new.getName().equals(name)
	 *         |      else new.hasValidName()
	 *XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX @post   The new disk item has the given writability.
	 *XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX         | new.isWritable() == writable
	 * @post   The creation time is initialized to some time during 
	 *         constructor execution.
	 *         | (new.getCreationTime().getTime() >= 
	 *         |             System.currentTimeMillis()) &&
	 *         | (new.getCreationTime().getTime() <=
	 *         |             (new System).currentTimeMillis())
	 * @post   The new disk item has no time of last modification.
	 *         | new.getModificationTime() == null
	 * @throws DiskItemNotWritableException [must]
	 *         The given directory is not writable.
	 *         | !parent.isWritable()
	 * @throws IllegalArgumentException [must]
	 *         The given directory is not effective or the new disk item
	 *         can not have the given valid name as its name and the given
	 *         writable parent directory as its parent directory.
	 *         | parent == null || 
	 *         | (parent.isWritable() && isValidName(name) &&
	 *         |  !canHaveAsNameInParentDirectory(name,parent))
	 */
	 @Model protected DiskItem(Directory parent, String name) 
	              throws IllegalArgumentException, DiskItemNotWritableException{
	   if ((parent == null) ||
		   (parent.isWritable() && !canHaveAsNameInParentDirectory(name,parent)))
	     throw new IllegalArgumentException();
	   setNameForParentDirectory(name,parent);
	   try {
		setParentDirectory(parent);
	   } catch (IllegalArgumentException e1) {
		 // cannot occur 
	    	 assert false;
	   }
	   try {
		parent.addToItemsAndUpdateModificationTime(this);
	   } catch (DiskItemNotWritableException e) {
		   //cannot occur
		   assert false;
	   } catch (IllegalAddException e) {
		   //cannot occur
		   assert false;
	   }
	 }
	    
	/**********************************************************
	 * delete/termination
	 **********************************************************/

	/**
	 * Check whether this disk item is already terminated.
	 */
	@Raw public boolean isTerminated() {
		return this.isTerminated;
	}

	/**
	 * Check whether this disk item can be deleted.
	 * 
	 * @return False if either:
	 *         - this disk item is already terminated
	 *         - this disk item is not writable
	 *         - this disk item is not a root, and its parent
	 *           directory is not writable;
	 *         Undefined otherwise.
	 *       | if ( isTerminated() || !isWritable() ||
	 *       |      (!isRoot() && !getParentDirectory().isWritable()) )
	 *       |   then result == false
	 */
	public boolean canBeTerminated() {
		// when the result is undefined according to the specification,
		// the implementation returns true.
		return !isTerminated();
	}

	/**
	 * Terminate this disk item.
	 * 
     * @post This disk item is terminated.
     *       | new.isTerminated()
     * @effect If this disk item is not a root, this disk item is
     *         removed from its parent directory.
     *         | if (!isRoot())
     *         | then getParentDirectory().
     *         |          removeFromItemsAndUpdateModificationTime(this)
	 * @throws ImpossibleDeleteException [must]
	 * 		   This disk item can not be deleted.
	 * 		   | ! canBeTerminated()
	 */
	public void terminate() throws ImpossibleDeletionException {
		if (!canBeTerminated()) {
			throw new ImpossibleDeletionException(this);
		}
		try {
			getParentDirectory().removeFromItemsAndUpdateModificationTime(this);
		} catch (NullPointerException e) {
		   // this item is a root item and can thus not
		   // be removed from its parent directory.
		   //assert isRoot();
		} catch (DiskItemNotWritableException e) {
		   // cannot occur: canBeDeleted() implies 
		   //               getParentDirectory().isWritable()
		   assert false;
		} catch (NoSuchItemException e) {
		  // cannot occur: this item is not a root item
		  // and thus must have a parent directory.
		  assert false;
		}
		try {
		  setIsTerminated(true);
		} catch (IllegalArgumentException e) {
			assert false;
		}
	}
	
	/**
	 * Check whether this disk item can have the given terminated-state
	 * as ist terminated-state.
	 * @param terminatedState
	 *        The terminated-state to check.
	 * @return True if this disk item is not terminated, or if the given
	 *         terminated-state is true. 
	 *         | !isTerminated || terminatedState
	 */
	public boolean canHaveAsTerminationState(boolean terminatedState) {
		return (!isTerminated || terminatedState);
	}

	/**
	 * Set the terminated-state for this disk item according to the
	 * given flag.
	 * 
	 * @param flag
	 *        The flag to be registered.
	 * @post The terminated-state of this disk item is set according
	 *       to the given flag.
	 *       | new.isTerminated() == flag
	 * @throws IllegalArgumentException [must]
	 *         If this disk item is already terminated, then the disk
	 *         item must remain terminated.
	 *         | !canHaveAsTerminationState(flag)  
	 */
	private void setIsTerminated(boolean flag) throws IllegalArgumentException {
		if (!canHaveAsTerminationState(flag)) {
			throw new IllegalArgumentException();
		}
		this.isTerminated = flag;
	}

	/**
	 * Variable registering whether or not this disk item 
	 * has been terminated.
	 * 
	 */
	private boolean isTerminated = false;

	/**********************************************************
	 * name
	 **********************************************************/

	/**
	 * Checks whether the name of this item equals or is lexicographically 
	 * ordered after the given name, ignoring case.
	 * 
	 * @param name
	 *        The name to compare with
	 * @return True if the given name is effective, this item
	 *         has an effective name and the name of this item comes
	 *         strictly after the given name, ignoring case; 
	 *         false otherwise.
	 *       | result == (name != null) && (getName() != null) &&
	 *       |           (getName().compareToIgnoreCase(name) > 0)
	 */
	@Raw public boolean isOrderedAfter(String name) {
		return (name != null) && (getName() != null)
			&& (getName().compareToIgnoreCase(name) > 0);
	}

	/**
	 * Checks whether the name of this item equals or is lexicographically 
	 * ordered before the given name, ignoring case.
	 * 
	 * @param name
	 *        The name to compare with
	 * @return True if the given name is effective, this item
	 *         has an effective name and the name of this item comes
	 *         strictly before the given name, ignoring case; 
	 *         false otherwise.
	 *       | result == (name != null) && (getName() != null) &&
	 *       |           (getName().compareToIgnoreCase(name) < 0)
	 */
	@Raw public boolean isOrderedBefore(String name) {
		return (name != null) && (getName() != null)
			&& (getName().compareToIgnoreCase(name) < 0);
	}

	/**
	 * Checks whether this item is ordered after the given other item
	 * according to the lexicographic ordering of their names,
	 * ignoring case.
	 * 
	 * @param other
	 *        The item to compare with
	 * @return True if the given other item is effective, and the name
	 *         of this item is lexicographically ordered after the name
	 *         of the given other item;
	 *         false otherwise.
	 *       | result == (other != null) && 
	 *       |           isOrderedAfter(other.getName())
	 */
	@Raw public boolean isOrderedAfter(DiskItem other) {
		return (other != null) && isOrderedAfter(other.getName());
	}

	/**
	 * Checks whether this item is ordered before the given other item
	 * according to the lexicographic ordering of their names,
	 * ignoring case.
	 * 
	 * @param other
	 *        The item to compare with
	 * @return True if the given other item is effective, and the name
	 *         of this item is lexicographically ordered before the name
	 *         of the given other item;
	 *         false otherwise.
	 *       | result == (other != null) && 
	 *       |           isOrderedBefore(other.getName())
	 */
	@Raw public boolean isOrderedBefore(DiskItem other) {
		return (other != null) && isOrderedBefore(other.getName());
	}

	/**
	 * Return the name of this disk item.
	 */
	@Raw public String getName() {
		return name;
	}

	/**
	 * Check whether the given name is a legal name for a disk item.
	 *
	 * @return	True if the given string is effective, if it is not
	 * 			empty and if it only consists of letters, digits, dots,
	 * 			hyphens and underscores; false otherwise.
	 * 			| result ==
	 * 			|	(name != null) && name.matches("([a-zA-Z_0-9.-])+")
	 */
	public static boolean isValidName(String name) {
		return (name != null && name.matches("([a-zA-Z_0-9.-])+"));
	}
		
	/**
	 * Check whether the name of this disk item can be changed into the
	 * given name.
	 * 
	 * @return  True if this disk item is not terminated, the given 
	 *          name is a valid name for a disk item, this disk item
	 *          is writable, and either this item is a root item or
	 *          the given name does not cause multiple items having
	 *          the same name in the parent directory;
	 *          false otherwise.
	 *          | result == !isTerminated() && isWritable() && 
	 *          |           isValidName(name) &&
	 *          |           ( isRoot() || 
	 *          |             !getParentDirectory().exists(name) ||
	 *          |             getParentDirectory().getItem(name) == this )
	 */
	public boolean canAcceptAsNewName(String name) {
	  try {
	    return (!isTerminated() && isValidName(name) &&
	    		    ( 
	    			  !getParentDirectory().exists(name) ||
	    			  getParentDirectory().getItem(name) == this ) );
	  } catch (NoSuchItemException e) {
	    // cannot occur: getItem(name) can only throw this 
	    // exception when !exists(name)
	    assert false;
	    return false;
	  }
	}	

	/**
	 * Check whether this disk item has a properly spelled name.
	 *
	 * @return	True if and only if the name of this disk item
	 *           is a valid name.
	 * 			| result == isValidName(getName())
	 */
	@Raw public boolean hasValidName() {
		return isValidName(getName());
	}

	/**
	 * Set the name of this disk item to the given name.
	 *
	 * @param	name
	 * 			The new name for this disk item.
	 * @effect  If this disk item can accept the given name as
	 *          its name, the name of this disk item is set to
	 *          the given name.
	 *          Otherwise the name of this disk item remains
	 *          unchanged.
	 *          | if (canAcceptAsNewName(name))
	 *          | then setName(name)
	 *          | else new.getName().equals(getName())
	 * @effect  If this disk item can accept the given name as
	 *          its name, the modification time of this disk item is 
	 *          updated.
	 *          | if (canAcceptAsNewName(name))
	 *          | then setModificationTime()
	 * @effect  If this disk item is not a root item and this disk item
	 *          can accept the given name as its name, this disk item 
	 *          may have been shifted to the left or right in the
	 *          sequence of items registered in its parent directory.
	 *          | if (canAcceptAsNewName(name) && !isRoot())
	 *          | then let parent = getParentDirectory() in
	 *          | (new parent).hasValidItems()
	 * @throws  DiskItemNotWritableException
	 *          This disk item is not writable.
	 *          | !isWritable() [must]
	 */
	public void changeName(String name) throws DiskItemNotWritableException{
	  if (canAcceptAsNewName(name)) {
	    setModificationTime();
        if (isRoot()) {
    	      setName(name);
	    } else {
		  try {
		    Directory parent = getParentDirectory();
		    int position = parent.getIndexOf(this);
		    setName(name);
		    parent.moveItemAtToSortedPosition(position);			
		  } catch (NoSuchItemException e) {
			// cannot occur
			assert false;
		  }
	    }
	  }
	}

	/**
	 * Return the name for a new disk item which is to be used
	 * when the given name is not valid.
	 *
	 * @return A valid disk item name.
	 *         | isValidName(result)
	 */
	@Raw public String getDefaultName() {
		return "new_item";
	}

	/**
	 * Set the name of this disk item to the given name taking into
	 * account the given (parent) directory.
	 *
	 * @param   name
	 * 		    The new name for this disk item.
	 * @param   parent
	 *          The directory in which the new name of this disk
	 *          item must not occur
     * @pre     This disk item is not terminated.
     *          | ! isTerminated()          
	 * @pre     The given directory is an effective directory.
	 *          | directory != null
	 * @post    If the given name is a legal name for a disk item
	 *          and the given name does not occur in the given directory,
	 *          the name of this disk item is set to the given name.
	 *          Otherwise the name of this disk item is set to
	 *          a valid name.
	 *          | if (isValidName(name) && !parent.exists(name)
	 *          |      then new.getName().equals(name)
	 *          |      else new.hasValidName()
	 */
	@Raw private void setNameForParentDirectory(String name,
			     Directory parent) {
		String nameForParent = name;
		if (!isValidName(nameForParent)) {
			nameForParent = getDefaultName();
		}
		while (parent.exists(nameForParent)){
			nameForParent = nameForParent+'_';
		}
		setName(nameForParent);
	}	
	
	/**
	 * Set the name of this disk item to the given name.
	 *
	 * @param   name
	 * 		   The new name for this disk item.
     * @pre     This disk item is not terminated.
     *          | ! isTerminated()
	 * @post    If the given name is a legal name for a disk item,
	 *          the name of this disk item is set to the given name.
	 *          Otherwise the name of this disk item is set to
	 *          a valid name.
	 *          | if (isValidName(name))
	 *          |      then new.getName().equals(name)
	 *          |      else new.hasValidName()
	 */
	@Model @Raw private void setName(String name) {
		if (isValidName(name)) {
			this.name = name;
		}
		else {
			this.name = getDefaultName();
		}
	}

	/**
	 * Variable referencing the name of this file.
	 */
	private String name;

	/**********************************************************
	 * creationTime
	 **********************************************************/

	/**
	 * Return the time at which this disk item was created.
	 */
	@Raw public Date getCreationTime() {
		return creationTime == null ? null : new Date(creationTime.getTime());
	}

	/**
	 * Return whether this disk item has a valid creation time.
	 *
	 * @return True if and only if this disk item has an effective
	 *         creation time which does not lie in the future.
	 *         | result == (getCreationTime() != null) &&
	 *         | (getCreationTime().getTime() <=
	 *         |              System.currentTimeMillis())
	 */
	@Raw public boolean hasValidCreationTime() {
		Date creationTime = getCreationTime();
		return creationTime != null
			&& creationTime.getTime() <= System.currentTimeMillis();
	}

	/**
	 * Variable referencing the time of creation.
	 */
	private final Date creationTime = new Date();

	/**********************************************************
	 * modificationTime
	 **********************************************************/

	/**
	 * Return the time at which this disk item was last modified, 
	 * that is at which the name or contents was last changed.
	 * If this disk item has not yet been modified after
	 * construction, null is returned.
	 */
	@Raw public Date getModificationTime() {
		return modificationTime == null ? null : new Date(modificationTime
			.getTime());
	}

	/**
	 * Return whether this disk item has a valid modification time.
	 *
	 * @pre    This disk item has a valid creation time.
	 *         | hasValidCreationTime()
	 * @return True if and only if this disk item either has no
	 *         effective modification time, or the modification time
	 *         lies between the creation time and the current time.
	 *         | result == (getModificationTime() == null) ||
	 *         | ( (getModificationTime().getTime() >=
	 *         |                   getCreationTime().getTime()) &&
	 *         |   (getModificationTime().getTime() <=
	 *         |                   System.currentTimeMillis()))
	 */
	@Raw public boolean hasValidModificationTime() {
		Date modificationTime = getModificationTime();

		return (modificationTime == null)
			|| ((modificationTime.getTime() >= getCreationTime().getTime()) && 
				(modificationTime.getTime() <= System.currentTimeMillis()) );
	}

	/**
	 * Set the modification time of this disk item to the current time.
	 *
	 * @pre    This disk item is not terminated.
     *         | ! isTerminated()
	 * @post   The new modification time is effective.
	 *         | new.getModificationTime() != null
	 * @post   The new modification time lies between the system
	 *         time at the beginning of method execution and
	 *         the system time at the end of method execution.
	 *         | (new.getModificationTime().getTime() >=
	 *         |                    System.currentTimeMillis()) &&
	 *         | (new.getModificationTime().getTime() <=
	 *         |                    (new System).currentTimeMillis())
	 */
	@Model protected void setModificationTime() {
		modificationTime = new Date();
	}

	/**
	 * Variable referencing the time of the last modification,
	 * possibly null.
	 */
	private Date modificationTime = null;

	/**
	 * Return whether this disk item and the given other disk item
	 * have an overlapping use period.
	 *
	 * @param other
	 *        The other disk item to compare with.
	 * @pre   The given other disk item is an effective disk item.
	 *        | other != null
	 * @return False if one or both disk items do not have a modification
	 *         time.
	 *         Otherwise, true if the respective open intervals from
	 *         creation time to the modification time overlap.
	 *        | if ((getModificationTime() == null)||
	 *        |       other.getModificationTime() == null)
	 *        |    then result == false
	 *        |    else result ==
	 *        | (getCreationTimeTime().before(other.getModificationTime()) &&
	 *        |     other.getCreationTime().before(getModificationTime()))
	 */
	public boolean hasOverlappingUsePeriod(DiskItem other) {
		if ((getModificationTime() == null)
			|| other.getModificationTime() == null) {
			return false;
		}
		return (getCreationTime().before(other.getModificationTime()) && other
			.getCreationTime().before(getModificationTime()));
	}






	
	/**********************************************************
	 * parent directory
	 **********************************************************/	
	
	/**
	 * Return the root item to which this item directly or indirectly
	 * belongs. In case this item is a root item, the item itself is 
	 * the result.
	 * 
	 * @return If this item is a root item, this item is returned;
	 *         Otherwise the root to which the parent item of this 
	 *         item belongs is returned.
	 *         | result.isRoot() && 
	 *         | result.equalsOrIsDirectOrIndirectParentOf(this)
	 */
	 public DiskItem getRoot() {
		 if (isRoot()) {
		   return this;
		 } else {
		   return getParentDirectory().getRoot();
		 }
     }
				
	/**
	 * Move this disk item to a given directory.
	 * 
	 * @param  target
	 *         The target directory.
     * @effect If this disk item is not a root, this disk item is
     *         removed from its parent directory.
     *         | if (!isRoot())
     *         | then getParentDirectory().
     *         |          removeFromItemsAndUpdateModificationTime(this)
     * @effect This disk item is added to the target directory.
     *         | target.addToItemsAndUpdateModificationTime(this)
    	 * @effect The modification time is updated.
	 *         | setModificationTime()
	 * @post   The given directory is registered as the parent directory 
	 *         of this item.
	 *         | new.getParentDirectory() == target
	 * @throws IllegalArgumentException [must]
	 *         The given target directory is not effective, or the parent
	 *         directory of this disk item is the given target directory.
	 *         | (target == null) || 
	 *         | (target == getParentDirectory())
 	 */
	public void move(Directory target) throws IllegalArgumentException, 
                                              IllegalAddException,
                                              DiskItemNotWritableException {
	  if ( (target == null) || (getParentDirectory() == target) )
		  throw new IllegalArgumentException();
	  if (!target.isWritable())
		  throw new DiskItemNotWritableException(target);
	  if (!target.canHaveAsItem(this))
		  throw new IllegalAddException(target,this);
	  
	  if (!isRoot()) {
	    try {
		  getParentDirectory().removeFromItemsAndUpdateModificationTime(this);
		  // throws DiskItemNotWritableException when
		  // !getParentDirectory().isWritable()
		} catch (NoSuchItemException e) {
		  // cannot occur 
	    	  assert false;
	    }
      }
	  try {
	    target.addToItemsAndUpdateModificationTime(this);
	  } catch (DiskItemNotWritableException e) {
	     // cannot occur
		 assert false;
	  } catch (IllegalAddException e) {
		 //cannot occur
		 assert false;
	  }
	  try {
		setParentDirectory(target);
	  } catch (IllegalArgumentException e) {
		  //cannot occur
		  assert false;
  	  }
	  setModificationTime();
	}
		
	 

			  	 
	
	/**
	 * Check whether this item is equal to the given item or is a direct
	 * or indirect parent of the given item.
	 * 
	 * @param other
	 *        The disk item to check.
	 * @return True if the given item is equal to this item or
	 *         if the given item is effective and this item equals or is
	 *         the direct or indirect parent of the parent of the given
	 *         item;
	 *         False otherwise.
	 *       | result == (this == item) || 
	 *       |            ( item != null && 
	 *       |              equalsOrIsDirectOrIndirectParentOf(
	 *       |                        item.getParentDirectory()) )  
	 */
	@Raw public boolean equalsOrIsDirectOrIndirectParentOf(@Raw DiskItem item) {
		return ((this == item) || 
			    ( (item != null) && 
			    	  (equalsOrIsDirectOrIndirectParentOf(item.getParentDirectory()))
			    	) );
	}



	/**
	 * Check whether this item belongs to a proper parent 
	 * directory.
	 *
	 * @return True if this item can have its parent directory
	 *       	as parent directory.
	 *        | result == canHaveAsParentDirectory(getParentDirectory())
	 */
	@Raw public boolean hasValidParentDirectory() {
		return canHaveAsParentDirectory(getParentDirectory());
	}

	/** 
	 * Check whether this disk item can have the given directory as
	 * its parent directory.
	 * 
	 * @param   directory
	 *          The directory to check.
	 * @return  If this disk item is terminated, true if the given
	 *          directory is not effective, false otherwise.
	 *          | if (this.isTerminated())
	 *          |    then result == (directory == null)
	 * @return  Otherwise, if the given directory is not effective,
	 *          true if this disk item is a root item, or the parent
	 *          directory of this disk item is writable, false otherwise.
	 *          | else if (directory == null)
	 *          |   then result ==
	 *          |     (this.isRoot() ||
	 *          |      this.getParentDirectory().isWritable());
	 * @return  Otherwise, false if the given directory is terminated.
	 *          | else if (directory.isTerminated())
	 *          |   then result == false
	 * @return  Otherwise, false if the given directory is the same as
	 *          this disk item, or if this disk item is the direct or
	 *          indirect parent directory of the given directory.
	 *          | else if (equalsOrIsDirectOrIndirectParentOf(directory))
	 *          |   then result == false
	 * @return  Otherwise, if the parent directory of this disk item is
	 *          the same as the given directory, true if that directory
	 *          has this disk item as one of its items, false otherwise.
	 *          | else if (getParentDirectory() == directory)
	 *          |   then result == directory.hasAsItem(this)
	 * @return  Otherwise, true if the given directory is writable, and
	 *          if this disk item is a root item, or the parent directory
	 *          of this disk item is writable, false otherwise.
	 *          | else result ==
	 *          |   (directory.isWritable() &&
	 *          |   (isRoot() || getParentDirectory().isWritable()))
	 */
	@Raw public boolean canHaveAsParentDirectory(@Raw Directory directory) {
		if (this.isTerminated())
			return (directory == null);
		if (directory == null)
			return (this.isRoot() || this.getParentDirectory().isWritable());
		if (directory.isTerminated())
			return false;
		if (equalsOrIsDirectOrIndirectParentOf(directory))
			return false;
		if (getParentDirectory() == directory)
			return directory.hasAsItem(this);
		return directory.isWritable()
			&& (isRoot() || getParentDirectory().isWritable());
	}

	/**
	 * Check whether this disk item can have the given name as its
	 * name and the given parent directory as its parent directory
	 * 
	 * @param   name
	 *          The name to check.
	 * @param   directory
	 *          The directory to check.
	 * @return  False if the given name is not a valid name for a
	 *          disk item.
	 *          | if (! isValidName(name))
	 *          |   then result == false
	 * @return  Otherwise, false if this disk item can not have the
	 *          given directory as its parent directory.
	 *          | if (! canHaveAsParentDirectory(directory))
	 *          |   then result == false
	 * @return  Otherwise, true if the given directory is not effective,
	 *          or the given directory is the parent directory of this
	 *          disk item, or if the given directory does not have a
	 *          disk item with the given name; false otherwise.
	 *          | else result ==
	 *          |   ((directory == null) ||
	 *          |    (getParentDirectory() == directory) ||
	 *          |    (! directory.exists(name)))
	 */
	@Raw public boolean canHaveAsNameInParentDirectory(String name,
			@Raw Directory directory) {
		if (!isValidName(name))
			return false;
		if (!canHaveAsParentDirectory(directory))
			return false;
		return  (directory == null) 
		        || (getParentDirectory() == directory)
			    || ( !directory.exists(name));
	}

	/**
	 * Set the parent directory of this item to the given directory.
	 *
	 * @param  directory
	 *         The new parent directory for this item.
     * @pre    This disk item is not terminated.
     *         | ! isTerminated()
	 * @post   The parent directory of this item is set to the given 
	 *         directory.
	 *         | new.getParentDirectory() == directory
	 * @throws IllegalArgumentException [must]
	 *         This item cannot have the given directory as its
	 *         parent directory.
	 *         | ! canHaveAsParentDirectory(parentDirectory)
	 */
	@Raw
	protected void setParentDirectory(Directory parentDirectory)
			throws IllegalArgumentException {
		if (!canHaveAsParentDirectory(parentDirectory)) {
			throw new IllegalArgumentException("Inappropriate item!");
		}
		this.parentDirectory = parentDirectory;
	}

	/**
	 * Return the parent directory (if any) to which this item
	 * applies.
	 */
	@Raw public Directory getParentDirectory() {
		return parentDirectory;
	}

	/**
	 * Variable referencing the directory (if any) to which this 
	 * disk item belongs.
	 * 
	 * @invar This diskitem must can have the referenced directory
	 *        as its parent directory.
	 *        | canHaveAsParentDirectory(parentDirectory)
	 */
	private Directory parentDirectory;
	
	@Override
	public boolean canBeRecursivelyDeleted() {
		for(DiskItem
		return false;
	}
	@Override
	public void deleteRecursive() throws ImpossibleDeletionException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean canHaveAsName(String name) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasProperName() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasProperCreationTime() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasProperModificationTime() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasEffectiveParentDirectory() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getAbsolutePath() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean hasProperParentDirectory() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean canAcceptAsNewParentDirectory(Directory directory) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean moveViolatesWritability(Directory target) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public long getTotalDiskUsage() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void copy(Directory target) throws DiskItemNotWritableException {
		// TODO Auto-generated method stub
		
	}
}
