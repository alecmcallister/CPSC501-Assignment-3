import com.sun.org.apache.bcel.internal.generic.NEW;

import Objects.*;

public class ObjectCreator {

	public static Object CreateSampleObject() {
		return new SimpleObject();
//		return new Object[] { new SimpleObject(), new SimpleObject(123, 56.75f) };
	}

	public static Object CreateSimpleObject() {
		return new SimpleObject();
	}

	public static Object CreateObjectWithReferences() {
		return new Object();
	}

	public static Object CreateObjectWithPrimitiveArray() {
		return new Object();
	}

	public static Object CreateObjectWithReferenceArray() {
		return new Object();
	}

	public static Object CreateCollectionObject() {
		return new Object();
	}
}