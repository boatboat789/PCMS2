package utilities;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlStatementHandler {

	public SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

	public PreparedStatement setSqlInt(PreparedStatement prepared, int value, int index) {
		try {
			if (value == 0 ) {
				prepared.setNull(index, java.sql.Types.INTEGER);
			} else {
				prepared.setInt(index, value);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prepared;
	}
	public PreparedStatement setSqlBigDecimal(PreparedStatement prepared, BigDecimal val, int index ) throws SQLException {
		try {  
			if (val == null || val.equals("undefined") || val.equals("") ) {
				prepared.setNull(index, java.sql.Types.DECIMAL);
			} 
			else {   
				prepared.setBigDecimal(index, val);
			} 
		}
		catch(Exception e) {
			prepared.setNull(index, java.sql.Types.DECIMAL);
		}
		return prepared;
	} 
	public PreparedStatement setSqlDate(PreparedStatement prepared, String dateStr, int index)
			throws SQLException, ParseException {
		if (dateStr == null || dateStr.equals("undefined") || dateStr.equals("") ) {
			prepared.setNull(index, java.sql.Types.DATE);
		} 
		else if (isValidDate(dateStr,this.sdf1)){ 
			Date date = sdf1.parse(dateStr);	
			prepared.setDate(index, this.convertJavaDateToSqlDate(date));
		}	
		else if (isValidDate(dateStr,this.sdf2)){ 
			Date date = sdf2.parse(dateStr);
			prepared.setDate(index, this.convertJavaDateToSqlDate(date));
		}
		else { 
			prepared.setNull(index, java.sql.Types.DATE);
		}
		return prepared;
	}

	public PreparedStatement setSqlBigDecimal(PreparedStatement prepared, String val, int index) throws SQLException {
		try {
			if (val == null || val.equals("undefined") || val.equals("") ) {
				prepared.setNull(index, java.sql.Types.DECIMAL);
			} 
			else {  
				BigDecimal bigDecimal = new BigDecimal(val.replace(",", "")); 
				prepared.setBigDecimal(index, bigDecimal);
			}	 
		}
		catch(Exception e) {
			prepared.setNull(index, java.sql.Types.DECIMAL);
		}
		return prepared;
	}
	public PreparedStatement setSqlString(PreparedStatement prepared, String value, int index) {
		try {
			if (value.equals("")|| value == null) {
				prepared.setNull(index, java.sql.Types.VARCHAR);
			} else {
				prepared.setString(index, value);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prepared;
	}

	public boolean checkIsValidDate(String date) {
		boolean bl_isDate = true;
		if (date == null || date.equals("undefined") || date.equals("") || !isValidDate(date)) {
			bl_isDate = false;
		}
		return bl_isDate;
	}

	public String addStringOrIfNotEmpty(String where) {
		if (!where.equals("")) {
			where += " or \r\n";
		}
		return where;
	}

	public String addStringAndIfNotEmpty(String where) {
		if (!where.equals("")) {
			where += " and \r\n";
		}
		return where;
	}

	public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}

	public Date formatDate(String date, String initDateFormat, String endDateFormat) throws ParseException {
		Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
		SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
		String parsedDate = formatter.format(initDate);
		Date dateCheck = new SimpleDateFormat(endDateFormat).parse(parsedDate);
		return dateCheck;
	}

	public boolean isValidDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public static boolean isValidDate(String inDate, SimpleDateFormat dateFormat) {
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public boolean isNumeric(String str) {
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

	public Double tryParseDouble(String doubleVal) {
		double dbVal = 0;
		try {
			doubleVal = doubleVal.replaceAll(",","");
			dbVal = Double.parseDouble(doubleVal);
		} catch (NumberFormatException e) {
			dbVal = 0;
		}
		return dbVal;
	}
}
