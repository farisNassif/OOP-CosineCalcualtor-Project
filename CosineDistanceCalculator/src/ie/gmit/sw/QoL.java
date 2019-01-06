package ie.gmit.sw;

import java.util.Scanner;

/**
 * Handy Quality of Life methods that'll clean up the program a bit like
 * requesting String/int input
 * 
 * @author Faris Nassif
 */
public class QoL {
	Menu m = new Menu();
	static Scanner console = new Scanner(System.in);

	public static String getInputString(String output) {
		String input = null;
		System.out.print(output);

		input = txtAppender(console.next());
		return input;
	}

	public void result() {
		System.out.print("Cosine Distance of <" + m.getFirstFile() + "> & <" + m.getSecondFile() + "> : ");
	}

	public static int getInputInt(String output) {
		int input;
		System.out.println(output);

		input = console.nextInt();
		return input;
	}

	public static String txtAppender(String fileName) {
		String fileExtension;
		fileExtension = ".txt";
		if (!fileName.contains(fileExtension)) {
			fileName = fileName + fileExtension;
			return fileName;
		}
		return fileName;
	}

}
