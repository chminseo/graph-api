package graph.model;

import java.lang.reflect.Array;

public class DefaultEdge<V extends IVertex<?>> implements IEdge<V> {
	
	public double weight ;
	

	private V [] vs ; // = new Object[2];
	
	@SuppressWarnings("unchecked")
	public DefaultEdge(V start, V end, double weight) {
		vs = (V[]) Array.newInstance(start.getClass(), 2);
		vs[0] = start;
		vs[1] = end;
		this.weight = weight;
	}
	
	public V[] vertex() {
		return vs.clone();
	}
	
	public DefaultEdge(V s, V e) {
		this(s, e, 0.0);
	}
	
	

	@Override
	public double getWeight() {
		return weight;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V [] getVertexes() {
		V [] cloned = (V[]) Array.newInstance( vs[0].getClass(), 2);
		cloned[0] = vs[0];
		cloned[1] = vs[1];
		return (V[]) cloned;
	}
}
