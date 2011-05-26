package main;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

public class Vertex implements Serializable {
	private static final long serialVersionUID = -6300664513616426780L;
	
	public int id;
	public Graph graph; 
	Collection<Edge> edges;
	
	public Vertex(Graph graph, int id) {
		super();
		this.graph = graph;
		this.id = id;
	}
	
	public Vertex() {
		this.edges = new LinkedList<Edge>();
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setGraph(Graph graph) {
		this.graph = graph;
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
