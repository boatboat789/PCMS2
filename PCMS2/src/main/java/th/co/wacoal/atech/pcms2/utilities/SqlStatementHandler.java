package th.co.wacoal.atech.pcms2.utilities;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import th.in.totemplate.core.sql.Database;

public class SqlStatementHandler {

	/**
	 * Drop-in replacement for database.queryList() that drops stale temp tables
	 * on the same connection BEFORE and AFTER running the SQL.
	 * Mirrors the PPMM2 SqlStatementHandler.queryList(JdbcTemplate, dropSql, sql) pattern
	 * adapted for the legacy Database class used in PCMS2.
	 *
	 * Drop-before: prevents "object already exists" if the connection was reused from pool
	 *              with leftover temp tables from a previous request.
	 * Drop-after:  returns the connection to the pool clean so the next request
	 *              does not encounter stale temp tables.
	 *
	 * NOTE: dropSql must NOT include #tempLotNoList/#tempUserStatusList/#tempCustomerList/
	 *       #tempCustomerShortList — those are created by PCMSSearchDaoImpl before this call.
	 */
	public static List<Map<String, Object>> queryList(Database database, String dropSql, String sql) {
		// drop BEFORE — clear stale temp tables before SQL Server compiles the batch
		try (Statement cleanup = database.getConnection().createStatement()) {
			cleanup.execute(dropSql);
		} catch (Exception ignored) {}

		try {
			return database.queryList(sql);
		} finally {
			// drop AFTER — return connection to pool clean
			try (Statement cleanup = database.getConnection().createStatement()) {
				cleanup.execute(dropSql);
			} catch (Exception ignored) {}
		}
	}

	public SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMdd");

	public SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
	public SimpleDateFormat sdf10 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public SimpleDateFormat sdf11 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public SimpleDateFormat sdf12 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public SimpleDateFormat sdfFullDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public String addStringAndIfNotEmpty(String where)
	{
		if ( ! where.equals("")) {
			where += " and \r\n";
		}
		return where;
	}

	public String addStringOrIfNotEmpty(String where)
	{
		if ( ! where.equals("")) {
			where += " or \r\n";
		}
		return where;
	}

	public boolean checkIsValidDate(String date)
	{
		boolean bl_isDate = true;
		if (date == null) {
			bl_isDate = false;
		} else if (date.equals("undefined") || date.equals("") || ! isValidDate(date)) {
			bl_isDate = false;
		}
		return bl_isDate;
	}

