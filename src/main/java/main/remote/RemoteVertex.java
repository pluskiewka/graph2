package main.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteVertex extends Remote {
	
	/**
	 * Pobranie identyfikatora wierzchołka.
	 * @return
	 * @throws RemoteException
	 */
	public Integer getId() throws RemoteException;
	
	/**
	 * Referencja na graf.
	 * @return
	 * @throws RemoteException
	 */
	public RemoteGraph getGraph() throws RemoteException;
	
	/**
	 * Dodanie nowej krawędzi skierowanej, wychodzącej od tego wierzchołka, a kończącej się na 
	 * wierzchołku vertex, z podaniem wagi krawędzi.
	 * @param vertex
	 * @param level
	 * @return
	 * @throws RemoteException
	 */
	public RemoteEdge newEdge(RemoteVertex vertex, Integer level) throws RemoteException;
	
	/**
	 * Przeliczenie wartości koloru na wszystkich krawędzia skierowanych mających początek w tym 
	 * wierzchołku, w zależności od wagi i wartości brzegowych wag krawędzi.
	 * @throws RemoteException
	 */
	public void computeColor() throws RemoteException;
	
	/**
	 * Przeszukiwanie w zbiorze krawędzi najmniejszej.
	 * @throws RemoteException
	 */
	public void computeMin() throws RemoteException;
	
	/**
	 * Przeszukiwanie w zbiorze krawędzi największej.
	 * @throws RemoteException
	 */
	public void computeMax() throws RemoteException;

	/**
	 * Uzyskanie listy wszystkich krawędzie skierowanych, wychodzących z tego wierzchołka.
	 * @return
	 * @throws RemoteException
	 */
	public List<RemoteEdge> getEdges() throws RemoteException;
	
	/**
	 * Pobranie nazwy krawędzi.
	 * @return
	 * @throws RemoteException
	 */
	public String getName() throws RemoteException;
}
