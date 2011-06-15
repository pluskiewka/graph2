package main;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteServer;
import main.remote.RemoteVertex;

/**
 * Obiekty tej klas intefejsem przeypominają graf, w rzeczywistości stanowią proxy dla całości
 * grafu, który jest rozproszony po węzła, i dopiero tam są rzeczywiste informacje o grafie.
 */
public class Server extends UnicastRemoteObject implements Serializable, RemoteServer, RemoteGraph {
	private static final long serialVersionUID = 1605997964417190854L;
	private static final Logger logger = Logger.getLogger(Server.class);

	static {
		System.setProperty("java.security.policy", "cfg/policy.properties");
		PropertyConfigurator.configure("cfg/server.logger.log4j.properties");
		
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
		if(args.length != 1) {
			System.err.println("Usage: java main/Server <hostname>");
			System.exit(-1);
		}
		
		System.setProperty("java.rmi.server.hostname", args[0]);
		
		try {
			Naming.rebind("Graph", new Server());
		} catch (Exception e) {
			logger.error(e.toString());
			System.exit(-1);
		}
	}
	
	private List<RemoteGraph> graphs;
	private AtomicInteger currentGraph, next;
	private RemoteEdge minEdge, maxEdge;
	
	public Server() throws RemoteException {
		graphs = Collections.synchronizedList(new LinkedList<RemoteGraph>());
		currentGraph = new AtomicInteger(0);
		next = new AtomicInteger(0);
	}
	
	@Override
	public void registerGraph(RemoteGraph graph) {
		synchronized(graphs) {
			graphs.add(graph);
		}
		
		logger.info("New node registered");
	}

	@Override
	public void unregisterGraph(RemoteGraph graph) {
		synchronized(graphs) {
			graphs.remove(graph);
		}
		
		logger.info("Node unregistered");
	}

	@Override
	public RemoteVertex newVertex(Integer id) throws RemoteException {
		RemoteVertex vertex;
		RemoteGraph graph;
		
		synchronized(graphs) {
			synchronized(currentGraph) {
				currentGraph.set((currentGraph.get() +1) % graphs.size());
				graph = graphs.get(currentGraph.get());
			}
		}
		
		synchronized(next) {
			vertex = graph.newVertex(next.incrementAndGet());
		}
		
		logger.info("New vertex " + vertex.getId());
		
		return vertex;
	}

	@Override
	public RemoteEdge newEdge(RemoteVertex v1, RemoteVertex v2, Integer level) throws RemoteException {
		RemoteEdge edge = v1.getGraph().newEdge(v1, v2, level);
		boolean t = false;
		
		if(minEdge == null && maxEdge == null) {
			minEdge = edge;
			maxEdge = edge;
		} else {
			synchronized(minEdge) {
			synchronized(maxEdge) {
				
				if(level < minEdge.getLevel()) {
					minEdge = edge;
					t = true;
					
					logger.info("Having new min edge");
				} else if(level > maxEdge.getLevel()) {
					maxEdge = edge;
					t = true;
					
					logger.info("Having new max edge");
				} else {
					t = false;
					
					logger.info("Only new edge");
				}
				
			}
			}
		}
		
		if(t) 
			this.computeColor();
		else
			edge.computeColor();
		
		logger.info("New edge " + edge.getSource().getId() + "-" + edge.getDest().getId());
		
		return edge;
	}

	@Override
	public Integer getMaxEdgeLength() throws RemoteException {
		synchronized(maxEdge) {
			return maxEdge.getLevel();
		}
	}

	@Override
	public Integer getMinEdgeLength() throws RemoteException {
		synchronized(minEdge) {
			return minEdge.getLevel();
		}
	}

	@Override
	public void computeColor() throws RemoteException {
		long p1 = System.nanoTime();
		synchronized(graphs) {
			for(RemoteGraph graph : graphs)
				graph.computeColor();
		}
		long p2 = System.nanoTime();
		logger.info("Computing color finished in " + Double.toString((double)(p2-p1)/1000000000.0));
	}

