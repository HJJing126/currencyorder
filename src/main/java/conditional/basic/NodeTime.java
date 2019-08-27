package conditional.basic;

public class NodeTime extends Node{
    public Timestamp timestamp;
	public NodeTime(int type, String value, Timestamp  timestamp) {
		super(type, value);
		// TODO Auto-generated constructor stub
		this. timestamp=  timestamp;
	}
	
	
	public boolean compare(NodeTime n2) {
		if(this.timestamp.compare(n2.timestamp) == true)
			return true;
		return false;
	}

}
