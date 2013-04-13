package filesystem;


import filesystem.exception.*;
import be.kuleuven.cs.som.annotate.*;


public abstract class RealDiskItem extends DiskItem {
	
	
	/**
	 * Initialize this real diskitem with a given parent, name and writability
	 * @param parent
	 * 		  The parent folder of this new real diskitem
	 * @param name
	 * 		  The name of this new diskitem
	 * @param writable
	 * 		  The writability of this new diskitem
	 * @post  The writability of this file equals the given writability
	 * 			| isWritable == writable
	 * @effect Initialize this new real diskitem as an diskitem with the given parent and name
	 * 			|super(parent, name)
	 * 			
	 * @throws DiskItemNotWritableException
	 * 			The parent of this new real diekitem is not writable
	 * 			| !parent.isWritable()
	 */
	protected RealDiskItem(Directory parent, String name, boolean writable)  
            throws IllegalArgumentException, 
            DiskItemNotWritableException
	{
		super(parent, name);
		if (!parent.isWritable())
			throw new DiskItemNotWritableException(parent);
		setWritability(writable);
		
	}
	/**
	 * Initialize this real diskitem with a given name and writability
	 * 
	 * @param name
	 * 		  The name of this new diskitem
	 * @param writable
	 * 		  The writability of this new diskitem
	 * @effect Initialize a new real diskitem with null as its parent, 
	 * 			the given name as its name and the given writability as its writability
	 * 			|this(null,name, writable)
	 * 			
	 */
	protected RealDiskItem(String name, boolean writable)
			throws IllegalArgumentException, 
            DiskItemNotWritableException
	{
		this(null, name, writable);
		
	}
	/**
	 * Check whether this real disk item can be terminated
	 * @return True if and only if this real disk item is writable and
	 * 			if the super method returns true
	 * 			| result == super.canBeTerminated() && isWritable()
	 */
	public boolean canBeTerminated()
	{
		return super.canBeTerminated() && isWritable();
	}
	/**
	 * Check whether this real disk item can accept the given name as its name
	 * @param name
	 * 		  The name to check
	 * @return True if and only if the super method returns true and this real diskitem is writable
	 * 			| result == super.canAcceptAsNewName(name) && isWritable()
	 */
	public boolean canAcceptAsNewName(String name)
	{
		return super.canAcceptAsNewName(name) && isWritable();
	}
	/**
	 * Change the name of this real diskitem
	 * @effect  The supermethod is invoked with the given name
	 * 			| super.changeName(name)
	 * @throws  DiskItemNotWritableException
	 * 			This real diskitem is not writable
	 * 			|!isWritable()
	 */
	public void changeName(String name) throws DiskItemNotWritableException {
		if (!isWritable()) {
			  throw new DiskItemNotWritableException(this);
			        // NOTICE, the specification of the first assignment
			        // has been changed.
	         }
		super.changeName(name);
	}
	/**********************************************************
	 * isWritable
	 **********************************************************/
	/**
	 * Check whether this file is writable.
	 */
	@Raw public boolean isWritable() {
		return isWritable;
	}
	 /**
	  * Variable registering whether or not this file is writable.
	 */
	
	private boolean isWritable;
	
	/**
	 * Set the writability of this disk item to the given writability.
	 *
	 * @param isWritable
	 *        The new writability
     * @pre    This disk item is not terminated.
     *         | ! isTerminated()  
	 * @post  The given writability is registered as the writability
	 *        for this disk item.
	 *        | new.isWritable() == isWritable
	 */
	public void setWritability(boolean isWritable) {
		this.isWritable = isWritable;
	}


}
