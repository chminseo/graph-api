package graph;

import graph.model.DefaultEdge;
import graph.model.IDirectedEdge;

public class DirectedEdge<D> extends DefaultEdge<D> implements IDirectedEdge<D>{

	public DirectedEdge(D s, D e) {
		super(s, e);
	}

	public DirectedEdge(D s, D e, double weight) {
		super(s, e, weight);
	}

}
