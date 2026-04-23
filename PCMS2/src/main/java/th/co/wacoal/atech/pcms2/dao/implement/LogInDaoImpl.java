package th.co.wacoal.atech.pcms2.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.LogInDao;
import th.co.wacoal.atech.pcms2.entities.UserDetail;
import th.in.totemplate.core.sql.Database;
@Repository // Spring annotation to mark this as a DAO component
public class LogInDaoImpl implements LogInDao { 

	public SimpleDateFormat sdfDateTime1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private Database database;
    @Autowired
	public LogInDaoImpl(@Qualifier("pcmsDatabase")Database database) {
    	this.database = database;
	}
	@Override
	public UserDetail getUserDetail(String userId) {
		UserDetail user = null;
		String Firstname ,UserId;
         try {
             String sql = "SELECT * FROM [Users] WHERE UserId = ?  "; 
             Connection        connection = this.database.getConnection();
             PreparedStatement prepared   = connection.prepareStatement(sql);
             try {
                 prepared.setString(1, userId);

                 ResultSet resultset = prepared.executeQuery();
                 if(resultset.next()) {
                     Firstname = "";
      				 if (resultset.getString("Firstname") != null) {
      					Firstname = resultset.getString("Firstname");
          			 }
      				UserId = "";
      				 if (resultset.getString("UserId") != null) {
      					UserId = resultset.getString("UserId");
          			 }
      				String ChangeDate = "";
      				if (resultset.getDate("ChangeDate") != null) {
      					Timestamp timestamp1 = (Timestamp) resultset.getTimestamp("ChangeDate");
      					ChangeDate = this.sdfDateTime1.format(timestamp1);
      				} 
      				String LastSignDate = "";
      				LastSignDate = this.sdfDateTime1.format(Calendar.getInstance().getTime());
      				String RegistDate = "";
      				if (resultset.getDate("RegistDate") != null) {
      					Timestamp timestamp1 = (Timestamp) resultset.getTimestamp("RegistDate");
      					RegistDate = this.sdfDateTime1.format(timestamp1);
      				}  
                	 user = new UserDetail();
                     user.setId(resultset.getInt("Id"));
                     user.setFirstName(Firstname);
                     user.setUserId(UserId);
                     user.setIsSystem(resultset.getBoolean("IsAdminSystem"));
                     user.setIsAdmin(resultset.getBoolean("IsAdminUser"));
                     user.setPermitId(resultset.getString("PermissionId"));
                     user.setResponsible(resultset.getString("Responsible"));
                     user.setLastSignDate(LastSignDate);
                     user.setChangeBy(resultset.getString("ChangeBy"));
                     user.setChangeDate(ChangeDate);
                     user.setRegistBy(resultset.getString("RegistBy"));
                     user.setRegistDate(RegistDate);
                     user.setCustomer(resultset.getBoolean("IsCustomer"));
                     user.setUserType("USER");

                 }
             } catch(SQLException e) {
                 e.printStackTrace();
             } finally {
                 if(prepared != null)   { prepared.close(); }
//                 if(connection != null) { connection.close(); }
//                 if(database != null)   { database.close(); }
             }
         } catch(SQLException e) {
             e.printStackTrace();
         }
		return user;
	}


	@Override
	public UserDetail getUserDetail(String userId,String passWord) {
		UserDetail user = null;
		String Firstname ,UserId;
         try {
             String sql = "SELECT * FROM [Users] WHERE UserId = ? and Password = ? ";
             Connection        connection = this.database.getConnection();
             PreparedStatement prepared   = connection.prepareStatement(sql);
             try {
                 prepared.setString(1, userId);
                 prepared.setString(2, passWord);

                 ResultSet resultset = prepared.executeQuery();
                 if(resultset.next()) {

                     Firstname = "";
      				 if (resultset.getString("Firstname") != null) {
      					Firstname = resultset.getString("Firstname");
          			 }

      				UserId = "";
      				 if (resultset.getString("UserId") != null) {
      					UserId = resultset.getString("UserId");
          			 }
      				String ChangeDate = "";
      				if (resultset.getDate("ChangeDate") != null) {
      					Timestamp timestamp1 = (Timestamp) resultset.getTimestamp("ChangeDate");
      					ChangeDate = this.sdfDateTime1.format(timestamp1);
      				}
      				String LastSignDate = "";
      				LastSignDate = this.sdfDateTime1.format(Calendar.getInstance().getTime());
      				String RegistDate = "";
      				if (resultset.getDate("RegistDate") != null) {
      					Timestamp timestamp1 = (Timestamp) resultset.getTimestamp("RegistDate");
      					RegistDate = this.sdfDateTime1.format(timestamp1);
      				}  
                	 user = new UserDetail();
                     user.setId(resultset.getInt("Id"));
                     user.setFirstName(Firstname);
                     user.setUserId(UserId);
                     user.setIsSystem(resultset.getBoolean("IsAdminSystem"));
                     user.setIsAdmin(resultset.getBoolean("IsAdminUser"));
                     user.setPermitId(resultset.getString("PermissionId"));
                     user.setResponsible(resultset.getString("Responsible"));
                     user.setLastSignDate(LastSignDate);
                     user.setChangeBy(resultset.getString("ChangeBy"));
                     user.setChangeDate(ChangeDate);
                     user.setRegistBy(resultset.getString("RegistBy"));
                     user.setRegistDate(RegistDate);
                     user.setCustomer(resultset.getBoolean("IsCustomer"));
                     user.setUserType("USER"); 

                 }
             } catch(SQLException e) {
                 e.printStackTrace();
             } finally {
                 if(prepared != null)   { prepared.close(); }
//                 if(connection != null) { connection.close(); }
//                 if(database != null)   { database.close(); }
             }
         } catch(SQLException e) {
             e.printStackTrace();
         }
		return user;
	} 
}
