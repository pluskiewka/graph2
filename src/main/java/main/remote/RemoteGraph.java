package main.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Reprezentuje interfejs grafu, implementacja jego jest dwojaka, pod postacią serwera, gdzie 
 * utrzymywane są lokalne grafy rozproszone na poszczególnych węzłach.
 */
public interface RemoteGraph extends Remote {

	/**
	 * Utworzenie nowego wierzchołka..
	 * @param id
	 * @return
	 * @throws RemoteException
	 */
	public RemoteVertex newVertex(Integer id) throws RemoteException;
	
	/**
	 * Utworzenie nowej krawędzi, z ustawieniem wartości wagi i tym samym koloru.
	 * @param v1
	 * @param v2
	 * @param level
	 * @return
	 * @throws RemoteException
	 */
	public RemoteEdge newEdge(RemoteVertex v1, RemoteVertex v2, Integer level) throws RemoteException;
	
	/**
	 * Pobranie krawędzi o największej wadze w danym grafie.
	 * @return
	 * @throws RemoteException
	 */
	public Integer getMaxEdgeLength() throws RemoteException;
	
	/**
	 * Pobranie krawędzi o najmniejszej wadze w danym grafie.
	 * @return
	 * @throws RemoteException
	 */
	public Integer getMinEdgeLength() throws RemoteException;
	
	/**
	 * Przeliczenie wartości koloru w zależności od wagi krawędzi oraz wartości brzegowych zakresu wag.
	 * @throws RemoteException
	 */
	public void computeColor() throws RemoteException;
	
	/**
	 * Przeszkuwanie grafu w celu znalezienia najmniejszej krawędzi.
	 * @throws RemoteException
	 */
	public void computeMin() throws RemoteException;
	
	
	/**
	 * Przeszukwianie grafu w celu znalezienia największej krawędzi.
	 * @throws RemoteException
	 */
	public void computeMax() throws RemoteException;
	
	/**
	 * W przypadku, gdy krawędź podana jako parametr ma mniejszą wagę, zostanie ustalona jako najmniejsza.
	 * @param edge
	 * @throws RemoteException
	 */
	public void setMin(RemoteEdge edge) throws RemoteException;
	
	
	/**
	 * W przypadku, gdy krawędź podana jako parametr ma większą wagę, zostanie ustalona jako największa.
	 * @param edge
	 * @throws RemoteException
	 */
	public void setMax(RemoteEdge edge) throws RemoteException;
	
	/**
	 * Ustalenie wartości krawędzi, a tym samym czy wartość wagi krawędzi dokonuje zmiany zakresu wag krawędzi.
	 * @param edge
	 * @param level
	 * @throws RemoteException
	 */
	public void setLevel(RemoteEdge edge, Integer level) throws RemoteException;
	
	/**
	 * Pobranie listy wszystkich krawędzi grafu.
	 * @return
	 * @throws RemoteException
	 */
	public List<RemoteVertex> getVertexes() throws RemoteException;
}
