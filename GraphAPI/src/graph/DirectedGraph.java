package graph;

import graph.model.EdgeException;
import graph.model.IDirectedEdge;
import graph.model.VertexException;
import graph.model.IEdge.EdgeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class DirectedGraph<D, E extends IDirectedEdge<D>> extends Graph.GraphType<D, E>{

		final private ArrayList<IndexVertex<D>> vset ;
		final Graph<D, E> graphFacade ;
		public DirectedGraph(Graph<D, E> mx, ArrayList<IndexVertex<D>> list) {
			vset = list;
			this.graphFacade = mx;
		}
		
		@Override
		public E setEdge(D s, D e, double weight) {
			
			IndexVertex<D> vs = new IndexVertex<D>(s, vset);
			IndexVertex<D> ve = new IndexVertex<D>(e, vset);
			
			graphFacade.setWeight(vs.index(), ve.index(), weight);
			
			return newEdge(s, e, weight);
			
		}
		
		public E removeEdge(D s, D e) {
			// TEST 구현해야함.
			IndexVertex<D> vs = graphFacade.findVertex(s);
			IndexVertex<D> ve = graphFacade.findVertex(e);
			
			graphFacade.setWeight(vs.index(), ve.index(), DoubleMatrix.INF_WEIGHT);
			
			return newEdge(s, e, DoubleMatrix.INF_WEIGHT);
		}
		
		@Override
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
			
			List<E> val = graphFacade.listWeights(vs, eType);
			
			return val; 
			
			
		}

		@Override
		public E getEdge(D s, D e) {
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
			
			
			weight = graphFacade.getWeight(vs.index(), ve.index());
			
			if ( weight < 0 ) {
				throw new EdgeException("cannot find edge between : " + s + " and " + e);
			}
			
			E edge ;
			
			edge = newEdge(s, e, weight);
			
//			cachedEdge.put(edge, edge);
			
			return edge;
		}

		@Override
		public E newEdge(D s, D e, double weight) {
			// TEST created and not tested method stub
			return (E) new DirectedEdge<D>(s, e, weight);
		}
	}