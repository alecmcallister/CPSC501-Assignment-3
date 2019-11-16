package Objects;

// TODO: Be able to deal with circular references
public class ObjectWithReferences {

	public SimpleObject referencedObjectA;
	public SimpleObject referencedObjectB;

	public ObjectWithReferences() {

	}

	public ObjectWithReferences(SimpleObject referencedObjectA, SimpleObject referencedObjectB) {
		this.referencedObjectA = referencedObjectA;
		this.referencedObjectB = referencedObjectB;
	}

}
