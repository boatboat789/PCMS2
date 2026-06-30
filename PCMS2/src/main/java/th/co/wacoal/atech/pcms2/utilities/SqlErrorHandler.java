package th.co.wacoal.atech.pcms2.utilities;

public class SqlErrorHandler {
	public static String handlerSqlErrorText(String iconStatus)
	{
		String systemStatus = "";
		if (iconStatus.equals("I")) {
			systemStatus = "อัพเดตข้อมูลสำเร็จ";
		} else {
			systemStatus = "เกิดข้อผิดพลาด / กรุณาติดต่อทีม IT";
		}
		return systemStatus;
	}
}