	public boolean isValidDate(String inDate)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public static boolean isValidDate(String inDate, SimpleDateFormat dateFormat)
	{
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public java.sql.Date convertJavaDateToSqlDate(java.util.Date date)
	{
		return new java.sql.Date(date.getTime());
	}

	public java.sql.Timestamp convertJavaDateToSqlTimestamp(Date date)
	{
		return new java.sql.Timestamp(date.getTime());
	}

	public Date formatDate(String date, String initDateFormat, String endDateFormat) throws ParseException
	{
		Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
		SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
		String parsedDate = formatter.format(initDate);
		Date dateCheck = new SimpleDateFormat(endDateFormat).parse(parsedDate);
		return dateCheck;
	}

	public boolean isNumeric(String str)
	{
		if (str == null) {
			return false;
		}
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	public PreparedStatement setSqlBigDecimal(PreparedStatement prepared, BigDecimal val, int index) throws SQLException
	{
		try {
			if (val == null) {
				prepared.setNull(index, java.sql.Types.DECIMAL);
			} else if (val.equals("undefined") || val.equals("")) {
				prepared.setNull(index, java.sql.Types.DECIMAL);
			} else {
				prepared.setBigDecimal(index, val);
			}
		} catch (Exception e) {
			prepared.setNull(index, java.sql.Types.DECIMAL);
		}
		return prepared;
	}

	public PreparedStatement setSqlBigDecimal(PreparedStatement prepared, double val, int index) throws SQLException
	{
		try {
			prepared.setDouble(index, val);
		} catch (Exception e) {
			prepared.setNull(index, java.sql.Types.DECIMAL);
		}
		return prepared;
	}

	public PreparedStatement setSqlDouble(PreparedStatement prepared, String val, int index) throws SQLException
	{
		try {
//			System.out.println(val);
			if (val != null && ! val.trim().isEmpty()) {
				double doubleValue = Double.parseDouble(val.replace(",", "").trim()); // Remove commas and convert to double
				prepared.setDouble(index, doubleValue);
			} else {
				prepared.setNull(index, java.sql.Types.FLOAT); // Set NULL if value is empty
			}
		} catch (Exception e) {
			prepared.setNull(index, java.sql.Types.FLOAT); // Set NULL if conversion fails
		}
		return prepared;
	}

	public PreparedStatement setSqlBigDecimal(PreparedStatement prepared, String val, int index) throws SQLException
	{
		try {
			if (val == null) {
				prepared.setNull(index, java.sql.Types.DECIMAL);
			} else if (val.equals("undefined") || val.equals("")) {
				prepared.setNull(index, java.sql.Types.DECIMAL);
			} else {
				BigDecimal bigDecimal = new BigDecimal(val.replace(",", "").trim());
				prepared.setBigDecimal(index, bigDecimal);
			}
		} catch (Exception e) {
			prepared.setNull(index, java.sql.Types.DECIMAL);
		}
		return prepared;
	}

	public PreparedStatement setSqlDate(PreparedStatement prepared, Date dateStr, int index) throws SQLException
	{
		try {
			if (dateStr == null) {
				prepared.setNull(index, java.sql.Types.DATE);
			} else {
				prepared.setDate(index, this.convertJavaDateToSqlDate(dateStr));
			}
		} catch (Exception e) {
			prepared.setNull(index, java.sql.Types.DATE);
		}
		return prepared;
	}

	public PreparedStatement setSqlDate(PreparedStatement prepared, String dateStr, int index) throws SQLException
	{
		try {
			if (dateStr == null) {
				prepared.setNull(index, java.sql.Types.DATE);
			} else if (dateStr.equals("undefined") || dateStr.equals("")) {
				prepared.setNull(index, java.sql.Types.DATE);
			} else if (isValidDate(dateStr, this.sdf1)) {
				Date date = sdf1.parse(dateStr);
				prepared.setDate(index, this.convertJavaDateToSqlDate(date));
			} else if (isValidDate(dateStr, this.sdf2)) {
				Date date = sdf2.parse(dateStr);
				prepared.setDate(index, this.convertJavaDateToSqlDate(date));
			} else if (isValidDate(dateStr, this.sdf3)) {
				Date date = sdf3.parse(dateStr);
				prepared.setDate(index, this.convertJavaDateToSqlDate(date));
			} else if (isValidDate(dateStr, this.sdf4)) {
				Date date = sdf4.parse(dateStr);
				prepared.setDate(index, this.convertJavaDateToSqlDate(date));
			} else {
				prepared.setNull(index, java.sql.Types.DATE);
			}
		} catch (Exception e) {
			prepared.setNull(index, java.sql.Types.DATE);
		}
		return prepared;
	}

	public PreparedStatement setSqlInt(PreparedStatement prepared, int value, int index) throws SQLException
	{
		try {
			if (value == 0) {
				prepared.setNull(index, java.sql.Types.INTEGER);
			} else {
				prepared.setInt(index, value);
			}
		} catch (Exception e) {
			prepared.setNull(index, java.sql.Types.INTEGER);
		}
		return prepared;
	}

	public PreparedStatement setSqlString(PreparedStatement prepared, String value, int index) throws SQLException
	{
		try {
			if (value == null) {
				prepared.setNull(index, java.sql.Types.VARCHAR);
			} else if (value.equals("")) {
				prepared.setNull(index, java.sql.Types.VARCHAR);
			} else {
				prepared.setString(index, value);
			}
		} catch (Exception e) {
			prepared.setNull(index, java.sql.Types.VARCHAR);
		}
		return prepared;
	}

	public PreparedStatement setSqlTime(PreparedStatement prepared, String timeString, int index)
			throws SQLException, ParseException
	{

		if (timeString == null || timeString.equals("undefined") || timeString.equals("")) {
			prepared.setNull(index, java.sql.Types.TIME);
		} else {
			Time time = Time.valueOf(timeString);
			prepared.setTime(index, time);
		}
		return prepared;
	}

	public PreparedStatement setSqlTimeStamp(PreparedStatement prepared, String dateStr, int index) throws SQLException
	{
		try {
			if (dateStr == null) {
				prepared.setNull(index, java.sql.Types.DATE);
			} else if (dateStr.equals("undefined") || dateStr.equals("")) {
				prepared.setNull(index, java.sql.Types.DATE);
			} else if (dateStr.equals("01/01/0001 00:00:00")) {
				prepared.setNull(index, java.sql.Types.DATE);
			} else if (isValidDate(dateStr, this.sdf10)) {
				Date date = this.sdf10.parse(dateStr);
				prepared.setTimestamp(index, this.convertJavaDateToSqlTimestamp(date));
			} else if (isValidDate(dateStr, this.sdf11)) {
				Date date = this.sdf11.parse(dateStr);
				prepared.setTimestamp(index, this.convertJavaDateToSqlTimestamp(date));
			} else if (isValidDate(dateStr, this.sdf12)) {
				Date date = this.sdf12.parse(dateStr);
				prepared.setTimestamp(index, this.convertJavaDateToSqlTimestamp(date));
			} else if (isValidDate(dateStr, this.sdfFullDatetime)) {
				Date date = this.sdfFullDatetime.parse(dateStr);
				prepared.setTimestamp(index, this.convertJavaDateToSqlTimestamp(date));
			} else {
				prepared.setNull(index, java.sql.Types.DATE);
			}
		} catch (Exception e) {
			prepared.setNull(index, java.sql.Types.DATE);
		}
		return prepared;
	}

	@SuppressWarnings("unlikely-arg-type")
	public PreparedStatement setSqlTimeStamp(PreparedStatement prepared, Date dateStr, int index) throws SQLException
	{
		try {
			if (dateStr == null) {
				prepared.setNull(index, java.sql.Types.TIMESTAMP);
			} else if (dateStr.equals("undefined") || dateStr.equals("")) {
				prepared.setNull(index, java.sql.Types.TIMESTAMP);
			} else {
				prepared.setTimestamp(index, this.convertJavaDateToSqlTimestamp(dateStr));
			}
		} catch (Exception e) {
			prepared.setNull(index, java.sql.Types.TIMESTAMP);
		}
		return prepared;
	}

	public String formatDateTime(Timestamp timestamp1)
	{ // Convert Timestamp to LocalDateTime
		LocalDateTime localDateTime = timestamp1.toLocalDateTime();
		// Convert LocalDateTime to UTC (Z) format
		return localDateTime.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
	}

}
