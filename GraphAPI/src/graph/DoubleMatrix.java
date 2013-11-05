package graph;

public class DoubleMatrix {

	int ox, oy;
	int base ;
	int depth;
	int rowLen, colLen;
	
	final public static double INF_WEIGHT = -1.0;
	
	double [][] map ;
	
	DoubleMatrix ( int ox, int oy, int base, int depth, int maxRow, int maxCol) {
		this.ox = ox;
		this.oy = oy;
		this.base = base;
		this.depth = depth;
		
		int size = (int) Math.pow(base, depth);
		
		rowLen = size;
		colLen = size;
		if ( ox + size > maxRow) {
			rowLen = ox + size - maxRow ;
		}
		
		if ( oy + size > maxCol ) {
			colLen = oy + size - maxCol;
		}
		
		map = new double[rowLen][colLen];
		
		init(map);
	}
	
	private void init(double [][] map) {
		for( int i = 0 ; i < map.length ; i++ ) {
			for( int k = 0 ; k < map[i].length ; k++) {
				map[i][k] = -1.0;
			}
		}
	}
	
//	public int getLength() {
//		return (int) Math.pow(base, depth);
//	}
	
	public int getRowLength() {
		return rowLen;
	}
	
	public int getCollength() {
		return colLen;
	}
	
	public void setValue( int r, int c, double weight  ) {
		int R = r - ox;
		int C = c - oy;
		this.map[R][C] = weight;
	}
	
	public double getValue(int r, int c) {
		return (r == c ) ? 0 : map[r-ox][c-oy];
	}
	
	public double [] getWeightsFrom(int startIdx) {
		int R = startIdx - ox;
		int L = getCollength();
		double [] retArr = new double [L];
		for( int c = 0 ; c < L ; c++ ){
			retArr[c] = map[R][c];
		}
		
		return retArr;
	}
	
	public double [] getWeightsTo(int endIdx ) {
		int C = endIdx - oy;
		int L = getRowLength();
		double [] retArr = new double [L];
		for( int s = 0 ; s < L ; s ++ ){
			retArr[s] = 
					map[s][C];
		}
		
		return retArr;
	}
	
	/**
	 * (r,c) 를 포함하는 가중치 행렬을 반환한다.<br/>
	 * 만일 r, 또는 c가 0보다 작을 경우 0보다 큰 매개변수를 포함하는 행렬을 반환한다.
	 * @param r
	 * @param c
	 * @return
	 */
	public boolean contains(int r, int c) {
//		int L = getLength();
		
		if ( r < 0 ) {
			return oy <= c && c < oy + colLen ;
		} else if ( c < 0 ) {
			return ox <= r && r < ox + rowLen;
		} else {			
			return ( ox <= r && r < ox + rowLen ) 
					&& (oy <= c && c < oy + colLen ) ;
		}
	}
	
//	public boolean cotainsRow(int row ) {
//		return ox <= row && row < ox + getLength();
//	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		int L = getLength();
		sb.append("[" + ox + "," + oy + "]" + "\n");
		for( int r = 0 ; r < map.length ; r++) {
			for( int c = 0 ; c < map[r].length ; c++) {
				sb.append(map[r][c] + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
