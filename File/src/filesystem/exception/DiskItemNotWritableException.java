
package filesystem.exception;

import filesystem.DiskItem;

/**
 * A class for signalling illegal attempts to change a disk item
 * due to writability restrictions.
 */
public class DiskItemNotWritableException extends IllegalManipulationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize this new disk item not writable exception involving the
	 * given disk item.
	 * 
	 * @param	diskitem
	 * 			The disk item for the new disk item not writable exception.
	 * @post	The disk item involved in the new disk item not writable exception
	 * 			is set to the given disk item.
	 * 			| new.getDiskItem() == disk item
	 */
	public DiskItemNotWritableException(DiskItem diskitem) {
		super(diskitem);
	}
	
}
