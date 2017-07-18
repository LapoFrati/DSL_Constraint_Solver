package parsing;

public class ParseError extends Exception {
	private static final long serialVersionUID = 1L;
	ParseError(String message) {
        super(message);
    }
}
