package main;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import main.Node;
import main.Server;
import main.remote.RemoteEdge;
import main.remote.RemoteVertex;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServerTest {

	final Integer MAX = 10, MIN = 0;
	
	Server server;
	Node node;
	
	@Mock RemoteVertex v1, v2;
	
	@BeforeClass
	public void setUpBeforeClass() throws Exception {
		server = new Server();
		node = new Node(server);
		server.registerGraph(node);
	}
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public final void testNewVertex() throws RemoteException {
		Integer id = 1;
		RemoteVertex v = server.newVertex(id);
		assertEquals(id, v.getId());
	}

	@Test
	public final void testNewEdge() throws RemoteException {
		Integer level = 0;
		RemoteEdge e = server.newEdge(v1, v2, level);
		assertEquals(v1, e.getSource());
		assertEquals(v2, e.getDest());
		assertEquals(level, e.getLevel());
	}

	@Test
	public final void testGetMaxEdgeLength() throws RemoteException {
		Integer level = MAX;
		RemoteEdge e = server.newEdge(v1, v2, MAX);
		assertEquals(level, e.getLevel());
		assertEquals(e.getLevel(), server.getMaxEdgeLength());
	}

	@Test
	public final void testGetMinEdgeLength() throws RemoteException {
		Integer level = MIN;
		RemoteEdge e = server.newEdge(v1, v2, MIN);
		assertEquals(level, e.getLevel());
		assertEquals(e.getLevel(), server.getMinEdgeLength());
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
	public final void testSetLevel() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetVertexes() {
		fail("Not yet implemented"); // TODO
	}

}
