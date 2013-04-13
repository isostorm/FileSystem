package filesystem;

import filesystem.exception.DiskItemNotWritableException;

public class DirectoryLink extends Link{

	protected DirectoryLink(Directory parent, String name, RealDiskItem referral)
			throws IllegalArgumentException, DiskItemNotWritableException {
		super(parent, name, referral);
		// TODO Auto-generated constructor stub
	}

}
