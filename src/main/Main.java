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
	private Edge minEdge, maxEdge;
	
	private int next = 0;
	
	public Main() {
		this.vertexes = new LinkedList<Vertex>();
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
		v1.edges.add(e);
		if(minEdge == null && maxEdge == null) {
			minEdge = e;
			maxEdge = e;
		} else {
			if(level < minEdge.level) {
				minEdge = e;
				for(final Vertex v : vertexes) {
					new Thread(new Runnable(){
						@Override
						public void run() {
							for(Edge t : v.edges) {
								t.color = color(t.level);
							}
						}
					}).start();
				}
			} else if(level > maxEdge.level) {
				maxEdge = e;
				for(final Vertex v : vertexes) {
					new Thread(new Runnable(){
						@Override
						public void run() {
							for(Edge t : v.edges) {
								t.color = color(t.level);
							}
						}
					}).start();
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
			for(final Vertex v : vertexes) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						for(Edge t : v.edges) {
							synchronized(minEdge) {
								if(t.level < minEdge.level)
									minEdge = t;
							}
						}
					}
				}).start();
			}
			for(final Vertex v : vertexes) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						for(Edge t : v.edges) {
							t.color = color(t.level);
						}
					}
				}).start();
			}
		} else if(maxEdge.equals(e) && level < e.level) {
			e.level = level;
			for(final Vertex v : vertexes) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						for(Edge t : v.edges) {
							synchronized(maxEdge) {
								if(t.level > maxEdge.level)
									maxEdge = t;
							}
						}
					}
				}).start();
			}
			for(final Vertex v : vertexes) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						for(Edge t : v.edges) {
							t.color = color(t.level);
						}
					}
				}).start();
			}
		} else if(level > maxEdge.level) {
			e.level = level;
			maxEdge = e;
			for(final Vertex v : vertexes) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						for(Edge t : v.edges) {
							t.color = color(t.level);
						}
					}
				}).start();
			}
		} else if(level < minEdge.level) {
			e.level = level;
			minEdge = e;
			for(final Vertex v : vertexes) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						for(Edge t : v.edges) {
							t.color = color(t.level);
						}
					}
				}).start();
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
				assert(e.color == (int)(255.0*(double)(e.level - min)/(double)(max-min)));
			}
		}
		p2 = System.nanoTime();
		System.out.println("Time: "+Double.toString((double)(p2-p1)/1000000000.0));
	}
}
