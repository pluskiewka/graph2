package main;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteServer;
import main.remote.RemoteVertex;

public class Server extends UnicastRemoteObject implements Serializable, RemoteServer, RemoteGraph {
	private static final long serialVersionUID = 1605997964417190854L;

	static {
		System.setProperty("java.security.policy", "policy.properties");
		
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}
		
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		} catch (RemoteException e) {
			System.err.println(e.toString());
			System.exit(-1);
		}
		
	}
	
	public static void main(String []args) {
		try {
			Naming.rebind("Graph", new Server());
		} catch (Exception e) {
			System.err.println(e.toString());
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
		System.err.println("new graph");
	}

	@Override
	public void unregisterGraph(RemoteGraph graph) {
		graphs.remove(graph);
		System.err.println("del graph");
	}

	@Override
	public RemoteVertex newVertex() throws RemoteException {
		currentGraph = (currentGraph +1) % graphs.size();
		return graphs.get(currentGraph).newVertex();
	}

	@Override
	public RemoteEdge newEdge(RemoteVertex v1, RemoteVertex v2, Integer level)
			throws RemoteException {
		return v1.getGraph().newEdge(v1, v2, level);
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
	}

	@Override
	public List<RemoteVertex> getVertexes() throws RemoteException {
		List<RemoteVertex> vertexes = new LinkedList<RemoteVertex>();
		
		for(RemoteGraph graph : graphs)
			vertexes.addAll(graph.getVertexes());
		
		return vertexes;
	}
}
