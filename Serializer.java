import org.jdom.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import java.lang.reflect.*;
import java.util.ArrayList;

public class Serializer {

	public static org.jdom.Document serialize(Object obj) {
		Element serialized = new Element("serialized");
		Document doc = new Document(serialized);
		doc.setRootElement(serialized);

		AddJDOMElementToDoc(doc, obj);

		return doc;
	}

	public static void AddJDOMElementToDoc(Document doc, Object obj) {
		Element objElement = new Element("object");

		objElement.setAttribute("class", obj.getClass().getName());
		objElement.setAttribute("id", Integer.toString(obj.hashCode()));

		ArrayList<Object> refObjects = new ArrayList<Object>();

		for (Field f : obj.getClass().getFields()) {

			Element field = new Element("field");
			field.setAttribute("name", f.getName());
			field.setAttribute("declaringClass", obj.getClass().getName());

			if (f.getType().isPrimitive() || f.getType() == String.class) {

				Element primValue = new Element("value");

				try {
					primValue.setText(f.get(obj).toString());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				field.addContent(primValue);

			} else {
				Element refValue = new Element("reference");

				try {
					Object refObject = f.get(obj);
					refValue.setText(Integer.toString(refObject.hashCode()));

					refObjects.add(refObject);

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				field.addContent(refValue);
			}

			objElement.addContent(field);

		}

		if (obj.getClass().isArray()) {

			objElement.setAttribute("length", Integer.toString(Array.getLength(obj)));

			for (int i = 0; i < Array.getLength(obj); i++) {

				Object o = Array.get(obj, i);
				if (obj.getClass().getComponentType().isPrimitive()) {

					Element primValue = new Element("value");

					primValue.setText(o.toString());

					objElement.addContent(primValue);
				} else {

					Element refValue = new Element("reference");

					refValue.setText(Integer.toString(o.hashCode()));

					objElement.addContent(refValue);
					refObjects.add(o);
				}

			}
		}

		for (Object o : refObjects) {
			AddJDOMElementToDoc(doc, o);

		}
		
		doc.getRootElement().addContent(objElement);
	}

	public static String GetXMLStringFromDocument(Document doc) {
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		return xmlOutput.outputString(doc);
	}

}
