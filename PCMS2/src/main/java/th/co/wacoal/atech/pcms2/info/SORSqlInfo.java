/*
 * To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package th.co.wacoal.atech.pcms2.info;

import org.springframework.stereotype.Repository;

import th.in.totemplate.core.sql.DatabaseInfo;

@Repository
public class SORSqlInfo implements DatabaseInfo {
	////////////////////j/////////////////////////////////////////////////////////
	private static final String _driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//////////////////////////////////////////////////////////////////////////
	private static final String _url = "jdbc:sqlserver://10.11.44.220;database=SOR_PRODUCTION;useUnicode=true;characterEncoding=UTF-8;";
	private static final String _username = "sorpcms";
	private static final String _password = "SorToPcms@2";


	private static SORSqlInfo _info;
	private SORSqlInfo() {  }

	public static SORSqlInfo getInstance() {
		if (SORSqlInfo._info == null) {
			SORSqlInfo._info = new SORSqlInfo();
		}
		return SORSqlInfo._info;
	}

	@Override
	public String getDriver() {
		return SORSqlInfo._driver;
	}

	@Override
	public String getUrl() {
		return SORSqlInfo._url;
	}

	@Override
	public String getUsername() {
		return SORSqlInfo._username;
	}

	@Override
	public String getPassword() {
		return SORSqlInfo._password;
	}
}
