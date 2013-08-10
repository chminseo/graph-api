package graph.model;

import java.lang.reflect.Array;

public class DefaultEdge<V extends IVertex<?>, W> implements IEdge<V, W> {
	
	public W weight ;
	

	private V [] vs ; // = new Object[2];
	
	@SuppressWarnings("unchecked")
	public DefaultEdge(V start, V end, W weight) {
		vs = (V[]) Array.newInstance(start.getClass(), 2);
		vs[0] = start;
		vs[1] = end;
		this.weight = weight;
	}
	
	
	public DefaultEdge(V s, V e) {
		this(s, e, null);
	}

	@Override
	public W getWeight() {
		return weight;
	}
}
