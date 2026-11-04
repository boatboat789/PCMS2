package th.co.wacoal.atech.pcms2.regression;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assume;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;

import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.logic.PCMSMainProcess;
import th.co.wacoal.atech.pcms2.service.PCMSDetailService;

/**
 * ชั้น service + Gson ของ endpoint ค้นหา — เรียก bean เดียวกับที่ controller ใช้ (PCMSMainProcess /
 * PCMSDetailService) โดยส่ง request เป็น JSON เหมือนที่เบราว์เซอร์ POST แล้วเก็บผล g.toJson(...)
 * ลงโฟลเดอร์ -Dsvc.out=<dir> เพื่อ diff ระหว่างโค้ดสองเวอร์ชัน (ไม่มี baseline ถาวร — ใช้เทียบ A/B)
 *
 * รัน: mvn -o test -Dtest=PCMSServiceGsonCheck -Dpcms.harness.enabled=true -Dsvc.out=C:\temp\svc_new
 * ต่อ DB ตาม src/test/resources/database.properties (DEV) — ไม่เขียนข้อมูล
 */
public class PCMSServiceGsonCheck {

	private static final Path CASES_DIR = Paths.get("src/test/resources/pcms2/regression/cases");

	@Test
	public void dumpServiceJson() throws Exception {
		Assume.assumeTrue("ข้าม: ไม่ได้เปิด -Dpcms.harness.enabled=true", Boolean.getBoolean("pcms.harness.enabled"));
		String out = System.getProperty("svc.out");
		Assume.assumeTrue("ข้าม: ไม่ได้ระบุ -Dsvc.out=<dir>", out != null && !out.isEmpty());
		Path outDir = Paths.get(out);
		Files.createDirectories(outDir);

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("pcms2-service-context.xml");
		try {
			PCMSMainProcess mainProcess = ctx.getBean(PCMSMainProcess.class);
			PCMSDetailService detailService = ctx.getBean(PCMSDetailService.class);
			Gson g = new Gson();
			for (PCMSHarnessCase c : PCMSHarnessCase.loadAll(CASES_DIR)) {
				if (!c.enabled) {
					continue;
				}
				// เลียนแบบ @RequestBody ArrayList<PCMSTableDetail>: serialize แล้ว parse กลับจาก JSON
				String body = g.toJson(java.util.Collections.singletonList(c.bean));
				ArrayList<PCMSTableDetail> req = new ArrayList<>(java.util.Arrays.asList(g.fromJson(body, PCMSTableDetail[].class)));
				write(outDir, "main_" + c.name + ".json", g.toJson(mainProcess.searchByDetail(req, false)));
				req = new ArrayList<>(java.util.Arrays.asList(g.fromJson(body, PCMSTableDetail[].class)));
				write(outDir, "main_customer_" + c.name + ".json", g.toJson(mainProcess.searchByDetail(req, true)));
				req = new ArrayList<>(java.util.Arrays.asList(g.fromJson(body, PCMSTableDetail[].class)));
				write(outDir, "detail_" + c.name + ".json", g.toJson(detailService.searchByDetail(req)));
				System.out.println("[svc] dumped " + c.name);
			}
		} finally {
			ctx.close();
		}
	}

	private static void write(Path dir, String name, String json) throws Exception {
		Files.write(dir.resolve(name), json.getBytes(StandardCharsets.UTF_8));
	}
}
