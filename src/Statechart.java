import java.util.LinkedList;

public class Statechart {

	public LinkedList<State> states=new LinkedList<>();
	public LinkedList<Transition> transitions=new LinkedList<>();
	public LinkedList<Event> events=new LinkedList<>();
	public LinkedList<Variable> variables=new LinkedList<>();
	
	public void addState(State state)
	{
		this.states.add(state);
	}
	
	public void addVariable(Variable var)
	{
		this.variables.add(var);
	}
	
	public void addEvent(Event event)
	{
		this.events.add(event);
	}
	
	public void addTransition(Transition transition)
	{
		this.transitions.add(transition);
	}
	
	public void traceFailedPath_Constant(String path)
	{
		if(path.equals(""))
		{
			return;
		}
		else
		{
			String[] sids=path.split(" ");
			LinkedList<State> test_states=new LinkedList<>();
			for(int i=0;i<sids.length;i++)
			{
				String id=sids[i];
				if(this.states.size()>0)
				{
					for(State state:this.states)
					{
						if(state.id.equals(id))
						{
							test_states.add(state);
						}
					}
				}
			}
			
			for(int i=0;i<test_states.size();i++)
			{
				State item_state=test_states.get(i);
				checkUpdatedVariableInState(item_state);
				if(i+1<test_states.size()&&item_state.incoming_transitions.size()>0)
				{
					for(String itran:item_state.incoming_transitions)
					{
						if(this.transitions.size()>0)
						{
							for(Transition tran:this.transitions)
							{
								if(tran.id.equals(itran)&&tran.to_state.equals(item_state.id)&&test_states.get(i+1).id.equals(tran.from_state))
								{
									checkUpdatedVariableInTransition(test_states.get(i+1),tran,item_state);
								}
							}
						}
					}
				}
			}
			
		}
	}
	
	public void checkUpdatedVariableInTransition(State pre_state, Transition trans,State state) {
		
		if(trans!=null&&trans.updated_variables.size()>0)
		{
			for(UpdatedVariable upvar:trans.updated_variables)
			{
				if(upvar.usedVariables.size()>0)
				{
					for(String varname:upvar.usedVariables)
					{
						if(this.variables.size()>0)
						{
							for(Variable var:this.variables)
							{
								if(var.name.equals(varname))
								{
									if(var.isConsant==Variable.constant)
									{
										System.out.println("Variable "+ upvar.name+" has been updated with constant variable "+ varname+" in the transition from state "+pre_state.name+" to state "+state.name);
									}
								}
							}
						}
					}
				}
			}
			
			if (trans.used_variables.size() > 0) {
				for (String varname : trans.used_variables) {
					if (this.variables.size() > 0) {
						for (Variable var : this.variables) {
							if (var.name.equals(varname)) {
								if (var.isConsant == Variable.constant) {
									System.out.println("Constant variable " + varname + " has been used in a conidtional statement in the transition from state " + pre_state.name+ " to state " + state.name);
								}
							}
						}
					}
				}
			}
			
		}
	}
	
	public void checkUpdatedVariableInState(State state) {
		
		if(state!=null&&state.updated_variables.size()>0)
		{
			for(UpdatedVariable upvar:state.updated_variables)
			{
				if(upvar.usedVariables.size()>0)
				{
					for(String varname:upvar.usedVariables)
					{
						if(this.variables.size()>0)
						{
							for(Variable var:this.variables)
							{
								if(var.name.equals(varname))
								{
									if(var.isConsant==Variable.constant)
									{
										System.out.println("Variable "+ upvar.name+" has been updated with constant variable "+ varname+" in state "+state.name);
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
