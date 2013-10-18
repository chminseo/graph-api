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


public class Test_Directed_Graph extends TestGraph{

	@Before
	public void setUp() throws Exception {
		graph = Graphs.newDirectedGraph();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_add_vertext() {
		
		graph.addVertext("Incheon");
		graph.setEdge("Seoul", "Busan", 233);
		
		check_list(
				new String[]{"Incheon", "Seoul", "Busan"},
				graph.listVertice()
		);
		
		try {
			graph.addVertext("Seoul");
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
	
	@Test
	public void weight_of_the_same_vertex_should_be_zero () {
		initVertex(new String[]{"A", "B"});
		
		assertEquals (0, graph.weight("A", "A"), 0.1 );
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
