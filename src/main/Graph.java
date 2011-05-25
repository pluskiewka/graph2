package main;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Graph {
	private Collection<Vertex> vertexes;
	private Edge minEdge, maxEdge;
	
	private int next = 0;
	
	public Graph() {
		this.vertexes = new LinkedList<Vertex>();
	}
	
	public int color(int level) {
		return (int)(255.0*((double)(level - minEdge.level)/(double)(maxEdge.level - minEdge.level)));
	}
	
	private void computeColor() {
		for(final Vertex v : vertexes) {
			v.computeColor();
		}
	}
	
	private void computeMin() {
		for(final Vertex v : vertexes) {
			v.computeMin();
		}
	}
	
	private void computeMax() {
		for(final Vertex v : vertexes) {
			v.computeMax();
		}
	}
	
	void setMin(Edge edge) {
		synchronized(minEdge) {
			if(edge.level < minEdge.level)
				minEdge = edge;
		}
	}
	
	void setMax(Edge edge) {
		synchronized(maxEdge) {
			if(edge.level > maxEdge.level)
				maxEdge = edge;
		}
	}
	
	public Vertex newVertex() {
		Vertex v = new Vertex(this, next++);
		vertexes.add(v);
		return v;
	}
	
	public Edge newEdge(Vertex v1, Vertex v2, int level) {
		Edge e = v1.newEdge(v2);
		
		if(minEdge == null && maxEdge == null) {
			minEdge = e;
			maxEdge = e;
		} else {
			if(level < minEdge.level) {
				minEdge = e;
				computeColor();
			} else if(level > maxEdge.level) {
				maxEdge = e;
				computeColor();
			} else {
				e.color = color(e.level);
			}
		}
		return e;
	}
	
	public void setLevel(Edge e, int level) {
		if(e.level == level)
			return;
		
		if((minEdge.equals(e) && level < e.level) || (maxEdge.equals(e) && level > e.level)) {
			e.level = level;
			e.color = color(e.level);
		} else if(minEdge.equals(e) && level > e.level){
			e.level = level;
			computeMin();
			computeColor();
		} else if(maxEdge.equals(e) && level < e.level) {
			e.level = level;
			computeMax();
			computeColor();
		} else if(level > maxEdge.level) {
			e.level = level;
			maxEdge = e;
			computeColor();
		} else if(level < minEdge.level) {
			e.level = level;
			minEdge = e;
			computeColor();
		} else {
			e.level = level;
			e.color = color(e.level);
		}
	}
	
	public static void main(String []args) {
		Graph graph = new Graph();
		
		java.util.Scanner sc = new Scanner(System.in);
		
		System.err.println("First, new vertexes...");
		while(!sc.nextLine().equals(""));
		
		long p1 = System.nanoTime();
		for(int i=0; i<Integer.parseInt(args[0]); i++) {
			graph.newVertex();
		}
		long p2 = System.nanoTime();

		Random rand = new Random(new Date().getTime());
		
		System.err.println("Time: "+Double.toString((double)(p2-p1)/1000000000.0)+"\nNext, new edges...");
		while(!sc.nextLine().equals(""));
		
		p1 = System.nanoTime();
		for(Vertex v1 : graph.vertexes) {
			for(Vertex v2 : graph.vertexes) {
				graph.newEdge(v1, v2, rand.nextInt(200)-100);
			}
		}
        p2 = System.nanoTime();
        
		System.out.println("Time: "+Double.toString((double)(p2-p1)/1000000000.0)+"\nNext, set new level....");
		while(!sc.nextLine().equals(""));
		
		p1 = System.nanoTime();
		for(Vertex v : graph.vertexes) {
			for(Edge e : v.edges) {
				graph.setLevel(e, rand.nextInt(400)-200);
			}
		}
		p2 = System.nanoTime();
		
		System.out.println("Time: "+Double.toString((double)(p2-p1)/1000000000.0)+"\nNext, testing....");
		while(!sc.nextLine().equals(""));
		
		Integer min = null, max = null;
		
		p1 = System.nanoTime();
		for(Vertex v : graph.vertexes) {
			for(Edge e : v.edges) {
				if((min != null && e.level < min) || (min == null))
					min = e.level;
				else if((max != null && e.level > max) || (max == null))
					max = e.level;
			}
		}
		
		for(Vertex v : graph.vertexes) {
			for(Edge e : v.edges) {
				if(e.color != (int)(255.0*(double)(e.level - min)/(double)(max-min)))
					System.err.println(e.toString() + " powinno być " + (int)(255.0*(double)(e.level - min)/(double)(max-min)));
			}
		}
		p2 = System.nanoTime();
		System.out.println("Time: "+Double.toString((double)(p2-p1)/1000000000.0));
	}
}
