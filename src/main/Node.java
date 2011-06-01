package main;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import main.object.Graph;
import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteServer;
import main.remote.RemoteVertex;

/**
 * Reperezentuje lokalny fragment grafu, czyli zbiór wierczhołków, który każdy z nich ma zbiór
 * krawędzi skierowanych.
 */
public class Node extends UnicastRemoteObject implements Serializable, RemoteGraph {
	private static final long serialVersionUID = 275245875093789520L;
	private static final Logger logger = Logger.getLogger(Node.class);
	private static RemoteServer server;
	
	static {
		System.setProperty("java.security.policy", "cfg/policy.properties");
		PropertyConfigurator.configure("cfg/logger.log4j.properties");
		
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}
		
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		} catch (RemoteException e) {
			logger.warn(e.toString());
		}
	}
	
	public static void main(String []args) {
		Node node;
		
		if(args.length != 1) {
			System.err.println("Usage: java main/Node <server>");
			System.exit(-1);
		}
		
		try {
			try {
				server = (RemoteServer)Naming.lookup("//"+args[0]+"/Graph");
			} catch (Exception e) {
				logger.error(e.toString());
				System.exit(-1);
			} 
			
			node = new Node(server);
			server.registerGraph(node);
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		} 
	}
	
	private final RemoteGraph localGraph;
	
	public Node(RemoteGraph graph) throws RemoteException {
		this.localGraph = new Graph(graph);
	}

	@Override
	public RemoteVertex newVertex(Integer id) throws RemoteException {
		return localGraph.newVertex(id);
	}

	@Override
	public RemoteEdge newEdge(RemoteVertex v1, RemoteVertex v2, Integer level) throws RemoteException {
		return localGraph.newEdge(v1, v2, level);
	}

	@Override
	public Integer getMaxEdgeLength() throws RemoteException {
		return localGraph.getMaxEdgeLength();
	}

	@Override
	public Integer getMinEdgeLength() throws RemoteException {
		return localGraph.getMinEdgeLength();
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
