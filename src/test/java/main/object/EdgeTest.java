package main.object;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Random;

import main.object.Edge;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EdgeTest {

	@Mock RemoteVertex v1, v2;
	@Mock RemoteGraph graph;
	Integer level, minLevel, maxLevel;
	Edge edge;
	
	@Before
	public void setUp() throws Exception {
		Random rand = new Random(new Date().getTime());
		minLevel = rand.nextInt();
		maxLevel = rand.nextInt();
		if(minLevel > maxLevel) {
			int t = minLevel;
			minLevel = maxLevel;
			maxLevel = t;
		}
		graph = Mockito.mock(RemoteGraph.class);
		Mockito.when(graph.getMinEdgeLength()).thenReturn(minLevel);
		Mockito.when(graph.getMaxEdgeLength()).thenReturn(maxLevel);
		level = rand.nextInt();
		edge = new Edge(graph, v1, v2, level);
	}

	@Test
	public final void computeAndGetColor() throws Exception {
		edge.computeColor();
		assertTrue((int)(255.0*((double)(level - minLevel)/(double)(maxLevel - minLevel))) == edge.getColor());
	}
	
	@Test
	public final void testGetGraph() {
		assertEquals(graph, edge.getGraph());
	}

	@Test
	public final void testSetAndGetLevel() {
		Random rand = new Random(new Date().getTime());
		level = rand.nextInt();
		edge.setLevel(level);
		assertEquals(level, edge.getLevel());
	}

	@Test
	public final void testGetSource() throws RemoteException {
		assertEquals(v1, edge.getSource());
	}

	@Test
	public final void testGetDest() throws RemoteException {
		assertEquals(v2, edge.getDest());
	}
	
	@Test
	public final void testEquals() throws RemoteException {
		assertEquals(new Edge(graph, v1, v2, level), edge);
	}
	
	@Test
	public final void testHashCode() throws RemoteException {
		assertTrue(v1.hashCode()*v2.hashCode() == edge.hashCode());
	}

}
