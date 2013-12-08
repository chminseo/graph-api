package graph;

import static org.junit.Assert.*;
import graph.model.DefaultEdge;
import graph.model.IEdge;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGraph<E extends IEdge<String>> {

	Graph<String, E> graph ;
	
	@Before
	public void setUp() throws Exception {
		;
	}
	
	@SuppressWarnings("unchecked")
	protected void installDirectedGraph(){
		graph = (Graph<String, E>) Graphs.<String, DirectedEdge<String>>newDirectedGraph();
	}
	
	@SuppressWarnings("unchecked")
	protected void installUndirectedGraph() {
		graph = (Graph<String, E>) Graphs.<String, DefaultEdge<String>>newUndirectedGraph();
	}
	
	@After
	public void tearDown() throws Exception {
	}

	
	/**
	 * installing vertice
	 * @param ao
	 */
	protected void vertexes(String... ao) {
		for(String s : ao) {
			graph.addVertex(s);
		}
	}
	
	public void assertEdge(String s, String e) {		
		try {			
			graph.getEdge(s, e);
		} catch (Exception ec) {
			fail("exception should not be thrown");
		}
	}
	public void assertNoEdge(String s, String e){
		try {
			graph.getEdge(s, e);
			fail("exception expected. but not thrown.");
		} catch (Exception ec) {}
	}

	/**
	 * install a edge between two vertices
	 * @param from
	 * @param to
	 * @param weight
	 * @return
	 */
	TestGraph<E> edge(String from, String to, double weight) {
		graph.setEdge(from, to, weight);
		return this;
	}
	
	public void assertVertexValues(String [] expected,  String[] actual) {
		assertEquals (expected.length , actual.length);
		for (int i = 0; i < actual.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
		
		for( String s : expected) {
			assertTrue ( graph.hasVertex(s));
		}
	}

	protected static abstract class VertexAdapter<D, E extends IEdge<D>> implements VertextListener<D, E> {
		@Override
		public void vertexAdded(D vertex, Graph<D, E> graph) {}

		@Override
		public void vertexRemoved(D vertex, Graph<D, E> graph) {}

		@Override
		public void vertexUpdated(D vertex, Graph<D, E> graph) {}
	}
	
	protected static class EdgeAdaptor<D> implements EdgeListener<D>{

		@Override
		public void edgeCreated(IEdge<D> edge) {}

		@Override
		public void edgeRemoved(IEdge<D> edge) {}

		@Override
		public void edgeChanged(IEdge<D> edge, double oldWeight) {}
		
	}

}
