package main;

import java.rmi.Naming;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Auto {

	private static final Logger logger = Logger.getLogger(Client.class);
	
	static {
		System.setProperty("java.security.policy", "cfg/policy.properties");
		PropertyConfigurator.configure("cfg/logger.log4j.properties");
		
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}
	}
	
	public static void main(String []args) {
		RemoteGraph graph;
		
		if(args.length != 1) {
			System.err.println("Usage: java main/Auto <server>");
			System.exit(-1);
		}
		
		try {
			graph = (RemoteGraph)Naming.lookup("//"+args[0]+"/Graph");
			
			for(int i=0; i<5; i++) {
				graph.newVertex(i);
			}
			
			Random rand = new Random(new Date().getTime());
			
			List<RemoteVertex> vertexes = graph.getVertexes();
			for(RemoteVertex vertex1 : vertexes) {
				for(RemoteVertex vertex2 : vertexes) {
					graph.newEdge(vertex1, vertex2, rand.nextInt(100)-50);
				}
			}
			
			List<RemoteEdge> edges = new LinkedList<RemoteEdge>();
			for(RemoteVertex vertex : vertexes) {
				edges.addAll(vertex.getEdges());
			}
			
			for(RemoteEdge edge : edges) {
				graph.setLevel(edge, rand.nextInt(200)-100);
			}

			Integer minLevel = null, maxLevel = null;
			
			for(RemoteEdge edge : edges) {
				if(minLevel == null || minLevel > edge.getLevel()) {
					minLevel = edge.getLevel();
				}
				if(maxLevel == null || maxLevel < edge.getLevel()) {
					maxLevel = edge.getLevel();
				}
			}
			
			for(RemoteEdge edge : edges) {
				System.err.println((int)255.0*((double)edge.getLevel()-minLevel)/(double)(maxLevel - minLevel) + " vs. "+ edge.getColor());
			}
			
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
}
