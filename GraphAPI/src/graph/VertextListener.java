package graph;

import graph.model.IEdge;
import graph.model.IVertex;

public interface VertextListener<D, E extends IEdge<IVertex<D>>> {
	
	public void  vertexAdded(IVertex<D> vertex, Graph<D, E> graph);
	public void vertexRemoved(IVertex<D> vertex, Graph<D, E> graph);
	public void vertexUpdated(IVertex<D> vertex, Graph<D, E> graph);

}
