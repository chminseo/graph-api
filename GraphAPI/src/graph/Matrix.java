package graph;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import graph.model.DefaultEdge;
import graph.model.Vertex;
import graph.model.IVertex;

public class Matrix <D extends Comparable<D>, W > {

	private ArrayList<IVertex<D>> vset = new ArrayList<IVertex<D>>();
	
	private HashSet<DefaultEdge<? extends IVertex<D>,W >> eset = 
			new HashSet<DefaultEdge<? extends IVertex<D>,W>>();
	
	private int offsetX, offsetY;
	

	
	public Matrix (int ox, int oy) {
		offsetX = ox;
		offsetY = oy;
	}
	
	public <V extends IVertex<D>> boolean hasVertex(V v) {
		return vset.contains(v);
	}
	
	public <V extends IVertex<D>> void addVertext(V v){
		// TODO not thread-safe
		if ( vset.contains(v) ) {
			throw new DuplicateVertexException(v);
		}
		vset.add(v);
	}
	
	private <V extends IVertex<D>> void addVertex(V v, boolean skipIfExist ) {
		if ( hasVertex(v) ) {
			if ( ! skipIfExist ) {
				throw new DuplicateVertexException(v);
			}
		} else {
			vset.add(v);
		}
	}
	
//	private boolean hasVertex(D data) {
//		return vset.contains(new Vertext<D>(data));
//	}
	
	public IVertex<D> addVertext(D data) {
		Vertex<D> v = new Vertex<D>(data);
		addVertext(v);
		return v;
		
	}
	public <V extends IVertex<D>> V[] listVertice(){
		@SuppressWarnings("unchecked")
		V[] vertice = (V[]) Array.newInstance(IVertex.class, vset.size());
		vset.toArray(vertice);
		return vertice;
	}
	
	public <V extends IVertex<D>> void addEdge(V s, V e, W weight) {
		// TODO not thread-safe
		if ( !vset.contains(s) ) {
			vset.add(s);
		}
		if ( !vset.contains(e)) {
			vset.add(e);
		}
		
		eset.add(new DefaultEdge<V, W>(s, e, weight));
	}
	
	public void addEdge(D s, D e, W w) {
		Vertex<D> vs = new Vertex<D>(s);
		Vertex<D> ve = new Vertex<D>(e);
		
		addVertex(vs, true);
		addVertex(ve, true);
		
		DefaultEdge<? extends IVertex<D>, W> edge = 
				new DefaultEdge<IVertex<D>, W>(vs, ve, w); 
		eset.add(edge);
	}
	
}
