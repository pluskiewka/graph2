package main.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Random;

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
public class GraphTest {

	@Mock RemoteGraph server;
	Integer minLevel, maxLevel;
	Graph graph;
	
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
		server = Mockito.mock(RemoteGraph.class);
		Mockito.when(server.getMinEdgeLength()).thenReturn(minLevel);
		Mockito.when(server.getMaxEdgeLength()).thenReturn(maxLevel);
		graph = new Graph(server);
	}

	@Test
	public final void testGetMinEdgeLength() throws RemoteException {
		assertEquals(minLevel, graph.getMinEdgeLength());
	}

	@Test
	public final void testGetMaxEdgeLength() throws RemoteException {
		assertEquals(maxLevel, graph.getMaxEdgeLength());
	}

	@Test
	public final void testGetVertexes() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testComputeColor() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testComputeMin() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testComputeMax() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetMin() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetMax() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testNewVertex() throws RemoteException {
		Integer id = 0;
		RemoteVertex vertex = graph.newVertex(id);
		assertEquals(id, vertex.getId());
		assertEquals(new Vertex(graph, id), vertex);
		assertTrue(graph.getVertexes().size() == 1);
		assertEquals(vertex, graph.getVertexes().get(0));
	}

	@Test
	public final void testNewEdgeAndSetLevel() throws RemoteException {
		Integer id = 0, level = 0;
		RemoteVertex v1 = graph.newVertex(id++);
		RemoteVertex v2 = graph.newVertex(id++);
		RemoteEdge edge = graph.newEdge(v1, v2, level);
		assertTrue(level == edge.getLevel());
		Random rand = new Random(new Date().getTime());
		level = rand.nextInt();
		graph.setLevel(edge, level);
		assertTrue(level == edge.getLevel());
	}

}
