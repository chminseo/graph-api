package graph;

import graph.model.EdgeException;
import graph.model.IVertex;
import graph.model.Vertex;
import graph.model.VertexException;
import graph.model.IEdge.EdgeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class DirectedGraph<D, E extends DirectedEdge<IVertex<D>>> extends Graph.GraphType<D, E>{

		final private ArrayList<IndexVertex<D>> vset ;
		final Graph<D,E> graphFacade ;
		public DirectedGraph(Graph<D, E> mx, ArrayList<IndexVertex<D>> list) {
			vset = list;
			this.graphFacade = mx;
		}
		
		@Override
		public E setEdge(D s, D e, double weight) {
			
			IndexVertex<D> vs = new IndexVertex<D>(new Vertex<D>(s), vset);
			IndexVertex<D> ve = new IndexVertex<D>(new Vertex<D>(e), vset);
			
			graphFacade.addVertex(vs, true);
			graphFacade.addVertex(ve, true);
			
//			DefaultEdge<IVertex<D>, W> edge = 
//					new DefaultEdge<IVertex<D>, W>(vs, ve, w);
			graphFacade.setWeight(vs.index(), ve.index(), weight);
			
			return (E) new DirectedEdge<IVertex<D>> (vs, ve, weight);
		}
		
		public E removeEdge(D s, D e) {
			// TEST 구현해야함.
			IndexVertex<D> vs = graphFacade.findVertex(s);
			IndexVertex<D> ve = graphFacade.findVertex(e);
			
			graphFacade.setWeight(vs.index(), ve.index(), DoubleMatrix.INF_WEIGHT);
			
			return (E) new DirectedEdge<IVertex<D>>(vs.v, ve.v, DoubleMatrix.INF_WEIGHT);
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
			
			DirectedEdge<IVertex<D>> edge ;
			
			edge = new DirectedEdge<IVertex<D>>(vs, ve, weight);
			
//			cachedEdge.put(edge, edge);
			
			return (E) edge;
		}

		@Override
		public E newEdge(IVertex<D> s, IVertex<D> e, double weight) {
			// TEST created and not tested method stub
			return (E) new DirectedEdge<IVertex<D>>(s, e, weight);
		}
	}