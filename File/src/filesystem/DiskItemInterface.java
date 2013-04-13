package filesystem;

import java.util.Date;

import filesystem.exception.*;
public interface DiskItemInterface {
	/**********************************************************
	 * delete/termination
	 **********************************************************/

    /**
     * Check whether this item has been terminated [raw].
     */
    boolean isTerminated();
    
    /**
     * Check whether this disk item can be deleted.
     * 
     * @return False if either:
     *         - this disk item is already terminated
     *         - this disk item has an effective parent directory, 
     *           and its parent directory is not writable;
     *         Undefined otherwise.
     *       | if ( isTerminated() ||
     *       |      (hasEffectiveParentDirectory() && !getParentDirectory().isWritable()) )
     *       |   then result == false
     */
    boolean canBeTerminated();
    
    /**
     * Delete this disk item.
     * 
     * @post   If this disk item cannot be deleted, the postcondition
     *         cannot be realized.
     *       | if (!canBeTerminated())
     *       |   then false         
     * @post   This disk item is terminated.
     *       | new.isTerminated()
     * @post   If this disk item has an effective parent directory, 
     *         this disk item is removed from its parent directory.
     *       | if (hasEffectiveParentDirectory()) 
     *       |   then let oldParent = getParentDirectory() in
     *       |          !(new oldParent).hasAsItem(this)
     * @post   If this disk item has an effective parent directory, 
     *         the new modification time of its parent directory is effective.
     *       | if (!hasEffectiveParentDirectory())  
     *       |   then let oldParent = getParentDirectory() in 
	 *       |          (new oldparent).getModificationTime() != null
	 * @post   If this disk item has an effective parent directory, 
	 *         the new modification time of its parent directory 
	 *         lies between the system
	 *         time at the beginning of method execution and the
	 *         system time at the end of method execution.
     *       | if (hasEffectiveParentDirectory())  
     *       |   then let oldParent = getParentDirectory() in
	 *       | ((new oldParent).getModificationTime().getTime() >= 
	 *       |                    System.currentTimeMillis()) &&
	 *       | ((new oldParent).getModificationTime().getTime() <= 
	 *       |                    (new System).currentTimeMillis())
	 *  
     * @throws ImpossibleDeleteException
	 * 			This disk item can not be deleted.
	 * 			| ! canBeTerminated()
     */
    void terminate() throws ImpossibleDeletionException;
    
    
    /**
     * Check whether this disk item can be recursively deleted.
     * 
     * @return True if:
     *         - this disk item is not already terminated, and
     *         - in case this item has an effective parent directory,
     *           this parent directory is writable, and
     *         - this disk item, as well as all the disk items of which
	 *           this disk item is directly or indirectly parent of are 
	 *           writable;
	 *         False otherwise.
	 *       | result ==
	 *       |    ( !isTerminated()                        &&
	 *       |      ( !hasEffectiveParentDirectory()   ||
	 *       |        getParentDirectory().isWritable() )  &&
	 *       |      ( for each item in RealDiskItem:
	 *       |          ( !this.equalsOrIsDirectOrIndirectParentOf(item) ||
	 *       |            item.isWritable() ) ) )
     */
    boolean canBeRecursivelyDeleted();

    /**
     * Delete this disk item recursively.
     * 
     * @post   If this disk item cannot be deleted recursively, the postcondition
     *         cannot be realized.
     *       | if (!canBeRecursivelyDeleted())
     *       |   then false         
	 * @post	This disk item, as well as all the disk items of which
	 *          this disk item is directly or indirectly parent of
	 *          are destroyed as well.
	 * 			| for each item in DiskItem:
	 * 			|	if (this.equalsOrIsDirectOrIndirectParentOf(item))
	 * 			|		then (new item).isTerminated()
     * @post   If this disk item has an effective parent directory, 
     *         this disk item is removed from its parent directory.
     *       | if (hasEffectiveParentDirectory()) 
     *       |   then let oldParent = getParentDirectory() in
     *       |          !(new oldParent).hasAsItem(this)
     * @post   If this disk item has an effective parent directory, 
     *         the new modification time of its parent directory is effective.
     *       | if (!hasEffectiveParentDirectory())  
     *       |   then let oldParent = getParentDirectory() in 
	 *       |          (new oldparent).getModificationTime() != null
	 * @post   If this disk item has an effective parent directory, 
	 *         the new modification time of its parent directory 
	 *         lies between the system
	 *         time at the beginning of method execution and the
	 *         system time at the end of method execution.
     *       | if (hasEffectiveParentDirectory())  
     *       |   then let oldParent = getParentDirectory() in
	 *       | ((new oldParent).getModificationTime().getTime() >= 
	 *       |                    System.currentTimeMillis()) &&
	 *       | ((new oldParent).getModificationTime().getTime() <= 
	 *       |                    (new System).currentTimeMillis())
	 *  
     * @throws ImpossibleDeleteException
	 * 			This disk item can not be recursively deleted.
	 * 			| ! canBeRecursivelyDeleted()
     */    
    void deleteRecursive() throws ImpossibleDeletionException;
    
