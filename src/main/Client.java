package main;

import java.rmi.Naming;

import javax.swing.UIManager;

import main.gui.MainFrame;
import main.remote.RemoteGraph;

public class Client {
	static {
		System.setProperty("java.security.policy", "policy.properties");
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (Exception e) {
			System.err.println(e.toString());
		} 
	}
	
	public static void main(String []args) {
		RemoteGraph graph;
		MainFrame frame;
		try {
			graph = (RemoteGraph)Naming.lookup("//127.0.0.1/Graph");
			frame = new MainFrame(graph);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
