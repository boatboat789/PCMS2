	package dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import dao.BackGroundJobDao;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

public class BackGroundJobDaoImpl implements BackGroundJobDao { 
	private Database database;
	private String message;
 
	public BackGroundJobDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	} 
	@Override
	public void execUpsertToMainProd() {
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToMainProd] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertToTEMPProdWorkDate() {
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToTEMP_ProdWorkDate] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void execUpsertToTEMPUserStatusOnWeb() {
		// TODO Auto-generated method stub
		Connection connection;
		connection = this.database.getConnection();
		String sql = "EXEC [dbo].[spd_UpsertToTEMP_UserStatusOnWeb] ";
		try {
			PreparedStatement prepared = connection.prepareStatement(sql);
			prepared.execute();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	} 
}
