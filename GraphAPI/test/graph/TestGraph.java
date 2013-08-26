package graph;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestGraph {

	Graph<String, Integer> graph ;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	
	void initVertex(String [] ao) {
		for(String s : ao) {
			graph.addVertext(s);
		}
	}

}
