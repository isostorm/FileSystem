
package filesystem.exception;

import filesystem.DiskItem;

/**
 * A class for signalling illegal attempts to delete a disk item.
 */
public class ImpossibleDeletionException extends IllegalManipulationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize this new impossible deletion exception involving the
	 * given disk item.
	 * 
	 * @param	diskitem
	 * 			The disk item for the new impossible deletion exception.
	 * @post	The disk item involved in the new impossible deletion exception
	 * 			is set to the given disk item.
	 * 			| new.getDiskItem() == disk item
	 */
	public ImpossibleDeletionException(DiskItem diskitem) {
		super(diskitem);
	}
}
