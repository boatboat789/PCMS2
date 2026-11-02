package th.co.wacoal.atech.pcms2.dao.implement;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.LogInDao;
import th.co.wacoal.atech.pcms2.entities.UserDetail;

@Repository
public class LogInDaoImpl implements LogInDao {

	public SimpleDateFormat sdfDateTime1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private JdbcTemplate jdbc;

	@Autowired
	public LogInDaoImpl(@Qualifier("pcmsDatabase") JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public UserDetail getUserDetail(String userId) {
		String sql = "SELECT * FROM [Users] WHERE UserId = ?  ";
		List<UserDetail> results = this.jdbc.query(sql, new Object[]{userId}, (rs, rowNum) -> {
			String Firstname = "";
			if (rs.getString("Firstname") != null) {
				Firstname = rs.getString("Firstname");
			}
			String UserId = "";
			if (rs.getString("UserId") != null) {
				UserId = rs.getString("UserId");
			}
			String ChangeDate = "";
			if (rs.getDate("ChangeDate") != null) {
				Timestamp timestamp1 = (Timestamp) rs.getTimestamp("ChangeDate");
				ChangeDate = this.sdfDateTime1.format(timestamp1);
			}
			String LastSignDate = "";
			LastSignDate = this.sdfDateTime1.format(Calendar.getInstance().getTime());
			String RegistDate = "";
			if (rs.getDate("RegistDate") != null) {
				Timestamp timestamp1 = (Timestamp) rs.getTimestamp("RegistDate");
				RegistDate = this.sdfDateTime1.format(timestamp1);
			}
			UserDetail user = new UserDetail();
			user.setId(rs.getInt("Id"));
			user.setFirstName(Firstname);
			user.setUserId(UserId);
			user.setIsSystem(rs.getBoolean("IsAdminSystem"));
			user.setIsAdmin(rs.getBoolean("IsAdminUser"));
			user.setPermitId(rs.getString("PermissionId"));
			user.setResponsible(rs.getString("Responsible"));
			user.setLastSignDate(LastSignDate);
			user.setChangeBy(rs.getString("ChangeBy"));
			user.setChangeDate(ChangeDate);
			user.setRegistBy(rs.getString("RegistBy"));
			user.setRegistDate(RegistDate);
			user.setCustomer(rs.getBoolean("IsCustomer"));
			user.setUserType("USER");
			return user;
		});
		return results.isEmpty() ? null : results.get(0);
	}

	@Override
	public UserDetail getUserDetail(String userId, String passWord) {
		String sql = "SELECT * FROM [Users] WHERE UserId = ? and Password = ? ";
		List<UserDetail> results = this.jdbc.query(sql, new Object[]{userId, passWord}, (rs, rowNum) -> {
			String Firstname = "";
			if (rs.getString("Firstname") != null) {
				Firstname = rs.getString("Firstname");
			}
			String UserId = "";
			if (rs.getString("UserId") != null) {
				UserId = rs.getString("UserId");
			}
			String ChangeDate = "";
			if (rs.getDate("ChangeDate") != null) {
				Timestamp timestamp1 = (Timestamp) rs.getTimestamp("ChangeDate");
				ChangeDate = this.sdfDateTime1.format(timestamp1);
			}
			String LastSignDate = "";
			LastSignDate = this.sdfDateTime1.format(Calendar.getInstance().getTime());
			String RegistDate = "";
			if (rs.getDate("RegistDate") != null) {
				Timestamp timestamp1 = (Timestamp) rs.getTimestamp("RegistDate");
				RegistDate = this.sdfDateTime1.format(timestamp1);
			}
			UserDetail user = new UserDetail();
			user.setId(rs.getInt("Id"));
			user.setFirstName(Firstname);
			user.setUserId(UserId);
			user.setIsSystem(rs.getBoolean("IsAdminSystem"));
			user.setIsAdmin(rs.getBoolean("IsAdminUser"));
			user.setPermitId(rs.getString("PermissionId"));
			user.setResponsible(rs.getString("Responsible"));
			user.setLastSignDate(LastSignDate);
			user.setChangeBy(rs.getString("ChangeBy"));
			user.setChangeDate(ChangeDate);
			user.setRegistBy(rs.getString("RegistBy"));
			user.setRegistDate(RegistDate);
			user.setCustomer(rs.getBoolean("IsCustomer"));
			user.setUserType("USER");
			return user;
		});
		return results.isEmpty() ? null : results.get(0);
	}
}
