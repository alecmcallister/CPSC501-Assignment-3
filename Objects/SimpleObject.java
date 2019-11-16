package Objects;

public class SimpleObject {

	//public int intValue = 0;
	//public float floatValue = 2.14f;

	public int[] intArray;
	// public String[] stringArray = new String[] { "Some", "thing" };

	// public char[] name = new char[] { 'S', 'm', 'i', 't', 'h' };

	// public Object[] objectArray = new Object[] { new Integer(12), new
	// String("sssssss") };

	public SimpleObject() {

	}

	public SimpleObject(int intValue, float floatValue) {
		//this.intValue = intValue;
		//this.floatValue = floatValue;
		intArray = new int[] { intValue, intValue, intValue };
	}

}
