package main.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteGraph extends Remote {

	public RemoteVertex newVertex(Integer id) throws RemoteException;
	
	public RemoteEdge newEdge(RemoteVertex v1, RemoteVertex v2, Integer level) throws RemoteException;
	
	public RemoteEdge getMaxEdge() throws RemoteException;
	
	public RemoteEdge getMinEdge() throws RemoteException;
	
	public void computeColor() throws RemoteException;
	
	public void computeMin() throws RemoteException;
	
	public void computeMax() throws RemoteException;
	
	public void setMin(RemoteEdge edge) throws RemoteException;
	
	public void setMax(RemoteEdge edge) throws RemoteException;
	
	public void setLevel(RemoteEdge edge, Integer level) throws RemoteException;

	public List<RemoteVertex> getVertexes() throws RemoteException;
}
