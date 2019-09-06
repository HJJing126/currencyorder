package conditional.basic;

public class Node {
	
	public String type;
	public String value;
	
	public Node(String type, String value) {
		this.type = type;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Node [type=" + type + ", value=" + value + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int hashCode() {

        int result = 17;
        result = 31 * result + type.hashCode();
        result = 31 * result + value.hashCode();
       

        return result;
    }

    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Node)) {
            return false;
        }

        Node tsp = (Node) obj;
        return type.equals(tsp.type) && value.equals(tsp.value);

    }
	
	
	

}
