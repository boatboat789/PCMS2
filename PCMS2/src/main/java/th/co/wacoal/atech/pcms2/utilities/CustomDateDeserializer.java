package th.co.wacoal.atech.pcms2.utilities;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.ParsePosition;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class CustomDateDeserializer implements JsonDeserializer<Date> {

	private static final String DATE_FORMAT = "MMM d, yyyy"; // Adjust as needed

	@Override
	public Date deserialize(JsonElement json, Type typeOfT, com.google.gson.JsonDeserializationContext context)
			throws JsonParseException
	{
		if (json == null || json.isJsonNull()) {
			return null;
		}
		String dateStr = json.getAsString() == null ? null : json.getAsString().replace('\u202f', ' ').replace('\u00a0', ' '); // JDK 20+ ใช้ช่องว่างพิเศษก่อน AM/PM
		if (dateStr == null || dateStr.trim().isEmpty()) {
			return null;
		}
		// Gson serialize วันที่ด้วย Locale.US ("MMM d, yyyy" หรือ "MMM d, yyyy h:mm:ss a") — ต้อง parse ด้วย Locale.US เหมือนกัน
		// และไม่สร้าง SimpleDateFormat ซ้ำ/ไม่ทิ้งส่วนเวลาเงียบๆ (SimpleDateFormat ไม่ thread-safe จึงสร้างต่อครั้ง แต่ลองทีละรูปแบบ)
		String[] formats = { "MMM d, yyyy, h:mm:ss a", "MMM d, yyyy h:mm:ss a", DATE_FORMAT }; // JDK 8 ไม่มีจุลภาคหลังปี, JDK 9+ มี
		for (String f : formats) {
			SimpleDateFormat sdf = new SimpleDateFormat(f, Locale.US);
			sdf.setLenient(false);
			ParsePosition pos = new ParsePosition(0);
			Date d = sdf.parse(dateStr.trim(), pos);
			if (d != null && pos.getIndex() == dateStr.trim().length()) {
				return d;
			}
		}
		throw new JsonParseException("Failed to parse date: " + dateStr);
	}
}