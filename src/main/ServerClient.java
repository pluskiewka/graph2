package main;

import java.rmi.Naming;

import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import main.gui.MainFrame;
import main.remote.RemoteGraph;

public class ServerClient {
	private static final Logger logger = Logger.getLogger(ServerClient.class);
	
	static {
		System.setProperty("java.security.policy", "cfg/policy.properties");
		PropertyConfigurator.configure("cfg/logger.log4j.properties");
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			logger.error(e.toString());
		} 
	}
	
	public static void main(String []args) {
		if(args.length != 1) {
			System.err.println("Usage: java main/ServerClient <hostname>");
			System.exit(-1);
		}
		
		System.setProperty("java.rmi.server.hostname", args[0]);
		RemoteGraph graph;
		
		try {
			graph = new Server();
			Naming.rebind("Graph", graph);
			logger.info("Graph ready");
			new MainFrame(graph);
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
}
