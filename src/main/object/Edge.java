package main.object;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class Edge extends UnicastRemoteObject implements Serializable, RemoteEdge {
	private static final long serialVersionUID = -6302028332002939511L;
	
	private final RemoteGraph graph;
	private final RemoteVertex v1, v2;
	private int level, color;
	
	public Edge(RemoteGraph graph, RemoteVertex v1, RemoteVertex v2, int level) throws RemoteException {
		this.graph = graph;
		this.v1 = v1;
		this.v2 = v2;
		this.level = level;
	}
	
	@Override
	public Integer getLevel() {
		return level;
	}
	
	@Override
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	@Override
	public Integer getColor() {
		return color;
	}

	@Override
	public RemoteVertex getSource() throws RemoteException {
		return v1;
	}

	@Override
	public RemoteVertex getDest() throws RemoteException {
		return v2;
	}

	@Override
	public void computeColor() throws RemoteException {
		color = (int)(255.0*((double)(level - graph.getMinEdge().getLevel())/(double)(graph.getMaxEdge().getLevel() - graph.getMinEdge().getLevel())));
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
		return "Edge: " + v1 + " " + v2 + " level: " + level + " color: " + color;
	}

}
