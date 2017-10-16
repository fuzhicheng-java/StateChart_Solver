import java.util.HashSet;

public class Variable {

	public static int constant=1, local=2;
	public String domain_name;
	public String domain_id;
	public String name;
	public String full_name;
	public String id;
	public int isConsant;
	public HashSet<String> updated_states=new HashSet<>();
	public HashSet<String> updated_transitions=new HashSet<>();;
	public HashSet<VarRelation> used_states=new HashSet<>();;
	public HashSet<VarRelation> used_transitions=new HashSet<>();; 
	public boolean beUpdated=false;
	public int type=0; // type is indicate is operation
	public Variable(String domain_id,String domain_name,  String name, int isConsant)
	{
		this.domain_id=domain_id;
		this.domain_name=domain_name;
		this.name=name;
		this.isConsant=isConsant;
		this.type=0;
	}
	
	public static void updateBeUpdated(Statechart st, String var)
	{
		if(st.variables!=null&&st.variables.size()>0)
		{
		    for(Variable item: st.variables)
		    {
		    	if(item.name.equals(var))
		    	{
		    		item.beUpdated=true;
		    		break;
		    	}
		    }
		}
	}
	
	public static void addUpdatedTransition(Statechart st, String var, String location)
	{
		if(st.variables!=null&&st.variables.size()>0)
		{
		    for(Variable item: st.variables)
		    {
		    	if(item.name.equals(var))
		    	{
		    		item.updated_transitions.add(location);
		    		break;
		    	}
		    }
		}
	}
	
	public static void addUpdatedState(Statechart st, String var, String location)
	{
		if(st.variables!=null&&st.variables.size()>0)
		{
		    for(Variable item: st.variables)
		    {
		    	if(item.name.equals(var))
		    	{
		    		item.updated_states.add(location);
		    		break;
		    	}
		    }
		}
	}
	
	public static void addUsedState(Statechart st, String var, String location, String imp_var)
	{
		if(st.variables!=null&&st.variables.size()>0)
		{
		    for(Variable item: st.variables)
		    {
		    	if(item.name.equals(var))
		    	{
		    		item.used_states.add(new VarRelation(location, imp_var));
		    		break;
		    	}
		    }
		}
	}
	
	
	
	
	
	public static void addUsedTransition(Statechart st, String var, String location, String imp_var)
	{
		if(st.variables!=null&&st.variables.size()>0)
		{
		    for(Variable item: st.variables)
		    {
		    	if(item.name.equals(var))
		    	{
		    		item.used_transitions.add(new VarRelation(location, imp_var));
		    		break;
		    	}
		    }
		}
	}
}
