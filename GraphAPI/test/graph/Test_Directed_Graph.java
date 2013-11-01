package graph;
import static org.junit.Assert.*;

import graph.DuplicateVertexException;
import graph.model.EdgeException;
import graph.model.Vertex;
import graph.model.IVertex;
import graph.model.IEdge.EdgeType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class Test_Directed_Graph extends TestGraph{

	@Before
	public void setUp() throws Exception {
		installDirectedGraph();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_add_vertext() {
		
		graph.addVertex("Incheon");
		graph.setEdge("Seoul", "Busan", 233);
		
		check_list(
				new String[]{"Incheon", "Seoul", "Busan"},
				graph.listVertice()
		);
		
		try {
			graph.addVertex("Seoul");
			fail("DuplicateVertextException should be thrown, but not");
		} catch (DuplicateVertexException e) {}
		
		graph.setEdge("Suwon", "Seoul", 23);
		
		check_list(
				new String[]{"Incheon", "Seoul", "Busan", "Suwon" }, 
				graph.listVertice()
		);
	}
	
	
	
	@Test
	public void test_weight_of_edge() {
		initVertex(new String[]{"A", "B", "E", "I", "J", "K" });
		
		graph.setEdge("A", "E", 24);
		graph.setEdge("B", "K", 11);
		
		assertEquals (24.0, graph.getEdge("A", "E").getWeight(), 0.1);
		assertEquals (11.0, graph.getEdge("B", "K").getWeight(), 0.1);
	}
	
	@Test
	public void negative_weight_not_allowed() {
		try {
			graph.setEdge("A", "B", -1.0);
			fail ( "expected vertex exception, but not thrown");
		} catch ( EdgeException ve) {}
	}
	
	@Test
	public void asymmetry_edge_in_directed_graph () {
		initVertex(new String[]{"A", "B", "C", "D", "E"});
		
		try {
			graph.getEdge("A", "E");
			fail("EdgeException expected, but not");
		} catch (EdgeException e) {}
		
		graph.setEdge("A", "E", 24);
		
		assertEquals ( 24 , (int) graph.weight("A", "E")) ;
		assertNotNull ( graph.getEdge("A", "E"));
		
		try {
			// directed graph이기 때문에 (A,E)는 존재하나 (E,A)는 존재하지 않아야함.
			graph.getEdge("E", "A");
			fail("EdgeException expected, but not");
		} catch (EdgeException e) {}
		
	}
	
	/**    \ to
	 *      \   A   B   C
	 * from  \ ----------------
	 *     A |      10  20
	 *     B |          30
	 *     C |
	 */
	@Test
	public void test_get_edges_from_the_vertex() {
		initVertex(new String[]{"A", "B", "C"});
		createEdges("A", "B", 10);
		createEdges("A", "C", 20);
		createEdges("B", "C", 30);
		
		assertEquals ( 2, graph.getEdges("A", EdgeType.ANY_EDGE).size());
		assertEquals ( 0, graph.getEdges("A", EdgeType.INCOMING_EDGE).size());
		
		assertEquals ( 2, graph.getEdges("C", EdgeType.INCOMING_EDGE).size());
		assertEquals ( 1, graph.getEdges("B", EdgeType.OUTGOING_EDGE).size());
		assertEquals ( 1, graph.getEdges("B", EdgeType.INCOMING_EDGE).size());
		
	}
	
	@Test
	public void weight_of_the_same_vertex_should_be_zero () {
		initVertex(new String[]{"A", "B"});
		
		assertEquals (0, graph.weight("A", "A"), 0.1 );
	}
	
	@Test
	public void removal_of_vertex() {
		// TEST vertex 제거 시 연결된 edge 들을 제거해야 함. 나중에 구현.
		initVertex(new String[]{"A", "B", "C", "D"});
		
		
		
	}
	
	public void check_list(String [] data, IVertex<?>[] vs) {
		assertEquals (data.length , vs.length);
		for (int i = 0; i < vs.length; i++) {
			assertEquals(new Vertex<String>(data[i]), vs[i]);
		}
		
		for( String s : data) {
			assertTrue ( graph.hasVertex(s));
		}
	}

}
