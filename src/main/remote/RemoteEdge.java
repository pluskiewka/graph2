package main.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteEdge extends Remote {
	public Integer getLevel() throws RemoteException;
	
	public void setLevel(Integer level) throws RemoteException;
	
	public Integer getColor() throws RemoteException;
	
	public void computeColor() throws RemoteException;

	public RemoteVertex getSource() throws RemoteException;
	
	public RemoteVertex getDest() throws RemoteException;

	public String getName() throws RemoteException;
}
