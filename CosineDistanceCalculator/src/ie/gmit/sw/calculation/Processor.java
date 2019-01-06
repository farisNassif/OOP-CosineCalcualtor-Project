package ie.gmit.sw.calculation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.interfaces.Minhashator;
import ie.gmit.sw.objects.Poison;
import ie.gmit.sw.objects.Shingle;

/**
 * Processes values from the <code>BlockingQueue</code> and converts them to a
 * hash. They are then added to a map. Implements {@link Runnable} and
 * {@link Minhashator}
 * 
 * @author Faris Nassif
 */
public class Processor implements Runnable, Minhashator {

	private TreeSet<Integer> minHashes;
	private int fileCount = 2;
	private int aSize, bSize;
	private int numOfHashes;
	private ExecutorService pool;
	private BlockingQueue<Shingle> queue;
	private Map<Integer, List<Integer>> m = new ConcurrentHashMap<Integer, List<Integer>>();

	/**
	 * Creates a new {@link Processor} object
	 * 
	 * @param q           <code>BlockingQueue</code> of {@link Shingle} objects
	 * 
	 * @param numOfHashes The amount of hashes
	 * 
	 */
	public Processor(BlockingQueue<Shingle> q, int numOfHashes) {
		this.queue = q;
		this.numOfHashes = numOfHashes;
		this.pool = Executors.newFixedThreadPool(numOfHashes);
		initHashes();
	}

	/**
	 * Returns the minimum value of a {@link Shingle} object
	 *
	 * @return minimumVal The minimum hashed value of the {@link Shingle}
	 * 
	 */
	public int hashValue(Shingle s) {
		int minimumVal = Integer.MAX_VALUE;
		for (Integer hash : minHashes) {

			int minHashed = s.getShingleHashCode() ^ hash;
			if (minHashed < minimumVal) {
				minimumVal = minHashed;
			}
		}
		return minimumVal;
	}

	@Override
	public void run() {
		List<Integer> listA = new LinkedList<>();
		List<Integer> listB = new LinkedList<>();
		List<Integer> nullList = new LinkedList<>();
		// Max files
		fileCount = 2;

		// While loops twice for each file
		while (fileCount > 0) {
			try {
				// Take a shingle from the queue
				Shingle s = queue.take();
				// Do poison check, continue to else if no poison
				if (s instanceof Poison) {
					fileCount--;
				} else {
					pool.execute(new Runnable() {

						@Override
						public void run() {
							if (s.getDocumentId() == 1) {
								// Adds hash value of Shingle object s to the linked list
								listA.add(hashValue(s));
							} else if (s.getDocumentId() == 2) {
								// Adds hash value of Shingle object s to the linked list
								listB.add(hashValue(s));
							} else {
								nullList.add(hashValue(s));
							}
						}
					});// Runnable
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Interuppted exception " + e);
			}
		}
		// No more threads are submitted to pool service
		shutdown();

		aSize = listA.size();
		bSize = listB.size();

		// Puts both lists into the map at their corresponding index
		m.put(1, listA);
		m.put(2, listB);

		List<Integer> i = m.get(1);
		i.retainAll(m.get(2));

		// Prints the result
		new CosineCalculator(aSize, bSize, i);
	}

	@Override
	public void initHashes() {
		minHashes = new TreeSet<Integer>();

		// Random number for the minhashing
		Random randInt = new Random();

		for (int i = 0; i < numOfHashes; i++) {
			minHashes.add(randInt.nextInt());
		}
	}

	/**
	 * Ensures no more threads are submitted to the pool service & blocks all
	 * threads until the pool has finished
	 */
	public void shutdown() {
		pool.shutdown();

		try {
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			System.out.println("Error shutting down the pool");
			e.printStackTrace();
		}
	}
}