	/**********************************************************
	 * name
	 **********************************************************/

    /**
	 * Return the name of this disk item [raw].
	 */
	String getName();

	/**
	 * Check whether the given name is a legal name for this disk item,
	 * not taking into account its parent directory [raw].
	 * 
	 * NB. Taking into account the parent directory, in particular the 
	 * uniqueness of the name therein, leads to circular reasoning. 
	 * Uniqueness is imposed (indirectly) in hasProperParentDirectory.
	 * 
	 * @return	False if the given string is not effective, or if it is
	 * 			empty or if it contains characters other than letters, 
	 *          digits, underscores, dashes, dots; undefined otherwise.
	 * 			| if ((name == null) || !name.matches("([a-zA-Z_0-9.-])+"))
	 *          |   then result == false
	 */
	boolean canHaveAsName(String name);
	
	/**
	 * Check whether the name of this disk item can be changed into the
	 * given name.
	 * 
	 * @return	False if either:
	 *          - this item is terminated
	 *          - this item cannot have the given name as name (ignoring
	 *            its parent directory)
	 *          - this item has an effective parent directory that already
	 *            has another disk item with the given name.
	 *          Undefined otherwise.
	 *          | if ( isTerminated()                                   || 
	 *          |     !canHaveAsName(name)                              ||
	 *          |      ( !hasEffectiveParentDirectory() && 
	 *          |        getParentDirectory().exists(name) &&
	 *          |        (getParentDirectory().getItem(name) != this ) ) )
	 *          | then result == false
	 * NB encapsulating writability restrictions as done for move is also ok,
	 * but may seem a little strange because at this level writability is no
	 * issue...
	 */
	boolean canAcceptAsNewName(String name);
	
	/**
	 * Check whether this disk item has a properly spelled name,
	 * not taking into account its parent directory [raw].
	 * 
	 * @return	True if this disk item can have its name as name;
	 * 			false otherwise.
	 * 			| result == canHaveAsName(getName())
	 */
	boolean hasProperName();
	
