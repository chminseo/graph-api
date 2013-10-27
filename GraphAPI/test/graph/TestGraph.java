package graph;

import static org.junit.Assert.*;

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

	
	
	void initVertex(String [] ao) {
		for(String s : ao) {
			graph.addVertext(s);
		}
	}

}
