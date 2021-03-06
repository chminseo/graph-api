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

public class Graph <D, E extends IEdge<IVertex<D>>> {

	final private ArrayList<IndexVertex<D>> vset = new ArrayList<IndexVertex<D>>();
		
	private WeakHashMap<IEdge<? extends IVertex<D>>, IEdge<? extends IVertex<D>>> 
		cachedEdge = new WeakHashMap<IEdge<? extends IVertex<D>>, IEdge<? extends IVertex<D>>>();
	
	final private HashSet<DoubleMatrix> mxSet = new HashSet<DoubleMatrix>();
	
	private int base = 2;
	
	private GraphType<D, E> gType ;
	
	private ArrayList<VertextListener<D, E>> vListeners = new ArrayList<VertextListener<D, E>>();
	private ArrayList<EdgeListener<D, IVertex<D>>> eListeners = new ArrayList<EdgeListener<D,IVertex<D>>>();
	
	private Graph (int ox, int oy) {
		
	}
	
	static <D, E extends DirectedEdge<IVertex<D>>> Graph<D, E> newDirectedType () {
		Graph<D, E> mx = new Graph<D, E>(0,0);
		mx.gType = new DirectedGraph<D, E>(mx, mx.vset);
		return  mx;
	}
	
	static <D, E extends IEdge<IVertex<D>>> Graph<D, E> newUndirectedType() {
		Graph<D, E> mx = new Graph<D, E>(0,0);
		mx.gType = new UndirectedGraph<D, E>(mx, mx.vset);
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
	
	<V extends IVertex<D>> void addVertex(V v, boolean skipIfExist ) {
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
		for(VertextListener<D, E> vl : vListeners) {
			vl.vertexAdded(v, this);
		}
	}
	
	private <V extends IVertex<D>> void notifyVertexRemoved(V v) {
		
		for(VertextListener<D, E> vl : vListeners) {
			vl.vertexRemoved(v, this);
		}
	}
	
	private <E extends IEdge<IVertex<D>>> void notifyEdgeAdded(E edge) {
		for(EdgeListener<D, IVertex<D>> e : eListeners ){
			e.edgeCreated(edge);
		}
	}

	private <E extends IEdge<IVertex<D>>> void notifyEdgeChanged(E edge, double oldWeight ) {
		for(EdgeListener<D, IVertex<D>> e : eListeners ){
			e.edgeChanged(edge, oldWeight);
		}
	}
	
	private <E extends IEdge<IVertex<D>>> void notifyEdgeRemoved(E edge) {
		for(EdgeListener<D, IVertex<D>> e : eListeners ){
			e.edgeRemoved(edge);
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
		
		List<E> edges = getEdges(data, EdgeType.ANY_EDGE);
		for( E e : edges ) {
			IVertex<?> [] vs = e.getVertexes();
			removeEdge( (D)vs[0].getData(), (D) vs[1].getData() );
		}
		
		vset.remove(iv);
		
		notifyVertexRemoved(iv);
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
	/*
	 * 불필요할 수 있어서 가려놓음.
	 */
//	public <V extends IVertex<D>> void addEdge(V s, V e, double weight) {
//		// TODO not thread-safe
//		if ( !vset.contains(s) ) {
//			throw new RuntimeException(" no such vertext : " + s);
//		}
//		
//		if ( !vset.contains(e)) {
//			throw new RuntimeException(" no such vertext : " + e);
//		}
//		
//		int r = vset.indexOf(s);
//		int c = vset.indexOf(e);
//				
//		setWeight(r, c, weight);
//	}
	
	@SuppressWarnings("unchecked")
	public IEdge<IVertex<D>> removeEdge(D s, D e ) {
		ensureVertice(s, e);
		IEdge<IVertex<D>> edge = gType.removeEdge(s, e);
		
		notifyEdgeRemoved(edge);
		
		return edge;
	}
	
	public List<E> getEdges(D vertexData, EdgeType etype ) {
		return gType.getEdges(vertexData, etype );	
	}
	
	public void setEdge(D s, D e, double w) {
		if ( w <= 0 ) {
			throw new EdgeException ("negative weight value not allowed : " + w);
		}
		double currentWeight = weight(s, e);
		
		IEdge<IVertex<D>> newEdge = gType.setEdge(s, e, w);
		
		if ( currentWeight >= 0) {
			notifyEdgeChanged(newEdge, currentWeight);
		} else {
			notifyEdgeAdded(newEdge);
		}
	}
	
	void setWeight(int r, int c, double weight) {
		int ox = r - r % base;
		int oy = c - c % base;
		
		DoubleMatrix mx = getMatrix(ox, oy);
		mx.setValue(r, c, weight);
		
	}
	
	
	public IEdge<IVertex<D>> getEdge(D s, D e) {
		return gType.getEdge(s, e);
	}
	
	double getWeight(int r, int c) {
		return getMatrix(r, c).getValue(r, c);
	}
	
	private E asEdge(GraphType<D, E> graph, int from, int to, double weight ) {
		return graph.newEdge(vset.get(from), vset.get(to), weight);
//		return (E) new DefaultEdge<IVertex<?>>(vset.get(from), vset.get(to), weight );
	}
	
	List<E> listWeights(IndexVertex<D> vs, EdgeType e) {
		
		Iterator<DoubleMatrix> it = mxSet.iterator();
		
		DoubleMatrix mx = null ;
		
		int r, c;
		r = c = vs.index();
		
		ArrayList<E> edges = new ArrayList<E>();
		
		while ( it.hasNext()) {
			mx = it.next();
			double [] weights = null;
			// 출발지점이 vs 인 경우
			if ( EdgeType.OUTGOING_EDGE.isValid(e) && mx.contains(r, -1) ) {
				weights = mx.getWeightsFrom(r);
				for( int col = 0; col < weights.length ; col++) {
					if ( weights[col] > 0 ) {
						edges.add(asEdge(gType, r, mx.oy + col, weights[col]) );
					}
				}
			}
			
			// 도착지점이 vs 인 경우
			if ( EdgeType.INCOMING_EDGE.isValid(e) && mx.contains(-1, c) ){
				weights = mx.getWeightsTo(c);
				for( int row = 0; row < weights.length ; row ++) {
					if ( weights[row] > 0 ) {
						edges.add(asEdge(gType, row + mx.ox , c, weights[row]) );
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
	
	public void addVertexListener(VertextListener<D, E> listener) {
		if ( ! vListeners.contains(listener) ) {
			vListeners.add(listener);
		}
	}
	
	public void removeVertexListener(VertextListener<D, E> listener) {
		vListeners.remove(listener);
	}
	
	public void addEdgeListener(EdgeListener<D, IVertex<D>> listener) {
		if ( !eListeners.contains(listener)) {
			eListeners.add(listener);
		}
	}
	
	public void removeEdgeListener(VertextListener<D, E> listener) {
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
	
	abstract static class GraphType<D, E extends IEdge<IVertex<D>>> {
		public abstract E setEdge(D s, D e, double weight);
		public abstract E removeEdge(D s, D e);
		public abstract E getEdge(D s, D e );
		public abstract List<E> getEdges(D s, EdgeType eType);
		public abstract E newEdge(IVertex<D> s, IVertex<D> e, double weight);
	}
}
