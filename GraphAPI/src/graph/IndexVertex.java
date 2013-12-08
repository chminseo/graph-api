package graph;

import java.util.ArrayList;
import java.util.List;

class IndexVertex<D> {

	private List<IndexVertex<D>> list ;
	D value ;
	public IndexVertex(D v, ArrayList<IndexVertex<D>> list) {
		this.value = v;
		this.list = list;
	}
	
	int index() {
		return list.indexOf(this);
	}

	public D getData() {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode()*31 + index();
	}

	@Override
	public boolean equals(Object obj) {
		
		if ( obj instanceof IndexVertex) {
			return this.value.equals ( ((IndexVertex<D>)obj).value );
		}
		return false;
	}
	
	public boolean equalsValue(D value) {
		return this.value.equals(value);
	}

	@Override
	public String toString() {
		return "[iv : " + value.toString() + " at " + list.indexOf(this) + "]";
	}
}