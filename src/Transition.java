import java.util.HashSet;
import java.util.LinkedList;

public class Transition {

	public static int outgoing=2, incoming=1;
	public String from_state;
	public String to_state; 
	public String from_state_name;
	public String to_state_name; 
	public String domain_name;
	public String id;
	public LinkedList<String> raised_events=new LinkedList<>();
	public LinkedList<String> used_events=new LinkedList<>();
	public HashSet<UpdatedVariable> updated_variables=new HashSet<>();
	public LinkedList<String> used_variables=new LinkedList<>();
	public HashSet<String> actionSet=new HashSet<>();
	public boolean hasAlways=false;
	public String specification;
	
	public Transition(String id, String from, String to)
	{
		this.id=id;
		this.from_state=from;
		this.to_state=to;
	}
	
	public void addUpdatedVariable(UpdatedVariable item)
	{
		this.updated_variables.add(item);
	}
	
	public void addUsedVariable(String item)
	{
		this.used_variables.add(item);
	}
	
	public void addRaisedEvent(String item)
	{
		this.raised_events.add(item);
	}
	
	public void addUsedEvent(String item)
	{
		this.used_events.add(item);
	}
	
	public void addActionSet(String name)
	{
		this.actionSet.add(name);
	}
	
}
