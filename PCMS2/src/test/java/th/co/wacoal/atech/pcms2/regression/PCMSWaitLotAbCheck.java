package th.co.wacoal.atech.pcms2.regression;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Assume;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;

import th.co.wacoal.atech.pcms2.dao.PCMSDetailDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;

/**
 * A/B ของ getWaitLotCaseBySaleOrder (อ่านอย่างเดียว) — ไฟล์เดียวกันวางได้ทั้งโค้ดเก่า (b150c87 = main เดิมก่อน refactor)
 * และโค้ดใหม่ แล้วเทียบไฟล์ผลลัพธ์ -Dwaitlot.out
 * หา SO/Line ที่เป็น WaitLot จริงจาก DB (ReplacedProdOrder 'รอจัด Lot' ที่ ProductionOrderRP เป็น status Special — เส้นทาง countProdRP>0)
 * รัน: mvn test -Dtest=PCMSWaitLotAbCheck -Dpcms.harness.enabled=true -Dwaitlot.out=C:\temp\x.txt
 */
public class PCMSWaitLotAbCheck {

	@Test
	public void dump() throws Exception {
		Assume.assumeTrue(Boolean.getBoolean("pcms.harness.enabled"));
		Properties p = new Properties();
		try (FileInputStream in = new FileInputStream("src/test/resources/database.properties")) { p.load(in); }
		List<String[]> sales = new ArrayList<>();
		String sql = "SELECT TOP 8 a.SaleOrder, a.SaleLine FROM [PCMS].[dbo].[ReplacedProdOrder] a"
				+ " INNER JOIN [PCMS].[dbo].[FromSapMainProd] b ON a.ProductionOrderRP = b.ProductionOrder"
				+ " INNER JOIN [PCMS].[dbo].[viewUserStatusMappingPCMS] v ON b.[UserStatus] = v.UserStatus AND v.Special = 1"
				+ " WHERE a.DataStatus = 'O' AND a.ProductionOrder = N'รอจัด Lot' GROUP BY a.SaleOrder, a.SaleLine ORDER BY a.SaleOrder, a.SaleLine";
		try (Connection c = DriverManager.getConnection(p.getProperty("pcms.db.url"), p.getProperty("pcms.db.username"), p.getProperty("pcms.db.password"));
				ResultSet r = c.createStatement().executeQuery(sql)) {
			while (r.next()) { sales.add(new String[] { r.getString(1), r.getString(2) }); }
		}
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("pcms2-harness-context.xml");
		try (PrintWriter w = new PrintWriter(System.getProperty("waitlot.out", "waitlot_out.txt"), "UTF-8")) {
			Object dao = ctx.getBean(PCMSDetailDao.class);
			w.println("candidates=" + sales.size());
			ArrayList<PCMSSecondTableDetail> all = new ArrayList<>();
			for (String[] s : sales) {
				PCMSSecondTableDetail b = new PCMSSecondTableDetail();
				b.setSaleOrder(s[0]); b.setSaleLine(s[1]);
				all.add(b);
				ArrayList<PCMSSecondTableDetail> one = new ArrayList<>();
				one.add(b);
				dump(w, "single " + s[0] + "/" + s[1], call(dao, one));
			}
			if (!all.isEmpty()) { dump(w, "combined", call(dao, all)); }
		} finally {
			ctx.close();
		}
	}

	// เมธอดเป็น private ใน main เดิม (b150c87) แต่ public ในโค้ดใหม่ — เรียกผ่าน reflection ให้ไฟล์เดียวใช้ได้ทั้งสองฝั่ง
	@SuppressWarnings("unchecked")
	private static ArrayList<PCMSSecondTableDetail> call(Object dao, ArrayList<PCMSSecondTableDetail> in) throws Exception {
		java.lang.reflect.Method m = dao.getClass().getDeclaredMethod("getWaitLotCaseBySaleOrder", ArrayList.class);
		m.setAccessible(true);
		return (ArrayList<PCMSSecondTableDetail>) m.invoke(dao, in);
	}

	private static void dump(PrintWriter w, String label, ArrayList<PCMSSecondTableDetail> rows) {
		List<String> lines = new ArrayList<>();
		Gson g = new Gson();
		for (PCMSSecondTableDetail d : rows) { lines.add(g.toJson(d)); }
		Collections.sort(lines);
		w.println("## " + label + " rows=" + rows.size());
		for (String l : lines) { w.println(l); }
	}
}
