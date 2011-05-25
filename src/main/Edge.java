package main;

import java.io.Serializable;

public class Edge implements Serializable {
	private static final long serialVersionUID = -6302028332002939511L;
	
	public final Vertex v1, v2;
	public int level, color;
	
	public Edge(Vertex v1, Vertex v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj == this || (obj instanceof Edge && ((Edge)obj).v1.equals(this.v1) && ((Edge)obj).v2.equals(this.v2));
	}
	
	@Override
	public int hashCode() {
		return this.v1.id * this.v2.id;
	}
	
	@Override
	public String toString() {
		return "Edge: " + v1 + " " + v2 + " level: " + level + " color: " + color;
	}
}
