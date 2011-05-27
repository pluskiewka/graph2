package main;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import main.gui.MainFrame;
import main.remote.RemoteGraph;

public class Client {
	private static final Logger logger = Logger.getLogger(Client.class);
	
	static {
		System.setProperty("java.security.policy", "policy.properties");
		PropertyConfigurator.configure("logger.log4j.properties");
		
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}
		
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		} catch (RemoteException e) {
			logger.warn(e.toString());
		}

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			System.err.println(e.toString());
		} 
	}
	
	public static void main(String []args) {
		RemoteGraph graph;
		try {
			graph = (RemoteGraph)Naming.lookup("//127.0.0.1/Graph");
			new MainFrame(graph);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
