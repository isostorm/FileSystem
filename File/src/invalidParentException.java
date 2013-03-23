/*
 * A class for signaling that a disk item can't belong to a certain parent  
 */
public class invalidParentException extends RuntimeException {
	
	
	private static final long serialVersionUID = 1L;
	/**
	 * Initialize this invalid parent exception with the given parent and child
	 * @param parent
	 * 		  The parent to what you want to add the child
	 * @param child
	 * 		  The child to add to the parent
	 * @post  This parent equals the given parent
	 * 		  | new.getParent() == parent
	 * @post  This child equals the given child
	 * 		  | new.getChild() == child
	 */
	public invalidParentException(Directory parent, DiskItem child){
		this.parent = parent;
		this.child = child;
	}
	
	private final Directory parent;
	
	/**
	 * @return the parent
	 */
	public Directory getParent() {
		return parent;
	}

	/**
	 * @return the child
	 */
	public DiskItem getChild() {
		return child;
	}

	private final DiskItem child;
	
}
