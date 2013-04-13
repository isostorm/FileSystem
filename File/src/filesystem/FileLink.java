package filesystem;

import filesystem.exception.DiskItemNotWritableException;

public class FileLink extends Link {
	/**
	 * Initialize this new link with a given parent, name and referral
	 * @param parent
	 * 		  The directory in which the link must be placed
	 * @param name
	 * 			The name of the new link
	 * @param referral
	 * 			The file to which this link has to refer
	 * @effect This file link is initialized as a link to refer the given file
	 * 			| super(parent, name, referral)
	 * @throws DiskItemNotWritableException
	 * 			The given parent is no writable
	 * 			| !getParent().isWritable
	 */
	public FileLink(Directory parent, String name, File referral) throws DiskItemNotWritableException{
		super(parent, name, referral);
		
	}
}
