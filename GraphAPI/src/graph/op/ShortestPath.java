package graph.op;

import graph.Graph;
import graph.model.VertexException;

public class ShortestPath<D, W extends Number&Comparable<W> > {
	
	private Graph<D, W> graph ;
	
	public ShortestPath(Graph<D,W> g ) {
		// TODO Auto-generated constructor stub
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

		@SuppressWarnings("unchecked")
		W [] distance = (W[]) new Number[nodes.length];
		boolean [] found = new boolean [nodes.length];
		
		for (int i = 0; i < nodes.length; i++) {
			
			distance[i] = graph.weight(src, nodes[i]);
			found[i] = false;
			
			if ( nodes[i].equals(src)) {
				found[i] = true;
			}
		}
		
		ICountable<W> ic = new ICountable<W>() {

			@SuppressWarnings("unchecked")
			@Override
			public W add(W a, W b) {
				return (W) ((a == null || b == null ) 
						? Double.valueOf(Double.MAX_VALUE) : 
							Double.valueOf( a.doubleValue() + b.doubleValue()) );
			}

		};
		
		for ( int i = 0 ; i < nodes.length -2 ; i++) {
			int nextIdx = choose(distance, found);
			found[nextIdx] = true;
			D chosen = nodes[nextIdx];
			for( int w = 0 ; w < nodes.length ; w ++) {
				if ( found[w]) {
					continue;
				}
				
				W newWeight = ic.add(distance[nextIdx], graph.weight(chosen, nodes[w]));
				
				if (newWeight.compareTo( ic.add(distance[w], (W)Double.valueOf(0) ) ) < 0 ) {
					distance[w] = ic.add(distance[nextIdx], graph.weight(chosen, nodes[w]));
				}
			}
		}
		
		for( W w : distance) {
			System.out.println(w);
		}
	}

	
	private int choose(W[] distance, boolean[] found) {
		// TEST created and not tested method stub
		Number min = Number.class.cast(Double.MAX_VALUE);
		int minpos ;
		
		minpos = -1;
		
		for( int i =0 ; i < distance.length ; i++) {
			if ( distance[i] == null || found[i] ){
				continue;
			}
			if ( distance[i].doubleValue() < min.doubleValue() ) {
				min = distance[i];
				minpos = i;
			}
		}
		return minpos;
	}
}
