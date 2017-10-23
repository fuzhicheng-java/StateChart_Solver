import java.util.LinkedList;

public class ActionSet {

	public String name;
	public String id;
	public int type; //1 state, 2 transition
	public String fromName;
	public String toName;
	public LinkedList<String> actionNames=new LinkedList<>();
	
	public ActionSet(String id, int type)
	{
		this.id=id;
		this.type=type;
	}
}
