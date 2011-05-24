package main;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Main {
	private Collection<Vertex> vertexes;
	private Collection<Edge> edges;
	private Edge minEdge, maxEdge;
	
	private int next = 0;
	
	public Main() {
		this.vertexes = new LinkedList<Vertex>();
		this.edges = new HashSet<Edge>();
	}
	
	public Vertex newVertex() {
		Vertex v = new Vertex(next++);
		vertexes.add(v);
		return v;
	}
	
	private int color(int level) {
		return (int)(255.0*((double)(level - minEdge.level)/(double)(maxEdge.level - minEdge.level)));
	}
	
	public Edge newEdge(Vertex v1, Vertex v2, int level) {
		Edge e = new Edge(v1, v2);
		e.level = level;
		edges.add(e);
		if(minEdge == null && maxEdge == null) {
			minEdge = e;
			maxEdge = e;
		} else {
			if(level < minEdge.level) {
				minEdge = e;
//				Collection[] colls = new LinkedList[4];
//				for(int i=0; i<4; i++)
//					colls[i] = new LinkedList<Edge>();
//				
//				int i = 0;
//				for(Edge t : edges) {
//					colls[(i++)%4].add(t);
//				}
				for(Edge t : edges) {
					t.color = color(t.level);
				}
			} else if(level > maxEdge.level) {
				maxEdge = e;
				for(Edge t : edges) {
					t.color = color(t.level);
				}
			} else {
				e.color = color(e.level);
			}
		}
		return e;
	}
	
	public void setLevel(Edge e, int level) {
		if(e.level == level)
			return;
		
		if(minEdge.equals(e) && level < e.level) {
			e.level = level;
			e.color = color(e.level);
		} else if(maxEdge.equals(e) && level > e.level) {
			e.level = level;
			e.color = color(e.level);
		} else if(minEdge.equals(e) && level > e.level){
			e.level = level;
			for(Edge t : edges) {
				if(t.level < minEdge.level)
					minEdge = t;
			}
			for(Edge t : edges) {
				t.color = color(t.level);
			}
		} else if(maxEdge.equals(e) && level < e.level) {
			e.level = level;
			for(Edge t : edges) {
				if(t.level > maxEdge.level)
					maxEdge = t;
			}
			for(Edge t : edges) {
				t.color = color(t.level);
			}
		} else if(level > maxEdge.level) {
			e.level = level;
			maxEdge = e;
			for(Edge t : edges) {
				t.color = color(t.level);
			}
		} else if(level < minEdge.level) {
			e.level = level;
			minEdge = e;
			for(Edge t : edges) {
				t.color = color(t.level);
			}
		} else {
			e.level = level;
			e.color = color(e.level);
		}
	}
	
	public static void main(String []args) {
		Main graph = new Main();
		
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
        
		System.out.println("Edges.size() = "+graph.edges.size()+"\nTime: "+Double.toString((double)(p2-p1)/1000000000.0)+"\nNext, set new level....");
		while(!sc.nextLine().equals(""));
		
		p1 = System.nanoTime();
		for(Edge e : graph.edges) {
			graph.setLevel(e, rand.nextInt(400)-200);
		}
		p2 = System.nanoTime();
		
		System.out.println("Time: "+Double.toString((double)(p2-p1)/1000000000.0)+"\nNext, testing....");
		while(!sc.nextLine().equals(""));
		
		int min, max;
		Iterator<Edge> it = graph.edges.iterator();
		
		p1 = System.nanoTime();
		if(it.hasNext()) {
			Edge e = it.next();
			min = e.level; 
			max = e.level;
			while(it.hasNext()) {
				e = it.next();
				if(e.level < min)
					min = e.level;
				else if(e.level > max)
					max = e.level;
			}
			
			for(Edge t : graph.edges) {
				assert(t.color == (int)(255.0*(double)(t.level - min)/(double)(max-min)));
			}
		}
		p2 = System.nanoTime();
		System.out.println("Time: "+Double.toString((double)(p2-p1)/1000000000.0));
	}
}
