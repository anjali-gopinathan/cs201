//import com.sun.tools.javac.util.List;
import java.util.*;
public class HelloCS201 {
	public static void main(String[] args) {
		System.out.println("hi CS 201!");
		List<String> words = new ArrayList<String>();
		words.add("alligator");
		words.add("zebra");
		for(int i=0; i<words.size(); i++) {
			System.out.println(i + ": " + words.get(i));
		}
	}
}
