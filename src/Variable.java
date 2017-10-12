import java.util.LinkedList;

public class Variable {

	public static int constant=1, local=2;
	public String domain_name;
	public String domain_id;
	public String name;
	public String id;
	public int isConsant;
	public LinkedList<State> updated_states;
	public LinkedList<Transition> updated_transitions;
	public LinkedList<State> used_states;
	public LinkedList<Transition> used_transitions; 
	
	public Variable(String domain_id,String domain_name,  String name, int isConsant)
	{
		this.domain_id=domain_id;
		this.domain_name=domain_name;
		this.name=name;
		this.isConsant=isConsant;
	}
	
	public void addUpdatedState(State item)
	{
		this.updated_states.add(item);
	}
	
	public void addUsedState(State item)
	{
		this.used_states.add(item);
	}
	
	public void addUpdatedTransition(Transition item)
	{
		this.updated_transitions.add(item);
	}
	
	public void addUsedTransition(Transition item)
	{
		this.used_transitions.add(item);
	}

	
}
