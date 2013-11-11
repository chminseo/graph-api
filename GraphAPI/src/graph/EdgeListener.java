package graph;

import graph.model.IEdge;
import graph.model.IVertex;

public interface EdgeListener<D, V extends IVertex<D>> {
	void edgeCreated(IEdge<V> edge);
	void edgeRemoved(IEdge<V> edge);
	void edgeChanged(IEdge<V> edge, double oldWeight);
}