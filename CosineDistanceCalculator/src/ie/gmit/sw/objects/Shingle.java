package ie.gmit.sw.objects;

/**
 * Shingle Type, Used as the basic unit (element) to indicate a fixed size group
 * of words.
 * 
 * @author Faris Nassif
 *
 */
public class Shingle {
	// ID of the document Shingle belongs to
	private int fileId;
	// Hashcode of the word(s) making up the shingle
	private int hashCode;

	/**
	 * @param fileId   File ID of the Shingle
	 * @param hashCode Hashcode of words making up the Shingle
	 */
	public Shingle(int fileId, int hashCode) {
		super();
		this.fileId = fileId;
		this.hashCode = hashCode;
	}

	public int getDocumentId() {
		return this.fileId;
	}

	protected void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getShingleHashCode() {
		return hashCode;
	}

	protected void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	@Override
	public int hashCode() {
		final int p = 31;
		int result = 1;
		result = p * result + hashCode;
		return result;
	}
}
