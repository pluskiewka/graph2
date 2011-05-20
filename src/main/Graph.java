package main;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import org.objectweb.proactive.extensions.annotation.ActiveObject;

@ActiveObject
public class Graph implements Serializable {
	private static final long serialVersionUID = -5144342279727570466L;
	
	public Collection<Edge> edges;
	public Collection<Vertex> vertexes;
	public Edge min, max;
	
	public Graph() {
		edges = new LinkedList<Edge>();
		vertexes = new LinkedList<Vertex>();
	}
}