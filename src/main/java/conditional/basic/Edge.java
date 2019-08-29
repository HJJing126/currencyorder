package conditional.basic;

public class Edge {
	
	public String predicate;
	public Node destnode;
	
	public Edge(String predicate, Node object) {
		this.predicate = predicate;
		this.destnode= object;
	}
	
	
	 public int hashCode() {

	        int result = 17;
	        result = 31 * result + predicate.hashCode();
	        result = 31 * result + destnode.hashCode();
	       

	        return result;
	    }

	    public boolean equals(Object obj) {

	        if (this == obj) {
	            return true;
	        }
	        if (obj == null) {
	            return false;
	        }
	        if (!(obj instanceof Edge)) {
	            return false;
	        }

	        Edge tsp = (Edge) obj;
	        return predicate.equals(tsp.predicate) && destnode.equals(tsp.destnode);

	    }
		

		

	
	

}
