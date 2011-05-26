package main;

import java.rmi.Naming;


import main.gui.MainFrame;
import main.remote.RemoteGraph;

public class Client {
	
	public static void main(String []args) {
		RemoteGraph graph;
		MainFrame frame;
		try {
			graph = (RemoteGraph)Naming.lookup("//127.0.0.1/Graph");
			frame = new MainFrame(graph);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}
