package main.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteEdge extends Remote {
	
	/**
	 * Referencja na graf.
	 * @return
	 * @throws RemoteException
	 */
	public RemoteGraph getGraph() throws RemoteException;
	
	/**
	 * Pobranie wartości wagi krawędzi wagi.
	 * @return
	 * @throws RemoteException
	 */
	public Integer getLevel() throws RemoteException;
	
	/**
	 * Ustawienie wartości wagi krawędzi, wołana automatycznie przez graf,
	 * nie należy wołać samemu, inaczej nie nastąpi ponowne ustawienie wartości
	 * zakresu wag krawędzi w grafie.
	 * @param level
	 * @throws RemoteException
	 */
	public void setLevel(Integer level) throws RemoteException;
	
	/**
	 * Uzyskanie wartości koloru krawędzi.
	 * @return
	 * @throws RemoteException
	 */
	public Integer getColor() throws RemoteException;
	
	/**
	 * Obliczenie wartości koloru.
	 * @throws RemoteException
	 */
	public void computeColor() throws RemoteException;

	/**
	 * Referencja na wierzchołek początkowy.
	 * @return
	 * @throws RemoteException
	 */
	public RemoteVertex getSource() throws RemoteException;
	
	/**
	 * Referencja na wierzchołek końcowy.
	 * @return
	 * @throws RemoteException
	 */
	public RemoteVertex getDest() throws RemoteException;

	/**
	 * Uzyskanie nazwy krawędzi.
	 * @return
	 * @throws RemoteException
	 */
	public String getName() throws RemoteException;
}
