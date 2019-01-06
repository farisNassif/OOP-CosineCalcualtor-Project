package ie.gmit.sw.objects;

/**
 * Special type of shingle that indicates that the final shingle has been taken
 * from a file. Extends {@link Shingle}.
 * 
 * @author Faris Nassif
 */
public class Poison extends Shingle {

	/**
	 * The constructor creates a poison object
	 * 
	 * @param documentId the document ID
	 * @param hashCode   hash code of an array of type String
	 */
	public Poison(int id, int hashCode) {
		super(id, hashCode);
		this.setHashCode(-99);
	}

}
