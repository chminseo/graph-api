package graph;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import graph.model.DefaultEdge;
import graph.model.IEdge;
import graph.model.EdgeException;
import graph.model.VertexException;
import graph.model.Vertex;
import graph.model.IVertex;

public class Graph <D> {

	final private ArrayList<IndexVertex<D>> vset = new ArrayList<IndexVertex<D>>();
		
	private WeakHashMap<IEdge<? extends IVertex<D>>, IEdge<? extends IVertex<D>>> 
		cachedEdge = new WeakHashMap<IEdge<? extends IVertex<D>>, IEdge<? extends IVertex<D>>>();
	
	final private HashSet<DoubleMatrix> mxSet = new HashSet<DoubleMatrix>();
	
	private int base = 2;
	
	private GraphType<D> gType ;
	private Graph (int ox, int oy) {
		
	}
	
	static <D> Graph<D> newDirectedType () {
		Graph<D> mx = new Graph<D>(0,0);
		mx.gType = new DirectedGraph<D>(mx, mx.vset);
		return  mx;
	}
	
	static <D> Graph<D> newUndirectedType() {
		Graph<D> mx = new Graph<D>(0,0);
		mx.gType = new UndirectedGraph<D>(mx, mx.vset);
		return mx;
	}
	
	
	
	public <V extends IVertex<D> > boolean hasVertex(V v) {
		return vset.contains(v);
	}
	
	public boolean hasVertex(D data) {
		Iterator<IndexVertex<D>> it = vset.iterator();
		
		while ( it.hasNext()) {
			if ( it.next().getData().equals(data) ) {
				return true;
			}
		}
		
		return false;
	}
	
	public int getVertexSize() {
		return vset.size();
	}
	
	private <V extends IVertex<D>> void addVertext(V v){
		// TODO not thread-safe
		if ( vset.contains(v) ) {
			throw new DuplicateVertexException(v);
		}
		addVertex(v, false);
	}
	
	private <V extends IVertex<D>> void addVertex(V v, boolean skipIfExist ) {
		if ( hasVertex(v) ) {
			if ( ! skipIfExist ) {
				throw new DuplicateVertexException(v);
			}
		} else {
			IndexVertex<D> iv = null ;
			if ( ! (v instanceof IndexVertex) ){
				iv = new IndexVertex<D>(v, vset);		
			} else {
				iv = (IndexVertex<D>) v;
			}
			
			vset.add(iv);
		}
	}
	
	public IVertex<D> addVertext(D data) {
		IndexVertex<D> v = new IndexVertex<D>(new Vertex<D>(data), vset);
		addVertext(v);
		return v;
	}
	
	public <V extends IVertex<D>> V[] listVertice(){
		@SuppressWarnings("unchecked")
		V[] vertice = (V[]) Array.newInstance(IVertex.class, vset.size());
		vset.toArray(vertice);
		return vertice;
	}
	
	public D[] listVertex() {
		IVertex<D> [] vs = listVertice();
		@SuppressWarnings("unchecked")
		D[] ds = (D[]) new Object[vs.length];
		for( int i = 0 ; i < vs.length ; i++) {
			ds[i] = vs[i].getData();
		}
		return ds;
		
	}
	
	public <V extends IVertex<D>> void addEdge(V s, V e, double weight) {
		// TODO not thread-safe
		if ( !vset.contains(s) ) {
			throw new RuntimeException(" no such vertext : " + s);
		}
		
		if ( !vset.contains(e)) {
			throw new RuntimeException(" no such vertext : " + e);
		}
		
		int r = vset.indexOf(s);
		int c = vset.indexOf(e);
				
		setWeight(r, c, weight);
	}
	
	public void addEdge(D s, D e, double w) {
		gType.addEdge(s, e, w);
	}
	
	private void setWeight(int r, int c, double weight) {
		int ox = r - r % base;
		int oy = c - c % base;
		
		DoubleMatrix mx = getMatrix(ox, oy);
		mx.setValue(r, c, weight);
		
	}
	
	
	public IEdge<IVertex<D>> getEdge(D s, D e) {
		return gType.getEdge(s, e);
	}
	
	private double getWeight(int r, int c) {
		return getMatrix(r, c).getValue(r, c);
	}
	
	public double weight(D src, D dst) {
		Iterator<IndexVertex<D>> it = vset.iterator();
		
		int is = -1, id = -1;
		
		while ( it.hasNext()) {
			IndexVertex<D> v = it.next();
			
			if ( v.getData().equals(src)) {
				is = v.index();
			}
			
			if ( v.getData().equals(dst)) {
				id = v.index();
			}
		}
		
		if ( is < 0 ) {
			throw new VertexException("cannot find vertex src : " + src) ;
		}
		
		if ( id < 0) {
			throw new VertexException("cannot find vertex dst : " + dst) ;
		}
		
		
		return getWeight(is, id);
	}
	
	
	DoubleMatrix getMatrix(int r, int c) {
		Iterator<DoubleMatrix> it = mxSet.iterator();
		
		DoubleMatrix mx = null ;
		
		while ( it.hasNext()) {
			mx = it.next();
			if ( mx.contains(r, c)){
				return mx;
			}
		}
		
		mx = new DoubleMatrix(r - r%base, c - c%base, base, 1);
		mxSet.add(mx);
		
		return mx;
		
	}
	
