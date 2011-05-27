package main;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import main.object.Graph;
import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteServer;
import main.remote.RemoteVertex;

public class Node extends UnicastRemoteObject implements Serializable, RemoteGraph {
	private static final long serialVersionUID = 275245875093789520L;
	private static RemoteServer server;
	
	static {
		System.setProperty("java.security.policy", "policy.properties");
		
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}
		
		try {
			server = (RemoteServer)Naming.lookup("//127.0.0.1/Graph");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} 
	}
	
	public static void main(String []args) {
		Node node;
		try {
			node = new Node();
			Naming.rebind("Graph"+args[0], node);
			server.registerGraph(node);
		} catch (Exception e) {
			System.err.println(e.toString());
		} 
	}
	
	private final RemoteGraph localGraph;
	
	public Node() throws RemoteException {
		this.localGraph = new Graph();
	}

	@Override
	public RemoteVertex newVertex() throws RemoteException {
		return localGraph.newVertex();
	}

	@Override
	public RemoteEdge newEdge(RemoteVertex v1, RemoteVertex v2, Integer level)
			throws RemoteException {
		return localGraph.newEdge(v1, v2, level);
	}

	@Override
	public RemoteEdge getMaxEdge() throws RemoteException {
		return localGraph.getMaxEdge();
	}

	@Override
	public RemoteEdge getMinEdge() throws RemoteException {
		return localGraph.getMinEdge();
	}

	@Override
	public void computeColor() throws RemoteException {
		localGraph.computeColor();
	}

	@Override
	public void computeMin() throws RemoteException {
		localGraph.computeMin();
	}

	@Override
	public void computeMax() throws RemoteException {
		localGraph.computeMax();
	}

	@Override
	public void setMin(RemoteEdge edge) throws RemoteException {
		localGraph.setMin(edge);
	}

	@Override
	public void setMax(RemoteEdge edge) throws RemoteException {
		localGraph.setMax(edge);
	}

	@Override
	public void setLevel(RemoteEdge edge, Integer level) throws RemoteException {
		localGraph.setLevel(edge, level);
	}

	@Override
	public List<RemoteVertex> getVertexes() throws RemoteException {
		return localGraph.getVertexes();
	}
	
}
