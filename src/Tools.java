import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
	public static List<String> getAllMatches(String text, String regex) {
		text=text.replaceAll("\r", " ");
		//System.out.println(text);
        List<String> matches = new ArrayList<String>();
        Matcher m = Pattern.compile("(?=(" + regex + "))").matcher(text);
        while(m.find()) {
        	   String temp=m.group(1);
        	   //System.out.println(temp);
            matches.add(m.group(1));
        }
        return matches;
    }
}
