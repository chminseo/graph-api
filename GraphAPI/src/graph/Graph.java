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
import graph.model.Vertex;
import graph.model.IVertex;

public class Graph <D, W > {

	final private ArrayList<IndexVertex<D>> vset = new ArrayList<IndexVertex<D>>();
		
	private WeakHashMap<IEdge<? extends IVertex<D>, W>, IEdge<? extends IVertex<D>, W>> 
		cachedEdge = new WeakHashMap<IEdge<? extends IVertex<D>,W>, IEdge<? extends IVertex<D>,W>>();
	
	final private HashSet<WeightMatrix<W>> mxSet = new HashSet<WeightMatrix<W>>();
	
	private int base = 2;
	
	private GraphType<D, W> gType ;
	private Graph (int ox, int oy) {
		
	}
	
	static <D, W> Graph<D, W> newDirectedType () {
		Graph<D, W> mx = new Graph<D,W>(0,0);
		mx.gType = new DirectedGraph<D, W>(mx, mx.vset);
		return  mx;
	}
	
	static <D,W> Graph<D, W> newUndirectedType() {
		Graph<D, W> mx = new Graph<D, W>(0,0);
		mx.gType = new UndirectedGraph<D, W>(mx, mx.vset);
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
	
	public <V extends IVertex<D>> void addVertext(V v){
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
	
	public <V extends IVertex<D>> void addEdge(V s, V e, W weight) {
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
	
	public void addEdge(D s, D e, W w) {
		gType.addEdge(s, e, w);
	}
	
	private void setWeight(int r, int c, W weight) {
		int ox = r - r % base;
		int oy = c - c % base;
		
		WeightMatrix<W> mx = getMatrix(ox, oy);
		mx.setValue(r, c, weight);
		
	}
	
	
	public IEdge<IVertex<D>, W> getEdge(D s, D e) {
		return gType.getEdge(s, e);
	}
	
	private W getWeight(int r, int c) {
		return getMatrix(r, c).getValue(r, c);
	}
	
	
	WeightMatrix<W> getMatrix(int r, int c) {
		Iterator<WeightMatrix<W>> it = mxSet.iterator();
		
		WeightMatrix<W> mx = null ;
		
		while ( it.hasNext()) {
			mx = it.next();
			if ( mx.contains(r, c)){
				return mx;
			}
		}
		
		mx = new WeightMatrix<W>(r - r%base, c - c%base, base, 1);
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
	
	abstract static class GraphType<D, W> {
		public abstract void addEdge(D s, D e, W weight);
		public abstract IEdge<IVertex<D>, W> getEdge(D s, D e );
	}
	
	private static class UndirectedGraph<D,W> extends GraphType<D, W> {
		final private ArrayList<IndexVertex<D>> vset ;
		final Graph<D, W> mx ;
		
		public UndirectedGraph(Graph<D, W> mx, ArrayList<IndexVertex<D>> list) {
			vset = list;
			this.mx = mx;
		}

		@Override
		public void addEdge(D s, D e, W weight) {
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
		public IEdge<IVertex<D>, W> getEdge(D s, D e) {
			// TEST created and not tested method stub
			IndexVertex<D> vs = null ;
			IndexVertex<D> ve = null ;
			W weight = null;
			
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
			
			int is = vs.index();
			int ie = ve.index();
			
			if ( is < ie) {
				IndexVertex<D> tmp = vs;
				vs = ve;
				ve = tmp;
			}
			
			weight = mx.getWeight(vs.index(), ve.index());
			
			DefaultEdge<IVertex<D>, W> edge = new DefaultEdge<IVertex<D>, W>(vs, ve, weight);
//			cachedEdge.put(edge, edge);
			
			return edge;
		}
		
	}
	
	private static class DirectedGraph<D, W> extends GraphType<D, W>{

		final private ArrayList<IndexVertex<D>> vset ;
		final Graph<D, W> mx ;
		public DirectedGraph(Graph<D, W> mx, ArrayList<IndexVertex<D>> list) {
			vset = list;
			this.mx = mx;
		}
		
		@Override
		public void addEdge(D s, D e, W weight) {
			IndexVertex<D> vs = new IndexVertex<D>(new Vertex<D>(s), vset);
			IndexVertex<D> ve = new IndexVertex<D>(new Vertex<D>(e), vset);
			
			mx.addVertex(vs, true);
			mx.addVertex(ve, true);
			
//			DefaultEdge<IVertex<D>, W> edge = 
//					new DefaultEdge<IVertex<D>, W>(vs, ve, w);
			
			mx.setWeight(vs.index(), ve.index(), weight);
		}

		@Override
		public IEdge<IVertex<D>, W> getEdge(D s, D e) {
			IndexVertex<D> vs = null ;
			IndexVertex<D> ve = null ;
			W weight = null;
			
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
			
			weight = mx.getWeight(vs.index(), ve.index());
			
			DefaultEdge<IVertex<D>, W> edge = new DefaultEdge<IVertex<D>, W>(vs, ve, weight);
//			cachedEdge.put(edge, edge);
			
			return edge;
		}
		
	}	
}
