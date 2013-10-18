package graph;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Test_Undirected_Graph {

	Graph<String> graph ;
	@Before
	public void setUp() throws Exception {
		graph = Graphs.newUndirectedGraph();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String [] vertex = new String []{"A", "B", "C", "D", "E" };
		initVertex(vertex);
		
		graph.addEdge("A", "D", 12);
		
		double weight = graph.getEdge("D", "A").getWeight();
		
		assertEquals (12.0, weight, 0.1);
		
	}

	
	void initVertex(String [] ao) {
		for(String s : ao) {
			graph.addVertext(s);
		}
	}
	
}
