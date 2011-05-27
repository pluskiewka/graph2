package main.gui.model;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import main.remote.RemoteVertex;

public class RemoteVertexWrapper {
	private static final Logger logger = Logger.getLogger(RemoteVertexWrapper.class);
	public final RemoteVertex vertex;
	
	public RemoteVertexWrapper(RemoteVertex vertex) {
		this.vertex = vertex;
	}
	
	public String toString() {
		try {
			return vertex.getName();
		} catch (RemoteException e) {
			logger.error(e.toString());
			return "null";
		}
	}
}