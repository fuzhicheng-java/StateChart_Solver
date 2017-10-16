import java.util.LinkedList;

public class UpdatedVariable {
	public int type; //1 state, 2 transition
	public String location;// id
	public String name;
	LinkedList<String> usedVariables=new LinkedList<>();
	
	public UpdatedVariable(String name)
	{
		this.name=name;
	}
	public void addUsedVariable(String name)
	{
		this.usedVariables.add(name);
	}
}
