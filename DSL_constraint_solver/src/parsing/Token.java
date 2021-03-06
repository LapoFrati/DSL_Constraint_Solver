package parsing;

class Token{
	public static enum TokenType {
	    // Token types cannot have underscores
		VALUE("[a-z][0-9]"),
		VAR("[a-z]"),
	    CURLYOPEN("\\{"),
	    CURLYCLOSE("\\}"),
	    EQUAL("="),
		NOT("\\!"),
		COMMA(","),
		PARENOPEN("\\("),
		PARENCLOSE("\\)"),
		NEWLINE("\n");

	    public final String pattern;

	    private TokenType(String pattern) {
	      this.pattern = pattern;
	    }
	}
	
	private TokenType type;
	private String data;
	
	public Token(TokenType type, String data) {
	  this.type = type;
	  this.data = data;
	}
	
	@Override
	public String toString() {
	  return String.format("(%s %s)", type.name(), data);
	}
	
	public boolean is(TokenType type){ return this.type.equals(type); }
	
	public String get(){ return this.data; }

}