package main.object;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import main.Node;
import main.Server;
import main.object.Graph;
import main.remote.RemoteEdge;
import main.remote.RemoteVertex;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GraphTest {

	Integer MIN = 2, MAX = 10;
	Server graph;
	Node node;
	RemoteEdge minEdge, maxEdge, edge;
	Integer id1 = 1, id2 = 2;
	RemoteVertex v1, v2;
	Graph g;
	
	@BeforeClass
	public void setUpBeforeClass() throws Exception {
		graph = new Server();
		node = new Node(graph);
		graph.registerGraph(graph);
	}
	
	@Before
	public void setUp() throws Exception {
		g = new Graph(graph);
		v1 = g.newVertex(id1);
		v2 = g.newVertex(id2);
		maxEdge = g.newEdge(v1, v2, MAX);
		minEdge = g.newEdge(v2, v1, MIN);
	}

	@Test
	public final void testGetMinEdgeLength() {
		try {
			assertEquals(MIN, g.getMinEdgeLength());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testGetMaxEdgeLength() {
		try {
			assertEquals(MAX, g.getMaxEdgeLength());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
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
		try {
			g.setMin(minEdge);
			assertEquals(MIN, g.getMinEdgeLength());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testSetMax() {
		try {
			g.setMax(maxEdge);
			assertEquals(MAX, g.getMaxEdgeLength());
		} catch(RemoteException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testNewVertex() {
		try {
			Integer id = 0;
			RemoteVertex v = g.newVertex(id);
			assertEquals(id, v.getId());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testNewEdge() {
		Integer level = 0;
		try {
			RemoteEdge e = g.newEdge(v1, v2, level);
			assertEquals(level, e.getLevel());
			assertEquals(v1, e.getSource());
			assertEquals(v2, e.getDest());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testSetLevel() {
		Integer level = 1;
		try {
			g.setLevel(edge, level);
			assertEquals(level, edge.getLevel());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
