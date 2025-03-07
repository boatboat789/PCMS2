/*
 * To change this license header, choose License Headers in Pro	ject Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package th.co.wacoal.atech.pcms2.info;

import org.springframework.stereotype.Repository;

import th.in.totemplate.core.sql.DatabaseInfo;

@Repository
public class SqlPPMMInfo implements DatabaseInfo {
	//////////////////// j/////////////////////////////////////////////////////////
	private static final String _driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//////////////////////////////////////////////////////////////////////////
//  PRD
//	private static final String _url =  "jdbc:sqlserver://10.11.44.101;database=PPMM;useUnicode=true;characterEncoding=UTF-8;";
//	private static final String _username = "94687";
//	private static final String _password = "Boat789";
////	
//	private static final String _url = "jdbc:sqlserver://10.11.44.101;database=PPMM;useUnicode=true;characterEncoding=UTF-8;";
//	private static final String _username = "ppmmpgm";
//	private static final String _password = "1234";

////  DEV
	private static final String _url = "jdbc:sqlserver://10.11.44.101\\PCMSDEV:1435;database=PPMM;useUnicode=true;characterEncoding=UTF-8;";
	private static final String _username = "94687";
	private static final String _password = "Atech123"; 
	private static SqlPPMMInfo _info;

	public static SqlPPMMInfo getInstance() {
		if (SqlPPMMInfo._info == null) {
			SqlPPMMInfo._info = new SqlPPMMInfo();
		}
		return SqlPPMMInfo._info;
	}

	private SqlPPMMInfo() {
	}

	@Override
	public String getDriver() {
		return SqlPPMMInfo._driver;
	}

	@Override
	public String getPassword() {
		return SqlPPMMInfo._password;
	}

	@Override
	public String getUrl() {
		return SqlPPMMInfo._url;
	}

	@Override
	public String getUsername()
	{
		return SqlPPMMInfo._username;
	}
}
