package filesystem;

public interface DirectoryInterface {
	/**
	 * A method which checks if a valid name has been entered
	 * 
	 * @return	True if the given string is valid
	 * 			False when not (p.e. dots,...)
	 * 			| result ==
	 * 			|	(name != null) && name.matches(""([a-zA-Z_0-9-])+")
	 * @see		DiskItem
	 */
	boolean canHaveAsName(String name);
}
