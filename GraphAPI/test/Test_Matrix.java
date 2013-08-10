import static org.junit.Assert.*;

import graph.DuplicateVertexException;
import graph.Matrix;
import graph.model.Vertex;
import graph.model.IVertex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class Test_Matrix {

	Matrix<String, Integer> mx ;
	@Before
	public void setUp() throws Exception {
		mx = new Matrix<String, Integer>(0, 0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_add_vertext() {
		
		mx.addVertext(new Vertex<String>("Incheon"));
		mx.addEdge("Seoul", "Busan", 233);
		
		check_list(
				new String[]{"Incheon", "Seoul", "Busan"},
				mx.listVertice()
		);
		
		try {
			mx.addVertext(new Vertex<String>("Seoul"));
			fail("DuplicateVertextException should be thrown, but not");
		} catch (DuplicateVertexException e) {}
		
		mx.addEdge(vertex("Suwon"), vertex("Seoul"), 23);
		
		check_list(
				new String[]{"Incheon", "Seoul", "Busan", "Suwon" }, 
				mx.listVertice()
		);
	}
	
	
	static IVertex<String> vertex(String name) {
		return new Vertex<String> ( name) ;
	}
	public void check_list(String [] data, IVertex<?>[] vs) {
		assertEquals (data.length , vs.length);
		for (int i = 0; i < vs.length; i++) {
			assertEquals(new Vertex<String>(data[i]), vs[i]);
		}
	}

}
