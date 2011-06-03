package main.object;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import main.object.Edge;
import main.remote.RemoteGraph;
import main.remote.RemoteVertex;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EdgeTest {

	@Mock RemoteVertex v1, v2;
	@Mock RemoteGraph graph;
	Integer level;
	Edge e;
	
	@Before
	public void setUp() throws Exception {
		level = 0;
		e = new Edge(graph, v1, v2, level);
	}

	@Test
	public final void testGetGraph() {
		assertEquals(graph, e.getGraph());
	}

	@Test
	public final void testGetLevel() {
		assertEquals(level, e.getLevel());
	}

	@Test
	public final void testSetLevel() {
		level = 1;
		e.setLevel(level);
		assertEquals(level, e.getLevel());
	}

	@Test
	public final void testGetSource() {
		try {
			assertEquals(v1, e.getSource());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testGetDest() {
		try {
			assertEquals(v2, e.getDest());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
