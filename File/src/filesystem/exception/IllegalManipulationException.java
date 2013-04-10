package filesystem.exception;

import filesystem.DiskItem;

/**
 * A class for signalling illegal attempts to manipulate a disk item.
 */
public class IllegalManipulationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize this new illegal manipulation exception involving the
	 * given disk item.
	 * 
	 * @param	diskitem
	 * 			The disk item for the new illegal manipulation exception.
	 * @post	The disk item involved in the new illegal manipulation exception
	 * 			is set to the given disk item.
	 * 			| new.getDiskItem() == disk item
	 */
	public IllegalManipulationException(DiskItem diskitem) {
		this.diskitem = diskitem;
	}
	
	/**
	 * Return the disk item involved in this illegal manipulation exception.
	 */
	public DiskItem getDiskItem() {
		return diskitem;
	}
	
	/*
	 * Variable referencing the disk item for which manipulation was illegal.
	 */
	private final DiskItem diskitem;
}
