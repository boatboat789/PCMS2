package th.co.wacoal.atech.pcms2.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.PCMSSearchDao;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class PCMSSearchDaoImpl implements PCMSSearchDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New 
	private Database database;
	private String message; 
	@Autowired
	public PCMSSearchDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage()
	{
		return this.message;
	} 
	@Override
	public String handlerTempTableCustomerSearchList(List<String> customerNameList, List<String> customerShortNameList)
	{
		StringBuilder sql = new StringBuilder();
		// Step 1: สร้าง CREATE TABLE statements
		sql.append("If(OBJECT_ID('tempdb..#tempCustomerList') Is Not Null) begin Drop Table #tempCustomerList  end ;\n");
		sql.append("If(OBJECT_ID('tempdb..#tempCustomerShortList') Is Not Null) begin Drop Table #tempCustomerShortList  end ; ");
		sql.append("CREATE TABLE #tempCustomerList (CustomerName NVARCHAR(500));\n");
		sql.append("CREATE TABLE #tempCustomerShortList (CustomerShortName NVARCHAR(500));\n");
		if ( ! customerNameList.isEmpty()) {
			sql.append("INSERT INTO #tempCustomerList (CustomerName) VALUES\n");
			for (int i = 0; i < customerNameList.size(); i ++ ) {
				sql.append("('").append(customerNameList.get(i).replace("'", "''")).append("')");
				if (i < customerNameList.size()-1) {
					sql.append(",\n");
				} else {
					sql.append(";\n");
				}
			}
		}
		// Step 3: เติมข้อมูลจาก customerShortNameList
		if ( ! customerShortNameList.isEmpty()) {
			sql.append("INSERT INTO #tempCustomerShortList (CustomerShortName) VALUES\n");
			for (int i = 0; i < customerShortNameList.size(); i ++ ) {
				sql.append("('").append(customerShortNameList.get(i).replace("'", "''")).append("')");
				if (i < customerShortNameList.size()-1) {
					sql.append(",\n");
				} else {
					sql.append(";\n");
				}
			}
		}

		// Return SQL string
		return sql.toString();
	}
	@Override
	public void handlerCloseTempTableCustomerSearchList()
	{

		PreparedStatement prepared = null;
		Connection connection;
		connection = this.database.getConnection();
		// TODO Auto-generated method stub
		String sqlCreateTempTable = ""
				+ " If(OBJECT_ID('tempdb..#tempCustomerList') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempCustomerList \r\n"
				+ "	end ; \r\n"
				+ " If(OBJECT_ID('tempdb..#tempCustomerShortList') Is Not Null)\r\n"
				+ "	begin\r\n"
				+ "		Drop Table #tempCustomerShortList\r\n"
				+ "	end ; ";
		try {
			prepared = connection.prepareStatement(sqlCreateTempTable);
			// Step 1: สร้าง temp table
			prepared.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
}
