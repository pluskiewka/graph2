package main.object;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicInteger;

import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class Edge extends UnicastRemoteObject implements Serializable, RemoteEdge {
	private static final long serialVersionUID = -6302028332002939511L;
	
	private final RemoteGraph graph;
	private final RemoteVertex v1, v2;
	private AtomicInteger level, color;
	
	public Edge(RemoteGraph graph, RemoteVertex v1, RemoteVertex v2, Integer level) throws RemoteException {
		this.graph = graph;
		this.v1 = v1;
		this.v2 = v2;
		this.level = new AtomicInteger(level);
		this.color = new AtomicInteger(0);
	}
	
	/**
	 * Referencja na lokalny graf.
	 */
	@Override
	public RemoteGraph getGraph() {
		return graph;
	}
	
	/**
	 * Pobranie wartości wagi krawędzi.
	 */
	@Override
	public Integer getLevel() {
		synchronized(level) {
			return level.get();
		}
	}
	
	/**
	 * Ustawienie wartości wagi krawędzi.
	 */
	@Override
	public void setLevel(Integer level) {
		synchronized(level) {
			this.level.set(level);
		}
	}
	
	/**
	 * Uzyskanie koloru krawędzi.
	 */
	@Override
	public Integer getColor() {
		synchronized(color) {
			return color.get();
		}
	}

	/**
	 * Uzyskanie referencji na początek krawędzi skierowanej.
	 */
	@Override
	public RemoteVertex getSource() throws RemoteException {
		return v1;
	}

	/**
	 * Uzyskanie referencji na koniec krawędzi skierowanej.
	 */
	@Override
	public RemoteVertex getDest() throws RemoteException {
		return v2;
	}

	/**
	 * Przeliczenie koloru.
	 */
	@Override
	public void computeColor() throws RemoteException {
		int level;
		synchronized(this.level) {
			level = this.level.get();
		}
		synchronized(color) {
			color.set((int)(255.0*((double)(level - graph.getMinEdge().getLevel())/(double)(graph.getMaxEdge().getLevel() - graph.getMinEdge().getLevel()))));
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || (obj instanceof Edge && ((Edge)obj).v1.equals(this.v1) && ((Edge)obj).v2.equals(this.v2));
	}
	
	@Override
	public int hashCode() {
		return this.v1.hashCode() * this.v2.hashCode();
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public String getName() {
		return "Edge: " + v1 + " " + v2 + " level: " + level + " color: " + color;
	}

}
