package main;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

public class Graph {
	public class Vertex {
		final int id;
		
		@Override
		public boolean equals(Object obj) {
			return (obj instanceof Vertex && id == ((Vertex)obj).id);
		}
		
		@Override
		public int hashCode() {
			return id;
		}
		
		public Vertex(int id) {
			this.id = id;
		}
	}
	
	/**
	 * Klasa krawedzi, wskazuje na wierzcholki obu koncow krawedzi.
	 * Przechowuje informacje o wadze krawedzi. Dokonuje modyfikacji
	 * wartosci min/max wsrod wag krawedzi.
	 */
	public class Edge extends Thread {
		/**
		 * Wykonuje obliczenie nowej wartosci koloru, w razie zmiany wartosci
		 * min/max wsrod wszystkich wag krawedzi.
		 */
		class ColorThread extends Thread {
			public ColorThread() {
				super("Color_"+v1.id+"-"+v2.id);
			}
			
			@Override
			public void run() {
				while(true) {
					synchronized(changeColor) {
						try {
							changeColor.wait();
						} catch (InterruptedException e) { }
						
						color = (int)(255.0-255.0*((double)value-(double)min)/((double)(max-min)));
					}
				}
			}
		}
		
		/**
		 * Wykonuje ustawienie nowej wartosci min/max wsrod wszystkich
		 * krawedzi grafu.
		 */
		class MinMaxThread extends Thread {
			public MinMaxThread() {
				super("MinMax_" + v1.id + "-" + v2.id);
			}
			
			@Override
			public void run() {
				while(true) {
					synchronized(changeMinMax) {
						try {
							changeMinMax.wait();
						} catch(InterruptedException e) { }
						
						if(min > value)
							min = value;
						else if(max < value)
							max = value;
						
						synchronized(changeCount) {
							count--;
							if(count == 0)
								changeCount.notifyAll();
						}
					}
				}
			}
		}
		
		@Override
		public void run() {
			this.colorThread.start();
			this.minMaxThread.start();
		}
		
		@Override
		public boolean equals(Object obj) {
			return (obj instanceof Edge && v1.equals(((Edge)obj).v1) && v2.equals(((Edge)obj).v2));
		}
		
		@Override
		public int hashCode() {
			return v1.id*v2.id;
		}
		
		@Override
		public String toString() {
			return "Edge "+v1.id+" "+v2.id;
		}
		
		final Vertex v1, v2;
		private int color, value;
		private Thread colorThread, minMaxThread;
		
		public Edge(Vertex v1, Vertex v2) {
			this.v1 = v1;
			this.v2 = v2;
			this.value = 0;
			
			this.colorThread = new ColorThread();
			this.minMaxThread = new MinMaxThread();
		}
		
		public void setValue(int v) {
			if((value == min || value == max) && (v > min || v < max)) {
				value = v;
				/* Zmiana zaweza zakres min/max, wymaga wyszukanie nowego 
				 * min/max'a, wymaga przekolorowania wszystkich krawedzi. */
				synchronized(changeCount) {
					count = edges.size();
				}
				synchronized(changeMinMax) {
					changeMinMax.notifyAll();
				}
				synchronized(changeCount) {
					if(count > 0)
						try {
							changeCount.wait();
						} catch (InterruptedException e) { }
				}
				synchronized(changeColor) {
					changeColor.notifyAll();
				}
			} else if(v < min || v > max) {
				value = v;
				/* Zmiana poszerza zakres min/max, nie wymaga wyszukiwania nowego
				 * min/max'a, wymaga przekolorowania wszystkich krawedzi. */
				synchronized(changeMinMax) {
					if(min > value)
						min = value;
					else if(max < value)
						max = value;
				}
				
				synchronized(changeColor) {
					changeColor.notifyAll();
				}
			} else {
				value = v;
				/* Zmiana nie powoduje zmian zakresu min/max'a, nie wymaga szukania nowego
				 * min/max'a, nie wymaga przekolorowania wszystkich krawedzi. */
				synchronized(changeColor) {
					color = (int)(255.0-255.0*((double)value-(double)min)/((double)(max-min)));
				}
			}
		}
		
		public int getValue() {
			return value;
		}
		
		public int getColor() {
			return color;
		}
	}
	
	private Collection<Vertex> vertexes;
	private Collection<Edge> edges;
	private int min, max, count;
	private Object changeColor, changeMinMax, changeCount;
	
	public Graph() {
		this.vertexes = new HashSet<Vertex>();
		this.edges = new HashSet<Edge>();
		this.min = 0;
		this.max = 0;
		this.changeColor = new Object();
		this.changeMinMax = new Object();
		this.changeCount = new Object();
	}
	
	public void addVertex(Vertex v) {
		vertexes.add(v);
	}
	
	public void addEdge(Edge e) {
		if(!edges.contains(e))
			if(edges.add(e))
				System.err.println("Dodano krawedz " + e.toString());
	}
	
	public Collection<Vertex> getVertexes() {
		return vertexes;
	}
	
	public Collection<Edge> getEdges() {
		return edges;
	}
	
	public static void main(String []args) {
		Graph g = new Graph();
		for(int i=0; i<5; i++)
			g.addVertex(g.new Vertex(i+1));
		System.err.println("Dodano wierzchołki");
		
		for(Vertex v1 : g.getVertexes())
			for(Vertex v2 : g.getVertexes())
				if(!v1.equals(v2))
					g.addEdge(g.new Edge(v1, v2));
		System.err.println("Dodano krawedzie");
		
		for(Edge e : g.getEdges())
			e.start();
		System.err.println("Uruchomiono watki");
		
		Random rand = new Random(new Date().getTime());
		
		for(Edge e : g.getEdges()) {
			e.setValue(rand.nextInt(1000));
		}
		System.err.println("Wykonano zmiany");
		
		int min = 0, max = 0;
		for(Edge e : g.getEdges()) {
			if(min > e.getValue())
				min = e.getValue();
			else if(max < e.getValue())
				max = e.getValue();
		}
		
		int color;
		for(Edge e : g.getEdges()) {
			color = (int)(255.0-255.0*((double)e.getValue()-(double)min)/((double)(max-min)));
			if( color != e.getColor() )
				System.err.println("Błąd w krawędzi " + e.v1.id + "-" + e.v2.id + " - " + color + " a " + e.getColor());
		}
		
		System.err.println("Koniec");
		System.exit(0);
	}
}
