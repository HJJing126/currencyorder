package conditional.basic;

public class NodeTime extends Node{
    public Timestamp timestamp;
	public NodeTime(String type, String value, Timestamp  timestamp) {
		super(type, value);
		// TODO Auto-generated constructor stub
		this. timestamp=  timestamp;
	}
	
	
	public boolean compare(NodeTime n2) {
		if(this.timestamp.compare(n2.timestamp) == true)
			return true;
		return false;
	}
	
	public int hashCode() {

        int result = 17;
        result = 31 * result + type.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + timestamp.hashCode();

        return result;
    }

    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof NodeTime)) {
            return false;
        }

        NodeTime tsp = (NodeTime) obj;
        return type.equals(tsp.type) && value.equals(tsp.value) && timestamp.equals(tsp.timestamp);

    }

}
