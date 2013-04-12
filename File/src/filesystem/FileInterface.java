package filesystem;

public interface FileInterface extends DiskItemInterface {
	/**
	 * Check whether the given name is a legal name for this file [raw].
	 * 
	 * @return	True if the given string is effective, if it is not
	 * 			empty and if it only consists of letters, digits, dots,
	 * 			hyphens and underscores; false otherwise.
	 * 			| result ==
	 * 			|	(name != null) && name.matches(""([a-zA-Z_0-9.-])+")
	 * @see		DiskItem
	 */
	boolean canHaveAsName(String name);

	/**
	 * Check whether this file can have the given real directory as
	 * the its parent directory [raw].
	 * 
	 * @return  False if either
	 *          - the given real directory is not effective (extra case above
	 *            the cases defined in DiskItem)
	 *          - the given real directory is terminated
	 *          - the given real directory does not have proper items
	 *          - the given real directory has another item with the same
	 *            name as this file.
	 *          True otherwise.
	 *        | result == !( directory == null             ||
	 *        |              directory.isTerminated()      ||
	 *        |              !directory.hasProperItems()   ||
	 *        |              (directory.exists(getName())&&
	 *        |              !directory.hasAsItem(this)) ) 
	 * @see	DiskItem
	 */
	boolean canHaveAsParentDirectory(Directory directory);
}
