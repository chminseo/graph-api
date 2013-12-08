package graph;

import graph.model.IEdge;

public class Graphs {

	private Graphs(){}
	
	public static <D,E extends DirectedEdge<D>> Graph<D, E> newDirectedGraph() {
		return Graph.<D, E>newDirectedType();
	}
	
	public static <D,E extends IEdge<D>> Graph<D, E> newUndirectedGraph() {
		return Graph.<D, E>newUndirectedType();
	}
}
