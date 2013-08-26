package graph;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Test_Undirected_Graph {

	Graph<String, Integer> graph ;
	@Before
	public void setUp() throws Exception {
		graph = Graphs.<String, Integer>newUndirectedGraph();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String [] vertex = new String []{"A", "B", "C", "D", "E" };
		initVertex(vertex);
		
		graph.addEdge("A", "D", 12);
		
		Integer weight = graph.getEdge("D", "A").getWeight();
		
		assertEquals (12, weight.intValue());
		
		
		
	}

	
	void initVertex(String [] ao) {
		for(String s : ao) {
			graph.addVertext(s);
		}
	}
	
}
