package graph;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Test_Undirected_Graph extends TestGraph {

	@Before
	public void setUp() throws Exception {
		graph = Graphs.newUndirectedGraph();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void symmetry_edge_in_undirectedGraph() {
		
		initVertex(new String []{"A", "B", "C", "D", "E" });
		
		graph.setEdge("A", "D", 12);
		assertEquals (12.0, graph.getEdge("D", "A").getWeight(), 0.1);
		
		graph.setEdge("D", "A", 10);
		assertEquals (10.0, graph.getEdge("A", "D").getWeight(), 0.1);
	}
	
}
