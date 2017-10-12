
public class test {

	
	public static void main(String[] args)
	{
		String te="entry/c-m";
		String[] result=te.split("[-+*/^%!]");
		for(String s:result)
		{
			System.out.println(s);
		}
		
	}
}
