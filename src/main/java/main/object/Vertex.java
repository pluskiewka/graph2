package main.object;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class Vertex extends UnicastRemoteObject implements Serializable, RemoteVertex {
	private static final long serialVersionUID = -6300664513616426780L;
	private static final Logger logger = Logger.getLogger(Vertex.class);
	
	private final int id;
	private final RemoteGraph graph; 
	private List<RemoteEdge> edges;
	
	public Vertex(RemoteGraph graph, int id) throws RemoteException {
		this.graph = graph;
		this.id = id;
		this.edges = Collections.synchronizedList(new LinkedList<RemoteEdge>());
	}
	
	/**
	 * Pobranie unikatowego w skali całego grafu identyfikatora wierzchołka.
	 */
	@Override
	public Integer getId() {
		return id;
	}
	
	/**
	 * Uzyskanie referencji na graf lokalny.
	 */
	@Override
	public RemoteGraph getGraph() {
		return graph;
	}
	
	/**
	 * Uzyskanie listy krawędzi skierowanych wychodzących z tego wierzchołka.
	 */
	@Override
	public List<RemoteEdge> getEdges() {
		return edges;
	}
	
	/**
	 * Dodanie nowej krawędzi skierowanej do zbioru krawędzi skierowanych, wychodzących
	 * z tego wierzchołka.
	 */
	@Override
	public RemoteEdge newEdge(RemoteVertex vertex, Integer level) throws RemoteException {
		RemoteEdge edge = new Edge(graph, this, vertex, level);
		synchronized(edges) {
			edges.add(edge);
		}
		return edge;
	}
	
	/**
	 * Przeliczanie wartości koloru każdej krawędzi skierowanej wychodzącej z tego wierzchołka.
	 */
	@Override
	public void computeColor() throws RemoteException {
		synchronized(edges) {
			long p1 = System.nanoTime();
			for(RemoteEdge edge : edges) {
				edge.computeColor();
			}
			long p2 = System.nanoTime();
			logger.info("Compute color finished in " + Double.toString((double)(p2-p1)/1000000000.0));
		}
	}
	
	/**
	 * Przeszkuwanie zbioru krawędzie skierowanych, wychodzących z tego wierzchołka,
	 * w celu ustalenia minimalnej.
	 */
	@Override
	public void computeMin() throws RemoteException {
		synchronized(edges) {
			long p1 = System.nanoTime();
			for(RemoteEdge edge : edges) {
				graph.setMin(edge);
			}
			long p2 = System.nanoTime();
			logger.info("Compute min finished in " + Double.toString((double)(p2-p1)/1000000000.0));
		}
	}

	/**
	 * Przeszkuwanie zbioru krawędzie skierowanych, wychodzących z tego wierzchołka,
	 * w celu ustalenia maksymalnej.
	 */
	@Override
	public void computeMax() throws RemoteException {
		synchronized(edges) {
			long p1 = System.nanoTime();
			for(RemoteEdge edge : edges) {
				graph.setMax(edge);
			}
			long p2 = System.nanoTime();
			logger.info("Compute max finished in " + Double.toString((double)(p2-p1)/1000000000.0));
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
		return getName();
	}
	
	@Override
	public String getName() {
		return "Vertex: " + id;
	}
	
}
