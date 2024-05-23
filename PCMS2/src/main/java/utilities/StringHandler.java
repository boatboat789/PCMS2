package utilities;

public class StringHandler {

	private StringHandler() {

	}
	@SuppressWarnings("unused")
	private String removeLastChar(String s)    {
		return s.substring(0, s.length() - 1);
	}
}
