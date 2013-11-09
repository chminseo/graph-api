package graph;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import graph.model.DefaultEdge;
import graph.model.IEdge;
import graph.model.EdgeException;
import graph.model.IEdge.EdgeType;
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
	
	private ArrayList<VertextListener<D>> vListeners = new ArrayList<VertextListener<D>>();
	
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
			if ( v instanceof IndexVertex ){
				iv = (IndexVertex<D>) v;
			} else {
				iv = new IndexVertex<D>(v, vset);		
			}
			
			vset.add(iv);
			
			notifyVertexAdded(v);
		}
	}
	
	private <V extends IVertex<D>> void notifyVertexAdded(V v) {
		for(VertextListener<D> vl : vListeners) {
			vl.vertexAdded(v, this);
		}
	}
	
	public IVertex<D> addVertex(D data) {
		IndexVertex<D> v = new IndexVertex<D>(new Vertex<D>(data), vset);
		addVertext(v);
		return v;
	}
	
	/**
	 * 주어진 데이터에 해당하는 vertex를 제거함.<br/>
	 * 연결된 edge가 있으면 같이 제거함.
	 */
	@SuppressWarnings("unchecked")
	public IVertex<D> removeVertex(D data) {
		IndexVertex<D> iv = findVertex(data);
		if ( iv == null ) {
			throw new VertexException("no such vertex : " + data);
		}
		
		List<IEdge<IVertex<?>>> edges = getEdges(data, EdgeType.ANY_EDGE);
		for( IEdge<IVertex<?>> e : edges ) {
			IVertex<?> [] vs = e.getVertexes();
			removeEdge( (D)vs[0].getData(), (D) vs[1].getData() );
		}
		
		vset.remove(iv);
		
		return iv.v;
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
	
	// ensure the given nodes exist in the list of the vertex.
	final private void ensureVertice(D... nodes) {
		boolean notFound = true;
		for( int i = 0 ; i < nodes.length ; i++ ) {
			notFound = true;
			for(IndexVertex<D> iv : vset ) {
				if ( iv.getData().equals(nodes[i]) ) {
					notFound = false;
					break;
				}
			}
			if ( notFound ) {		
				throw new VertexException(" no such vertext : " + nodes[i]);
			}
		}
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
	
	@SuppressWarnings("unchecked")
	public IEdge<IVertex<D>> removeEdge(D s, D e ) {
		ensureVertice(s, e);
		return gType.removeEdge(s, e);
	}
	
	public List<IEdge<IVertex<?>>> getEdges(D vertexData, EdgeType etype ) {
		return gType.getEdges(vertexData, etype );	
	}
	
	public void setEdge(D s, D e, double w) {
		if ( w <= 0 ) {
			throw new EdgeException ("negative weight value not allowed : " + w);
		}
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
	
	private IEdge<IVertex<?>> asEdge(int from, int to, double weight ) {
		return new DefaultEdge<IVertex<?>>(vset.get(from), vset.get(to), weight );
	}
	
	private List<IEdge<IVertex<?>>> listWeights(IndexVertex<D> vs, EdgeType e) {
		
		Iterator<DoubleMatrix> it = mxSet.iterator();
		
		DoubleMatrix mx = null ;
		
		int r, c;
		r = c = vs.index();
		
		ArrayList<IEdge<IVertex<?>>> edges = new ArrayList<IEdge<IVertex<?>>>();
		
		while ( it.hasNext()) {
			mx = it.next();
			double [] weights = null;
			// 출발지점이 vs 인 경우
			if ( EdgeType.OUTGOING_EDGE.isValid(e) && mx.contains(r, -1) ) {
				weights = mx.getWeightsFrom(r);
				for( int col = 0; col < weights.length ; col++) {
					if ( weights[col] > 0 ) {
						edges.add(asEdge(r, mx.oy + col, weights[col]) );
					}
				}
			}
			
			// 도착지점이 vs 인 경우
			if ( EdgeType.INCOMING_EDGE.isValid(e) && mx.contains(-1, c) ){
				weights = mx.getWeightsTo(c);
				for( int row = 0; row < weights.length ; row ++) {
					if ( weights[row] > 0 ) {
						edges.add(asEdge(row + mx.ox , c, weights[row]) );
					}
				}
			}
		}
		
		return edges;
	}
	
	/**
	 * src 부터 dst까지 edge가 존재하면 weight값을 반환한다. 존재하지 않으면 -1을 반환한다.
	 * @param src
	 * @param dst
	 * @return edge가 존재하면 1을 반환, 그렇지 않으면 -1을 반환.
	 */
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
		
		int maxLen = getVertexSize();
		mx = new DoubleMatrix(r - r%base, c - c%base, base, 1, maxLen, maxLen );
		mxSet.add(mx);
		
		return mx;
		
	}
	
	public void addVertexListener(VertextListener<D> listener) {
		if ( ! vListeners.contains(listener) ) {
			vListeners.add(listener);
		}
	}
	
	public void removeVertexListener(VertextListener<D> listener) {
		vListeners.remove(listener);
	}
	
	IndexVertex<D> findVertex( D s) {
		for( IndexVertex<D> iv : vset) {
			if ( iv.getData().equals(s)) {
				return iv;
			}
		}
		
		throw new VertexException("no such vertex : " + s);
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
		public abstract IEdge<IVertex<D>> removeEdge(D s, D e);
		public abstract IEdge<IVertex<D>> getEdge(D s, D e );
		public abstract List<IEdge<IVertex<?>>> getEdges(D s, EdgeType eType);
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
		
		public IEdge<IVertex<D>> removeEdge(D s, D e) {
			// TEST 구현해야함.
			IndexVertex<D> vs = mx.findVertex(s);
			IndexVertex<D> ve = mx.findVertex(e);
			
			int is = vs.index();
			int ie = ve.index();
			
			if ( is < ie ) {
				IndexVertex<D> tmp = vs;
				vs = ve;
				ve = tmp;
			}
			
			mx.setWeight(vs.index(), ve.index(), DoubleMatrix.INF_WEIGHT);
			
			return new DefaultEdge<IVertex<D>>(vs.v, ve.v, DoubleMatrix.INF_WEIGHT);
		}
		public List<IEdge<IVertex<?>>> getEdges(D s, EdgeType eType) {
			IndexVertex<D> vs = null;
			Iterator<IndexVertex<D>> it = vset.iterator();
			
			while ( it.hasNext()) {
				IndexVertex<D> iv = it.next();
				if ( iv.getData().equals(s) ) {
					vs = iv;
					break;
				}
			}
			
			if ( vs == null ) {				
				throw new VertexException("can not find vertex s : " + s);
			}
			
			return mx.listWeights(vs, EdgeType.ANY_EDGE);
		};

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
		
		public IEdge<IVertex<D>> removeEdge(D s, D e) {
			// TEST 구현해야함.
			IndexVertex<D> vs = mx.findVertex(s);
			IndexVertex<D> ve = mx.findVertex(e);
			
			mx.setWeight(vs.index(), ve.index(), DoubleMatrix.INF_WEIGHT);
			
			return new DefaultEdge<IVertex<D>>(vs.v, ve.v, DoubleMatrix.INF_WEIGHT);
		}
		
		@Override
		public List<IEdge<IVertex<?>>> getEdges(D s, EdgeType eType) {
			IndexVertex<D> vs = null;
			Iterator<IndexVertex<D>> it = vset.iterator();
			
			while ( it.hasNext()) {
				IndexVertex<D> iv = it.next();
				if ( iv.getData().equals(s) ) {
					vs = iv;
					break;
				}
			}
			
			if ( vs == null ) {				
				throw new VertexException("can not find vertex s : " + s);
			}
			
			return mx.listWeights(vs, eType);
			
			
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
