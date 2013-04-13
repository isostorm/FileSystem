package filesystem;

import filesystem.exception.DiskItemNotWritableException;
import filesystem.exception.ImpossibleDeletionException;

public abstract class  Link extends DiskItem{
	/**
	 * Initialize this new link with a given parent, name and referral
	 * @param parent
	 * 		  The directory in which the link must be placed
	 * @param name
	 * 			The name of the new link
	 * @param referral
	 * 			The diskitem to which this link has to refer
	 * @effect This link is initialized as a diskitem with given name and parent
	 * 			| super(parent, name)
	 * @throws DiskItemNotWritableException
	 * 			The given parent is no writable
	 * 			| !getParent().isWritable
	 */
	public Link(Directory parent, String name, RealDiskItem referral) throws DiskItemNotWritableException{
		super(parent, name);
		this.setReferral(referral);
	}
	/**
	 * Return the disk item to which this link reffers
	 * 
	 */
	public RealDiskItem getReferral() {
		return referral;
	}
	/**
	 * Set the disk item to which this link has to reffer
	 * @param referral
	 * 			The disk item to reffer to
	 */
	public void setReferral(RealDiskItem referral) {
		this.referral = referral;
	}

	private RealDiskItem referral;
	
	/**
	 * Check whether this link can be deleted
	 * @return True if and only if this file can be deleted
	 * 			| result == canBeTerminated
	 */
	@Override
	public boolean canBeRecursivelyDeleted() {
		return canBeTerminated();
	}
	/**
	 * Delete this link
	 * @post   This link is terminated
	 * 			| new.isTerminated()
	 * @throws ImpossibleDeletionException
	 * 			This link can't be recursively deleted
	 * 			!canBeRecursivelyDeleted()
	 */
	@Override
	public void deleteRecursive() throws ImpossibleDeletionException {
		terminate();
		
	}
	
	/**
     * Returns the total disk usage of this file (always 0)
     * 
     * @return Returns 0
     *			| result == 0
     */
    
	final public long getTotalDiskUsage()
	{
		return 0;
	}
}
