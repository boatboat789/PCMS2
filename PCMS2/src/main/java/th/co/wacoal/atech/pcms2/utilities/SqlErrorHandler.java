package th.co.wacoal.atech.pcms2.utilities;

public class SqlErrorHandler {
	public static String handlerSqlErrorText(String iconStatus) {
		String  systemStatus = "";
		if(iconStatus.equals("I")) {
			systemStatus = "Update Success.";
		}
		else {
			systemStatus = "Something happen! Please contact IT.";
		}
		return systemStatus;
	}
}
