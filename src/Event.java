import java.util.LinkedList;

public class Event {

    public String domain_name;
    public String domain_id;
	public String name;
	public int type; //1 state, 2 transition
	public String raisedID;
	
	public LinkedList<State> raised_states=new LinkedList<>();
	public LinkedList<Transition> raised_transitions=new LinkedList<>();
	
	public Event(String domain_id, String domain_name, String name)
	{
		this.domain_id=domain_id;
		this.domain_name=domain_name;
		this.name=name;
	}
	
	
	public Event(String name)
	{
		this.name=name;
	}
	
	public void addRaisedState(State state)
	{
		this.raised_states.add(state);
	}
	
	public void addRaisedTransition(Transition transition)
	{
		this.raised_transitions.add(transition);
	}
	
}
