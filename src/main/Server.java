package main;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import main.object.Graph;
import main.remote.RemoteGraph;

public class Server {
	static {
		System.setProperty("java.security.policy", "policy.properties");
		
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}
		
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		} catch (RemoteException e) {
			System.err.println(e.toString());
			System.exit(-1);
		}
		
		
	}
	
	public static void main(String []args) {
		RemoteGraph graph;
		try {
			graph = new Graph();
			Naming.rebind("Graph", graph);
			System.err.println("Ready...");
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}
