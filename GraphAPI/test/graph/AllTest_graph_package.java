package graph;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	Test_Directed_Graph.class, 
	Test_ShortestPath.class,
	Test_Undirected_Graph.class,
	Test_VertexListener.class,
	Test_EdgeListener.class
})
public class AllTest_graph_package {

}
