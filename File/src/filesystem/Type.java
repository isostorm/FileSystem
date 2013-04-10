package filesystem;

import be.kuleuven.cs.som.annotate.Raw;

/**
 * An enumeration of file types.
 *    In its current definition, the class only distinguishes between
 *    text, pdf and java file types.
 *    
 * @invar	Each type must have a properly spelled extension.
 * 			| hasValidExtension()
 *    
 * @author Geert Delanote
 */

public enum Type {
	TEXT ("txt"), PDF ("pdf"), JAVA ("java");
	
	/**
	 * Initialize a new type with given extension.
	 * 
	 * @param extension
	 *        The extension of the new type.
	 * @post  The extension of this new type is set to the given
	 *        extension.
	 *        | new.getExtension().equals(extension)
	 */
	Type(String extension){
		this.extension = extension;
	}
	
	/**
	 * Return the extension of this type.
	 */
	@Raw
	public String getExtension(){
		return extension;
	}
	
    /**
     * Check whether this type has a valid extension.
     *
     * @return	True if and only if the extension of this
     *           file type is a valid extension.
     * 			| result == isValidName(getName())
     */
	@Raw
	public boolean hasValidExtension(){
		return isValidExtension(getExtension());
	}
	
    /**
     * Check whether the given extension is a legal extension
     * for a file type.
     *
     * @return	True if the given string is effective, if it is not
     * 			empty and if it only consists of small letters;
     *           false otherwise.
     * 			| result ==
     * 			|	(extension != null) && extension.matches("[a-z]+")
     */
	public static boolean isValidExtension(String extension){
      try{
    	    return extension.matches("[a-z]+");
      } catch (NullPointerException e){
    	    assert extension == null;
    	    return false;
      }
	}
	
	/**
     * Variable registering the extension of this type.
     * 
     * @invar This type must have a valid extension.
     *        | isValidExtension(extension)
     * 
     */
	private final String extension;
	
   /**
	* Return a textual representation of the type.
	* 
	* @return  The string "java"
	*          | result.equals(this.getExtension())
	*/	
	public String toString(){
		return getExtension();
	}

}
