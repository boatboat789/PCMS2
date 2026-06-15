package th.co.wacoal.atech.pcms2.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.PCMSSearchDao;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class PCMSSearchDaoImpl implements PCMSSearchDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private Database database;

	@Autowired
	public PCMSSearchDaoImpl(@Qualifier("pcmsDatabase") Database database) {
		this.database = database;
	}

	@Override
	public String handlerTempTableUserStatusList(List<String> statuses)
	{

		StringBuilder sql = new StringBuilder();

		// Step 1: drop + create temp tables
		sql.append("If(OBJECT_ID('tempdb..#tempLotNoList') Is Not Null) begin Drop Table #tempLotNoList end ;\n");
		sql.append("If(OBJECT_ID('tempdb..#tempUserStatusList') Is Not Null) begin Drop Table #tempUserStatusList end ;\n");

		sql.append("CREATE TABLE #tempLotNoList ( ");
		sql.append("LotNo NVARCHAR(50) COLLATE Thai_100_CI_AS PRIMARY KEY ");
		sql.append(");\n");

		sql.append("CREATE TABLE #tempUserStatusList ( ");
		sql.append("UserStatus NVARCHAR(50) COLLATE Thai_100_CI_AS PRIMARY KEY");
		sql.append(");\n");

		// เตรียม list ชั่วคราว
		List<String> lotNoList = new ArrayList<>();
		List<String> userStatusList = new ArrayList<>();

		for (String text : statuses) {
			if (text.equals("รอจัด Lot") || text.equals("ขาย stock") || text.equals("รับจ้างถัก") || text.equals("Lot ขายแล้ว")
					|| text.equals("พ่วงแล้วรอสวม") || text.equals("รอสวมเคยมี Lot")) {

				lotNoList.add(text);

			} else {
				userStatusList.add(text);
			}
		}

		// Step 2: insert LotNo
		if ( ! lotNoList.isEmpty()) {
			sql.append("INSERT INTO #tempLotNoList (LotNo) VALUES\n");
			for (int i = 0; i < lotNoList.size(); i ++ ) {
				sql.append("(N'").append(lotNoList.get(i).replace("'", "''")).append("')");
				if (i < lotNoList.size()-1) {
					sql.append(",\n");
				} else {
					sql.append(";\n");
				}
			}
		}

		// Step 3: insert UserStatus
		if ( ! userStatusList.isEmpty()) {
			sql.append("INSERT INTO #tempUserStatusList (UserStatus) VALUES\n");
			for (int i = 0; i < userStatusList.size(); i ++ ) {
				sql.append("(N'").append(userStatusList.get(i).replace("'", "''")).append("')");
				if (i < userStatusList.size()-1) {
					sql.append(",\n");
				} else {
					sql.append(";\n");
				}
			}
		}

		return sql.toString();
	}

	@Override
	public String handlerTempTableCustomerSearchList(List<String> customerNameList, List<String> customerShortNameList)
	{
		StringBuilder sql = new StringBuilder();
		// Step 1: สร้าง CREATE TABLE statements
		sql.append("If(OBJECT_ID('tempdb..#tempCustomerList') Is Not Null) begin Drop Table #tempCustomerList  end ;\n");
		sql.append("If(OBJECT_ID('tempdb..#tempCustomerShortList') Is Not Null) begin Drop Table #tempCustomerShortList  end ; ");
		sql.append("CREATE TABLE #tempCustomerList ( ");
		sql.append("CustomerName NVARCHAR(100) COLLATE Thai_100_CI_AS PRIMARY KEY");
		sql.append(");");
		sql.append("CREATE TABLE #tempCustomerShortList ( ");
		sql.append("CustomerShortName NVARCHAR(100) COLLATE Thai_100_CI_AS PRIMARY KEY ");
		sql.append(");");
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

	public void handlerCloseUserStatusAndLotNo() {
		executeDrop("#tempLotNoList", "#tempUserStatusList");
	}

	public void handlerCloseTempTableCustomerSearchList() {
		executeDrop("#tempCustomerList", "#tempCustomerShortList");
	}

	/** Drop temp tables ที่ระบุ — ใช้ร่วมกันใน cleanup methods */
	private void executeDrop(String... tableNames) {
		StringBuilder sql = new StringBuilder();
		for (String t : tableNames) {
			sql.append("IF OBJECT_ID('tempdb..").append(t).append("') IS NOT NULL DROP TABLE ").append(t).append(";\r\n");
		}
		Connection connection = this.database.getConnection();
		PreparedStatement prepared = null;
		try {
			prepared = connection.prepareStatement(sql.toString());
			prepared.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (prepared != null) try { prepared.close(); } catch (Exception ignored) {}
		}
	}
}
