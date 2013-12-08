package graph.model;

public interface IDirectedEdge<D> extends IUndirectedEdge<D> {

	D getStartVertex();

	D getEndVertex();

}
