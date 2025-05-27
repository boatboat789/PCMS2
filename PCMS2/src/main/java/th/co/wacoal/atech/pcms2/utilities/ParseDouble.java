package th.co.wacoal.atech.pcms2.utilities;

public class ParseDouble {

	public static Double tryParseDouble(String doubleVal) {
		double dbVal = 0;
		try {
			dbVal = Double.parseDouble(doubleVal.replace(",", ""));
		} catch (NumberFormatException e) {
			dbVal = 0;
		}
		return dbVal;
	}
}
