import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;


/**
 * An enumeration introducing different file types
 * 
 * @author  Mathias Benoit, Frederic Huysentruyt
 * @version 1.0
 * 
 */
public enum FileType {
	TXT("Text File"), PDF("PDF-file"), JAVA("Java-file");
	
	private FileType(String name){
		fullName = name;
	}
	
	private final String fullName;
	
	/**
	 * Return the full name of this file type
	 * 
	 */
	@Basic @Raw @Immutable
	public String getFullName(){
		return fullName;
	}
	
}
