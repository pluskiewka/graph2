package main.object;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import main.object.Vertex;
import main.remote.RemoteEdge;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VertexTest {

	@Mock RemoteGraph graph;
	@Mock RemoteVertex dest;
	Integer id = 1;
	Vertex v;
	
	@Before
	public void setUp() throws Exception {
		v = new Vertex(graph, id);
	}

	@Test
	public final void testGetId() {
		assertEquals(id, v.getId());
	}

	@Test
	public final void testGetGraph() {
		assertEquals(graph, v.getGraph());
	}

	@Test
	public final void testNewEdge() {
		Integer level = 0;
		try {
			RemoteEdge edge = v.newEdge(dest, level);
			assertEquals(level, edge.getLevel());
			assertEquals(edge.getDest(), dest);
			assertEquals(edge.getSource(), v);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
