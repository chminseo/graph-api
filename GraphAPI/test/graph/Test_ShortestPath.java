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
		
		graph.setEdge("BOST", "CHI", 1500);
		graph.setEdge("BOST",  "NY",  250);
		graph.setEdge("NY",   "CHI", 1000);
		graph.setEdge("CHI", "DEN", 1200);
		graph.setEdge("NY", "NO",  1400);
		graph.setEdge("NY", "MIA", 900);
		graph.setEdge("MIA", "NO", 1000);
		graph.setEdge("NO", "LA", 1700);
		graph.setEdge("DEN", "LA", 1000);
		graph.setEdge("DEN", "SF", 800);
		graph.setEdge("SF", "LA", 300);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_shortest_path() {
		assertEquals (8, graph.getVertexSize());
		new ShortestPath<String>(graph)
			.process("BOST", "LA");
	}

}
