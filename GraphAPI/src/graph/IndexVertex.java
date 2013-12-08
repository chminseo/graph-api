package graph;

import graph.model.IVertex;

import java.util.List;

class IndexVertex<D> implements IVertex<D> {

	private List<? extends IVertex<D>> list ;
	IVertex<D> v ;
	public IndexVertex(IVertex<D> v, List<? extends IVertex<D>> list) {
		this.v = v;
		this.list = list;
	}
	
	int index() {
		return list.indexOf(this);
	}

	@Override
	public D getData() {
		return v.getData();
	}

	@Override
	public int hashCode() {
		return v.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.v.equals(obj);
	}

	@Override
	public String toString() {
		return "[iv : " + v.toString() + " at " + list.indexOf(this) + "]";
	}
}