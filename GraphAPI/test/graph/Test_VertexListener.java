package graph;

import static org.junit.Assert.*;
import graph.model.DirectedEdge;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Test_VertexListener extends TestGraph<DirectedEdge<String>> {

	String [] vs = new String[]{"listening A", "listening B", "not listening X"};
	
	@Before
	public void setUp() throws Exception {
		installDirectedGraph();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void assert_listener_registration () {
		
		Listener_to_be_called vl = new Listener_to_be_called();
		
		graph.addVertex("no listener to be called");
		
		graph.addVertexListener(vl);
		graph.addVertex("listening A");
		graph.addVertex("listening B");
		
		graph.removeVertexListener(vl);
		graph.addVertex("not listening X");
		
		assertArrayEquals(
				new String[]{"listening A", "listening B"}, 
				vl.received);
		
	}
	
	static class Listener_to_be_called extends VertexAdapter<String, DirectedEdge<String>> {
		String [] received = new String[2];
		int idx = 0;
		@Override
		public void vertexAdded(String vertex, Graph<String, DirectedEdge<String>> graph) {
			received[idx++] = vertex;
		}
		
	};
	
	@Test
	public void listener_called_when_vertex_removed() {
		When_vertex_removed listener = new When_vertex_removed();

		graph.addVertex("A");
		graph.addVertex("B");
		
		graph.addVertexListener(listener);		
		graph.removeVertex("B");
		graph.removeVertexListener(listener);
		
		graph.removeVertex("A"); // not listening
		
		assertEquals ("B", listener.v);
	}
	
	static class When_vertex_removed extends VertexAdapter<String, DirectedEdge<String>>{
		String v;
		@Override
		public void vertexRemoved(String vertex, Graph<String, DirectedEdge<String>> graph) {
			v = vertex;
		}
	}

}
