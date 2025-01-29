/*
 * To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package th.co.wacoal.atech.pcms2.info;

import th.in.totemplate.core.sql.DatabaseInfo;

public class SqlInfo implements DatabaseInfo {
	////////////////////j/////////////////////////////////////////////////////////
	private static final String _driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//////////////////////////////////////////////////////////////////////////
	private static final String _url = "jdbc:sqlserver://10.11.44.101;database=PCMS;useUnicode=true;characterEncoding=UTF-8;sendStringParametersAsUnicode=false;";
	private static final String _username = "pcmspgm";
	private static final String _password = "p@sspgm";
	
////  DEV
//	private static final String _url = "jdbc:sqlserver://10.11.44.101\\PCMSDEV:1435;database=PCMS;useUnicode=true;characterEncoding=UTF-8;";
//	private static final String _username = "94687";
//	private static final String _password = "Atech123";
////
//	private static final String _url = "jdbc:sqlserver://10.11.44.101;database=PCMS;useUnicode=true;characterEncoding=UTF-8;sendStringParametersAsUnicode=false;";
//	private static final String _username = "94687";
//	private static final String _password = "Boat789";

//	private static final String _url = "jdbc:sqlserver://localhost:1433;database=PCMS;integratedSecurity=true;sendStringParametersAsUnicode=false;";
//	private static final String _username = " ";
//	private static final String _password = " ";
//
	private static SqlInfo _info;
	private SqlInfo() {  }

	public static SqlInfo getInstance() {
		if (SqlInfo._info == null) {
			SqlInfo._info = new SqlInfo();
		}
		return SqlInfo._info;
	}

	@Override
	public String getDriver() {
		return SqlInfo._driver;
	}

	@Override
	public String getUrl() {
		return SqlInfo._url;
	}

	@Override
	public String getUsername() {
		return SqlInfo._username;
	}

	@Override
	public String getPassword() {
		return SqlInfo._password;
	}
}
