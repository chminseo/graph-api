package graph;

import graph.model.IUndirectedEdge;

public interface VertextListener<D, E extends IUndirectedEdge<D>> {
	
	public void  vertexAdded(D vertex, Graph<D, E> graph);
	public void vertexRemoved(D vertex, Graph<D, E> graph);
	public void vertexUpdated(D vertex, Graph<D, E> graph);

}
