package ie.gmit.sw.calculation;

import java.util.List;

/**
 * Computes Cosine Distance
 * 
 * @author Faris Nassif
 * 
 */
public class CosineCalculator {
	private final List<Integer> i;
	private final int sizeA;
	private final int sizeB;
	private float cosine;

	public CosineCalculator(int sizeA, int sizeB, List<Integer> i2) {
		this.i = i2;
		this.sizeA = sizeA;
		this.sizeB = sizeB;
		calculateDistance();
	}

	public void calculateDistance() {
		cosine = (float) (i.size() / (Math.sqrt(sizeA) * Math.sqrt(sizeB)));
		System.out.printf("%.5f%%\n", (cosine * 100));
	}

	public List<Integer> getIntersection() {
		return i;
	}

	public int getItemASize() {
		return sizeA;
	}

	public int getItemBSize() {
		return sizeB;
	}
}
