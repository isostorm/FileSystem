package filesystem;

import filesystem.exception.DiskItemNotWritableException;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

public class RealDiskItem extends DiskItem {
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
	
	protected RealDiskItem(String name, boolean writable) {
		super(name);
		setWritability(writable);
	}
	public boolean canBeTerminated()
	{
		return super.canBeTerminated() && isWritable();
	}
	public boolean canAcceptAsNewName(String name)
	{
		return super.canAcceptAsNewName(name) && isWritable();
	}
	public void changeName(String name) throws DiskItemNotWritableException {
		if (!isWritable()) {
			  throw new DiskItemNotWritableException(this);
			        // NOTICE, the specification of the first assignment
			        // has been changed.
	         }
	}
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
