/*
 * To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package th.co.wacoal.atech.pcms2.info;

import org.springframework.stereotype.Repository;

import th.in.totemplate.core.sql.DatabaseInfo;

@Repository
public class SqlSORInfo implements DatabaseInfo {
	////////////////////j/////////////////////////////////////////////////////////
	private static final String _driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//////////////////////////////////////////////////////////////////////////
	private static final String _url = "jdbc:sqlserver://10.11.44.220;database=SOR_PRODUCTION;useUnicode=true;characterEncoding=UTF-8;";
	private static final String _username = "sorpcms";
	private static final String _password = "SorToPcms@2";


	private static SqlSORInfo _info;
	private SqlSORInfo() {  }

	public static SqlSORInfo getInstance() {
		if (SqlSORInfo._info == null) {
			SqlSORInfo._info = new SqlSORInfo();
		}
		return SqlSORInfo._info;
	}

	@Override
	public String getDriver() {
		return SqlSORInfo._driver;
	}

	@Override
	public String getUrl() {
		return SqlSORInfo._url;
	}

	@Override
	public String getUsername() {
		return SqlSORInfo._username;
	}

	@Override
	public String getPassword() {
		return SqlSORInfo._password;
	}
}
