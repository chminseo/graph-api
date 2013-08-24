package graph;

class WeightMatrix<W>  {
	int ox, oy;
	int base ;
	int depth;
	
	W [][] map ;
	
	@SuppressWarnings("unchecked")
	WeightMatrix ( int ox, int oy, int base, int depth) {
		this.ox = ox;
		this.oy = oy;
		this.base = base;
		this.depth = depth;
		
		int size = (int) Math.pow(base, depth);
		map = (W[][]) new Object[size][size];
	}
	
	public int getLength() {
		return (int) Math.pow(base, depth);
	}
	
	public void setValue( int r, int c, W weight  ) {
		int R = r - ox;
		int C = c - oy;
		this.map[R][C] = weight;
	}
	
	public W getValue(int r, int c) {
		return map[r-ox][c-oy];
	}
	
	public boolean contains(int r, int c) {
		int L = getLength();
		return ( ox <= r && r < ox + L ) 
				&& (oy <= c && c < oy + L ) ;
	}
}