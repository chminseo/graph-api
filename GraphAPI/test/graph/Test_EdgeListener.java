package graph;

import static org.junit.Assert.*;
import graph.model.IEdge;
import graph.model.IVertex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Test_EdgeListener extends TestGraph {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		installDirectedGraph();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void listener_registration() {
		EdgeListenerImpl listener = new EdgeListenerImpl();
		
		vertice(new String[]{"A", "B", "C"});
		
		graph.addEdgeListener(listener);
		
		graph.setEdge("A", "B", 23);
		graph.setEdge("A", "C", 33);
		graph.setEdge("B", "C", 10);
		
		
		graph.setEdge("A", "C", 4);
		graph.removeEdge("B", "C");
		graph.removeEdge("A", "C");
		
		assertEquals(3, listener.called_creation);
		assertEquals(1, listener.called_changed);
		assertEquals(2, listener.called_removal);
		
	}

	static class EdgeListenerImpl extends EdgeAdaptor<String, IVertex<String>> {
		int called_creation = 0;
		int called_changed = 0;
		int called_removal = 0;
		@Override
		public void edgeCreated(IEdge<IVertex<String>> edge) {
			called_creation ++ ;
		}
		
		@Override
		public void edgeChanged(IEdge<IVertex<String>> edge, double oldWeight) {
			called_changed ++ ;
		}
		
		@Override
		public void edgeRemoved(IEdge<IVertex<String>> edge) {
			called_removal ++ ;
		}
	}
}
