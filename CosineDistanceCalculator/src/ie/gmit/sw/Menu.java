package ie.gmit.sw;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ie.gmit.sw.calculation.Processor;
import ie.gmit.sw.objects.Shingle;
import ie.gmit.sw.parser.FileParser;

/**
 * Provides the UI to the user and allows for input, threads are ran from the
 * switch
 * 
 * @author Faris Nassif
 *
 */
public class Menu {

	BlockingQueue<Shingle> queue = new LinkedBlockingQueue<>();
	private Scanner console = new Scanner(System.in);
	private String firstFile;
	private String secondFile;
	// private int shingleSize;
	private int userChoice;

	public void showMenu() throws InterruptedException {
		userMenu();
		userChoice = console.nextInt();
		switch (userChoice) {
		case 1:
			fileInputPrompt();

			Thread t1 = new Thread(new FileParser(queue, getFirstFile(), 1));
			Thread t2 = new Thread(new FileParser(queue, getSecondFile(), 2));

			t1.start();
			t2.start();

			t1.join();
			t2.join();

			// Processes the result after t1 and t2 are dead
			Processor cons = new Processor(queue, 1);
			Thread tCons = new Thread(cons);
			// Starts t3, prints the result to the console and kills t3
			tCons.start();
			result();
			tCons.join();

			break;
		case 0:
			// Terminates the program
			System.out.println("\nTerminating Program ...");
			System.exit(0);
			break;
		default:
			// If user input is invalid
			System.out.println("\n**Please enter either 0 or 1**");
			break;
		}
		// Loops until prompted to exit by user
		this.showMenu();
	}

	public void userMenu() {
		System.out.println("\n ----Cosine Distance Calculator---");
		System.out.println("(1) To enter files for comparision ");
		System.out.println("(0) Exit ");
		System.out.print("==================================\nPlease Enter your option here: ");
	}

	public void result() {
		System.out.print("Cosine Distance of <" + getFirstFile() + "> & <" + getSecondFile() + "> : ");
	}

	public Menu fileInputPrompt() {
		this.firstFile = QoL.getInputString("(Shingle size is defaulted to 1)\nEnter the first file name : ");
		this.secondFile = QoL.getInputString("Enter the second file name : ");
		// this.shingleSize = QoL.getInputInt("Enter Shingle Size : ");
		return null;
	}

	public String getFirstFile() {
		return firstFile;
	}

	public void setFirstFile(String firstFile) {
		this.firstFile = firstFile;
	}

	public String getSecondFile() {
		return secondFile;
	}

	public void setSecondFile(String secondFile) {
		this.secondFile = secondFile;
	}
}
