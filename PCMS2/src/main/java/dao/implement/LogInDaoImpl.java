package dao.implement;

import java.security.DigestException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import dao.LogInDao;
import entities.ConfigCustomerUserDetail;
//import entities.EmployeeDetail;
import entities.UserDetail;
import info.SqlInfo;
import model.BeanCreateModel;
import th.in.totemplate.core.sql.Database;

public class LogInDaoImpl implements LogInDao {
	private BeanCreateModel bcModel = new BeanCreateModel();
	private Database database;
	private String message;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
	public SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd");

	public SimpleDateFormat sdfDateTime1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public LogInDaoImpl(Database database) {
		this.database = database;
		this.message = "";
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public UserDetail getUserDetail(String userId) {
		UserDetail user = null;
//		int Id = 0;
		String Firstname ,UserId
//		,PermitId ,Responsible ,ChangeBy ,RegistBy,Password
		;
//		Date LastSignDate,ChangeDate,RegistDate ;
//		Boolean IsAdminSystem,IsAdminUser;
         try {
             String sql = "SELECT * FROM [Users] WHERE UserId = ?  ";

             Database          database   = new Database(SqlInfo.getInstance());
             Connection        connection = database.getConnection();
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
//      				Password = "";
//     				 if (resultset.getString("Password") != null) {
//     					Password = resultset.getString("Password");
//         			 }
      				 
      				String ChangeDate = "";
      				if (resultset.getDate("ChangeDate") != null) {
      					Timestamp timestamp1 = (Timestamp) resultset.getTimestamp("ChangeDate");
      					ChangeDate = this.sdfDateTime1.format(timestamp1);
      				}
      				String ChangeBy = "";
      				if (resultset.getString("ChangeBy") != null) {
      					ChangeBy = (String) resultset.getString("ChangeBy");
      				}  
      				String LastSignDate = "";
      				LastSignDate = this.sdfDateTime1.format(Calendar.getInstance().getTime());
      				String RegistDate = "";
      				if (resultset.getDate("RegistDate") != null) {
      					Timestamp timestamp1 = (Timestamp) resultset.getTimestamp("RegistDate");
      					RegistDate = this.sdfDateTime1.format(timestamp1);
      				} 
      				String registBy = "";
      				if (resultset.getString("registBy") != null) {
      					registBy = (String) resultset.getString("registBy");
      				}    
                	 user = new UserDetail();
                     user.setId(resultset.getInt("Id"));
                     user.setFirstName(Firstname);
                     user.setUserId(UserId);
//                     user.setUserId(Password);
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
                 System.err.println(this.getClass().getName()+" - "+e.getMessage());
                 //e.printStackTrace();
             } finally {
                 if(prepared != null)   { prepared.close(); }
                 if(connection != null) { connection.close(); }
                 if(database != null)   { database.close(); }
             }
         } catch(ClassNotFoundException e) {
         } catch(SQLException e) {
             System.err.println(this.getClass().getName()+" - "+e.getMessage());
             //e.printStackTrace();
         }
		return user;
	}


