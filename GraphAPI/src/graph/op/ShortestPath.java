package graph.op;

import graph.Graph;
import graph.model.IUndirectedEdge;
import graph.model.VertexException;

public class ShortestPath<D> {
	
	private Graph<D, IUndirectedEdge<D>> graph ;
	
	public ShortestPath(Graph<D, IUndirectedEdge<D>> g ) {
		this.graph = g;
	}
	
	public void process(D src, D dst) {
		
		if ( ! graph.hasVertex(src)) {
			throw new VertexException("cannot find vertex src : " + src);
		}
		
		if ( ! graph.hasVertex(dst)) {
			throw new VertexException("cannot find vertex dst : " + dst);
		}
		
		D [] nodes = graph.listVertex();

		double [] distance = (double []) new double[nodes.length];
		boolean [] found = new boolean [nodes.length];
		
		for (int i = 0; i < nodes.length; i++) {
			
			distance[i] = graph.weight(src, nodes[i]);
			found[i] = false;
			
			if ( nodes[i].equals(src)) {
				found[i] = true;
			}
		}
		
		int nextIdx = -1;
		for ( int i = 0 ; i < nodes.length -2 ; i++) {
			nextIdx = choose(distance, found);
			found[nextIdx] = true;
			
			if ( nodes[nextIdx].equals(dst) ) {
				break;
			}
			D chosen = nodes[nextIdx];
			for( int w = 0 ; w < nodes.length ; w ++) {
				if ( found[w]) {
					continue;
				}
				
				double newWeight = add(distance[nextIdx], graph.weight(chosen, nodes[w]));
				
				if (distance[w] < 0 || newWeight < distance[w] ) {
					distance[w] = newWeight;
				}
			}
		}
		
		for( double w : distance) {
			System.out.println(w);
		}
	}
	
	private double add(double d0, double d1) {
		if ( d0 < 0 || d1 < 0 ) {
			return Double.MAX_VALUE;
		} else {
			return d0 + d1;
		}
	}

	
	private int choose( double [] distance, boolean[] found) {
		double min = Double.MAX_VALUE ;
		int minpos = -1;
		
		for( int i =0 ; i < distance.length ; i++) {
			if ( distance[i] < 0 || found[i] ){
				continue;
			}
			if ( distance[i] < min ) {
				min = distance[i];
				minpos = i;
			}
		}
		return minpos;
	}
}