	/**
	 * Change the name of this disk item to the given name.
	 * 
	 * @param	name
	 * 			The new name for this disk item.
	 * @post	If this disk item can accept the given name as its new 
	 *          name, the name of this disk item is set to the given name.
	 *        | if (canAcceptAsNewName(name))
	 *        |   then new.getName().equals(name)
	 *        |   else new.getName().equals(getName())
	 * @post   If this disk item can accept the given name as its new name,
	 *         then the new modification time is effective.
	 *         | if (canAcceptAsNewName())
	 *         |   then new.getModificationTime() != null
	 * @post   If this disk item can accept the given name as its new name,
	 *         then the new modification time lies between the system
	 *         time at the beginning of method execution and
	 *         the system time at the end of method execution.
	 *         | if (canAcceptAsNewName())
	 *         |   then ((new.getModificationTime().getTime() >= 
	 *         |                    System.currentTimeMillis()) &&
	 *         |         (new.getModificationTime().getTime() <= 
	 *         |                    (new System).currentTimeMillis()))
	 * @post    If this item has an effective parent directory, and the item in its
	 *          new state is ordered before the item in its old state,
	 *          this item may have been shifted to the left in the 
	 *          sequence of items registered in its parent directory.
     *        | if (hasEffectiveParentDirectory() && new.isOrderedBefore(getName()))
     *        |   then ((new getParentDirectory()).getIndexOf(this) <=
     *        |     getParentDirectory().getIndexOf(this))
	 * @post    If this item has an effective parent directory, and the item in its
	 *          new state is ordered after the item in its old state,
	 *          this item may have been shifted to the right in the 
	 *          sequence of items registered in its parent directory.
     *        | if (hasEffectiveParentDirectory() && new.isOrderedAfter(getName()))
     *        |   then ((new getParentDirectory()).getIndexOf(this) >=
     *        |     getParentDirectory().getIndexOf(this))
     * @post    If this item has an effective parent directory, items registered before 
     *          this item in the sequence of items in its parent 
     *          directory, that are ordered after this item in its new 
     *          state, are shifted one position to the right.
     *        | if (hasEffectiveParentDirectory())
     *        |   then let
     *        |   oldIndex = getParentDirectory().getIndexOf(this)
     *        | in
     *        |   for each I in 1..oldIndex-1:
     *        |     if (new.isOrderedBefore(
     *        |                        getParentDirectory().getEntryAt(I)))
     *        |       then (new getParentDirectory()).getEntryAt(I+1) ==
     *        |             getParentDirectory().getEntryAt(I)
     * @post    If this item has an effective parent directory, items registered after 
     *          this item in the sequence of items in its parent 
     *          directory, that are ordered before this item in its new 
     *          state, are shifted one position to the left.
     *        | if (hasEffectiveParentDirectory())
     *        |   then let
     *        |   oldIndex = getParentDirectory().getIndexOf(this)
     *        | in
     *        |   for each I in oldIndex+1..
     *        |                 getParentDirectory().getNbItems():
     *        |     if (new.isOrderedAfter(
     *        |                     getParentDirectory().getItemAt(I)))
     *        |       then (new getParentDirectory()).getItemAt(I-1) ==
     *        |             getParentDirectory().getItemAt(I)
	 * @throws   DiskItemNotWritableException
	 *          At this level, no statements can be done about the conditions:
	 *          see subclasses.
	 *        | true
	 */
	void changeName(String name) 
	            throws DiskItemNotWritableException;
		
	/**
	 * Checks whether the name of this item is lexicographically 
	 * ordered after the given name, ignoring case [raw].
	 * 
	 * @param name
	 *        The string to compare with
	 * @return True if the given name is effective, this item
	 *         has an effective name and the name of this item comes 
	 *         strictly after the given name, ignoring case; 
	 *         false otherwise.
	 *       | result == (name != null) && (getName() != null) &&
	 *       |           (getName().compareToIgnoreCase(name) > 0)
	 */
	boolean isOrderedAfter(String name);

	/**
	 * Checks whether the name of this item is lexicographically 
	 * ordered before the given name, ignoring case [raw].
	 * 
	 * @param name
	 *        The string to compare with
	 * @return True if the given name is effective, this item
	 *         has an effective name and the name of this item comes 
	 *         strictly before the given name, ignoring case; 
	 *         false otherwise.
	 *       | result == (name != null) && (getName() != null) &&
	 *       |           (getName().compareToIgnoreCase(name) < 0)
	 */
	boolean isOrderedBefore(String name);
	
	/**
	 * Checks whether this item is ordered after the given other item
	 * according to the lexicographic ordering of their names, 
	 * ignoring case [raw].
	 * 
	 * @param other
	 *        The item to compare with
	 * @return True if the given other item is effective, and the name
	 *         of this item is lexicographically ordered after the name
	 *         of the given other item; false otherwise.
	 *       | result == (other != null) && 
	 *       |           isOrderedAfter(other.getName())
	 */
	boolean isOrderedAfter(DiskItem other);

	/**
	 * Checks whether this item is ordered before the given other item
	 * according to the lexicographic ordering of their names, ignoring
	 * case [raw].
	 * 
	 * @param other
	 *        The item to compare with
	 * @return True if the given other item is effective, and the name
	 *         of this item is lexicographically ordered before the name
	 *         of the given other item; false otherwise.
	 *       | result == (other != null) && 
	 *       |           isOrderedBefore(other.getName())
	 */
	boolean isOrderedBefore(DiskItem other);


	/**********************************************************
	 * creationTime
	 **********************************************************/
	
