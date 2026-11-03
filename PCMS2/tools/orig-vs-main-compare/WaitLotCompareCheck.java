package th.co.wacoal.atech.pcms2.regression;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assume;
import org.junit.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.gson.Gson;

import th.co.wacoal.atech.pcms2.dao.PCMSDetailDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;

/** ชั่วคราว: เรียก getWaitLotCaseBySaleOrder (reflection) ทุก sale line ของ DEV แล้วเขียนผลลัพธ์เป็นไฟล์ (อ่านอย่างเดียว) */
public class WaitLotCompareCheck {
	@Test
	public void run() throws Exception {
		Assume.assumeTrue(Boolean.getBoolean("wl.enabled"));
		String out = System.getProperty("wl.out");
		int batch = Integer.getInteger("wl.batch", 200);
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("pcms2-harness-context.xml");
		try (PrintWriter w = new PrintWriter(out, "UTF-8")) {
			PCMSDetailDao dao = ctx.getBean(PCMSDetailDao.class);
			Class<?> target = AopUtils.getTargetClass(dao);
			Method m = target.getDeclaredMethod("getWaitLotCaseBySaleOrder", ArrayList.class);
			m.setAccessible(true);
			JdbcTemplate jdbc = ctx.getBean("pcmsJdbcTemplate", JdbcTemplate.class);
			List<Map<String, Object>> lines = jdbc.queryForList(
					"SELECT DISTINCT SaleOrder, SaleLine FROM [PCMS].[dbo].[FromSapMainSale] ORDER BY SaleOrder, SaleLine");
			Gson gson = new Gson();
			List<String> rows = new ArrayList<>();
			int exc = 0, batches = 0;
			for (int i = 0; i < lines.size(); i += batch) {
				ArrayList<PCMSSecondTableDetail> in = new ArrayList<>();
				for (Map<String, Object> r : lines.subList(i, Math.min(lines.size(), i + batch))) {
					PCMSSecondTableDetail b = new PCMSSecondTableDetail();
					b.setSaleOrder(String.valueOf(r.get("SaleOrder")));
					b.setSaleLine(String.valueOf(r.get("SaleLine")));
					in.add(b);
				}
				batches++;
				try {
					@SuppressWarnings("unchecked")
					List<PCMSSecondTableDetail> res = (List<PCMSSecondTableDetail>) m.invoke(dao, in);
					for (PCMSSecondTableDetail d : res) rows.add(gson.toJson(d));
				} catch (java.lang.reflect.InvocationTargetException e) {
					exc++;
					Throwable c = e.getCause() == null ? e : e.getCause();
					System.out.println("[waitlot] EXC batch " + batches + ": " + c.getClass().getSimpleName() + ": " + String.valueOf(c.getMessage()).replace('\n', ' '));
					if (exc >= 3) break;
				}
			}
			Collections.sort(rows);
			w.println("#lines=" + lines.size() + " batches=" + batches + " exceptions=" + exc + " rows=" + rows.size());
			for (String r : rows) w.println(r);
			System.out.println("[waitlot] lines=" + lines.size() + " batches=" + batches + " exceptions=" + exc + " rows=" + rows.size());
		} finally {
			ctx.close();
		}
	}
}
