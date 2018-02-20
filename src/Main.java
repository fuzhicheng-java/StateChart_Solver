import java.util.Scanner;

public class Main {

	public static void main(String[] args)
	{
		String file_path="";
		if(System.getProperty("os.name").startsWith("Windows"))
		{
			file_path="C:\\Users\\DaoDao\\Desktop\\statechart\\";
		}
		else
		{
			file_path="/Users/daodao/git/StateChart_Solver/statechart/";
		}
		String failed_path="_wWau0JwjEeeYAr1R4dqAHw _XyDBIJwjEeeYAr1R4dqAHw _73A5oJwiEeeYAr1R4dqAHw _jjGC0pwiEeeYAr1R4dqAHw";
		Statechart statechart=new Statechart();
		SCParser sp=new SCParser();
		try {
			sp.readStatechart(file_path+"CardiacArrestCombo.sct", statechart);
			statechart.initExecutionPattern();
			//statechart.traceFailedPath_Constant(failed_path);
			System.out.println("Please input function option:");
			System.out.println("1. Show Constants or Implicit Constants in the Statechart");
			System.out.println("2. Show Single Variable Impaction");
			System.out.println("3. Show Single Variable Tree Structure");
			System.out.println("4. Show Implicit Execution Pattern");
			System.out.println("5. Show Possible Incorrect Execution Pattern");
			System.out.println("6. Replace Implicit Constants with Customized Method");
			System.out.println("7. Generate Fault Tree");
			Scanner scanner=new Scanner(System.in);
			while (true) {
				int option = scanner.nextInt();

				if (option == 1) {
					statechart.getUnChangedVariablesInfo();
				} else if(option==2){
					System.out.println("Please input the name of variable/event/operation:");
					String name=scanner.next();
					statechart.getSingleVariableImpactionInfo(name);
				}
				else if(option==3)
				{
					System.out.println("Please input the name of variable:");
					String name=scanner.next();
					Tree tree=new Tree(name);
					tree.initVariableImpactTreeStructure(statechart, name);
				}
				
				else if(option==4)
				{
					statechart.getExecutionPattern();
				}
				
				else if(option==5)
				{
					statechart.validateExecutionPattern();
				}
				else if(option==6)
				{
					System.out.println("Please input the name of variable you want to change to configurable variable:");
					String name=scanner.next();
					statechart.generateConfigurableStatechart(file_path, file_path+"CardiacArrestCombo.sct", name);
				}
				else if(option==7)
				{
					System.out.println("Please input the name of region you want to explore");
					String regionName=scanner.next();
					System.out.println("Please input the name of state you want to generate fault tree");
					String nodeName=scanner.next();
//					statechart.generateConfigurableStatechart(file_path, file_path+"CardiacArrestCombo.sct", name);
					String path="C:\\Users\\DaoDao\\Desktop\\statechart\\fault_tree.xml";
					statechart.generateFaultTree(path, regionName, nodeName);
				}
				
				System.out.println("Please input function option:");
				System.out.println("1. Show Constants or Implicit Constants in the Statechart");
				System.out.println("2. Show Single Variable Impaction");
				System.out.println("3. Show Single Variable Tree Structure");
				System.out.println("4. Show Implicit Execution Pattern");
				System.out.println("5. Show Possible Incorrect Execution Pattern");
				System.out.println("6. Replace Implicit Constants with Customized Method");
				System.out.println("7. Generate Fault Tree");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
	}
}
