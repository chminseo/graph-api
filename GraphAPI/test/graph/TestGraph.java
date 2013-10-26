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
		graph = Graphs.newDirectedGraph();
	}
	
	protected void installUndirectedGraph() {
		graph = Graphs.newUndirectedGraph();
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
