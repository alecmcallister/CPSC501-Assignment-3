import org.jdom.*;

import Objects.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Object user = GetUserInput();
		Document doc = Serializer.serialize(user);
		
		System.out.println(Serializer.GetXMLStringFromDocument(doc));
		
		Object deserialized = Deserializer.deserialize(doc);

		if (deserialized.getClass().isArray()) {
			for (int i = 0; i < Array.getLength(deserialized); i++) {
				new Inspector().inspect(Array.get(deserialized, i), true);
			}
		}
	}

	public static Object GetUserInput() {
		ArrayList<Object> result = new ArrayList<Object>();
		
		Scanner s = new Scanner(System.in); // Create a Scanner object
		System.out.println(
				"Enter Object number to create:\n"
				+ "\t1: Simple object\n"
				+ "\t2: Object with references\n"
				+ "\t3: Object with primitive array\n"
				+ "\t4: Object with array of references\n"
				+ "\t5: Object using collection instance\n"
				+ "Enter -1 to complete initialization.");

		String userInput;

		while (!(userInput = s.nextLine()).equals("-1")) {

			Object created = null;
			
			if (userInput.equals("1")) {
				System.out.println("Creating Simple object...");
				created = CreateSimpleObject(s);
			}
			if (userInput.equals("2")) {
				System.out.println("Creating Object with references...");
				
				created = CreateObjectWithReferences(s);
			}
			if (userInput.equals("3")) {
				System.out.println("Creating Object with primitive array...");
				
				created = CreateObjectWithPrimitiveArray(s);
			}
			if (userInput.equals("4")) {
				System.out.println("Creating Object with array of references...");
				
				created = CreateObjectWithReferenceArray(s);
			}
			if (userInput.equals("5")) {
				System.out.println("Creating Object using collection instance...");
				
				created = CreateObjectWithCollection(s);
			}
			
			if (created != null)
				result.add(created);
			
			System.out.println(
					"Enter Object number to create:\n"
					+ "\t1: Simple object\n"
					+ "\t2: Object with references\n"
					+ "\t3: Object with primitive array\n"
					+ "\t4: Object with array of references\n"
					+ "\t5: Object using collection instance\n"
					+ "Enter -1 to complete initialization.");

		}

		System.out.println("Completed object creation.\nSerializing...\n");

		return result.toArray();
	}
	
	static Object CreateSimpleObject(Scanner s) {
		System.out.println("Enter integer: ");
		int a = Integer.parseInt(s.nextLine());
		
		System.out.println("Enter float: ");
		float b = Float.parseFloat(s.nextLine());
		
		return new SimpleObject(a, b);
	}
	
	
	static Object CreateObjectWithReferences(Scanner s) {
		System.out.println("Creating SimpleObject 1");
		
		Object A = CreateSimpleObject(s);
		
		System.out.println("Creating SimpleObject 2");
		
		Object B = CreateSimpleObject(s);
		
		return new ObjectWithReferences((SimpleObject)A, (SimpleObject)B);
	}
	
	static Object CreateObjectWithPrimitiveArray(Scanner s) {
		System.out.println("Enter size of primitive array: ");
		int size = Integer.parseInt(s.nextLine());

		int[] array = new int[size];
		
		for (int i = 0; i < size; i++)
		{
			System.out.println("Enter integer for index " + i + ":");
			
			array[i] = Integer.parseInt(s.nextLine());
		}
		
		return new ObjectWithPrimitiveArray(array);
	}
	
	static Object CreateObjectWithReferenceArray(Scanner s) {
		System.out.println("Enter size of SimpleObject array: ");
		int size = Integer.parseInt(s.nextLine());

		Object[] array = new Object[size];
		
		for (int i = 0; i < size; i++)
		{
			System.out.println("Creating SimpleObject " + i);			
			array[i] = CreateSimpleObject(s);
		}
		
		return new ObjectWithReferenceArray(array);
	}
	
	static Object CreateObjectWithCollection(Scanner s) {
		System.out.println("Enter size of SimpleObject collection: ");
		int size = Integer.parseInt(s.nextLine());

		ArrayList<Object> array = new ArrayList<Object>();
		
		for (int i = 0; i < size; i++)
		{
			System.out.println("Creating SimpleObject " + i);			
			array.add(CreateSimpleObject(s));
		}
		
		return new ObjectWithCollection(array);
	}
	
	
	
}
