package graph;

public class DoubleMatrix {

	int ox, oy;
	int base ;
	int depth;
	
	double [][] map ;
	
	DoubleMatrix ( int ox, int oy, int base, int depth) {
		this.ox = ox;
		this.oy = oy;
		this.base = base;
		this.depth = depth;
		
		int size = (int) Math.pow(base, depth);
		map = new double[size][size];
		
		init(map);
	}
	
	private void init(double [][] map) {
		for( int i = 0 ; i < map.length ; i++ ) {
			for( int k = 0 ; k < map[i].length ; k++) {
				map[i][k] = -1.0;
			}
		}
	}
	
	public int getLength() {
		return (int) Math.pow(base, depth);
	}
	
	public void setValue( int r, int c, double weight  ) {
		int R = r - ox;
		int C = c - oy;
		this.map[R][C] = weight;
	}
	
	public double getValue(int r, int c) {
		return (r == c ) ? 0 : map[r-ox][c-oy];
	}
	
	public boolean contains(int r, int c) {
		int L = getLength();
		return ( ox <= r && r < ox + L ) 
				&& (oy <= c && c < oy + L ) ;
	}
}
