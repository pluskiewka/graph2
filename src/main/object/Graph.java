package main.object;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

public class Graph extends UnicastRemoteObject implements Serializable, RemoteGraph {
	private static final long serialVersionUID = -4380588331184967314L;
	
	private List<RemoteVertex> vertexes;
	private RemoteEdge minEdge, maxEdge;
	
	public Graph() throws RemoteException {
		this.vertexes = new LinkedList<RemoteVertex>();
	}
	
	@Override
	public RemoteEdge getMinEdge() {
		return minEdge;
	}
	
	@Override
	public RemoteEdge getMaxEdge() {
		return maxEdge;
	}

	@Override
	public List<RemoteVertex> getVertexes() throws RemoteException {
		return vertexes;
	}
	
	@Override
	public void computeColor() throws RemoteException {
		for(RemoteVertex vertex : vertexes) {
			vertex.computeColor();
		}
	}
	
	@Override
	public void computeMin() throws RemoteException {
		for(RemoteVertex vertex : vertexes) {
			vertex.computeMin();
		}
	}
	
	@Override
	public void computeMax() throws RemoteException {
		for(RemoteVertex vertex : vertexes) {
			vertex.computeMax();
		}
	}
	
	@Override
	public void setMin(RemoteEdge edge) throws RemoteException {
		synchronized(minEdge) {
			if(edge.getLevel() < minEdge.getLevel())
				minEdge = edge;
		}
	}
	
	@Override
	public void setMax(RemoteEdge edge) throws RemoteException {
		synchronized(maxEdge) {
			if(edge.getLevel() > maxEdge.getLevel())
				maxEdge = edge;
		}
	}
	
	@Override
	public RemoteVertex newVertex(Integer id) throws RemoteException {
		RemoteVertex v = new Vertex(this, id);
		vertexes.add(v);
		return v;
	}
	
	@Override
	public RemoteEdge newEdge(RemoteVertex v1, RemoteVertex v2, Integer level) throws RemoteException {
		RemoteEdge edge = v1.newEdge(v2, level);
		
		if(minEdge == null && maxEdge == null) {
			minEdge = edge;
			maxEdge = edge;
		} else {
			if(level < minEdge.getLevel()) {
				minEdge = edge;
				this.computeColor();
			} else if(level > maxEdge.getLevel()) {
				maxEdge = edge;
				this.computeColor();
			} else {
				edge.computeColor();
			}
		}
		return edge;
	}
	
	@Override
	public void setLevel(RemoteEdge edge, Integer level) throws RemoteException {
		if(edge.getLevel().equals(level))
			return;
		
		if((minEdge.equals(edge) && level < edge.getLevel()) || (maxEdge.equals(edge) && level > edge.getLevel())) {
			edge.setLevel(level);
			edge.computeColor();
		} else if(minEdge.equals(edge) && level > edge.getLevel()){
			edge.setLevel(level);
			this.computeMin();
			this.computeColor();
		} else if(maxEdge.equals(edge) && level < edge.getLevel()) {
			edge.setLevel(level);
			this.computeMax();
			this.computeColor();
		} else if(level > maxEdge.getLevel()) {
			edge.setLevel(level);
			maxEdge = edge;
			this.computeColor();
		} else if(level < minEdge.getLevel()) {
			edge.setLevel(level);
			minEdge = edge;
			this.computeColor();
		} else {
			edge.setLevel(level);
			edge.computeColor();
		}
	}

}
