package main.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfejs zastrzeżony tylko dla implementacji serwera, wymagany przez węzły.
 */
public interface RemoteServer extends Remote{
	/**
	 * Rejestracja nowego węzła obliczającego.
	 * @param graph
	 * @throws RemoteException
	 */
	public void registerGraph(RemoteGraph graph) throws RemoteException;
	
	/**
	 * Wyrejestrowanie węzła, dane nie są kopiowane. FIXME
	 * @param graph
	 * @throws RemoteException
	 */
	public void unregisterGraph(RemoteGraph graph) throws RemoteException;
}
