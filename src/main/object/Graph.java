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
	public RemoteEdge getMinEdge() throws RemoteException {
		return graph.getMinEdge();
	}
	
	/**
	 * Zwraca maksymalną krawędź w całym grafie.
	 * @throws RemoteException 
	 */
	@Override
	public RemoteEdge getMaxEdge() throws RemoteException {
		return graph.getMaxEdge();
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
		synchronized(vertexes) {
			for(final RemoteVertex vertex : vertexes) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							vertex.computeColor();
						} catch (RemoteException e) {
							logger.error(e.toString());
						}
					}
				}).start();
			}
		}
	}
	
	/**
	 * Przeszukiwanie lokalne grafu, w celu znalezienia minimalnej krawędzi. Podobnie jak w przypadku 
	 * przeliczania koloru, wykonuje się to wielowątkowo w jednym węźle, gdzie jeden wątke odpowiada 
	 * jednemu wierzchołkowi.
	 */
	@Override
	public void computeMin() throws RemoteException {
		synchronized(vertexes) {
			for(final RemoteVertex vertex : vertexes) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							vertex.computeMin();
						} catch (RemoteException e) {
							logger.error(e.toString());
						}
					}
				}).start();
			}
		}
	}

	/**
	 * Przeszukiwanie lokalne grafu, w celu znalezienia maksymalnej krawędzi. Podobnie jak w przypadku 
	 * przeliczania koloru, wykonuje się to wielowątkowo w jednym węźle, gdzie jeden wątke odpowiada 
	 * jednemu wierzchołkowi.
	 */
	@Override
	public void computeMax() throws RemoteException {
		synchronized(vertexes) {
			for(final RemoteVertex vertex : vertexes) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							vertex.computeMax();
						} catch (RemoteException e) {
							logger.error(e.toString());
						}
					}
				}).start();
			}
		}
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
