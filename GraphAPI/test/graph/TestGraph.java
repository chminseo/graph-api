package graph;

import static org.junit.Assert.*;
import graph.model.IEdge;
import graph.model.IVertex;
import graph.model.Vertex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGraph {

	Graph<String> graph ;
	
	@Before
	public void setUp() throws Exception {
		;
	}
	
	protected void installDirectedGraph(){
		graph = Graphs.<String>newDirectedGraph();
	}
	
	protected void installUndirectedGraph() {
		graph = Graphs.<String>newUndirectedGraph();
	}
	
	protected void installVertexes() {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	
	/**
	 * installing vertice
	 * @param ao
	 */
	void vertexes(String... ao) {
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
	TestGraph edge(String from, String to, double weight) {
		graph.setEdge(from, to, weight);
		return this;
	}
	
	public void assertVertexValues(String [] data, IVertex<?>[] vs) {
		assertEquals (data.length , vs.length);
		for (int i = 0; i < vs.length; i++) {
			assertEquals(new Vertex<String>(data[i]), vs[i]);
		}
		
		for( String s : data) {
			assertTrue ( graph.hasVertex(s));
		}
	}

	protected static abstract class VertexAdapter<D> implements VertextListener<D> {
		@Override
		public void vertexAdded(IVertex<D> vertex, Graph<D> graph) {}

		@Override
		public void vertexRemoved(IVertex<D> vertex, Graph<D> graph) {}

		@Override
		public void vertexUpdated(IVertex<D> vertex, Graph<D> graph) {}
	}
	
	protected static class EdgeAdaptor<D, V extends IVertex<D>> implements EdgeListener<D, V>{

		@Override
		public void edgeCreated(IEdge<V> edge) {}

		@Override
		public void edgeRemoved(IEdge<V> edge) {}

		@Override
		public void edgeChanged(IEdge<V> edge, double oldWeight) {}
		
	}

}
