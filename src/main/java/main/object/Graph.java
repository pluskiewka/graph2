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

/**
 * Obiekty tej klasy nie są utrzymywane na serwerze, ale w węzłach, jako lokalne grafy, przechowujące
 * fragmenty całego grafu.
 */
public class Graph extends UnicastRemoteObject implements Serializable, RemoteGraph {
	private static final long serialVersionUID = -4380588331184967314L;
	private static final Logger logger = Logger.getLogger(Graph.class);
	
	private final RemoteGraph graph;
	private List<RemoteVertex> vertexes;
	
	public Graph(RemoteGraph graph) throws RemoteException {
		this.graph = graph;
		this.vertexes = Collections.synchronizedList(new LinkedList<RemoteVertex>());
	}

	/**
	 * Zwraca minimalną krawędź w całym grafie.
	 * @throws RemoteException 
	 */
	@Override
	public Integer getMinEdgeLength() throws RemoteException {
		return graph.getMinEdgeLength();
	}
	
	/**
	 * Zwraca maksymalną krawędź w całym grafie.
	 * @throws RemoteException 
	 */
	@Override
	public Integer getMaxEdgeLength() throws RemoteException {
		return graph.getMaxEdgeLength();
	}

	/**
	 * Zwraca lokalny zbiór wszystkich wierzchołków grafu.
	 */
	@Override
	public List<RemoteVertex> getVertexes() throws RemoteException {
		return vertexes;
	}
	
	/**
	 * Przelicza wartości koloru, w zależności od wagi krawędzi. Wykonywane jest to wielowątkowo
	 * w jednym weźle, w każdym wątku przeliczane są krawędzie skierowane, wychodzące z jednego 
	 * wierzchołka.
	 */
	@Override
	public void computeColor() throws RemoteException {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized(vertexes) {
					long p1 = System.nanoTime();
					for(RemoteVertex vertex : vertexes) {
						try {
							vertex.computeColor();
						} catch (RemoteException e) {
							logger.warn(e.toString());
						}
					}
					long p2 = System.nanoTime();
					logger.info("Compute color finished in " + Double.toString((double)(p2-p1)/1000000000.0));
				}
			}
			
		}).start();
	}
	
	/**
	 * Przeszukiwanie lokalne grafu, w celu znalezienia minimalnej krawędzi. Podobnie jak w przypadku 
	 * przeliczania koloru, wykonuje się to wielowątkowo w jednym węźle, gdzie jeden wątke odpowiada 
	 * jednemu wierzchołkowi.
	 */
	@Override
	public void computeMin() throws RemoteException {
		long p1 = System.nanoTime();
		for(final RemoteVertex vertex : vertexes) {
			try {
				vertex.computeMin();
			} catch (RemoteException e) {
				logger.warn(e.toString());
			}
		}
		long p2 = System.nanoTime();
		logger.info("Compute min finished in " + Double.toString((double)(p2-p1)/1000000000.0));
	}

	/**
	 * Przeszukiwanie lokalne grafu, w celu znalezienia maksymalnej krawędzi. Podobnie jak w przypadku 
	 * przeliczania koloru, wykonuje się to wielowątkowo w jednym węźle, gdzie jeden wątke odpowiada 
	 * jednemu wierzchołkowi.
	 */
	@Override
	public void computeMax() throws RemoteException {
		long p1 = System.nanoTime();
		for(final RemoteVertex vertex : vertexes) {
			try {
				vertex.computeMax();
			} catch (RemoteException e) {
				logger.warn(e.toString());
			}		
		}
		long p2 = System.nanoTime();
		logger.info("Compute max finished in " + Double.toString((double)(p2-p1)/1000000000.0));
	}
	
	/**
	 * Ustawienie krawędzi minimalnej, o ile podana ma wagę mniejszą niż obecna minimalna krawędź.
	 */
	@Override
	public void setMin(RemoteEdge edge) throws RemoteException {
		graph.setMin(edge);
	}
	
	/**
	 * Ustawienie krawędzi maksymalnej, o ile podana ma wagę większą niż obecna maksymalna krawędź.
	 */
	@Override
	public void setMax(RemoteEdge edge) throws RemoteException {
		graph.setMax(edge);
	}
	
	/**
	 * Dodanie nowej wierzchołka do lokalnego grafu.
	 */
	@Override
	public RemoteVertex newVertex(Integer id) throws RemoteException {
		RemoteVertex v = new Vertex(this, id);
		synchronized(vertexes) {
			vertexes.add(v);
		}
		return v;
	}
	
	/**
	 * Dodanie nowej krawędzi do grafu, wraz z ustaleniem wagi.
	 */
	@Override
	public RemoteEdge newEdge(RemoteVertex v1, RemoteVertex v2, Integer level) throws RemoteException {
		return v1.newEdge(v2, level);
	}
	
	/**
	 * Ustawienie wartości wagi dla danej krawędzi, należy tę metodę używać.
	 */
	@Override
	public void setLevel(RemoteEdge edge, Integer level) throws RemoteException {
		edge.setLevel(level);
	}

}