	@Override
	public void computeMin() throws RemoteException {
		long p1 = System.nanoTime();
		synchronized(graphs) {
			for(RemoteGraph graph : graphs)
				graph.computeMin();
		}
		long p2 = System.nanoTime();
		logger.info("Computing min finished in " + Double.toString((double)(p2-p1)/1000000000.0));
	}

	@Override
	public void computeMax() throws RemoteException {
		long p1 = System.nanoTime();
		synchronized(graphs) {
			for(RemoteGraph graph : graphs)
				graph.computeMax();
		}
		long p2 = System.nanoTime();
		logger.info("Computing max finished in " + Double.toString((double)(p2-p1)/1000000000.0));
	}

	@Override
	public void setMin(RemoteEdge edge) throws RemoteException {
		synchronized(minEdge) {
			if(edge.getLevel() < minEdge.getLevel()) {
				minEdge = edge;
				logger.info("New min edge " + minEdge.getSource().getId() + "-" + minEdge.getDest().getId());
			}
		}
	}

	@Override
	public void setMax(RemoteEdge edge) throws RemoteException {
		synchronized(maxEdge) {
			if(edge.getLevel() > maxEdge.getLevel()) {
				maxEdge = edge;
				logger.info("New max edge " + maxEdge.getSource().getId() + "-" + maxEdge.getDest().getId());
			}
		}
	}

	@Override
	public void setLevel(RemoteEdge edge, Integer level) throws RemoteException {
		if(edge.getLevel().equals(level))
			return;
		
		int min = minEdge.getLevel(), max = maxEdge.getLevel(), current = edge.getLevel();
		boolean tmin = false, tmax = false, tcolor = false; 
		
		synchronized(minEdge) {
		synchronized(maxEdge) {
			
			/* Krawędź jest jedną z granicznych i zmiana jej wagi spowoduje powiększenie zakresu wag,
			 * czyli krawędź nadal będzie jedną z granicznych. */
			if((minEdge.equals(edge) && level < current) || (maxEdge.equals(edge) && level > current)) {
				edge.getGraph().setLevel(edge, level);
				tcolor = true;
				
				logger.info("Enlarge range with the same edge border");
			/* Krawędź jest minimalną krawędzią i nowa wartość powoduje zawężenie zakresu wag. */
			} else if(minEdge.equals(edge) && level > current){
				edge.getGraph().setLevel(edge, level);
				tmin = true;
				tcolor = true;
				
				logger.info("Min edge is going to change level to higher");
			/* Krawędź jest maksymalną krawędzią i nowa wartość powoduje zawężenie zakresu wag. */
			} else if(maxEdge.equals(edge) && level < current) {
				edge.getGraph().setLevel(edge, level);
				tmax = true;
				tcolor = true;
				
				logger.info("Max edge is going to change level to lower");
			/* Krawędź nie jest krawędzią skrajną, ale nowa wartość podowuje powiększenie zakresu wag. */
			} else if(level > max) {
				edge.getGraph().setLevel(edge, level);
				maxEdge = edge;
				tcolor = true;
				
				logger.info("Having new max edge");
			/* Krawędź nie jest krawędzią skrajną, ale nowa wartość podowuje powiększenie zakresu wag. */
			} else if(level < min) {
				edge.getGraph().setLevel(edge, level);
				minEdge = edge;
				tcolor = true;
				
				logger.info("Having new min edge");
			/* Nie powoduje żadnych zmian, po prostu ustalamy wartość wagi krawędzi. */
			} else {
				edge.getGraph().setLevel(edge, level);
				
				logger.info("Only setting new level");
			}
		}
		}
		
		if(tmin)
			this.computeMin();
		if(tmax)
			this.computeMax();
		if(tcolor)
			this.computeColor();
		else
			edge.computeColor();
	}

	@Override
	public List<RemoteVertex> getVertexes() throws RemoteException {
		List<RemoteVertex> vertexes = new LinkedList<RemoteVertex>();
		
		synchronized(graphs) {
			for(RemoteGraph graph : graphs)
				vertexes.addAll(graph.getVertexes());
		}
		
		return vertexes;
	}
}
