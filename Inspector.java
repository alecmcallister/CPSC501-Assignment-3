import java.lang.*;
import java.lang.reflect.*;

public class Inspector {

	public void inspect(Object obj, boolean recursive) {
		println("");
		inspectClass(new InspectObj(obj.getClass(), obj, recursive, 0));
	}

	private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
		inspectClass(new InspectObj(c, obj, recursive, depth));
	}

	private void inspectClass(InspectObj o) {
		if (o == null || o.obj == null || o.c == null)
			return;

		println("\n" + getIndent(o.depth) + getClassName(o.c));

		if (o.c.isArray()) {
			println(getIndent(o.depth) + getArrayContents(o.obj));

			// Don't think this is what the assignment is asking for...
			// Goes into an infinite loop while doing script8

			// if (o.recursive) {
			// for (int i = 0; i < Array.getLength(o.obj); i++) {
			// Object objectAtIndex = Array.get(o.obj, i);
			// if (objectAtIndex != null)
			// inspectClass(objectAtIndex.getClass(), objectAtIndex, o.recursive,
			// o.depth + 1);
			// }
			// }
		}

		printDeclaringClass(o);

		printSuperClass(o);

		printInterface(o);

		printConstructors(o);

		printMethods(o);

		printFields(o);

		println("");
	}



	private void printDeclaringClass(InspectObj o) {
		println(getIndent(o.depth) + " Declaring Class: "
				+ getClassName((o.c.getConstructors().length > 0)
						? o.c.getConstructors()[0].getDeclaringClass()
						: o.c.getDeclaringClass()));
	}

	private void printSuperClass(InspectObj o) {
		println(getIndent(o.depth) + " Super-Class: " + getClassName(o.c.getSuperclass()));

		if (o.c.getSuperclass() != null && o.c.getSuperclass() != o.c.getClass())
			inspectClass(o.c.getSuperclass(), o.obj, o.recursive, o.depth + 1);
	}

	private void printInterface(InspectObj o) {
		for (Class i : o.c.getInterfaces()) {
			println(getIndent(o.depth) + " Interface: " + getClassName(i));
			if (o.c != i)
				inspectClass(i, o.obj, o.recursive, o.depth + 1);
		}
	}



	private void printConstructors(InspectObj o) {
		println(getIndent(o.depth) + " Constructors: "
				+ ((o.c.getConstructors().length == 0) ? "[N/A]" : ""));

		for (Constructor con : o.c.getConstructors()) {
			println(getIndent(o.depth) + getConstructorInfo(con));
		}
	}

	private String getConstructorInfo(Constructor con) {
		String result = " - ";

		result += Modifier.toString(con.getModifiers());
		result += " " + con.getName();
		result += "(" + separateListWithCommas(con.getParameterTypes()) + ")";

		return result;
	}



	private void printMethods(InspectObj o) {
		println(getIndent(o.depth) + " Declared Methods: "
				+ ((o.c.getDeclaredMethods().length == 0) ? "[N/A]" : ""));

		for (Method m : o.c.getDeclaredMethods()) {
			println(getIndent(o.depth) + getMethdInfo(m));
		}
	}

	private String getMethdInfo(Method m) {
		String result = " - ";

		result += Modifier.toString(m.getModifiers());
		result += " " + getClassName(m.getReturnType());
		result += " " + m.getName();
		result += "(" + separateListWithCommas(m.getParameterTypes()) + ")";

		if (m.getExceptionTypes().length > 0)
			result += " throws " + separateListWithCommas(m.getExceptionTypes());

		return result;
	}



	private void printFields(InspectObj o) {
		println(getIndent(o.depth) + " Declared Fields: "
				+ ((o.c.getDeclaredFields().length == 0) ? "[N/A]" : ""));

		for (Field f : o.c.getDeclaredFields()) {
			println(getIndent(o.depth) + getFieldInfo(o, f));

			if (!f.getType().isPrimitive() && o.recursive) {
				Object value = getFieldValue(o, f);
				if (value != null)
					inspectClass(f.getType(), value, o.recursive, o.depth + 1);
			}
		}
	}

	private String getFieldInfo(InspectObj o, Field f) {
		String result = " - ";

		result += Modifier.toString(f.getModifiers());
		result += " " + getClassName(f.getType());
		result += " " + f.getName() + " = ";

		Object value = getFieldValue(o, f);

		if (value == null)
			return result + value;

		if (f.getType().isPrimitive()) {
			result += value;
		} else {
			result += getIdentityHash(value);
		}

		if (f.getType().isArray()) {
			result += " " + getArrayContents(value);
		}

		return result;
	}

	private Object getFieldValue(InspectObj o, Field f) {
		Object value = null;
		try {
			f.setAccessible(true);
			value = f.get(o.obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}



	private String getClassName(Class c) {
		return (c != null) ? c.getName() : "[N/A]";
	}

	public String getIndent(int depth) {
		String indent = "";
		for (int i = 0; i < depth; i++)
			indent += "\t";

		return indent;
	}

	public String getArrayContents(Object a) {
		if (!a.getClass().isArray())
			return "";

		String result = "(Length: " + Array.getLength(a) + ") [";
		for (int i = 0; i < Array.getLength(a); i++)
			result += Array.get(a, i) + (i + 1 < Array.getLength(a) ? ", " : "");

		result += "]";

		return result;
	}

	public String separateListWithCommas(Class[] list) {
		String result = "";
		for (int i = 0; i < list.length; i++)
			result += getClassName(list[i]) + (i + 1 == list.length ? "" : ", ");

		return result;
	}

	private String getIdentityHash(Object o) {
		return getClassName(o.getClass()) + "@" + Integer.toHexString(o.hashCode());
	}

	private void println(Object arg) {
		System.out.println(arg);
	}



	public class InspectObj {
		public Class c;
		public Object obj;
		public boolean recursive;
		public int depth;

		public InspectObj(Class c, Object obj, boolean recursive, int depth) {
			this.c = c;
			this.obj = obj;
			this.recursive = recursive;
			this.depth = depth;
		}
	}
}

