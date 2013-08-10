package graph.model;

public class Vertex<D> implements IVertex<D> {
	
	private D data ;
	public Vertex (D data) {
		this.data = data;
	}
	
	@Override
	public D getData(){
		return data;
	}

	@Override
	public boolean equals(Object other) {
		// TEST created and not tested method stub
		
		if ( other == null) return false;
		if ( other == this) {
			return true;
		}
		
		if ( other instanceof IVertex){
			IVertex<?> vo = (IVertex<?>) other;
			return this.data.equals (vo.getData());
		} else return false;
	}

	
	public String toString(){
		return data.toString();
	}
	
}
