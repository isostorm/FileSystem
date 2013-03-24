package filesystem;
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
import be.kuleuven.cs.som.annotate.Raw;

public abstract class DiskItem {
	/**
	 * @invar This name of this disk item must be a valid name
	 * 			| isValid(name)
	 * 
	 */
	private String name;
	
	/**
	 * @param  writable
	 * 		   The writable state of this new file
	 * @effect The name of this new file is set to the given name
	 * 		   | setName(name)
	 * @effect The writable state of this new disk item is set to the given flag
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
	 * Initialize a new disk item with a given name
	 * 
	 * @effect A new disk item is initialized with the given name as its name
	 * 		   and true as its writable state
	 * 		   | this(name, true)
	 * 
	 */
	public DiskItem(String name)
	{
		
		this(name, true);
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
	 * @post   the new name is equal to the given name.
	 * 		  | if canHaveAs(name) 
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

		if(canHaveAsName(name)){
			this.name = name;
		}


		if (this.name == null){
			this.name = "X";
		}
		else {
			setCurrentModificationTime();
		}

	}
	/**
	 * Check whether the given the name is valid for this diskitem
	 * 
	 * @return True if and only if the given name contains only alphabetical and numerical characters and dots, 
	 * 		  	hyphens and underscores, and the given name is of at least length 1 
	 *        	and the given name is effective.
	 *        	| result == name.matches("[A-Za-z0-9._-]+")) 
	 */
	public boolean canHaveAsName(String name){
		return name.matches("[A-Za-z0-9._-]+");
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
	@Raw @Basic
	public void setLastModificationTime(Date lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}
	/**
	 * Set the time of last modification of this file to the current time
	 * 
	 * @effect The time of last modification of this file is set to the current time
	 * 		   | setCurrentModificationTime(currentTime)
	 */
	@Raw @Basic
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
	
	protected boolean writable = true;
	
	/**
	 * Check whether this file is writable
	 * 
	 * Some methods have no effect when the file is not writable
	 */
	@Basic @Raw
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
	@Raw @Basic
	public void setWritable(boolean writable) {
		this.writable = writable;
	}
	
	private Directory directory;
	
	/**
	 * Set the parent directory of this disk item
	 * 
	 * @param dir
	 * 		  The parent directory that must contain this disk item
	 * @post  The directory of this disk item equals the given directory,
	 * 		  if the given directory is not effective, 
	 * 		  this disk item is considered a root item.
	 * 		  | new.getDirectory() == dir
	 */
	@Raw @Basic
	private void setDirectory(Directory dir){
		directory = dir;
	}
	/**
	 * Returns the parent directory of this disk item
	 * 
	 * @result The parent directory of this disk item.
	 *         | result == directory
	 * 
	 */
	@Raw @Basic
	public Directory getDirectory(){
			return directory;
	}
	/**
	 * Returns the root disk item of this disk item.
	 * 
	 * @return The root disk item of this disk item.
	 *         If this disk items has no parent directory, this disk item is returned.
	 *         Otherwise the root of the parent directory is returned.
	 *         | if(getDirectory() == null) then
	 *         |    result == this
	 *         | else
	 *         |    result == getDirectory().getRoot()
	 */
	public DiskItem getRoot()
	{
		if(getDirectory() == null)
		{
			return this;
		}
		else
		{
			return getDirectory().getRoot();
		}
	}
	/**
	 * Changes this disk item into a root disk item.
	 * @effect The parent directory is set to null.
	 *         | setDirectory(null)
	 */
	public void makeRoot()
	{
		move(null);
	}
	/**
	 * Checks if this disk items name precedes the other disk items name (non-case sensitive) lexicographically.
	 * 
	 * @param otherItem The other disk item to check against.
	 * @return True if the name of this item precedes the other disk items name lexicographically.
	 *         | if getName().compareToIgnoreCase(otherName) < 0 and otherItem != null then
	 *         |    result == true
	 *         | else
	 *         |    result == false
	 * 
	 */
	protected boolean precedes(String otherName)
	{
		String thisName = getName();
		return otherName != null && (thisName.compareToIgnoreCase(otherName) < 0);
	}
	/**
	 * Checks whether this disk item can be moved to the given target directory.
	 * 
	 * @param target The target directory to check against.
	 * @return True if the target exists
	 *         and the target doesn't already have a sub item with the same (case insensitive) name
	 *         and this disk item and the target are both not terminated
	 *         | result == (target != null && !target.exists(getName()) && !isTerminated() && !target.isTerminated())
	 */
	public boolean canMoveTo(Directory target)
	{
		if(isTerminated())
		{
			return false;
		}
		if(target == null)
		{
			return true;
		}
		else if (target.exists(getName()) || target.isTerminated())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	/**
	 * If this disk item can be moved to the target directory, the disk item will be moved to the target directory
	 * 
	 * @param target The target directory.
	 * @post The parent directory of this disk item is the target directory and the target directory contains this disk item.
	 *       | target.hasAsItem(this) and getDirectory() == target
	 * @throws InvalidParentException
	 *         If this disk item can't be moved to the target directory.
	 *         | not canMoveTo(target)
	 */
	public void move(Directory target) throws InvalidParentException
	{
		if(!this.canMoveTo(target))
		{
			System.out.println("Throw exception here");
			throw new InvalidParentException(target, this);
		}
		else
		{
			if(getDirectory() != null)
			{
				getDirectory().removeItem(this);
			}
			if(target != null)
			{
				target.addItem(this);
			}
			setDirectory(target);
		}
	}
	private boolean isTerminated;
	/**
	 * Checks whether this disk item is terminated or not.
	 * 
	 * @return Whether this disk item is terminated or not.
	 *         | result == isTerminated
	 */
	@Basic
	public boolean isTerminated()
	{
		return this.isTerminated;
	}
	/**
	 * Terminates this disk item and breaks the relation with its parent directory.
	 * 
	 * @Post Sets the terminated state of this disk item to true.
	 *       | isTerminated() == true
	 * @Effect If this disk item had a parent directory, this disk items parent directory will be set to null.
	 *       | if(getDirectory() != null) then
	 *       |    setDirectory(null)
	 * @Effect If this disk item had a parent directory,
	 *         this disk item will be removed from the sub items the parent directory has.
	 *       | if(getDirectory() != null) then
	 *       |    getDirectory().removeItem(this)
	 * 
	 */
	public void terminate()
	{
		makeRoot();
		isTerminated = true;
		
	}
}
