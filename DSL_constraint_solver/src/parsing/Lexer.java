package parsing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parsing.Token.TokenType;

public class Lexer {
	String path;
	public Queue<Token> lex() throws IOException{
		String input = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		ArrayDeque<Token> tokens = new ArrayDeque<Token>();
		
		StringBuffer tokenPatternsBuffer = new StringBuffer();
		  for (TokenType tokenType : TokenType.values())
		    tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
		Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));
		
		Matcher matcher = tokenPatterns.matcher(input);
		while (matcher.find()) {
			for(TokenType tokenType : TokenType.values()){
				if (matcher.group(tokenType.name()) != null) {
					tokens.add(new Token(tokenType, matcher.group(tokenType.name())));
					continue;
				}
			}
		}
		//for (TokenType tokenType : TokenType.values())
		//	  System.out.println(tokenType.name());
		
		System.out.println(tokens);
		
		return tokens;
	}
	public Lexer(String path){
		this.path = path;
	}
}
