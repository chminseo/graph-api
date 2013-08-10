package graph;

import graph.model.IVertex;

public class DuplicateVertexException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7666513063375085628L;

	public DuplicateVertexException(IVertex<?> v) {
		super(v.toString());
	}
}
