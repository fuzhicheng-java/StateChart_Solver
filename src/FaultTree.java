import java.util.LinkedList;

public class FaultTree {
	public String region_id;
	public String region_name;
	public State state;
	public FaultTree(String rid, String rname)
	{
		this.region_id=rid;
		this.region_name=rname;
	}
}

