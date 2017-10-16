import java.util.Scanner;

public class Main {

	public static void main(String[] args)
	{
		String model_file_pre="C:\\Users\\DaoDao\\Desktop\\statechart\\";
		String failed_path="_wWau0JwjEeeYAr1R4dqAHw _XyDBIJwjEeeYAr1R4dqAHw _73A5oJwiEeeYAr1R4dqAHw _jjGC0pwiEeeYAr1R4dqAHw";
		Statechart statechart=new Statechart();
		SCParser sp=new SCParser();
		try {
			sp.readStatechart(model_file_pre+"CardiacArrestCombo.sct", statechart);
			//statechart.traceFailedPath_Constant(failed_path);
			System.out.println("Please input function option:");
			System.out.println("1. Show Constants or Implicit Constants in the Statechart");
			System.out.println("2. Show Single Variable Impaction");
			Scanner scanner=new Scanner(System.in);
			while (true) {
				int option = scanner.nextInt();

				if (option == 1) {
					statechart.getUnChangedVariablesInfo();
				} else {
					System.out.println("Please input the name of variable/event/operation:");
					String name=scanner.next();
					statechart.getSingleVariableImpactionInfo(name);
				}
				
				System.out.println("Please input function option:");
				System.out.println("1. Show Constants or Implicit Constants in the Statechart");
				System.out.println("2. Show Single Variable Impaction");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
	}
}
