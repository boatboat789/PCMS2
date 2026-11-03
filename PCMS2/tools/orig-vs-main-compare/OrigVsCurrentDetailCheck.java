package th.co.wacoal.atech.pcms2.regression;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assume;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import th.co.wacoal.atech.pcms2.dao.PCMSDetailDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

/** ชั่วคราว: เรียก PCMSDetailDao.searchByDetail ด้วย case JSON แล้วเขียนผลเป็นไฟล์ (อ่านอย่างเดียว, ต้องต่อ DEV เท่านั้น) */
public class OrigVsCurrentDetailCheck {
	@Test
	public void run() throws Exception {
		Assume.assumeTrue(Boolean.getBoolean("wl.enabled"));
		File casesDir = new File(System.getProperty("wl.cases"));
		File outDir = new File(System.getProperty("wl.outdir"));
		outDir.mkdirs();
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("pcms2-harness-context.xml");
		try {
			Object srv;
			if (ctx.containsBean("pcmsJdbcTemplate")) {
				srv = ((org.springframework.jdbc.core.JdbcTemplate) ctx.getBean("pcmsJdbcTemplate")).queryForObject("SELECT @@SERVERNAME", String.class);
			} else {
				th.in.totemplate.core.sql.Database db = (th.in.totemplate.core.sql.Database) ctx.getBean("pcmsDatabase");
				srv = db.queryList("SELECT @@SERVERNAME AS s").get(0).get("s");
			}
			System.out.println("[cmp] server=" + srv);
			if (!String.valueOf(srv).toUpperCase().contains("PCMSDEV")) throw new IllegalStateException("not DEV: " + srv);
			PCMSDetailDao dao = ctx.getBean(PCMSDetailDao.class);
			Gson gson = new Gson();
			File[] files = casesDir.listFiles((d, n) -> n.endsWith(".json"));
			java.util.Arrays.sort(files);
			for (File f : files) {
				JsonObject o = new JsonParser().parse(new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8)).getAsJsonObject();
				if (o.has("enabled") && !o.get("enabled").getAsBoolean()) continue;
				String name = o.get("name").getAsString();
				PCMSTableDetail bean = gson.fromJson(o.get("bean"), PCMSTableDetail.class);
				normalize(bean);
				ArrayList<PCMSTableDetail> in = new ArrayList<>();
				in.add(bean);
				List<String> rows = new ArrayList<>();
				String status = "";
				try {
					ArrayList<PCMSSecondTableDetail> res = dao.searchByDetail(in);
					for (PCMSSecondTableDetail d : res) rows.add(gson.toJson(d));
				} catch (Exception e) {
					status = " EXC " + e.getClass().getSimpleName() + ": " + String.valueOf(e.getMessage()).replace('\n', ' ');
				}
				Collections.sort(rows);
				try (PrintWriter w = new PrintWriter(new File(outDir, "searchByDetail__" + name + ".txt"), "UTF-8")) {
					w.println("#rows=" + rows.size() + status);
					for (String r : rows) w.println(r);
				}
				System.out.println("[cmp] " + name + " rows=" + rows.size() + status);
			}
		} finally {
			ctx.close();
		}
	}

	private static void normalize(PCMSTableDetail bean) throws Exception {
		for (Field f : PCMSTableDetail.class.getDeclaredFields()) {
			if (Modifier.isStatic(f.getModifiers())) continue;
			f.setAccessible(true);
			if (f.get(bean) != null) continue;
			if (f.getType() == String.class) f.set(bean, "");
			else if (List.class.isAssignableFrom(f.getType())) f.set(bean, new ArrayList<String>());
		}
	}
}
