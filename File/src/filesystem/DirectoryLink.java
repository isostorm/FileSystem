package filesystem;

import filesystem.exception.DiskItemNotWritableException;

public class DirectoryLink extends Link{

	/**
	 * Initialize this new link with a given parent, name and referral
	 * @param parent
	 * 		  The directory in which the link must be placed
	 * @param name
	 * 			The name of the new link
	 * @param referral
	 * 			The directory to which this link has to refer
	 * @effect This directory link is initialized as a link to refer the given directory
	 * 			| super(parent, name, referral)
	 * @throws DiskItemNotWritableException
	 * 			The given parent is no writable
	 * 			| !getParent().isWritable
	 */
	public DirectoryLink(Directory parent, String name, Directory referral) throws DiskItemNotWritableException{
		super(parent, name, referral);
		
	}

}
