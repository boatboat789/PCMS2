package th.co.wacoal.atech.pcms2.dao.master.implement.PPMM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.master.PPMM.UserStatusDetailDao;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;

@Repository // Spring annotation to mark this as a DAO component
public class UserStatusDetailDaoImpl implements UserStatusDetailDao {
	private String select = ""
			+ "     [Id]\r\n"
			+ "      ,[UserStatusSapId]\r\n"
			+ "      ,[UserStatus]\r\n"
			+ "      ,[DataStatus]\r\n";
	private BeanCreateService bcModel = new BeanCreateService();
	private JdbcTemplate jdbc;
	public SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
	public SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

	// query [PPMM].[dbo].[UserStatusDetail] → ต้องใช้ ppmmDatabase (เดิมใช้ pcmsDatabase ผิด convention)
	@Autowired
	public UserStatusDetailDaoImpl(@Qualifier("ppmmDatabase") JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	@Override
	public  ArrayList<PCMSAllDetail> getUserStatusDetail( ){
		ArrayList<PCMSAllDetail> list = null;
		String where = " where  ";
		where += " a.[DataStatus] in ( 'O' ) \r\n";
		String sql =
				  " "
				+ " SELECT DISTINCT  \r\n"
				+ this.select
				+ " FROM [PPMM].[dbo].[UserStatusDetail] as a \r\n "
				+ where
				+ " ";
//
		List<Map<String, Object>> datas = this.jdbc.queryForList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSAllDetail(map));
		}
		return list;
	}
}
