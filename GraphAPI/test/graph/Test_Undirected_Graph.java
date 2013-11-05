package graph;

import static org.junit.Assert.*;
import graph.model.IEdge.EdgeType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Test_Undirected_Graph extends TestGraph {

	@Before
	public void setUp() throws Exception {
		installUndirectedGraph();
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
	
	/**
	 *     \ to
	 *      \   A   B   C
	 * from  \ ----------------
	 *     A | --         
	 *     B | 10   --         
	 *     C | 20   30  --
	 */
	@Test
	public void test_get_edges_from_the_vertex() {
		initVertex(new String[]{"A", "B", "C"});
		createEdges("A", "B", 10);
		createEdges("A", "C", 20);
		createEdges("C", "B", 30);
		
		assertEquals(2, graph.getEdges("A", EdgeType.OUTGOING_EDGE).size());
		assertEquals(2, graph.getEdges("A", EdgeType.INCOMING_EDGE).size());

		assertEquals(2, graph.getEdges("B", EdgeType.OUTGOING_EDGE).size());
		assertEquals(2, graph.getEdges("B", EdgeType.INCOMING_EDGE).size());
		
		assertEquals(2, graph.getEdges("C", EdgeType.OUTGOING_EDGE).size());
		assertEquals(2, graph.getEdges("C", EdgeType.INCOMING_EDGE).size());
	}
	
}
