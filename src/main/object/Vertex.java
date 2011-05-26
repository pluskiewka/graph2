package main.object;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class Vertex extends UnicastRemoteObject implements Serializable, RemoteVertex {
	private static final long serialVersionUID = -6300664513616426780L;
	
	private final int id;
	private final RemoteGraph graph; 
	private List<RemoteEdge> edges;
	
	public Vertex(RemoteGraph graph, int id) throws RemoteException {
		this.graph = graph;
		this.id = id;
		this.edges = new LinkedList<RemoteEdge>();
	}
	
	public Integer getId() {
		return id;
	}
	
	public RemoteGraph getGraph() {
		return graph;
	}
	
	@Override
	public List<RemoteEdge> getEdges() throws RemoteException {
		return edges;
	}
	
	@Override
	public RemoteEdge newEdge(RemoteVertex vertex, Integer level) throws RemoteException {
		RemoteEdge edge = new Edge(graph, this, vertex, level);
		edges.add(edge);
		return edge;
	}
	
	@Override
	public void computeColor() throws RemoteException {
		for(RemoteEdge edge : edges) {
			edge.computeColor();
		}
	}
	
	@Override
	public void computeMin() throws RemoteException {
		for(RemoteEdge edge : edges) {
			graph.setMin(edge);
		}
	}
	
	@Override
	public void computeMax() throws RemoteException {
		for(RemoteEdge edge : edges) {
			graph.setMax(edge);
		}
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
		return "Vertex: " + id;
	}
	
}
