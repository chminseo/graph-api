package graph;

import static org.junit.Assert.*;
import graph.model.IVertex;

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
	void vertice(String [] ao) {
		for(String s : ao) {
			graph.addVertex(s);
		}
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
	
	protected static abstract class VertexAdapter<D> implements VertextListener<D> {
		@Override
		public void vertexAdded(IVertex<D> vertex, Graph<D> graph) {}

		@Override
		public void vertexRemoved(IVertex<D> vertex, Graph<D> graph) {}

		@Override
		public void vertexUpdated(IVertex<D> vertex, Graph<D> graph) {}
	}

}
