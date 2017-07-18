package parsing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	public static Queue<String> tokenize(String path) throws IOException{
		String input = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		ArrayDeque<String> tokens = new ArrayDeque<String>();
		// tokens := { | } | , | = | x | x1 | \n | ( | ) ! 
		Pattern pattern = Pattern.compile("\\{|\\}|,|=|[a-z][0-9]?|\n|\\(|\\)|\\!");
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
		    tokens.add(matcher.group());
		}
		return tokens;
	}
}
