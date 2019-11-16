import org.jdom.*;
import org.jdom.input.*;

import Objects.*;

import java.util.List;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Deserializer {

	public static org.jdom.Document GetJDOMDocumentFromXMLString(String xml) {

		SAXBuilder builder = new SAXBuilder();
		Document doc = null;

		try {
			doc = (Document) builder.build(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	public static Object deserialize(org.jdom.Document document) {

		Element root = document.getRootElement();

		List objectList = root.getChildren("object");

		Object[] result = new Object[objectList.size()];

		HashMap<Integer, Object> map = new HashMap<Integer, Object>();

		for (int i = 0; i < objectList.size(); i++) {

			Element objElement = (Element) objectList.get(i);
			int objID = Integer.parseInt(objElement.getAttributeValue("id"));

			Class objClass = null;
			Object newObj = null;

			try {
				objClass = Class.forName(objElement.getAttributeValue("class"));

				if (!objClass.isArray()) {

					newObj = objClass.newInstance();

				} else {
					int arraySize = Integer.parseInt(objElement.getAttributeValue("length"));
					newObj = Array.newInstance(objClass, arraySize);
				}

				result[i] = newObj;

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (newObj != null) {
				List fieldList = objElement.getChildren("field");

				if (newObj.getClass().isArray()) {
					int arraySize = Integer.parseInt(objElement.getAttributeValue("length"));

					Object arrayObj = Array.newInstance(objClass.getComponentType(), arraySize);

					for (int k = 0; k < arraySize; k++) {
//							Array.set(arrayObj, k, ConvertStringToPrimitive(f, ((Element)(fieldElement.getChildren().get(k))).getValue()));

						Object val = ConvertStringToPrimitive(newObj.getClass().getComponentType(),
								((Element) (objElement.getChildren().get(k))).getValue());

						Array.set(arrayObj, k, 111111);

					}

					newObj = arrayObj;

				}

				for (int j = 0; j < fieldList.size(); j++) {

					Element fieldElement = (Element) fieldList.get(j);

					try {
						Field f = objClass.getField(fieldElement.getAttributeValue("name"));
						f.setAccessible(true);

						if (f.getType().isPrimitive() || f.getType() == String.class) {

							String value = fieldElement.getChild("value").getValue();

//							SetField(newObj, f, value);
							f.set(newObj, ConvertStringToPrimitive(f.getType(), value));

						} else if (f.getType().isArray()) {

							int arraySize = fieldElement.getChildren("reference").size();

							Object arrayObj = Array.newInstance(f.getType().getComponentType(), arraySize);

							for (int k = 0; k < arraySize; k++) {
								// populate array here.

								Array.set(arrayObj, k, map.get(Integer.parseInt(
										((Element) fieldElement.getChildren("reference").get(k)).getValue())));
							}

							f.set(newObj, arrayObj);

						} else {

							int reference = Integer.parseInt(fieldElement.getChild("reference").getValue());

							f.set(newObj, map.get(reference));
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				map.put(objID, newObj);
			}

		}

		return result;
	}

	static Object ConvertStringToPrimitive(Class f, String value) {
		Object result = value;

		if (int.class.isAssignableFrom(f)) {
			result = Integer.parseInt(value);
		} else if (float.class.isAssignableFrom(f)) {
			result = Float.parseFloat(value);
		} else if (boolean.class.isAssignableFrom(f)) {
			result = Boolean.parseBoolean(value);
		}

		return result;
	}

	static void SetField(Object obj, Field f, String value)
			throws NumberFormatException, IllegalArgumentException, IllegalAccessException {

		if (int.class.isAssignableFrom(f.getType())) {
			f.set(obj, Integer.parseInt(value));
		} else if (float.class.isAssignableFrom(f.getType())) {
			f.set(obj, Float.parseFloat(value));
		} else if (boolean.class.isAssignableFrom(f.getType())) {
			f.set(obj, Boolean.parseBoolean(value));
		} else {
			f.set(obj, value);
		}
	}

}