	@Override
	public UserDetail getUserDetail(String userId,String passWord) {
		UserDetail user = null;
//		int Id = 0;
		String Firstname ,UserId
//		,PermitId ,Responsible ,ChangeBy ,RegistBy,Password
		;
//		Date LastSignDate,ChangeDate,RegistDate ;
//		Boolean IsAdminSystem,IsAdminUser;
         try {
             String sql = "SELECT * FROM [Users] WHERE UserId = ? and Password = ? ";

             Database          database   = new Database(SqlInfo.getInstance());
             Connection        connection = database.getConnection();
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
//      				Password = "";
//     				 if (resultset.getString("Password") != null) {
//     					Password = resultset.getString("Password");
//         			 }
      				 
      				String ChangeDate = "";
      				if (resultset.getDate("ChangeDate") != null) {
      					Timestamp timestamp1 = (Timestamp) resultset.getTimestamp("ChangeDate");
      					ChangeDate = this.sdfDateTime1.format(timestamp1);
      				}
      				String ChangeBy = "";
      				if (resultset.getString("ChangeBy") != null) {
      					ChangeBy = (String) resultset.getString("ChangeBy");
      				}  
      				String LastSignDate = "";
      				LastSignDate = this.sdfDateTime1.format(Calendar.getInstance().getTime());
      				String RegistDate = "";
      				if (resultset.getDate("RegistDate") != null) {
      					Timestamp timestamp1 = (Timestamp) resultset.getTimestamp("RegistDate");
      					RegistDate = this.sdfDateTime1.format(timestamp1);
      				} 
      				String registBy = "";
      				if (resultset.getString("registBy") != null) {
      					registBy = (String) resultset.getString("registBy");
      				}    
                	 user = new UserDetail();
                     user.setId(resultset.getInt("Id"));
                     user.setFirstName(Firstname);
                     user.setUserId(UserId);
//                     user.setUserId(Password);
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
                 System.err.println(this.getClass().getName()+" - "+e.getMessage());
                 //e.printStackTrace();
             } finally {
                 if(prepared != null)   { prepared.close(); }
                 if(connection != null) { connection.close(); }
                 if(database != null)   { database.close(); }
             }
         } catch(ClassNotFoundException e) {
         } catch(SQLException e) {
             System.err.println(this.getClass().getName()+" - "+e.getMessage());
             //e.printStackTrace();
         }
		return user;
	}
	@Override
	public String descryptedText(String ciphertext) {
		String decryptedText = "";
//		MessageDigest md5;
//		try {
//
//			byte[] cipherData = Base64.getDecoder().decode(ciphertext);
//			byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);
//			md5 = MessageDigest.getInstance("MD5");
//			final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8),
//					md5);
//			SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
//			IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);
//
//			byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
//			Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
//			aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
//			byte[] decryptedData = aesCBC.doFinal(encrypted);
//			decryptedText = new String(decryptedData, StandardCharsets.UTF_8);
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchPaddingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidKeyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidAlgorithmParameterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalBlockSizeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (BadPaddingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return decryptedText;
	}

	public byte[][] GenerateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password,
			MessageDigest md) {

		int digestLength = md.getDigestLength();
		int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
		byte[] generatedData = new byte[requiredLength];
		int generatedLength = 0;
		try {
			md.reset();

			// Repeat process until sufficient data has been generated
			while (generatedLength < keyLength + ivLength) {

				// Digest data (last digest if available, password data, salt if available)
				if (generatedLength > 0) {
					md.update(generatedData, generatedLength - digestLength, digestLength);
				}
				md.update(password);
				if (salt != null) {
					md.update(salt, 0, 8);
				}
				md.digest(generatedData, generatedLength, digestLength);

				// additional rounds
				for (int i = 1; i < iterations; i++) {
					md.update(generatedData, generatedLength, digestLength);
					md.digest(generatedData, generatedLength, digestLength);
				}

				generatedLength += digestLength;
			}

			// Copy key and IV into separate byte arrays
			byte[][] result = new byte[2][];
			result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
			if (ivLength > 0) {
				result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);
			}

			return result;

		} catch (DigestException e) {
			throw new RuntimeException(e);

		} finally {
			// Clean out temporary data
			Arrays.fill(generatedData, (byte) 0);
		}
	}

	@Override
	public ArrayList<ConfigCustomerUserDetail> getConfigCustomerUserDetail(String userId) {
		ArrayList<ConfigCustomerUserDetail> list = null;
		String sql =
				  " SELECT [Id]\r\n"
				  + "      ,[EmployeeID]\r\n"
				  + "      ,[CustomerNo]\r\n"
				  + "      ,[CustomerDivision]\r\n"
				  + "      ,[IsPCMSDetailPage]\r\n"
				  + "      ,[IsPCMSSumPage]\r\n"
				  + "      ,[IsProdPathBtn]\r\n"
				  + "      ,[IsLBMSPathBtn]\r\n"
				  + "      ,[IsQCMSPathBtn]\r\n"
				  + "      ,[IsInspectPathBtn]\r\n"
				  + "      ,[IsSFCPathBtn]\r\n"
				  + "      ,[DataStatus]\r\n"
				  + "  FROM [PCMS].[dbo].[ConfigCustomerUser] as a\r\n"
			  	+ " where a.[EmployeeID] = '" + userId+ "' \r\n"
			  	+ " ORDER BY EmployeeID desc ";
//		System.out.println(sql);
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genConfigCustomerUserDetail(map));
		}
		return list;
	}
}
