package main;

import java.io.Serializable;

public class Vertex implements Serializable {
	private static final long serialVersionUID = -6300664513616426780L;
	
	public final int id;
	
	public Vertex(int id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj == this || (obj instanceof Vertex && ((Vertex)obj).id == this.id);
	}
	
	@Override
	public int hashCode() {
		return this.id;
	}
}
