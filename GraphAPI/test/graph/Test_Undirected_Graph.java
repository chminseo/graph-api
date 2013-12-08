package graph;

import static org.junit.Assert.*;
import graph.model.UndirectedEdge;
import graph.model.EdgeException;
import graph.model.IUndirectedEdge.EdgeType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Test_Undirected_Graph extends TestGraph<UndirectedEdge<String>> {

	@Before
	public void setUp() throws Exception {
		installUndirectedGraph();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void symmetry_edge_in_undirectedGraph() {
		
		vertexes("A", "B", "C", "D", "E");
		
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
		vertexes("A", "B", "C");
		edge("A", "B", 10);
		edge("A", "C", 20);
		edge("C", "B", 30);
		
		assertEquals(2, graph.getEdges("A", EdgeType.OUTGOING_EDGE).size());
		assertEquals(2, graph.getEdges("A", EdgeType.INCOMING_EDGE).size());

		assertEquals(2, graph.getEdges("B", EdgeType.OUTGOING_EDGE).size());
		assertEquals(2, graph.getEdges("B", EdgeType.INCOMING_EDGE).size());
		
		assertEquals(2, graph.getEdges("C", EdgeType.OUTGOING_EDGE).size());
		assertEquals(2, graph.getEdges("C", EdgeType.INCOMING_EDGE).size());
	}
	
	@Test
	public void removal_of_edge_between_vertice() {
		vertexes("A", "B", "C");
		edge("A", "B", 10).edge("A", "C", 20).edge("C", "B", 30);
		
		graph.getEdge("A", "B");
		graph.removeEdge("A", "B");
		try {
			graph.getEdge("A", "B");
			fail ( "exception should be thrown : " + EdgeException.class.getName());
		} catch ( EdgeException e) {}
		
	}
	
}
