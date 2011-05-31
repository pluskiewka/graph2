package main.object;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
	
	private List<RemoteVertex> vertexes;
	private RemoteEdge minEdge, maxEdge;
	
	public Graph() throws RemoteException {
		this.vertexes = new LinkedList<RemoteVertex>();
	}

	/**
	 * Zwraca lokalną, minimalną krawędź.
	 */
	@Override
	public RemoteEdge getMinEdge() {
		return minEdge;
	}
	
	/**
	 * Zwraca lokalną, maksymalną krawędź.
	 */
	@Override
	public RemoteEdge getMaxEdge() {
		return maxEdge;
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
	
	/**
	 * Przeszukiwanie lokalne grafu, w celu znalezienia minimalnej krawędzi. Podobnie jak w przypadku 
	 * przeliczania koloru, wykonuje się to wielowątkowo w jednym węźle, gdzie jeden wątke odpowiada 
	 * jednemu wierzchołkowi.
	 */
	@Override
	public void computeMin() throws RemoteException {
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

	/**
	 * Przeszukiwanie lokalne grafu, w celu znalezienia maksymalnej krawędzi. Podobnie jak w przypadku 
	 * przeliczania koloru, wykonuje się to wielowątkowo w jednym węźle, gdzie jeden wątke odpowiada 
	 * jednemu wierzchołkowi.
	 */
	@Override
	public void computeMax() throws RemoteException {
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
	
	/**
	 * Ustawienie krawędzi minimalnej, o ile podana ma wagę mniejszą niż obecna minimalna krawędź.
	 */
	@Override
	public void setMin(RemoteEdge edge) throws RemoteException {
		synchronized(minEdge) {
			if(edge.getLevel() < minEdge.getLevel())
				minEdge = edge;
		}
	}
	
	/**
	 * Ustawienie krawędzi maksymalnej, o ile podana ma wagę większą niż obecna maksymalna krawędź.
	 */
	@Override
	public void setMax(RemoteEdge edge) throws RemoteException {
		synchronized(maxEdge) {
			if(edge.getLevel() > maxEdge.getLevel())
				maxEdge = edge;
		}
	}
	
	/**
	 * Dodanie nowej wierzchołka do lokalnego grafu.
	 */
	@Override
	public RemoteVertex newVertex(Integer id) throws RemoteException {
		RemoteVertex v = new Vertex(this, id);
		vertexes.add(v);
		return v;
	}
	
	/**
	 * Dodanie nowej krawędzi do grafu, wraz z ustaleniem wagi oraz kalibracją zakresu wag krawędzie.
	 */
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
	
	/**
	 * Ustawienie wartości wagi dla danej krawędzi, należy tę metodę używać.
	 */
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
