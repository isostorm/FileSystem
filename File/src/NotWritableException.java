/**
 * A class for signaling illegal attempts to change a file.
 */

public class NotWritableException extends RuntimeException {

	
	private static final long serialVersionUID = 7422344796018755413L;
	
	private final DiskItem diskItem;
	/**
	 * Initialize this not writable exception with the given disk item
	 * 
	 * @param  item
	 * 		   The disk item whereby the exception was thrown
	 * @post   The disk item of this exception equals the given item
	 * 		   | new.getdiskItem() == item
	 * @effect A runtime exception is initialized with a message,
	 * 		   containing the name of the disk item
	 * 		   | super("The item \""+item.getName() +"\" is not writable")
	 */
	public NotWritableException(DiskItem item){
		super("The item \""+item.getName() +"\" is not writable");
		diskItem = item;
	}
	/**
	 * The disk item whereby the exception was thrown
	 */
	public DiskItem getDiskItem(){
		return diskItem;
	}
}
