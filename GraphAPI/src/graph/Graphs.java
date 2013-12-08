package graph;

import graph.model.IEdge;
import graph.model.IVertex;

public class Graphs {

	private Graphs(){}
	
	public static <D,E extends DirectedEdge<IVertex<D>>> Graph<D, E> newDirectedGraph() {
		return Graph.<D, E>newDirectedType();
	}
	
	public static <D,E extends IEdge<IVertex<D>>> Graph<D, E> newUndirectedGraph() {
		return Graph.<D, E>newUndirectedType();
	}
}
