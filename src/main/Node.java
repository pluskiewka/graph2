package main;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import main.object.Graph;
import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteServer;
import main.remote.RemoteVertex;

public class Node extends UnicastRemoteObject implements Serializable, RemoteGraph {
	private static final long serialVersionUID = 275245875093789520L;
	private static final Logger logger = Logger.getLogger(Node.class);
	private static RemoteServer server;
	
	static {
		System.setProperty("java.security.policy", "policy.properties");
		PropertyConfigurator.configure("logger.log4j.properties");
		
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}
		
		try {
			server = (RemoteServer)Naming.lookup("//127.0.0.1/Graph");
		} catch (Exception e) {
			logger.error(e.toString());
			System.exit(-1);
		} 
	}
	
	public static void main(String []args) {
		Node node;
		try {
			node = new Node();
			Naming.rebind("Graph"+args[0], node);
			server.registerGraph(node);
			logger.info("Graph"+args[0]+" ready");
		} catch (Exception e) {
			logger.error(e.toString());
		} 
	}
	
	private final RemoteGraph localGraph;
	
	public Node() throws RemoteException {
		this.localGraph = new Graph();
	}

	@Override
	public RemoteVertex newVertex(Integer id) throws RemoteException {
		RemoteVertex v = localGraph.newVertex(id);
		logger.info("New vertex " + v.getName());
		return v;
	}

	@Override
	public RemoteEdge newEdge(RemoteVertex v1, RemoteVertex v2, Integer level)
			throws RemoteException {
		RemoteEdge e = localGraph.newEdge(v1, v2, level);
		logger.info("New edge " + e.getName());
		return e;
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
		logger.info("Set edge " + edge.getName() + "level to " + level
				+ "\nMin: " + localGraph.getMinEdge().getName() + ", Max: " + localGraph.getMaxEdge().getName());
	}

	@Override
	public List<RemoteVertex> getVertexes() throws RemoteException {
		return localGraph.getVertexes();
	}
	
}
