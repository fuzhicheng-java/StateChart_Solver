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
	public HashSet<String> used_events=new HashSet<>();
	public HashSet<String> usedVariableInEntryCondition=new HashSet<>();
	public HashSet<UpdatedVariable> updated_variables=new HashSet<>();
	public HashSet<String> actionSet=new HashSet<>();	
	public int isEntry=0;
	public static String entryFlag="sgraph:Entry";
	public String specification;
	public LinkedList<Transition> allTrainsitions;
	
	public LinkedList<SubNode> nodes=new LinkedList<>();
	public State(String domain_id, String domain_name, String id ,String name, String entry)
	{
		this.domain_id=domain_id;
		this.domain_name=domain_name;
		this.id=id;
		this.name=name;
		if(entry.equals(State.entryFlag))
		{
			this.isEntry=1;
		}
		else
		{
			this.isEntry=0;
		}
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
	
	public void addUsedEvent(String name)
	{
		this.used_events.add(name);
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
	
	public boolean hasOutGoingStateExceptGivenState(State state, Statechart statechart)
	{
		if(this.id.equals(state.id))
		{
			return true;
		}
		if(this.outgoing_transitions.size()>0)
		{
			for(String tid:this.outgoing_transitions)
			{
				Transition t=statechart.getTransition(tid);
				if(!t.to_state.equals(state.id))
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			return false;
		}
	}
	

}
