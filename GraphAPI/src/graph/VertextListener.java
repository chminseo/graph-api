package graph;

import graph.model.IEdge;

public interface VertextListener<D, E extends IEdge<D>> {
	
	public void  vertexAdded(D vertex, Graph<D, E> graph);
	public void vertexRemoved(D vertex, Graph<D, E> graph);
	public void vertexUpdated(D vertex, Graph<D, E> graph);

}