	/**
	 * Return the time at which this disk item was created [raw].
	 */
	Date getCreationTime();
	
	/**
	 * Return whether this disk item has a proper creation time [raw].
	 * 
	 * @return True if and only if this disk item has an effective 
	 *         creation time which does not lie in the future.
	 *         | result == (getCreationTime() != null) && 
	 *         | (getCreationTime().getTime() <= 
	 *         |              System.currentTimeMillis()+/-delta)
	 * NB. The delta above depends on the actual execution.
	 */
	boolean hasProperCreationTime();	

	/**********************************************************
	 * modificationTime
	 **********************************************************/
	
	/**
	 * Return the time at which this disk item was last modified, 
	 * that is  at which the name or contents (if any) were
	 * last changed. 
	 * If this disk item has not yet been modified after 
	 * construction, null is returned [raw].
	 */
	Date getModificationTime();
	
	/**
	 * Return whether this disk item has a proper modification 
	 * time [raw].
	 * 
	 * @pre    This disk item has a proper creation time.
	 *         | hasProperCreationTime()
	 * @return True if and only if this disk item either has no 
	 *         effective modification time, or the modification time 
	 *         lies between the creation time and the current time.
	 *         | result == (getModificationTime() == null) || 
	 *         | ( (getModificationTime().getTime() >= 
	 *         |                   getCreationTime().getTime()) &&
	 *         |   (getModificationTime().getTime() <= 
	 *         |                   System.currentTimeMillis())+/-delta )
	 * NB. The delta above depends on the actual execution.
	 */
	public boolean hasProperModificationTime();

	/**
	 * Return whether this disk item and the given other disk item 
	 * have an overlapping use period.
	 * 
	 * @param other
	 *        The other disk item to compare with.
	 * @pre   The given other disk item is an effective disk item.
	 *        | other != null
	 * @return False if one or both disk items do not have a 
	 *         modification time.
	 *         Otherwise, true if the respective open intervals from 
	 *         creation time to the modification time overlap.
	 *        | if ((getModificationTime() == null)|| 
	 *        |       other.getModificationTime() == null)
	 *        |    then result == false
	 *        |    else result ==
	 *        | (getCreationTimeTime().before(other.getModificationTime()) &&
	 *        |     other.getCreationTime().before(getModificationTime()))
	 */
	boolean hasOverlappingUsePeriod(DiskItem other);
	
	/********************
	 * PARENT DIRECTORY *
	 *******************/

	  /**
	   * Return the parent directory to which this item applies [raw].
	   */
	  Directory getParentDirectory();

	  /**
	   * Check whether this disk item has an effective parent 
	   * directory [raw].
	   * 
	   * @return True if the parent directory is effective;
	   *         false otherwise.
	   *       | result == getParentDirectory() != null
	   */
	  boolean hasEffectiveParentDirectory();

	  /**
	   * Return the root item to which this item directly or indirectly
	   * belongs. In case this item has no effective parent directory, the item itself is 
	   * the result.
	   * 
	   * @return If this item has no effective parent directory, this item is returned;
	   *         Otherwise the root to which the parent item of this 
	   *         item belongs is returned.
	   *       | if (!hasEffectiveParentDirectory())
	   *       |   then result == this
	   *       |   else result == getParentDirectory().getRoot()
	   * or alternatively:
	   *       | !result.hasEffectiveParentDirectory() && 
	   *       | result.equalsOrIsDirectOrIndirectParentOf(this)
	   */
	  DiskItem getRoot();

	  /**
	   * Check whether the given item is equal to this item or directly
	   * or indirectly belongs to this item [raw].
	   * 
	   * @param other
	   *        The disk item to check.
	   * @return False if the given item is terminated; otherwise
	   *         True if the given item is equal to this item or
	   *         if the given item has an effective parent directory
	   *         and this item is the direct or indirect parent of 
	   *         the parent of the given item;
	   *         False otherwise (i.e. also if the above 
	   *         specification would lead to infinite recursion or if
	   *         the given item is not effective).
	   *       | result == !item.isTerminated()                        && 
	   *       |            ( this.equals(item)                     || 
	   *       |            ( item != null && 
	   *       |              item.hasEffectiveParentDirectory() &&
	   *       |              equalsOrIsDirectOrIndirectParentOf(
	   *       |                        item.getParentDirectory()) ))
	   */
	  boolean equalsOrIsDirectOrIndirectParentOf(DiskItem item);
	  
