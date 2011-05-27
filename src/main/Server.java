package main;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteServer;
import main.remote.RemoteVertex;

public class Server extends UnicastRemoteObject implements Serializable, RemoteServer, RemoteGraph {
	private static final long serialVersionUID = 1605997964417190854L;
	private static final Logger logger = Logger.getLogger(Server.class);

	static {
		System.setProperty("java.security.policy", "policy.properties");
		PropertyConfigurator.configure("logger.log4j.properties");
		
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}
		
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		} catch (RemoteException e) {
			logger.error(e.toString());
			System.exit(-1);
		}
		
	}
	
	public static void main(String []args) {
		try {
			Naming.rebind("Graph", new Server());
			logger.info("Graph ready");
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
	
	private List<RemoteGraph> graphs;
	private int currentGraph;
	
	public Server() throws RemoteException {
		graphs = new LinkedList<RemoteGraph>();
		currentGraph = 0;
	}
	
	@Override
	public void registerGraph(RemoteGraph graph) {
		graphs.add(graph);
		logger.info("Registered new graph-node");
	}

	@Override
	public void unregisterGraph(RemoteGraph graph) {
		graphs.remove(graph);
		logger.info("Unregistered graph-node");
	}

	@Override
	public RemoteVertex newVertex() throws RemoteException {
		currentGraph = (currentGraph +1) % graphs.size();
		RemoteVertex v = graphs.get(currentGraph).newVertex();
		logger.info("New vertex " + v.getName());
		return v;
	}

	@Override
	public RemoteEdge newEdge(RemoteVertex v1, RemoteVertex v2, Integer level)
			throws RemoteException {
		RemoteEdge e = v1.getGraph().newEdge(v1, v2, level);
		logger.info("New edge " + e.getName());
		return e;
	}

	@Override
	public RemoteEdge getMaxEdge() throws RemoteException {
		RemoteEdge edge = null, tmp = null;
		for(RemoteGraph graph : graphs) {
			tmp = graph.getMaxEdge();
			if(edge == null || tmp.getLevel() > edge.getLevel())
				edge = tmp;
		}
		return edge;
	}

	@Override
	public RemoteEdge getMinEdge() throws RemoteException {
		RemoteEdge edge = null, tmp = null;
		for(RemoteGraph graph : graphs) {
			tmp = graph.getMinEdge();
			if(edge == null || tmp.getLevel() < edge.getLevel())
				edge = tmp;
		}
		return edge;
	}

	@Override
	public void computeColor() throws RemoteException {
		for(RemoteGraph graph : graphs)
			graph.computeColor();
	}

	@Override
	public void computeMin() throws RemoteException {
		for(RemoteGraph graph : graphs)
			graph.computeMin();
	}

	@Override
	public void computeMax() throws RemoteException {
		for(RemoteGraph graph : graphs)
			graph.computeMax();
	}

	@Override
	public void setMin(RemoteEdge edge) throws RemoteException {
		edge.getGraph().setMin(edge);
	}

	@Override
	public void setMax(RemoteEdge edge) throws RemoteException {
		edge.getGraph().setMax(edge);
	}

	@Override
	public void setLevel(RemoteEdge edge, Integer level) throws RemoteException {
		edge.getGraph().setLevel(edge, level);
		logger.info("Set edge " + edge.getName() + "level to " + level
				+ "\nMin: " + this.getMinEdge().getName() + ", Max: " + this.getMaxEdge().getName());
	}

	@Override
	public List<RemoteVertex> getVertexes() throws RemoteException {
		List<RemoteVertex> vertexes = new LinkedList<RemoteVertex>();
		
		for(RemoteGraph graph : graphs)
			vertexes.addAll(graph.getVertexes());
		
		return vertexes;
	}
}
