package graph;

import graph.Graph.GraphType;
import graph.Graph.IndexVertex;
import graph.model.DefaultEdge;
import graph.model.EdgeException;
import graph.model.IEdge;
import graph.model.IVertex;
import graph.model.VertexException;
import graph.model.IEdge.EdgeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class UndirectedGraph<D> extends Graph.GraphType<D> {
		final private ArrayList<Graph.IndexVertex<D>> vset ;
		final Graph<D> mx ;
		
		public UndirectedGraph(Graph<D> mx, ArrayList<Graph.IndexVertex<D>> list) {
			vset = list;
			this.mx = mx;
		}

		@Override
		public IEdge<IVertex<D>> setEdge(D s, D e, double weight) {
			
			Graph.IndexVertex<D> vs = mx.findVertex(s);
			Graph.IndexVertex<D> ve = mx.findVertex(e);
			
			mx.addVertex(vs, true);
			mx.addVertex(ve, true);
			
			int is = vs.index();
			int ie = ve.index();
			
			if ( is < ie) {
				Graph.IndexVertex<D> tmp = vs;
				vs = ve;
				ve = tmp;
			}
			
			mx.setWeight(vs.index(), ve.index(), weight);
			return new DefaultEdge<IVertex<D>> (vs, ve, weight);
			
		}
		
		public IEdge<IVertex<D>> removeEdge(D s, D e) {
			Graph.IndexVertex<D> vs = mx.findVertex(s);
			Graph.IndexVertex<D> ve = mx.findVertex(e);
			
			int is = vs.index();
			int ie = ve.index();
			
			if ( is < ie ) {
				Graph.IndexVertex<D> tmp = vs;
				vs = ve;
				ve = tmp;
			}
			
			mx.setWeight(vs.index(), ve.index(), DoubleMatrix.INF_WEIGHT);
			
			return new DefaultEdge<IVertex<D>>(vs.v, ve.v, DoubleMatrix.INF_WEIGHT);
		}
		public List<IEdge<IVertex<?>>> getEdges(D s, EdgeType eType) {
			Graph.IndexVertex<D> vs = null;
			Iterator<Graph.IndexVertex<D>> it = vset.iterator();
			
			while ( it.hasNext()) {
				Graph.IndexVertex<D> iv = it.next();
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
			Graph.IndexVertex<D> vs = null ;
			Graph.IndexVertex<D> ve = null ;
			double weight = -1;
			
			Iterator<Graph.IndexVertex<D>> it = vset.iterator();
			
			while ( it.hasNext()) {
				Graph.IndexVertex<D> iv = it.next();
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
				Graph.IndexVertex<D> tmp = vs;
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