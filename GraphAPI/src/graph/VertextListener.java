package graph;

import graph.model.IVertex;

public interface VertextListener<D> {
	
	public void  vertexAdded(IVertex<D> vertex, Graph<D> graph);
	public void vertexRemoved(IVertex<D> vertex, Graph<D> graph);
	public void vertexUpdated(IVertex<D> vertex, Graph<D> graph);

}
