package graph.model;

import java.lang.reflect.Array;

public class UndirectedEdge<D> implements IUndirectedEdge<D> {
	
	public double weight ;
	

	protected D [] vs ; // = new Object[2];
	
	@SuppressWarnings("unchecked")
	public UndirectedEdge(D start, D end, double weight) {
		vs = (D[]) Array.newInstance(start.getClass(), 2);
		vs[0] = start;
		vs[1] = end;
		this.weight = weight;
	}
	
	public D[] vertex() {
		return vs.clone();
	}
	
	@Override
	public double getWeight() {
		return weight;
	}

	@SuppressWarnings("unchecked")
	@Override
	public D [] getVertexes() {
		D [] cloned = (D[]) Array.newInstance( vs[0].getClass(), 2);
		cloned[0] = vs[0];
		cloned[1] = vs[1];
		return (D[]) cloned;
	}
}
