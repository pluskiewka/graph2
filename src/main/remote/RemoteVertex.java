package main.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteVertex extends Remote {
	
	public Integer getId() throws RemoteException;
	
	public RemoteGraph getGraph() throws RemoteException;
	
	public RemoteEdge newEdge(RemoteVertex vertex, Integer level) throws RemoteException;
	
	public void computeColor() throws RemoteException;
	
	public void computeMin() throws RemoteException;
	
	public void computeMax() throws RemoteException;

	public List<RemoteEdge> getEdges() throws RemoteException;
}
