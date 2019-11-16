package Objects;

// TODO: Be able to deal with circular references
public class ObjectWithReferences {

	public Object referencedObject;

	public ObjectWithReferences(Object referencedObject) {
		this.referencedObject = referencedObject;
	}

}
