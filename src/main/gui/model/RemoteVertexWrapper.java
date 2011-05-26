package main.gui.model;

import java.rmi.RemoteException;

import main.remote.RemoteVertex;

public class RemoteVertexWrapper {
	public final RemoteVertex vertex;
	
	public RemoteVertexWrapper(RemoteVertex vertex) {
		this.vertex = vertex;
	}
	
	public String toString() {
		try {
			return vertex.getName();
		} catch (RemoteException e) {
			return "error";
		}
	}
}