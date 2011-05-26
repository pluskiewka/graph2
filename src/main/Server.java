package main;

import java.rmi.Naming;
import main.object.Graph;
import main.remote.RemoteGraph;

public class Server {
	static {
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}
	}
	
	public static void main(String []args) {
		RemoteGraph graph;
		try {
			graph = new Graph();
			Naming.rebind("Graph", graph);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}
