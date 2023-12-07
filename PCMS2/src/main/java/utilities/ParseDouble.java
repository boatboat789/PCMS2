package utilities;

public class ParseDouble {

	public Double tryParseDouble(String doubleVal) { 
		double dbVal = 0;
		try {
			dbVal = Double.parseDouble(doubleVal);
		}
		catch(NumberFormatException e) { 
			dbVal = 0 ;
		}
		return dbVal;
	}
}
