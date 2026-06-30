package th.co.wacoal.atech.pcms2.utilities;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.JdbcUtils;

public class SqlStatementHandler {

	/**
	 * Drop-in replacement for database.queryList() that drops stale temp tables
	 * on the same connection BEFORE and AFTER running the SQL.
	 * Mirrors the PPMM2 SqlStatementHandler.queryList(JdbcTemplate, dropSql, sql) pattern.
	 *
	 * Drop-before: prevents "object already exists" if the connection was reused from pool
	 *              with leftover temp tables from a previous request.
	 * Drop-after:  returns the connection to the pool clean so the next request
	 *              does not encounter stale temp tables.
	 *
	 * NOTE: dropSql must NOT include #tempLotNoList/#tempUserStatusList/#tempCustomerList/
	 *       #tempCustomerShortList — those are created by PCMSSearchDaoImpl before this call.
	 */
	/**
	 * queryForList() ใช้ executeQuery() ซึ่ง expect result set เป็น TDS response แรก
	 * — ใช้ไม่ได้กับ batch ที่มี CREATE INDEX (DDL done packet ไม่ถูก suppress โดย SET NOCOUNT ON)
	 *
	 * แก้โดยใช้ stmt.execute() แล้ว iterate ผ่าน results จนเจอ result set แทน
	 */
	public static List<Map<String, Object>> queryList(JdbcTemplate jdbc, String dropSql, String sql) {
		return jdbc.execute((StatementCallback<List<Map<String, Object>>>) stmt -> {
			try { stmt.execute(dropSql); } catch (Exception ignored) {}
			List<Map<String, Object>> rows = new ArrayList<>();
			// backstop against a misbehaving driver only — NOT a segment limit.
			// Under SET NOCOUNT ON, DML done-packets are suppressed but DDL (CREATE INDEX/
			// TABLE, DROP) are NOT, so a large batch (e.g. searchByDetail builds ~30 temp
			// tables) emits far more than a few dozen segments before the final SELECT.
			// guard=50 truncated the walk → empty result. Keep it well above any real batch.
			int guard = 100000;
			try {
				boolean hasResult = stmt.execute("SET NOCOUNT ON;\n" + sql);
				while (guard-- > 0) {
					if (hasResult) {
						ResultSet rs = stmt.getResultSet();
						if (rs != null) {
							ResultSetMetaData meta = rs.getMetaData();
							int colCount = meta.getColumnCount();
							while (rs.next()) {
								Map<String, Object> row = new LinkedHashMap<>(colCount);
								for (int i = 1; i <= colCount; i++) {
									row.put(JdbcUtils.lookupColumnName(meta, i),
											JdbcUtils.getResultSetValue(rs, i));
								}
								rows.add(row);
							}
							break;
						}
					}
					if (stmt.getUpdateCount() == -1) break;
					hasResult = stmt.getMoreResults();
				}
			} finally {
				// reset session state + drop temp tables — must run even if main SQL throws
				try { stmt.execute("SET NOCOUNT OFF;"); } catch (Exception ignored) {}
				try { stmt.execute(dropSql); } catch (Exception ignored) {}
			}
			return rows;
		});
	}

	public final ThreadLocal<SimpleDateFormat> sdf1 = ThreadLocal.withInitial(() -> new SimpleDateFormat("dd.MM.yyyy"));
	public final ThreadLocal<SimpleDateFormat> sdf2 = ThreadLocal.withInitial(() -> new SimpleDateFormat("dd/MM/yyyy"));
	public final ThreadLocal<SimpleDateFormat> sdf3 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));
	public final ThreadLocal<SimpleDateFormat> sdf4 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
	public final ThreadLocal<SimpleDateFormat> sdf10 = ThreadLocal.withInitial(() -> new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
	public final ThreadLocal<SimpleDateFormat> sdf11 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
	public final ThreadLocal<SimpleDateFormat> sdf12 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	public final ThreadLocal<SimpleDateFormat> sdfFullDatetime = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));

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
			} else if (isValidDate(dateStr, this.sdf1.get())) {
				Date date = sdf1.get().parse(dateStr);
				prepared.setDate(index, this.convertJavaDateToSqlDate(date));
			} else if (isValidDate(dateStr, this.sdf2.get())) {
				Date date = sdf2.get().parse(dateStr);
				prepared.setDate(index, this.convertJavaDateToSqlDate(date));
			} else if (isValidDate(dateStr, this.sdf3.get())) {
				Date date = sdf3.get().parse(dateStr);
				prepared.setDate(index, this.convertJavaDateToSqlDate(date));
			} else if (isValidDate(dateStr, this.sdf4.get())) {
				Date date = sdf4.get().parse(dateStr);
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
			} else if (isValidDate(dateStr, this.sdf10.get())) {
				Date date = this.sdf10.get().parse(dateStr);
				prepared.setTimestamp(index, this.convertJavaDateToSqlTimestamp(date));
			} else if (isValidDate(dateStr, this.sdf11.get())) {
				Date date = this.sdf11.get().parse(dateStr);
				prepared.setTimestamp(index, this.convertJavaDateToSqlTimestamp(date));
			} else if (isValidDate(dateStr, this.sdf12.get())) {
				Date date = this.sdf12.get().parse(dateStr);
				prepared.setTimestamp(index, this.convertJavaDateToSqlTimestamp(date));
			} else if (isValidDate(dateStr, this.sdfFullDatetime.get())) {
				Date date = this.sdfFullDatetime.get().parse(dateStr);
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