	static class IndexVertex<D> implements IVertex<D> {

		private List<? extends IVertex<D>> list ;
		private IVertex<D> v ;
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
	
	abstract static class GraphType<D> {
		public abstract void addEdge(D s, D e, double weight);
		public abstract IEdge<IVertex<D>> getEdge(D s, D e );
	}
	
	private static class UndirectedGraph<D> extends GraphType<D> {
		final private ArrayList<IndexVertex<D>> vset ;
		final Graph<D> mx ;
		
		public UndirectedGraph(Graph<D> mx, ArrayList<IndexVertex<D>> list) {
			vset = list;
			this.mx = mx;
		}

		@Override
		public void addEdge(D s, D e, double weight) {
			// TEST created and not tested method stub
			
			IndexVertex<D> vs = new IndexVertex<D>(new Vertex<D>(s), vset);
			IndexVertex<D> ve = new IndexVertex<D>(new Vertex<D>(e), vset);
			
			mx.addVertex(vs, true);
			mx.addVertex(ve, true);
			
			int is = vs.index();
			int ie = ve.index();
			
			if ( is < ie) {
				IndexVertex<D> tmp = vs;
				vs = ve;
				ve = tmp;
			}
			
			mx.setWeight(vs.index(), ve.index(), weight);
			
		}

		@Override
		public IEdge<IVertex<D>> getEdge(D s, D e) {
			// TEST created and not tested method stub
			IndexVertex<D> vs = null ;
			IndexVertex<D> ve = null ;
			double weight = -1;
			
			Iterator<IndexVertex<D>> it = vset.iterator();
			
			while ( it.hasNext()) {
				IndexVertex<D> iv = it.next();
				if ( iv.getData().equals(s) ) {
					vs = iv; 
				}
				
				if ( iv.getData().equals(e) ) {
					ve = iv;
				}
				
				if ( vs != null && ve != null ) {
					break;
				}
			}
			
			
			if ( vs == null ) {
				throw new VertexException("can not find vertex : " + s);
			}
			
			if ( ve == null ) {
				throw new VertexException("can not find vertex : " + e);
			}
			
			int is = vs.index();
			int ie = ve.index();
			
			if ( is < ie) {
				IndexVertex<D> tmp = vs;
				vs = ve;
				ve = tmp;
			}
			
			weight = mx.getWeight(vs.index(), ve.index());
			
			if ( weight < 0 ) {
				throw new EdgeException("cannot find edge between : " + s + " and " + e);
			}
			
			DefaultEdge<IVertex<D>> edge = new DefaultEdge<IVertex<D>>(vs, ve, weight);
//			cachedEdge.put(edge, edge);
			
			return edge;
		}
		
	}
	
	private static class DirectedGraph<D> extends GraphType<D>{

		final private ArrayList<IndexVertex<D>> vset ;
		final Graph<D> mx ;
		public DirectedGraph(Graph<D> mx, ArrayList<IndexVertex<D>> list) {
			vset = list;
			this.mx = mx;
		}
		
		@Override
		public void addEdge(D s, D e, double weight) {
			IndexVertex<D> vs = new IndexVertex<D>(new Vertex<D>(s), vset);
			IndexVertex<D> ve = new IndexVertex<D>(new Vertex<D>(e), vset);
			
			mx.addVertex(vs, true);
			mx.addVertex(ve, true);
			
//			DefaultEdge<IVertex<D>, W> edge = 
//					new DefaultEdge<IVertex<D>, W>(vs, ve, w);
			
			mx.setWeight(vs.index(), ve.index(), weight);
		}

		@Override
		public IEdge<IVertex<D>> getEdge(D s, D e) {
			IndexVertex<D> vs = null ;
			IndexVertex<D> ve = null ;
			double weight = -1.0;
			
			Iterator<IndexVertex<D>> it = vset.iterator();
			
			while ( it.hasNext()) {
				IndexVertex<D> iv = it.next();
				if ( iv.getData().equals(s) ) {
					vs = iv; 
				}
				
				if ( iv.getData().equals(e) ) {
					ve = iv;
				}
				
				if ( vs != null && ve != null ) {
					break;
				}
			}
			
			if ( vs == null ) {
				throw new VertexException("can not find vertex s : " + s);
			}
			
			if ( ve == null ) {
				throw new VertexException("can not find vertex e : " + e);
			}
			
			
			weight = mx.getWeight(vs.index(), ve.index());
			
			if ( weight < 0 ) {
				throw new EdgeException("cannot find edge between : " + s + " and " + e);
			}
			
			DefaultEdge<IVertex<D>> edge ;
			
			edge = new DefaultEdge<IVertex<D>>(vs, ve, weight);
			
//			cachedEdge.put(edge, edge);
			
			return edge;
		}
	}
}
