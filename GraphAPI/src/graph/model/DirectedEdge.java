package graph.model;


public class DirectedEdge<D> extends UndirectedEdge<D> implements IDirectedEdge<D>{

	public DirectedEdge(D s, D e, double weight) {
		super(s, e, weight);
	}

	@Override
	public D getStartVertex() {
		return vs[0];
	}
	
	@Override
	public D getEndVertex() {
		return vs[1];
	}

}
