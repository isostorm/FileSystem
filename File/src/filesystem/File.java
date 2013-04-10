package filesystem;

import filesystem.exception.*;

import be.kuleuven.cs.som.annotate.Raw;
import be.kuleuven.cs.som.annotate.Model;

/**
 * A class of files.
 *
 * @invar	Each file must have a properly spelled name.
 * 			| hasValidName()
 * @invar	Each file must have a valid size.
 * 			| hasValidSize()
 * @invar   Each file must have a valid creation time.
 *          | hasValidCreationTime()
 * @invar   Each file must have a valid modification time.
 *          | hasValidModificationTime()
 * @invar   Each file must have a valid type.
 *          | hasValidType()         
 */
public class File extends DiskItem{

    /**********************************************************
     * Constructors
     **********************************************************/

    
    
    /**
     * Initialize a new file with given parent directory, name,
     * size and writability.
     *
     * @param  parent
     *         The parent directory of the new file.       
     * @param  name
     *         The name of the new file.
     * @param  size
     *         The size of the new file.
     * @param  writable
     *         The writability of the new file.
     * @param  type
     *         The type of the new file. 
     * @effect The new file has the given size
     *         | setSize(size)
     * @effect The new file is a disk item with the given
     *         name and writability.
     *         | super(parent,name,writable)
     * @post   The type of this new file is set to the given type.
     *         |new.getType() == type        
     */
    public File(Directory parent, String name, Type type,
    		       int size, boolean writable)
                       throws IllegalArgumentException, 
                              DiskItemNotWritableException,
                              IllegalAddException {
    	    super(parent,name,writable);
        setSize(size);
        this.type=type;
    }

    /**
     * Initialize a new writable, empty file with given parent directory
     * and name.
     *
     * @param  parent
     *         The parent directory of the new file.
     * @param  name
     *         The name of the new file.
     * @param  type
     *         The type of the new file.        
     * @effect This new file is initialized with the given name
     *         and the given parent directory, type, 
     *         the new file is empty and writable.
     *         | this(parent,name,type,0,true)
     */
    public File(Directory parent, String name, Type type)
                       throws IllegalArgumentException, 
                              DiskItemNotWritableException,
                              IllegalAddException {
        this(parent,name,type,0,true);
    }    
    
   /**
	* Return a textual representation of this file.
	* 
	* @return  The name of this file followed by a dot
	*          followed by the extension respresenting the
	*          type of this file.
	*          | result.equals(getName()+"."+getExtension())
	*/    
    public String toString(){
    	  return getName()+"."+getType();
    }
    
	/**********************************************************
	 * delete/termination
	 **********************************************************/
   /**
    * Check whether this disk item can be deleted.
    * 
    * @return False if either:
    *         - this disk item is already terminated
    *         - this disk item is not writable
    *         - this disk item is not a root, and its parent
    *           directory is not writable;
    *         True otherwise.
    *       | result == !( isTerminated() || !isWritable() ||
    *       |      (!isRoot() && !getParentDirectory().isWritable()) )
    * @see superclass
    */
   // public boolean canBeTerminated()
   
    /**********************************************************
     * type
     **********************************************************/
    /**
     * Return whether this file has a valid type.
     *
     * @return True if and only if the type of this file is a valid
     *         type.
     *         | result == isValidType(getType())
     */
    @Raw
    public boolean hasValidType(){
    	  return isValidType(getType());
    }
    /**
     * Return whether the given type is a valid type for a file.
     *
     * @param  type
     *         The type to check.
     * @return True if and only if the given type is effective.
     *         | result == (type != null)
     */
    public static boolean isValidType(Type type){
    	  return type != null;
    }
    
    /**
     * Return the type of this file.
     */ 
    @Raw
    public Type getType(){
    	  return type;
    }
    
	/**
	 * Variable referencing the type of this file.
	 * 
	 * @invar The referenced type must be a valid type for a
	 *        file.
	 *        | isValidType(type)
	 * 
	 */
    private final Type type;

    /**********************************************************
     * size
     **********************************************************/

    /**
     * Return the maximum file size.
     *
     * @return The maximum file size is positive.
     *         | result > 0
     */
    public static int getMaximumSize() {
        return Integer.MAX_VALUE;
    }

    /**
     * Return whether the given size is a valid size for a file.
     *
     * @param  size
     *         The size to check.
     * @return True if and only if the given size is positive and does not
     *         exceed the maximum size.
     *         | result == ((size >= 0) && (size <= getMaximumSize()))
     */
    public static boolean isValidSize(int size) {
        return ((size >= 0) && (size <= getMaximumSize()));
    }

    /**
     * Return whether this file has a valid size.
     *
     * @return True if and only if the size of this file is a valid
     *         size.
     *         | result == isValidSize(getSize())
     */
    @Raw public boolean hasValidSize() {
        return isValidSize(getSize());
    }

    /**
     * Increases the size of this file with the given delta.
     *
     * @param  delta
     *         The amount of bytes by which the size of this file
     *         must be increased.
     * @pre    The given delta must be strictly positive.
     *         | delta > 0
     * @effect The size of this file is increased with the given delta.
     *         | changeSize(delta)
      */
    public void enlarge(int delta) throws DiskItemNotWritableException {
        changeSize(delta);
    }

    /**
     * Decreases the size of this file with the given delta.
     *
     * @param  delta
     *         The amount of bytes by which the size of this file
     *         must be decreased.
     * @pre    The given delta must be strictly positive.
     *         | delta > 0
     * @effect The size of this file is decreased with the given delta.
     *         | changeSize(-delta)
     */
    public void shorten(int delta) throws DiskItemNotWritableException {
        changeSize(-delta);
    }

    /**
     * Change the size of this file with the given delta.
     *
     * @param  delta
     *         The amount of bytes by which the size of this file
     *         must be increased or decreased.
     * @pre    The given delta must not be 0
     *         | delta != 0
     * @effect The size of this file is adapted with the given delta.
     *         | setSize(getSize()+delta)
     * @effect The modification time is updated.
     *         | setModificationTime()
     * @throws FileNotWritableException
     *         This file is not writable.
     *         | !isWritable() [must]
     */
    @Model private void changeSize(int delta) throws DiskItemNotWritableException{
        if (!isWritable()) {
            throw new DiskItemNotWritableException(this);
        }
        setSize(getSize()+delta);
        setModificationTime();
    }

    /**
     * Set the size of this file to the given size.
     *
     * @param  size
     *         The new size for this file.
     * @pre    The given size must be legal
     *         | isValidSize(size)
     * @pre    This file is not terminated.
     *         | ! isTerminated()
     * @post   The given size is registered as the size of this file.
     *         | new.getSize() == size
     */
    @Model @Raw private void setSize(int size) {
        this.size = size;
    }

    /**
     * Return the size of this file (in bytes).
     */
    @Raw public int getSize() {
        return size;
    }

    /**
     * Variable registering the size of this file (in bytes).
     */
    private int size;
}