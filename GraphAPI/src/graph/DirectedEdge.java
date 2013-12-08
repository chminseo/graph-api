package graph;

import graph.model.DefaultEdge;
import graph.model.IDirectedEdge;
import graph.model.IVertex;

public class DirectedEdge<V extends IVertex<?>> extends DefaultEdge<V> implements IDirectedEdge<V>{

	public DirectedEdge(V s, V e) {
		super(s, e);
	}

	public DirectedEdge(V vs, V ve, double weight) {
		super(vs, ve, weight);
	}

}
