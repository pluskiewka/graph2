package main;

import java.rmi.Naming;

import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import main.gui.MainFrame;
import main.remote.RemoteGraph;

public class Client {
	private static final Logger logger = Logger.getLogger(Client.class);
	
	static {
		System.setProperty("java.security.policy", "cfg/policy.properties");
		PropertyConfigurator.configure("cfg/client.logger.log4j.properties");
		
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
	
	public static void main(String []args) {
		RemoteGraph graph;
		
		if(args.length != 1) {
			System.err.println("Usage: java main/Client <server>");
			System.exit(-1);
		}
		
		try {
			graph = (RemoteGraph)Naming.lookup("//"+args[0]+"/Graph");
			new MainFrame(graph);
		} catch (Exception e) {
			logger.error(e.toString());
			System.exit(-1);
		}
	}
}
