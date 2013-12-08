package graph;

import graph.model.IUndirectedEdge;

public interface EdgeListener<D> {
	void edgeCreated(IUndirectedEdge<D> edge);
	void edgeRemoved(IUndirectedEdge<D> edge);
	void edgeChanged(IUndirectedEdge<D> edge, double oldWeight);
}