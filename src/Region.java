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

	public void updateFakeEntryState(Statechart statechart) {
		// TODO Auto-generated method stub
	    String tid=null;
		for(String key:this.entryState.outgoing_transitions)
		{
			tid=key;
			break;
		}
		Transition ts=statechart.getTransition(tid);
		State state=statechart.getState(ts.to_state);
		this.entryState=state;
	}
	
	public State getState(String name)
	{
		for(State temp:this.states)
		{
			if(temp.name.equals(name))
			{
				return temp;
			}
		}
		return null;
	}
}
