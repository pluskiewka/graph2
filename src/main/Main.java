package main;

import java.util.Collection;
import java.util.LinkedList;

public class Main {
	private Collection<Vertex> vertexes;
	private Collection<Edge> edges;
	private Edge minEdge, maxEdge;
	
	private int next = 0;
	
	public Main() {
		this.vertexes = new LinkedList<Vertex>();
		this.edges = new LinkedList<Edge>();
	}
	
	public Vertex newVertex() {
		Vertex v = new Vertex(next++);
		vertexes.add(v);
		return v;
	}
	
	public Edge newEdge(Vertex v1, Vertex v2, int level) {
		Edge e = new Edge(v1, v2);
		e.level = level;
		edges.add(e);
		if(minEdge == null && maxEdge == null) {
			minEdge = e;
			maxEdge = e;
		} else {
			if(level < minEdge.level) {
				minEdge = e;
			} else if(level > maxEdge.level) {
				maxEdge = e;
			}
		}
		return e;
	}
	
	public void setLevel(Edge e, int level) {
		if(e.level == level)
			return;
		
		if(minEdge.equals(e) && level < e.level) {
			e.level = level;
		} else if(maxEdge.equals(e) && level > e.level) {
			e.level = level;
		} else if(minEdge.equals(e) && level > e.level){
			e.level = level;
			for(Edge t : edges) {
				if(t.level < minEdge.level)
					minEdge = t;
			}
		} else if(maxEdge.equals(e) && level < e.level) {
			e.level = level;
			for(Edge t : edges) {
				if(t.level > maxEdge.level)
					maxEdge = t;
			}
		} else if(level > maxEdge.level) {
			e.level = level;
			maxEdge =e;
		} else if(level < minEdge.level) {
			e.level = level;
			minEdge = e;
		} else {
			e.level = level;
		}
	}
	
	public static void main(String []args) {
		
	}
}
