package ie.gmit.sw.parser;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import ie.gmit.sw.objects.Poison;
import ie.gmit.sw.objects.Shingle;

/**
 * Parses the user requested files and extracts a {@link Shingle} object,
 * implements <code>Runnable</code>
 * 
 * @author Faris Nassif
 */
public class FileParser implements Runnable {
	private String fileName;
	private int documentId;
	private int count = 0;
	private Deque<String> buffer = new LinkedList<>();
	private BlockingQueue<Shingle> q;
	// Didn't have time to allow the user to determine shingle size
	private int shingleSize = 1;

	public FileParser(BlockingQueue<Shingle> queue, String fileName, int docId) {
		super();
		this.q = queue;
		this.documentId = docId;
		this.fileName = fileName;
	}

	@Override
	public void run() {

		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		} catch (FileNotFoundException e1) {
			System.out.println("File not found");
		}

		String line = null;

		try {
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					String upperLine = line.toUpperCase().replaceAll("[^A-Za-z0-9 ]", "");

					String[] words = upperLine.split(" ");

					// Adds each String in an array to the buffer
					for (String s : words) {
						buffer.add(s);
					}
				}
			}
			// Adding the shingle to the blocking queue
			addShingleToQ();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			// Whatever's left in the buffer added to the blocking queue as a poison
			poison();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	} // Run

	/**
	 * Goes through the buffer until empty and creates a {@link Shingle} and adds it
	 * to the {@code BlockingQueue} each time
	 * 
	 * @throws InterruptedException
	 */
	private void addShingleToQ() throws InterruptedException {
		while (buffer.size() != 0) {
			Shingle s = retrieveShingle();
			if (!s.equals(null)) {
				q.put(s);
			}
		}
	}

	/**
	 * Gets the head of the buffer and adds the <code>String</code> to a buffer
	 * while count is less than the {@link Shingle} size
	 * 
	 * @return Returns a {@link Shingle}
	 */
	private Shingle retrieveShingle() {
		StringBuffer stringBuffer = new StringBuffer();
		count = 0;

		while (count < shingleSize) {
			if (buffer.peek() != null) {
				stringBuffer.append(buffer.poll());
				count++;
			} else {
				count = shingleSize;
			}
		}

		// New Shingle object
		if (stringBuffer.length() > 0) {
			return (new Shingle(documentId, stringBuffer.toString().hashCode()));
		} else {
			return (null);
		}
	}

	/**
	 * Whatever is left in the buffer is added to a last {@link Shingle} and added
	 * to the {@code BlockingQueue} as a {@link Poison}
	 * 
	 * @throws InterruptedException
	 */
	private void poison() throws InterruptedException {
		q.put(new Poison(0, 0));
	}
}
