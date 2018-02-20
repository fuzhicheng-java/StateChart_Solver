import java.util.LinkedList;

public class Region {

	
	public String id; 
	public String name;
	public LinkedList<State> states=new LinkedList<>();
	
	public State entryState;
	
	public Region(String id, String name)
	{
		this.id=id;
		this.name=name;
	}
}
