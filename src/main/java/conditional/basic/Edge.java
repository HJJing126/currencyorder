package conditional.basic;

public class Edge {
	
	public String predicate;
	public Node destnode;
	
	public Edge(String predicate, Node object) {
		this.predicate = predicate;
		this.destnode= object;
	}

}
