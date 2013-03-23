/**
 * A class of files involving a name, a content, a writable state, a creation time, 
 * a time of last modification, a maximum file size and a writable state
 * 
 * 
 * @author Frederic Huysentruyt, Mathias Benoit
 * @version 1.0
 */

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;

/**
 * @Invar The size must be a valid content for this file 
 * 		  | canHaveAsSize(size)
 * @Invar The extension of the file must be a valid extension for this file
 * 		  | canHaveAsExtension(ext)
 */
public class File extends DiskItem {
	
	
	/**
	 * Initialize a new File with a given name, size, writable state, directory and type
	 * 
	 * @param  name
	 * 		   The name for this new file
	 * @param  size
	 * 		   The size of this new file
	 * @param  writable
	 * 		   The writable state of this file
	 * @param  type
	 * 		   The file type of this file
	 * @param  dir
	 *         The parent directory of this file.
	 * @effect A new disk item is initialized with the given name and writable state
	 * 		   | super(name, writable)
	 * @effect The content of this file is set to the given size
	 * 		   | setSize(size)
	 * @effect The file type is set to the given type
	 *         | setType(type)
	 * @effect Moves this file to the given directory
	 *         | move(dir)
	 * @see    1.1.3.3 Post conditions
	 */
	public File(Directory dir, String name, int size, boolean writable, FileType type)
	{
		
		super(name, writable);
		setSize(size);
		setType(type);
		move(dir);
	}
	/**
	 * Initialize a new File with a given name, size, writable state and type
	 * 
	 * @param  name
	 * 		   The name for this new file
	 * @param  size
	 * 		   The size of this new file
	 * @param  writable
	 * 		   The writable state of this file
	 * @param  type
	 * 		   The file type of this file
	 * @effect A new file is initialized with null as its parent directory 
	 * 		   the given name, file type and writable state
	 * 		   | this(null,name, writable,type)
	 * @see    1.1.3.3 Post conditions
	 */
	public File(String name, int size, boolean writable, FileType type){
		this(null, name, size, writable, type);
	}
	/**
	 * Initializes this new file with a given name and type 
	 * 
	 * @param  name
	 * 		   The name for this new file
	 * @param  type
	 * 		   The file type of this file
	 * @effect The new file is initialized with the given name as its name,
	 * 	 	   0 as its size and true as its writable state and the given type as its type
	 * 		   | this(name,0,true, type)
	 */
	 
	public File(String name, FileType type)
	{
		this(name,0,true, type);
		
	}
	
	private int size;
	
	/**
	 * Checks whether the file size is valid.
	 * 
	 * @param    size The size to check
	 * @return   True if and only if the file size is less than or equal to the max file size 
	 * 			 and the size is more than or equal to 0.
	 *           | result == (size <= getMaxFileSize())
	 */
	public static boolean canHaveAsSize(int size)
	{
		return size <= getMaxFileSize() && size >= 0;
	}
	
	/**
	 * The size reveals how big this file is
	 * 
	 * @return the content of this file
	 */
	@Basic
	public int getSize() {
		return size;
	}

	/**
	 * Set the size of this file to the given size
	 * 
	 * @param  size 
	 * 		   The new size for this file
	 * @Pre    For this file the size must be a valid size
	 * @post   If the file is writable, the new size of this file is equal to the given size
	 * 		   | if(isWritable) then
	 * 				new.getSize == size
	 * @post   If the size was already initialized, the modification time will be set to the current time.
	 *         | if(this.getCreationTime() != null) then setCurrentModificationTime()
	 * @throws NotWritableException
	 * 		   The user has no rights to change the size of the file
	 * 		   | !isWritable()
	 */
	@Model
	private void setSize(int size) throws NotWritableException {
		if(isWritable()){
			this.size = size;
			if(this.getCreationTime() != null)
			{
				this.setCurrentModificationTime();
			}
		}else{
			throw new NotWritableException(this);
		}

	}
	/**
	 * Enlarge this file with the given value
	 *
	 * @param   sizeToAdd 
	 * 			Size to add to the current size
	 * @Pre 	The size to add must be less than the current size subtracted from the max file size. 
	 * 			| sizeToAdd <= getMaxFileSize() - getSize()
	 * @effect  Sets the size equal to the current size added with the given size to add. 
	 *          | setSize(getSize() + sizeToAdd)
	 */
	public void enlarge(int sizeToAdd) throws NotWritableException
	{
		setSize(getSize() + sizeToAdd);
	}
	/**
	 * Shorten this file with the given value
	 * 
	 * @Pre 	The size to shorten must be more than the current size subtracted from the max file size. 
	 * 			| sizeToShorten >= getMaxFileSize() - getSize()
	 * @effect  Sets the size equal to the current size added with the given size to short. 
	 *          | setSize(getSize() - sizeToShorten)
	 * @param   sizeToShorten
	 * 			Size to add to the current size
	 */
	public void shorten(int sizeToShorten) throws NotWritableException
	{
		setSize(getSize() - sizeToShorten);
	}
	
	private static final int MAX_FILE_SIZE = Integer.MAX_VALUE;

	/**
	 * The maximum file size expresses the biggest possible value for the size of any files
	 * 
	 * @return the maximum file size for all the files
	 * @see p.14 Immutable
	 */
	@Basic @Immutable
	public static int getMaxFileSize() {
		return MAX_FILE_SIZE;
	}
	
	private FileType type;
	
	/**
	 * Return the type of this file
	 */
	public FileType getType(){
		return type;
	}
	/**
	 * Set the type of this file to the given type
	 * @param type
	 * 		  The type to set
	 * @post  The new type of this file equals the given type
	 * 		  | new.getType() == type
	 */
	public void setType(FileType type){
		this.type = type; 
	}

}