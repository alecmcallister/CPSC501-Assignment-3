import org.jdom.*;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

//		Document doc = Serializer.serialize(ObjectCreator.CreateSampleObject());

		GetUserInput();
	}

	public static void GetUserInput() {
		Scanner myObj = new Scanner(System.in); // Create a Scanner object
		System.out.println(
				"Enter Object number to create:\n\t1: Simple object\n\t2: Object with references\nEnter -1 to complete initialization.");

		String userInput;
		
		while (!(userInput = myObj.nextLine()).equals("-1")) {

			if (userInput.equals("1")) {
				System.out.println("Creating Simple object...");
			}
		}

		System.out.println("Completed object creation.\nExiting...");

	}

}
