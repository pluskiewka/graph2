package main;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.extensions.annotation.ActiveObject;

@ActiveObject
public class Vertex implements Serializable {
	private static final long serialVersionUID = -6300664513616426780L;
	
	public final int id;
	public final Graph graph; 
	Collection<Edge> edges;
	
	public Vertex(Graph graph, int id) {
		this.graph = graph;
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
	
	@Override
	public String toString() {
		return "Vertex #" + id;
	}
	
	void computeColor() {
		new Thread(new Runnable(){
			@Override
			public void run() {
				for(Edge t : edges) {
					t.computeColor();
				}
			}
		}).start();
	}
	
	void computeMin() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(Edge t : edges) {
					graph.setMin(t);
				}
			}
		}).start();
	}
	
	void computeMax() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(Edge t : edges) {
					graph.setMax(t);
				}
			}
		}).start();
	}
	
	public Edge newEdge(Vertex v2) {
		Edge edge = new Edge(graph, this, v2);
		edges.add(edge);
		return edge;
	}
}
