package filesystem.exception;

import filesystem.Directory;
import filesystem.DiskItem;

/**
 * A class for signalling illegal attempts to add disk items to a Directory.
 */
public class IllegalAddException extends IllegalManipulationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initialialize this new illegal add exception with given
	 * target directory and given disk item name.
	 * 
	 * @param	directory
	 * 			The directory for the new illegal add exception.
	 * @param	item
	 * 			The disk item name for the new illegal add exception.
	 * @post	    The directory for the new illegal add exception is
	 * 			set to the given directory.
	 * 			| new.getDirectory() == directory
	 * @effect   The new illegal add exception is an illegal 
	 *           manipulation exception with the given item.
	 *           | super(item)
	 */
	public IllegalAddException(Directory directory, DiskItem item) {
        super(item);
	    this.directory = directory;
	}

	/**
	 * Return the directory involved in this illegal add exception.
	 */
	public Directory getDirectory() {
		return directory;
	}

	/**
	 * Variable referencing the directory involved in this
	 * illegal add exception.
	 */
	private final Directory directory;
}