package graph.model;

public class DefaultVertex<D> {
	
	private D data ;
	public DefaultVertex (D data) {
		this.data = data;
	}
	
	public D getData(){
		return data;
	}
	
	
}
