
public class Directory extends DiskItem{
	
	/**
	 * Initializes a new directory with the given name and writable state
	 * 
	 * @param  name
	 * 		   The name of the directory
	 * @param  writable
	 * 		   The writable state of the directory
	 * @effect Initialize a new disk item with the given name and writable state
	 * 		   | super(name, writable)
	 */
	public Directory(String name, boolean writable){
		super(name, writable);
	}
	
	/**
	 * Initializes a new directory with the given name
	 * 
	 * @param  name
	 * 		   The name of the directory
	 * @effect Initialize a new disk item with the given name and true as its writable state
	 * 		   | this(name, true)
	 */
	public Directory(String name){
		super(name, true);
	}
}
