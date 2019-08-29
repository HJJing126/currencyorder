package conditional.basic;

public class Condition extends Edge {

	public Condition(String predicate, Node object) {
		super(predicate, object);
		// TODO Auto-generated constructor stub
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


