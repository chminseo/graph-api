package graph;

import graph.model.EdgeType;
import graph.model.UndirectedEdge;
import graph.model.EdgeException;
import graph.model.IUndirectedEdge;
import graph.model.VertexException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class UndirectedGraph<D, E extends IUndirectedEdge<D>> extends Graph.GraphType<D, E> {
		final private ArrayList<IndexVertex<D>> vset ;
		final Graph<D, E> graphFacade ;
		
		public UndirectedGraph(Graph<D, E> mx, ArrayList<IndexVertex<D>> list) {
			vset = list;
			this.graphFacade = mx;
		}

		@Override
		public E setEdge(D s, D e, double weight) {
			
			IndexVertex<D> vs = graphFacade.findVertex(s);
			IndexVertex<D> ve = graphFacade.findVertex(e);
			
			int is = vs.index();
			int ie = ve.index();
			
			if ( is < ie) {
				IndexVertex<D> tmp = vs;
				vs = ve;
				ve = tmp;
			}
			
			graphFacade.setWeight(vs.index(), ve.index(), weight);
			return newEdge(vs.getData(), ve.getData(), weight);
			
		}
		
		public E removeEdge(D s, D e) {
			IndexVertex<D> vs = graphFacade.findVertex(s);
			IndexVertex<D> ve = graphFacade.findVertex(e);
			
			int is = vs.index();
			int ie = ve.index();
			
			if ( is < ie ) {
				IndexVertex<D> tmp = vs;
				vs = ve;
				ve = tmp;
			}
			
			graphFacade.setWeight(vs.index(), ve.index(), DoubleMatrix.INF_WEIGHT);
			
			return newEdge(vs.value, ve.value, DoubleMatrix.INF_WEIGHT);
		}
		public List<E> getEdges(D s, EdgeType eType) {
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
			
			List<E> list = graphFacade.listWeights(vs, EdgeType.ANY_EDGE);
			return list;
		};

		@Override
		public E getEdge(D s, D e) {
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
			
			weight = graphFacade.getWeight(vs.index(), ve.index());
			
			if ( weight < 0 ) {
				throw new EdgeException("cannot find edge between : " + s + " and " + e);
			}
			
			UndirectedEdge<D> edge = new UndirectedEdge<D>(vs.getData(), ve.getData(), weight);
//			cachedEdge.put(edge, edge);
			
			return (E) edge;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E newEdge(D s, D e, double weight) {
			return (E) new UndirectedEdge<D> (s, e, weight);
		}
		
	}