/*
` * To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package th.co.wacoal.atech.pcms2.info;

import org.springframework.stereotype.Repository;

import th.in.totemplate.core.sql.DatabaseInfo;

@Repository
public class SqlAtechERPInfo implements DatabaseInfo {
	//////////////////// j/////////////////////////////////////////////////////////
	private static final String _driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//////////////////////////////////////////////////////////////////////////
// 	PRD
	private static final String _url = "jdbc:sqlserver://10.11.44.219\\ATECHERP:1433;database=ERP_PROD;useUnicode=true;characterEncoding=UTF-8;";
	private static final String _username = "nonerp"; 
	private static final String _password = "ovoVuvkiNruFxifyd=yjo";
// 	QAS
//	private static final String _url = "jdbc:sqlserver://10.11.44.219\\ATECHERP:1433;database=ERP_QAS;useUnicode=true;characterEncoding=UTF-8;";
//	private static final String _username = "nonerpqas";
//	private static final String _password = "ovoVuvkiNru8b;gvgvl";
// 	DEV
//	private static final String _url = "jdbc:sqlserver://10.11.44.219\\ATECHERP:1433;database=ERP_DEV;useUnicode=true;characterEncoding=UTF-8;";
//	private static final String _username = "nonerpdev";
//	private static final String _password = "ovoVuvkiNrugfa";
////    
	private static SqlAtechERPInfo _info;

	private SqlAtechERPInfo() {
	}

	public static SqlAtechERPInfo getInstance()
	{
		if (SqlAtechERPInfo._info == null) {
			SqlAtechERPInfo._info = new SqlAtechERPInfo();
		}
		return SqlAtechERPInfo._info;
	}

	@Override
	public String getDriver()
	{
		return SqlAtechERPInfo._driver;
	}

	@Override
	public String getUrl()
	{
		return SqlAtechERPInfo._url;
	}

	@Override
	public String getUsername()
	{
		return SqlAtechERPInfo._username;
	}

	@Override
	public String getPassword()
	{
		return SqlAtechERPInfo._password;
	}
}
