package filesystem;

public abstract class Link extends DiskItem {

}
package filesystem;

public abstract class  Link {
	public Link(RealDiskItem referral){
		this.referral = referral;
	}
	
	private RealDiskItem referral;
}