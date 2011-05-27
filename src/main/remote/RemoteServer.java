package main.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote{
	
	public void registerGraph(RemoteGraph graph) throws RemoteException;
	
	public void unregisterGraph(RemoteGraph graph) throws RemoteException;
}
