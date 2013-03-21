/**
 * A class of items on the hard disk involving a name, a content, a writable state, a creation time, 
 * a time of last modification, a maximum file size and a writable state
 * 
 * 
 * @author Frederic Huysentruyt, Mathias Benoit
 * @version 1.0
 */
import java.util.Date;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

public abstract class DiskItem {
	
	private String name;
	
	/**
	 * @param  writable
	 * 		   The writable state of this new file
	 * @effect The name of this new file is set to the given name
	 * 		   | setName(name, true)
	 * @effect The writable state of this new file is set to the given flag with the initial parameter as true
	 * 	       | setWritable(writable)
	 * @post   The creation time equals to a date object containing the current time
	 * 		   | creationTime == new Date()
	 */
	public DiskItem(String name, boolean writable)
	{
		
		setName(name);
		setWritable(writable);
		creationTime = new Date();
	}
		
	/**
	 * The name of a Disk item
	 * 
	 * @return The name of this disk item
	 */
	public String getName() {
		return name;
	}
	/**
	 * Set the name of this disk item to the given name
	 * 
	 * @param name 
	 * 		  The name to set.
	 * @post  If the given name contains only alphabetical and numerical characters and dots, 
	 * 		  hyphens and underscores, and the given name is of at least length 1 
	 *        and the given name is effective, the new name is equal to the given name.
	 * 		  | if name.matches("[A-Za-z0-9._-]+")) 
	 * 				then new.name == name
	 * @post  If the old name was not set,  the new name will be equal to "X".
	 * 		  | if(this.name == null)
	 * 				then new.name == "X"
	 * @post  If the old name was set, the modification time will be set to the current time.
	 * 		  | if(this.name != null) 
	 * 				then setCurrentModificationTime()
	 * @throws NotWritableException
	 * 		   The user has no rights to change the name of the file
	 * 		   | !isWritable()
	 * @see    p.44-45
	 */
	public void setName(String name) throws NotWritableException{

		if(!isWritable())
			throw new NotWritableException(this);

		if(name.matches("[A-Za-z0-9._-]+")){
			this.name = name;
		}


		if (this.name == null){
			this.name = "X";
		}
		else {
			setCurrentModificationTime();
		}

	}
	
		
	protected final Date creationTime;
	/**
	 * The creation time reveals the time when the file was created
	 * @return the creationTime of this file
	 */
	@Basic @Immutable
	public Date getCreationTime() {
		return creationTime;
	}
	
	protected Date lastModificationTime;
	
	/**
	 * The time of last modification reveals when this file was modified for the last time
	 * @return the lastModificationTime of this file
	 */
	@Basic
	public Date getLastModificationTime() {
		return lastModificationTime;
	}
	/**
	 * Set the time of last modification of this file to the given time
	 * 
	 * @param lastModificationTime 
	 * 		  The new time of last modification of this file
	 * @post  The time of last modification equals the given time
	 * 		  | new.getLastModificationTime() == lastModificationTime
	 */
	public void setLastModificationTime(Date lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	/**
	 * Set the time of last modification of this file to the current time
	 * 
	 * @effect The time of last modification of this file is set to the current time
	 * 		   | setCurrentModificationTime(currentTime)
	 */
	public void setCurrentModificationTime(){
		setLastModificationTime(new Date());
	}
	/**
	 * Check whether the user period of this file overlaps with the user period of the other file
	 * 
	 * @param  other
	 * 		   The file to compare with, whether the user period overlaps
	 * @return True if and only if the other file is effective and if both the other file and this file have an effective time
	 * 		   of last modification and if the creation time of this file is before the modification time of the other file
	 * 		   and if the creation time of the other file is before the modification time of this file
	 * 		   | if(other != null 
	 * 				&& this.lastModificationTime != null 
	 * 				&& other.lastModificationTime != null
	 * 				&&creationTime.before(other.lastModificationTime)
	 * 				&& other.creationTime.before(lastModificationTime))
	 * 					result==true 				
	 */
	public boolean hasOverlappingUsePeriod(File other){
		if(other != null && this.lastModificationTime != null && other.lastModificationTime != null){
			//( start1 <= end2 and start2 <= end1 )
			return (creationTime.before(other.lastModificationTime)&& other.creationTime.before(lastModificationTime));
			
		}
		return false;
	}
	
	protected boolean writable;
	
	/**
	 * Check whether this file is writable
	 * 
	 * Some methods have no effect when the file is not writable
	 */
	@Basic
	public boolean isWritable() {
		return writable;
	}
	
	/**
	 * Set the writable state to the given flag
	 * 
	 * @param writable 
	 * 		  The new writable state for this file
	 * @post  The new writable state of this file is equal to the given flag
	 * 		  | new.isWritable() == writable
	 *  
	 */
	public void setWritable(boolean writable) {
		this.writable = writable;
	}
	
		
}
