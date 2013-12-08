package graph.model;

public interface IUndirectedEdge<D> {

	public enum EdgeType {
		OUTGOING_EDGE(1),
		INCOMING_EDGE(2),
		ANY_EDGE(3);
		
		private final int val;
		EdgeType(int val) {
			this.val = val;
		}
		
		public String toString() {
			return val == 1 ?
					"OUTGOING" : 
						(val == 2 ? "INCOMING" : "ANY" );
		}

		public boolean isValid(EdgeType e) { 
			return (this.val & e.val)  > 0 ;
		}
	}
	
	public double getWeight();
	
	public D [] getVertexes();
}
