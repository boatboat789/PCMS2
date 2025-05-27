package th.co.wacoal.atech.pcms2.service;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateDeserializer implements JsonDeserializer<Date> {

	private static final String DATE_FORMAT = "MMM d, yyyy"; // Adjust as needed

	@Override
	public Date deserialize(JsonElement json, Type typeOfT, com.google.gson.JsonDeserializationContext context)
			throws JsonParseException
	{
		String dateStr = json.getAsString();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			throw new JsonParseException("Failed to parse date: " + dateStr, e);
		}
	}
}