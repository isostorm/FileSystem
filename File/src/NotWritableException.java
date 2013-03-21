
public class NotWritableException extends RuntimeException {

	
	private static final long serialVersionUID = 7422344796018755413L;

	public NotWritableException(DiskItem item){
		super("The item \""+item.getName() +"\" is not writable");
	}
	
}
