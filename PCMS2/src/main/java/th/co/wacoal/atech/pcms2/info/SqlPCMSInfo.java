/*
 * To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package th.co.wacoal.atech.pcms2.info;

import org.springframework.stereotype.Repository;

import th.in.totemplate.core.sql.DatabaseInfo;

@Repository
public class SqlPCMSInfo implements DatabaseInfo {
	////////////////////j/////////////////////////////////////////////////////////
	private static final String _driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//////////////////////////////////////////////////////////////////////////
//	private static final String _url = "jdbc:sqlserver://10.11.44.101;database=PCMS;useUnicode=true;characterEncoding=UTF-8;sendStringParametersAsUnicode=false;";
//	private static final String _username = "pcmspgm";
//	private static final String _password = "p@sspgm";

	////
		private static final String _url = "jdbc:sqlserver://10.11.44.101;database=PCMS;useUnicode=true;characterEncoding=UTF-8;sendStringParametersAsUnicode=false;";
		private static final String _username = "94687";
		private static final String _password = "Boat789";
////  DEV
//	private static final String _url = "jdbc:sqlserver://10.11.44.101\\PCMSDEV:1435;database=PCMS;useUnicode=true;characterEncoding=UTF-8;";
//	private static final String _username = "94687";
//	private static final String _password = "Atech123";

//	private static final String _url = "jdbc:sqlserver://localhost:1433;database=PCMS;integratedSecurity=true;sendStringParametersAsUnicode=false;";
//	private static final String _username = " ";
//	private static final String _password = " ";
//
	private static SqlPCMSInfo _info;
	private SqlPCMSInfo() {  }

	public static SqlPCMSInfo getInstance() {
		if (SqlPCMSInfo._info == null) {
			SqlPCMSInfo._info = new SqlPCMSInfo();
		}
		return SqlPCMSInfo._info;
	}

	@Override
	public String getDriver() {
		return SqlPCMSInfo._driver;
	}

	@Override
	public String getUrl() {
		return SqlPCMSInfo._url;
	}

	@Override
	public String getUsername() {
		return SqlPCMSInfo._username;
	}

	@Override
	public String getPassword() {
		return SqlPCMSInfo._password;
	}
}
