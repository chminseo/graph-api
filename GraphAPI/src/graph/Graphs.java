package graph;

public class Graphs {

	private Graphs(){}
	
	public static <D, W> Graph<D,W> newDirectedGraph() {
		return Graph.<D,W>newDirectedType();
	}
	
	public static <D,W> Graph<D,W> newUndirectedGraph() {
		return Graph.<D,W>newUndirectedType();
	}
}