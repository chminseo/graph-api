package graph;

import graph.model.DefaultEdge;
import graph.model.EdgeException;
import graph.model.IEdge;
import graph.model.IVertex;
import graph.model.VertexException;
import graph.model.IEdge.EdgeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class UndirectedGraph<D, E extends IEdge<IVertex<D>>> extends Graph.GraphType<D, E> {
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
			
			graphFacade.addVertex(vs, true);
			graphFacade.addVertex(ve, true);
			
			int is = vs.index();
			int ie = ve.index();
			
			if ( is < ie) {
				IndexVertex<D> tmp = vs;
				vs = ve;
				ve = tmp;
			}
			
			graphFacade.setWeight(vs.index(), ve.index(), weight);
			return (E) new DefaultEdge<IVertex<D>> (vs, ve, weight);
			
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
			
			return (E) new DefaultEdge<IVertex<D>>(vs.v, ve.v, DoubleMatrix.INF_WEIGHT);
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
			
			DefaultEdge<IVertex<D>> edge = new DefaultEdge<IVertex<D>>(vs, ve, weight);
//			cachedEdge.put(edge, edge);
			
			return (E) edge;
		}

		@Override
		public E newEdge(IVertex<D> vs, IVertex<D> ve, double weight) {
			return (E) new DefaultEdge<IVertex<D>> (vs, ve, weight);
		}
		
	}