	  /**
	   * Return the absolute path of this disk item.
	   *  
	   * @return The absolute path of the parent directory (if 
	   *         there is one), followed by a slash, followed by the
	   *         name of this disk item.
	   *       | if (hasEffectiveParentDirectory())
	   *       |   then result.equals(getParentDirectory().getAbsolutePath()+"/"+getName())
	   *       |   else result.equals("/"+getName())
	   */
	  String getAbsolutePath();
	  
	  
	  /** 
	   * Check whether this disk item can have the given real directory as
	   * the its parent directory [raw].
	   * @param   directory
	   *          The real directory to check.
	   * @return  False if either
	   *          - the given real directory is terminated
	   *          - the given real directory does not have proper items
	   *          - the given real directory has another item with the same
	   *            name as this disk item.
	   *          Undefined otherwise.
	   *        | if ( directory != null                   &&
	   *        |      ( directory.isTerminated()       ||
	   *        |        !directory.hasProperItems()    ||
	   *        |        (directory.exists(getName())&&
	   *        |         !directory.hasAsItem(this)) ) )
	   *        |   then result == false
	   */
	  boolean canHaveAsParentDirectory(Directory directory);
	  
	  /**
	   * Check whether this item belongs to a proper parent 
	   * directory [raw].
	   *
	   * @return  True if this item can have its parent directory
	   *          (possibly not effective) as parent directory, and 
	   *          if in case this item has an effective parent directory, this item is 
	   *          registered as one of the items of that real directory; 
	   *          false otherwise.
	   *        | result ==
	   *        |   ( canHaveAsParentDirectory(getParentDirectory()) &&
	   *        |     ( !hasEffectiveParentDirectory() || 
	   *        |       getParentDirectory().hasAsItem(this) ) )
	   */
	  boolean hasProperParentDirectory();
	  
		/**
		 * Check whether this disk item can accept the given real directory
		 * as its new parent directory.
		 * 
		 * @param	directory
		 * 			The real directory to check.
		 * @return  True if this disk item is not terminated, 
		 *          the given real directory effective and not terminated,
		 *          this disk item can have the given real directory as its 
		 *          parent directory, and moving this disk item to the
		 *          given real directory does not violate writability 
		 *          restrictions; 
		 *          false otherwise.
		 * 			|	result ==
		 * 			|		(! isTerminated()) &&
		 * 			|		( (directory != null) &&
		 * 			|				(! directory.isTerminated() ) ) &&
		 * 			|		canHaveAsParentDirectory(directory) &&
		 *          |       !moveViolatesWritability(directory)
		 */
		boolean canAcceptAsNewParentDirectory(Directory directory);
		
