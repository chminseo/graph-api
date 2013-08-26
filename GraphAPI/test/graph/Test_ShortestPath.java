package graph;

import static org.junit.Assert.*;
import graph.op.ShortestPath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Test_ShortestPath extends TestGraph {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		graph = Graphs.newDirectedGraph();
		
		initVertex(new String[]{"LA", "SF", "DEN", "CHI", "BOST", "NY", "MIA", "NO"});
		
		graph.addEdge("BOST", "CHI", 1500);
		graph.addEdge("BOST",  "NY",  250);
		graph.addEdge("NY",   "CHI", 1000);
		graph.addEdge("CHI", "DEN", 1200);
		graph.addEdge("NY", "NO",  1400);
		graph.addEdge("NY", "MIA", 900);
		graph.addEdge("MIA", "NO", 1000);
		graph.addEdge("NO", "LA", 1700);
		graph.addEdge("DEN", "LA", 1000);
		graph.addEdge("DEN", "SF", 800);
		graph.addEdge("SF", "LA", 300);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_shortest_path() {
		assertEquals (8, graph.getVertexSize());
		new ShortestPath<String, Integer>(graph)
			.process("BOST", "LA");
	}

}
