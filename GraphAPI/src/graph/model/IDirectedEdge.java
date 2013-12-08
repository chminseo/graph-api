package graph.model;

public interface IDirectedEdge<D> extends IEdge<D> {

	D getStartVertex();

	D getEndVertex();

}
