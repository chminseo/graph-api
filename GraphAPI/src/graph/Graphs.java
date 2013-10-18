package graph;

public class Graphs {

	private Graphs(){}
	
	public static <D> Graph<D> newDirectedGraph() {
		return Graph.<D>newDirectedType();
	}
	
	public static <D> Graph<D> newUndirectedGraph() {
		return Graph.<D>newUndirectedType();
	}
}
