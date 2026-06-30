package th.co.wacoal.atech.pcms2.utilities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class FormatUtils {
	// SimpleDateFormat/DecimalFormat ไม่ thread-safe — ใช้ ThreadLocal กัน race ตอนหลาย request พร้อมกัน
	// Date formatters with descriptive names
	public static final ThreadLocal<SimpleDateFormat> DATE_ONLY_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy/MM/dd"));
	public static final ThreadLocal<SimpleDateFormat> DAY_MONTH_YEAR_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("dd/MM/yyyy"));
	public static final ThreadLocal<SimpleDateFormat> COMPACT_DATE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));
	public static final ThreadLocal<SimpleDateFormat> STANDARD_DATE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
	public static final ThreadLocal<SimpleDateFormat> DAY_MONTH_YEAR_TIME_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
	public static final ThreadLocal<SimpleDateFormat> DATE_TIME_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy/MM/dd HH:mm"));
	public static final ThreadLocal<SimpleDateFormat> DATE_TIME_SECONDS_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
	public static final ThreadLocal<SimpleDateFormat> STANDARD_DATE_TIME_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	public static final ThreadLocal<SimpleDateFormat> TIME_ONLY_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("HH:mm:ss"));
	public static final ThreadLocal<SimpleDateFormat> FULL_DATE_TIME_MILLIS_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));

	// Number formatters with descriptive names
	public static final ThreadLocal<DecimalFormat> FOUR_DECIMAL_FORMAT = ThreadLocal.withInitial(() -> new DecimalFormat("0.0000"));
	public static final ThreadLocal<DecimalFormat> WHOLE_NUMBER_FORMAT = ThreadLocal.withInitial(() -> new DecimalFormat("###,###,##0"));
	public static final ThreadLocal<DecimalFormat> TWO_DECIMAL_FORMAT = ThreadLocal.withInitial(() -> new DecimalFormat("###,###,##0.00"));
	public static final ThreadLocal<DecimalFormat> FOUR_DECIMAL_WITH_COMMAS = ThreadLocal.withInitial(() -> new DecimalFormat("###,###,##0.0000"));

	// Private constructor to prevent instantiation
	private FormatUtils() {
	}

	// ========== DATE FORMATTING METHODS ==========

	public static String formatDateOnly(Date date)
	{
		return date != null ? DATE_ONLY_FORMAT.get().format(date) : "";
	}

	public static String formatDayMonthYear(Date date)
	{
		return date != null ? DAY_MONTH_YEAR_FORMAT.get().format(date) : "";
	}

	public static String formatCompactDate(Date date)
	{
		return date != null ? COMPACT_DATE_FORMAT.get().format(date) : "";
	}

	public static String formatStandardDate(Date date)
	{
		return date != null ? STANDARD_DATE_FORMAT.get().format(date) : "";
	}

	public static String formatDayMonthYearTime(Date date)
	{
		return date != null ? DAY_MONTH_YEAR_TIME_FORMAT.get().format(date) : "";
	}

	public static String formatDateTime(Date date)
	{
		return date != null ? DATE_TIME_FORMAT.get().format(date) : "";
	}

	public static String formatDateTimeWithSeconds(Date date)
	{
		return date != null ? DATE_TIME_SECONDS_FORMAT.get().format(date) : "";
	}

	public static String formatStandardDateTime(Date date)
	{
		return date != null ? STANDARD_DATE_TIME_FORMAT.get().format(date) : "";
	}

	public static String formatTimeOnly(Date date)
	{
		return date != null ? TIME_ONLY_FORMAT.get().format(date) : "";
	}

	public static String formatFullDateTimeWithMillis(Date date)
	{
		return date != null ? FULL_DATE_TIME_MILLIS_FORMAT.get().format(date) : "";
	}

	// ========== NUMBER FORMATTING METHODS ==========

	public static String formatWithFourDecimals(Number number)
	{
		return number != null ? FOUR_DECIMAL_FORMAT.get().format(number) : "";
	}

	public static String formatWholeNumber(Number number)
	{
		return number != null ? WHOLE_NUMBER_FORMAT.get().format(number) : "";
	}

	public static String formatWithTwoDecimals(Number number)
	{
		return number != null ? TWO_DECIMAL_FORMAT.get().format(number) : "";
	}

	public static String formatWithFourDecimalsAndCommas(Number number)
	{
		return number != null ? FOUR_DECIMAL_WITH_COMMAS.get().format(number) : "";
	}

	// ========== MAP EXTRACTION METHODS ==========

	public static String getDateOnlyFromMap(Map<String, Object> map, String key)
	{
		return getFormattedDateFromMap(map, key, DATE_ONLY_FORMAT.get(), "");
	}

	public static String getDayMonthYearFromMap(Map<String, Object> map, String key)
	{
		return getFormattedDateFromMap(map, key, DAY_MONTH_YEAR_FORMAT.get(), "");
	}

	// Generic value extraction with default
	@SuppressWarnings("unchecked")
	public <T> T getValue(Map<String, Object> map, String key, T defaultValue)
	{
		if (map == null || ! map.containsKey(key) || map.get(key) == null) {
			return defaultValue;
		}
		try {
			return (T) map.get(key);
		} catch (ClassCastException e) {
			return defaultValue;
		}
	}

	// Formatted date extraction
	public static String getFormattedDateFromMap(Map<String, Object> map, String key, SimpleDateFormat formatter,
			String defaultStr)
	{
		if (map == null || ! map.containsKey(key) || map.get(key) == null) {
			return defaultStr;
		}

		try {
			Object value = map.get(key);
			if (value instanceof Timestamp) {
				return formatter.format((Timestamp) value);
			} else if (value instanceof Date) {
				return formatter.format((Date) value);
			}
			return defaultStr;
		} catch (Exception e) {
			return defaultStr;
		}
	}

	// Formatted number extraction
	public String getFormattedNumber(Map<String, Object> map, String key, DecimalFormat formatter, String defaultStr)
	{
		if (map == null || ! map.containsKey(key) || map.get(key) == null) {
			return defaultStr;
		}

		try {
			Object value = map.get(key);
			if (value instanceof BigDecimal) {
				return formatter.format(((BigDecimal) value).doubleValue());
			} else if (value instanceof Number) {
				return formatter.format(((Number) value).doubleValue());
			} else if (value instanceof String) {
				try {
					double num = Double.parseDouble((String) value);
					return formatter.format(num);
				} catch (NumberFormatException e) {
					return "";
				}
			}
			return defaultStr;
		} catch (Exception e) {
			return defaultStr;
		}
	}

}
