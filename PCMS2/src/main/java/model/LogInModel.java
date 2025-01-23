package model;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.LogInDao;
import dao.implement.LogInDaoImpl;
import entities.ConfigCustomerUserDetail;
import entities.UserDetail;
import info.SqlInfo;
import th.in.totemplate.core.sql.Database;

@Component
public class LogInModel extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private Database database;
   private LogInDao dao;
   @SuppressWarnings("unused")
   private String[] uiColumns;
   @SuppressWarnings("unused")
   private static final String columns = "";

   @Autowired
   public LogInModel() {
      try {
         this.database = new Database(SqlInfo.getInstance());
         this.dao = new LogInDaoImpl(this.database);
         this.uiColumns = arrayColumn();
      } catch (SQLException | ClassNotFoundException var2) {
         var2.printStackTrace();
      }

   }

   public static String stringColumn() {
      return "[]";
   }

   public static String[] arrayColumn() {
      return "".replaceAll("'", "").split(",");
   }

   @Override
   public void destroy() {
      this.database.close();
      super.destroy();
   }
   public UserDetail getUserDetail(String userId ) {
	   UserDetail bean = this.dao.getUserDetail(userId);
		return bean;
	}
	public String descryptedText(String encryptedText) {
		String userId = this.dao.descryptedText(encryptedText);
		return userId ;
		// TODO Auto-generated method stub

	}

	public ArrayList<ConfigCustomerUserDetail> getConfigCustomerUserDetail(String userId) {
		ArrayList<ConfigCustomerUserDetail> bean = this.dao.getConfigCustomerUserDetail(userId);
		return bean ;
	}

	public UserDetail getUserDetail(String userId, String userPassword) {
		   UserDetail bean = this.dao.getUserDetail(userId,userPassword);
			return bean;
	}
}
