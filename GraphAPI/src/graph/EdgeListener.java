package graph;

import graph.model.IEdge;

public interface EdgeListener<D> {
	void edgeCreated(IEdge<D> edge);
	void edgeRemoved(IEdge<D> edge);
	void edgeChanged(IEdge<D> edge, double oldWeight);
}