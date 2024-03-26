package dao.implement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.DataImportDao;
import entities.DataImport;
import entities.UserDetail;
import th.in.totemplate.core.net.FtpReceive;
import th.in.totemplate.core.sql.Database;

public class DataImportMainSaleDaoImpl implements DataImportDao {
   private Database database;
   private FtpReceive ftp;
   private String message;
   private int maxfield;

   public DataImportMainSaleDaoImpl(Database database, FtpReceive ftp) {
      this.database = database;
      this.ftp = ftp;
      this.message = ""; 
      this.maxfield = 32;
   }

   public String getMessage() {
      return this.message;
   }

   public int loadDataFile(UserDetail user, String fullname) {
      boolean success = false;
      try {
          if( this.fileReader(fullname) > 0 ) {
              success = true;
          }
      } catch(Exception e) { }

      return ( (success) ? 1 : 0 );
   }

   public int loadDataFTP(UserDetail user) {
      boolean success = false;
      int     value   = 0;
      try { 
    	  if( FtpReceive.STATUS.SUCCESS == this.ftp.receive()){
              for(File file : this.ftp.getFiles()) { 
                  value += this.loadDataFile( new UserDetail("SYSTEM", ""), file.getAbsolutePath()  );
              }
        	 success = true;
         } else {
            success = false;
         }
      } catch (Exception var5) {
      }

      return success ? 1 : 0;
   }

   public int loadDataFTP(UserDetail user, String fullname) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public int loadDataSAP(UserDetail user) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   private int fileReader(String fullname) throws SQLException {
      boolean success = false;
      File file = new File(fullname);
      FileInputStream input = null;  
      InputStreamReader reader = null;
      BufferedReader buffer = null;   
	  System.out.println(fullname);
      if (fullname.contains("ZSAPMAINSALE")) {  
//    	  System.out.println(fullname);
	      if (file.exists()) {
	         try {  
	            input = new FileInputStream(file);
	            reader = new InputStreamReader(input, "UTF-8");
	            buffer = new BufferedReader(reader);
	            String line = "";
	            int counter = 0;   
	            ArrayList<DataImport> datas = new ArrayList<DataImport>();
	            while((line = buffer.readLine()) != null) {
	               String[] field = line.split("\\|", -1);  
	               System.out.println(field.length+" " +this.maxfield);
	               if (field.length == this.maxfield) { 
	                  ++counter; 
	                  datas.add(new DataImport(field.length, field));
	               } else {
	                  this.message = "Field length is missing" +counter;
	               }
	            }  
	            System.out.println(!datas.isEmpty()+" "+datas.size());
	            if (!datas.isEmpty()) {
	               this.clearData(); 
//	               System.out.println("clear");
	               this.insertData(datas);  
//	               System.out.println("wtf");
	               this.database.update("EXEC spd_UpsertToMainSale");
//	               System.out.println("EXEC spd_UpsertToMainSale"); 
	               success = true;
	            }
	         } catch (FileNotFoundException var31) {
	            this.message = "File Not Found";
	            System.err.println("success4");
	         } catch (IOException var32) {
	            this.message = "Read file Error";
	            System.err.println("success5");
	         } finally {
	        	    try { if(buffer != null) { buffer.close(); } } catch(IOException e) { }
	                try { if(reader != null) { reader.close(); } } catch(IOException e) { }
	                try { if(input != null)  { input.close(); } }  catch(IOException e) { } 
	         }
	      }
      } 
      return success ? 1 : 0;
   }

   private int insertData(List<DataImport> datas) {
      int value = 0;
//      System.out.println(datas.isEmpty());
      if (!datas.isEmpty()) {
         try {
        	 String sql = "INSERT INTO [PCMS].[dbo].[SapTempMainSale] "
             		+ " (F001, F002, F003, F004, F005, F006, F007, F008, F009, F010,"
             		+ "	 F011, F012, F013, F014, F015, F016, F017, F018, F019, F020,"
             		+ "	 F021, F022, F023, F024, F025, F026, F027, F028, F029, F030,"
             		+ "  F031, F032 ) "
             		+ " VALUES ("
             		+ "        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
             		+ " 	   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
         		    + "        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
         		    + "        ?, ? "	
         		    + "        )"; 
            Connection connection = this.database.getConnection();
            PreparedStatement prepared = connection.prepareStatement(sql);
            int i = 0; 
            for(DataImport data : datas) {
                for(i=0; i<data.getMax(); i++) {                       
                    prepared.setString(i+1,  data.getField(i).trim());
                } 
                prepared.addBatch();
            }  
            prepared.executeBatch();
            value = i;
         } catch (SQLException var9) {
            throw new RuntimeException();
         }
      }

      return value;
   }

   private int clearData() {
      String sql = "DELETE FROM [PCMS].[dbo].[SapTempMainSale] ";
      int value = this.database.update(sql, new Object[0]);
      return value;
   }
}
