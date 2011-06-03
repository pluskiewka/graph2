package main.object;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import main.object.Vertex;
import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VertexTest {

	@Mock RemoteGraph graph;
	@Mock RemoteVertex dest;
	Integer id = 1, level, level1, minLevel, maxLevel;
	Vertex vertex;
	
	@Before
	public void setUp() throws Exception {
		Random rand = new Random(new Date().getTime());
		minLevel = rand.nextInt();
		maxLevel = rand.nextInt();
		graph = Mockito.mock(RemoteGraph.class);
		Mockito.when(graph.getMinEdgeLength()).thenReturn(minLevel);
		Mockito.when(graph.getMaxEdgeLength()).thenReturn(maxLevel);
		level = rand.nextInt();
		level1 = rand.nextInt();
		vertex = new Vertex(graph, id);
	}
	
	@Test
	public final void testGetId() {
		assertEquals(id, vertex.getId());
	}

	@Test
	public final void testGetGraph() {
		assertEquals(graph, vertex.getGraph());
	}
	
	@Test
	public final void testGetEdgesZero() throws RemoteException {
		List<RemoteEdge> edges;
		edges = vertex.getEdges();
		assertTrue(edges.size() == 0);
	}
	
	@Test
	public final void testGetEdgesOne() throws RemoteException {
		List<RemoteEdge> edges;
		RemoteEdge edge = vertex.newEdge(dest, level);
		edges = vertex.getEdges();
		assertTrue(edges.size() == 1);
		assertTrue(edges.contains(edge));
		assertTrue(edges.get(0).equals(edge));
	}

	@Test
	public final void testNewEdge() throws RemoteException {
		RemoteEdge edge = vertex.newEdge(dest, level);
		assertEquals(level, edge.getLevel());
		assertEquals(edge.getDest(), dest);
		assertEquals(edge.getSource(), vertex);
	}

	@Test
	public final void testComputeColorZero() throws RemoteException {
		vertex.computeColor();
		List<RemoteEdge> edges = vertex.getEdges();
		assertTrue(edges.size() == 0);
	}

	@Test
	public final void testComputeColorOne() throws RemoteException {
		RemoteEdge edge = vertex.newEdge(dest, level);
		vertex.computeColor();
		List<RemoteEdge> edges = vertex.getEdges();
		assertTrue(edges.size() == 1);
		assertEquals(edge, edges.get(0));
		assertTrue((int)(255.0*((double)(level - minLevel)/(double)(maxLevel - minLevel))) == edge.getColor());
	}

	@Test
	public final void testComputeColorMany() throws RemoteException {
		RemoteEdge edge1 = vertex.newEdge(dest, level);
		RemoteEdge edge2 = vertex.newEdge(dest, level1);
		vertex.computeColor();
		List<RemoteEdge> edges = vertex.getEdges();
		assertTrue(edges.size() == 2);
		assertEquals(edge1, edges.get(0));
		assertEquals(edge2, edges.get(1));
		assertTrue((int)(255.0*((double)(level - minLevel)/(double)(maxLevel - minLevel))) == edge1.getColor());
		assertTrue((int)(255.0*((double)(level1 - minLevel)/(double)(maxLevel - minLevel))) == edge2.getColor());
	}
	
//	@Test
//	public final void testComputeMinZero() throws RemoteException {
//		vertex.computeMin();
//		List<RemoteEdge> edges = vertex.getEdges();
//		assertTrue(edges.size() == 0);
//	}
//	
//	@Test
//	public final void testComputeMinOne() throws RemoteException {
//		RemoteEdge edge = vertex.newEdge(dest, level);
//		vertex.computeMin();
//		fail();
//	}
//	
//	@Test
//	public final void testComputeMinMany() throws RemoteException {
//		vertex.newEdge(dest, level);
//		level = minLevel;
//		vertex.newEdge(dest, level);
//		vertex.computeMin();
//		fail();
//	}
//	
//	@Test
//	public final void testComputeMaxZero() throws RemoteException {
//		vertex.computeMax();
//		List<RemoteEdge> edges = vertex.getEdges();
//		assertTrue(edges.size() == 0);
//	}
//	
//	@Test
//	public final void testComputeMaxOne() throws RemoteException {
//		vertex.newEdge(dest, level);
//		vertex.computeMax();
//		fail();
//	}
//	
//	@Test
//	public final void testComputeMaxMany() throws RemoteException {
//		vertex.newEdge(dest, level);
//		vertex.newEdge(dest, maxLevel);
//		vertex.computeMax();
//		fail();
//	}
	
	@Test
	public final void testEquals() throws RemoteException {
		assertEquals(new Vertex(graph, id), vertex);
	}
	
	@Test
	public final void testHashCode() {
		assertTrue(id == vertex.hashCode());
	}
}
