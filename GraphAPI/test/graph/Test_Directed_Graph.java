package graph;
import static org.junit.Assert.*;

import graph.DuplicateVertexException;
import graph.model.EdgeException;
import graph.model.EdgeType;
import graph.model.IDirectedEdge;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class Test_Directed_Graph extends TestGraph<IDirectedEdge<String>>{

	@Before
	public void setUp() throws Exception {
		installDirectedGraph();
		vertexes("A", "B", "C", "D", "E");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_add_vertext() {

		graph.addVertex("M");
		assertVertexValues(
				new String[]{"A", "B", "C", "D", "E", "M"}, 
				graph.listVertex()
		);
	}
	
	@Test
	public void when_duplicated_vertex_inserted() {
		try {
			graph.addVertex("A");
			fail("DuplicateVertextException should be thrown, but not");
		} catch (DuplicateVertexException e) {}
	}
	
	
	
	@Test
	public void getting_weight_of_edge() {
		graph.setEdge("A", "B", 24);
		graph.setEdge("B", "E", 11);
		
		assertEquals (24.0, graph.getEdge("A", "B").getWeight(), 0.1);
		assertEquals (11.0, graph.getEdge("B", "E").getWeight(), 0.1);
	}
	
	@Test
	public void negative_weight_not_allowed() {
		try {
			graph.setEdge("A", "B", -1.0);
			fail ( "expected vertex exception, but not thrown");
		} catch ( EdgeException ve) {}
	}
	
	@Test
	public void edge_isasymmetry_in_directed_graph () {
		graph.setEdge("A", "E", 24);
		
		assertEquals ( 24 , (int) graph.weight("A", "E")) ;
		
		assertNotNull ( graph.getEdge("A", "E"));
		// directed graph이기 때문에 (A,E)는 존재하나 (E,A)는 존재하지 않아야함.
		assertNoEdge("E", "A");
	}
	
	@Test
	public void two_kind_of_vertexes_in_edge() {
		graph.setEdge("A", "E", 24);		
		IDirectedEdge<String> edgeAE = graph.getEdge("A", "E");

		assertEquals ( "A", edgeAE.getStartVertex());
		assertEquals ( "E", edgeAE.getEndVertex());
	}
	
	/**    \ to
	 *      \   A   B   C
	 * from  \ ----------------
	 *     A |      10  20
	 *     B |          30
	 *     C |
	 */
	@Test
	public void querying_edges_from_a_vertex() {
		edge("A", "B", 10).edge("A", "C", 20).edge("B", "C", 30);
		
		assertEquals ( 2, graph.getEdges("A", EdgeType.ANY_EDGE).size());
		assertEquals ( 0, graph.getEdges("A", EdgeType.INCOMING_EDGE).size());
		
		assertEquals ( 2, graph.getEdges("C", EdgeType.INCOMING_EDGE).size());
		assertEquals ( 1, graph.getEdges("B", EdgeType.OUTGOING_EDGE).size());
		assertEquals ( 1, graph.getEdges("B", EdgeType.INCOMING_EDGE).size());
		
	}
	
	@Test
	public void weight_of_the_same_vertex_should_be_zero () {
		assertEquals (0, graph.weight("A", "A"), 0.1 );
	}
	
	@Test
	public void removal_of_vertex() {
		
		edge("A", "B", 13).edge("A", "C", 7).edge("A", "D", 5)
			.edge("B", "C", 11);
		
		assertEdge("A", "B");
		assertEdge("A", "C");
		assertEdge("A", "D");
		
		graph.removeVertex("A");
				
		assertNoEdge("A", "B");
		assertNoEdge("A", "C");
		assertNoEdge("A", "D");
		
		assertFalse( graph.hasVertex("A"));
		
	}
	
	/**    \ to
	 *      \   A   B   C   D
	 * from  \ ----------------
	 *     A |      10  20
	 *     B |          30   6
	 *     C |              12
	 *     D | 33
	 *     
	 *     (A->B : 10) (A->C : 20)
	 *     (B->C : 30) (B->D :  6)
	 *     (C->D : 12) (D->A : 33)
	 */
	@Test
	public void removal_of_edge_between_vertice() {
		edge("A", "B", 10).edge("A", "C", 20);
		edge("B", "C", 30).edge("B", "D",  6);
		edge("C", "D", 12).edge("D", "A", 33);
		
		assertEquals (2, graph.getEdges("A", EdgeType.OUTGOING_EDGE).size());
		graph.getEdge("A", "C"); // no exception here
		
		graph.removeEdge("A", "C");
		
		assertEquals (1, graph.getEdges("A", EdgeType.OUTGOING_EDGE).size());
		assertNoEdge("A", "C");
	}

}
