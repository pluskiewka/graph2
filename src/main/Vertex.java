package main;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

public class Vertex implements Serializable {
	private static final long serialVersionUID = -6300664513616426780L;
	
	public final int id;
	public Collection<Edge> edges;
	
	public Vertex(int id) {
		this.id = id;
		this.edges = new LinkedList<Edge>();
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