		/**
		 * Indicate whether the move of this disk item to the given
		 * real directory violates writability restrictions.
		 * 
		 * @param target
		 *        The target directory of the prospective move.
		 * @return False if either:
		 *         - this item has an effective parent directory that
		 *           is not writable.
		 *         - the target directory is effective and not writable.
		 *         Undefined otherwise.
		 *       | if ( ( hasEffectiveParentDirectory() && 
		 *       |        !getParentDirectory().isWritable() ) ||
		 *       |      ( target != null && !target.isWritable() ) )
		 *       |   then result == false
		 */
		boolean moveViolatesWritability(Directory target);
	  
		
	  /**
	   * Move this disk item to a given real directory.
	   * 
	   * @param target
	   *        The target directory.
       * @post   If this disk item cannot accept the given target 
       *         directory as its directory, 
       *         the disk item cannot be moved to the given directory.
       *       | if (!canAcceptAsNewParentDirectory(target)) 
       * 	   |   then false
       * 
	   * @post   If this disk item has an effective parent directory,
	   *         this item is removed from its original parent 
	   *         directory, and all elements to its right are shifted 
	   *         left by 1 position.
	   *       | if (hasEffectiveParentDirectory())
	   *       | then 
	   *       |     let oldParentDirectory = getParentDirectory() in
	   *       |         for each I in 
	   *       |                  oldParentDirectory.getIndexOf(this) + 1..
	   *       |                         oldParentDirectory.getNbItems():
	   *       |         (new oldParentDirectory).getItemAt(I-1) == 
	   *       |                         oldParentDirectory.getItemAt(I)
	   * @post   If this disk item has an effective parent directory,
	   *         the number of items of the original parent directory 
	   *         has decreased.
	   *       | if (hasEffectiveParentDirectory())
	   *       | then 
	   *       |     let oldParentDirectory = getParentDirectory() in
	   *       |         (new oldParentDirectory).getNbItems() == 
	   *       |          oldParentDirectory.getNbItems() - 1
	   * @post   If this disk item has an effective parent directory,
	   *         the new modification time of its original
	   *         parent is effective.
	   *       | if (hasEffectiveParentDirectory())
	   *       | then 
   	   *       |     let oldParentDirectory = getParentDirectory() in
       *       |         (new oldParentDirectory).getModificationTime() != null
	   * @post   If this disk item has an effective parent directory,
	   *         the new modification time of its original parent
	   *         lies between the system time at the beginning
	   *         of method execution and the system time at the
	   *         end of method execution.
	   *       | if (hasEffectiveParentDirectory())
	   *       | then  
   	   *       |     let oldParentDirectory = getParentDirectory() in
       *       |         ((new oldParentDirectory).
       *       |                    getModificationTime().getTime() >= 
	   *       |                    System.currentTimeMillis()) &&
	   *       |          ((new oldParentDirectory).
	   *       |                    getModificationTime().getTime() <= 
	   *       |                    (new System).currentTimeMillis())
	   * 
       * @post    The given directory is registered as the parent directory 
       *          of this item.
       *       | new.getParentDirectory() == target
	   * @post    The number of items registered in the given directory is
	   *          incremented with 1.
	   *        | (new target).getNbItems() == target.getNbItems() + 1
	   * @post    The new item is added to the items registered
	   *          in the given directory.
	   *        | (new target).hasAsItem(this)
	   * @post    All items registered in the given directory, that 
	   *          are ordered after the new item are shifted one 
	   *          position to the right.
	   *        | for each I in 1..target.getNbItems():
	   *        |   if (target.getItemAt(I).isOrderedAfter(this))
	   *        |     then (new target).getItemAt(I+1) == target.getItemAt(I)
       * @post   The new modification time of the given directory is 
       *         effective.
	   *         | (new target).getModificationTime() != null
	   * @post   The new modification time of the given directory lies 
	   *         between the system time at the beginning of method 
	   *         execution andthe system time at the end of method 
	   *         execution.
	   *         | ((new target).getModificationTime().getTime() >= 
	   *         |                    System.currentTimeMillis()) &&
	   *         | ((new target).getModificationTime().getTime() <= 
	   *         |                    (new System).currentTimeMillis())
	   * 
	   * @post   The new modification time is effective.
	   *       | new.getModificationTime() != null
	   * @post   The new modification time lies between the system
	   *         time at the beginning of method execution and
	   *         the system time at the end of method execution.
	   *       | (new.getModificationTime().getTime() >= 
	   *       |                    System.currentTimeMillis()) &&
	   *       | (new.getModificationTime().getTime() <= 
	   *       |                    (new System).currentTimeMillis())
	   * 
	   * @throws IllegalArgumentException
	   *         The given target directory is not effective.
	   *       | target == null
	   * @throws DiskItemNotWritableException
	   *         The move violates writability restrictions.
	   *       | moveViolatesWritability(target)
	   */
	  void move(Directory target) 
	         throws IllegalArgumentException, 
	                DiskItemNotWritableException, IllegalAddException;
	
		/*********
		 * OTHER *
		 ********/

	  /**
	   * Calculate the total disk usage.
	   * 
	   * @return The result is positive.
	   *       | result >= 0
	   */
	  long getTotalDiskUsage();	  
	  /**
	   * Copy the disk item to the given target directory.
	   * The name of the copy starts with the name of this disk item,
	   * followed by "_copy". 
	   * 
	   * @param target 
	   *        The target directory.
	   * @throws DiskItemNotWritableException
	   *         The target directory is not writable
	   */
	  void copy(Directory target) throws DiskItemNotWritableException;

}
