package graph;
import static org.junit.Assert.*;

import graph.DuplicateVertexException;
import graph.Graph;
import graph.Graphs;
import graph.model.EdgeException;
import graph.model.Vertex;
import graph.model.IVertex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class Test_Directed_Graph {

	Graph<String> mx ;
	@Before
	public void setUp() throws Exception {
//		mx = new Graph<String, Integer>(0, 0);
		mx = Graphs.newDirectedGraph();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_add_vertext() {
		
		mx.addVertext("Incheon");
		mx.addEdge("Seoul", "Busan", 233);
		
		check_list(
				new String[]{"Incheon", "Seoul", "Busan"},
				mx.listVertice()
		);
		
		try {
			mx.addVertext("Seoul");
			fail("DuplicateVertextException should be thrown, but not");
		} catch (DuplicateVertexException e) {}
		
		mx.addEdge("Suwon", "Seoul", 23);
		
		check_list(
				new String[]{"Incheon", "Seoul", "Busan", "Suwon" }, 
				mx.listVertice()
		);
	}
	
	
	
	@Test
	public void test_weight_of_edge() {
		initVertex(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L" });
		
		mx.addEdge("A", "E", 24);
		mx.addEdge("B", "K", 11);
		
		assertEquals (24.0, mx.getEdge("A", "E").getWeight(), 0.1);
		assertEquals (11.0, mx.getEdge("B", "K").getWeight(), 0.1);
	}
	
	@Test
	public void test_not_exsting_edge () {
		initVertex(new String[]{"A", "B", "C", "D", "E"});
		
		// the weight of non-existing edge is -1.0
		try {
			assertEquals(-1.0, mx.weight("A", "E"), 0.1 );
			mx.getEdge("A", "E");
			fail("EdgeException expected, but not");
		} catch (EdgeException e) {}
		
		mx.addEdge("A", "E", 24);
		
		assertEquals ( 24 , (int) mx.weight("A", "E")) ;
		assertNotNull ( mx.getEdge("A", "E"));
		
	}
	
	@Test
	public void weight_of_the_same_vertex_should_be_zero () {
		initVertex(new String[]{"A", "B"});
		
		assertEquals (0, mx.weight("A", "A"), 0.1 );
	}
	
	static IVertex<String> vertex(String name) {
		return new Vertex<String> ( name) ;
	}
	
	void initVertex(String [] ao) {
		for(String s : ao) {
			mx.addVertext(s);
		}
	}
	
	public void check_list(String [] data, IVertex<?>[] vs) {
		assertEquals (data.length , vs.length);
		for (int i = 0; i < vs.length; i++) {
			assertEquals(new Vertex<String>(data[i]), vs[i]);
		}
		
		for( String s : data) {
			assertTrue ( mx.hasVertex(s));
		}
	}

}
