import java.util.HashSet;
import java.util.LinkedList;

public class State {
	
	public String domain_name;
	public String domain_id;
	public static int outgoing=2, incoming=1;
	public String name;
	public String id;
	public HashSet<String> incoming_transitions=new HashSet<>();
	public HashSet<String> outgoing_transitions=new HashSet<>();
	public HashSet<String> raised_events=new HashSet<>();
	public HashSet<String> usedVariableInEntryCondition=new HashSet<>();
	public HashSet<UpdatedVariable> updated_variables=new HashSet<>();
	public HashSet<String> actionSet=new HashSet<>();
	public State(String domain_id, String domain_name, String id ,String name)
	{
		this.domain_id=domain_id;
		this.domain_name=domain_name;
		this.id=id;
		this.name=name;
	}
	
	public void addUpdatedVariable(UpdatedVariable var)
	{
		this.updated_variables.add(var);
	}
	
	public void addUsedVariableInEntryCondition(String var)
	{
		this.usedVariableInEntryCondition.add(var);
	}

	
	public void addRaisedEvent(String name)
	{
		this.raised_events.add(name);
	}
	
	public void addIncomingTransition(String item)
	{
		this.incoming_transitions.add(item);
	}
	
	public void addOutgoingTransition(String item)
	{
		this.outgoing_transitions.add(item);
	}
	
	public void addActionSet(String name)
	{
		this.actionSet.add(name);
	}
	
}
