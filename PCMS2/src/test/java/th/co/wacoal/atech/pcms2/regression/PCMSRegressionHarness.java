package th.co.wacoal.atech.pcms2.regression;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assume;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import th.co.wacoal.atech.pcms2.dao.PCMSDetailDao;
import th.co.wacoal.atech.pcms2.dao.PCMSMainDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

import static org.junit.Assert.fail;

/**
 * Phase 0 regression harness ของแผน refactor PCMS2 DAO (ดู
 * C:\Users\94687\.claude\plans\...refactored-lagoon.md)
 *
 * เรียก DAO method จริง (PCMSMainDao/PCMSDetailDao) ผ่าน Spring context แยกที่ไม่ scan
 * controller/SchedulerConfig/TaskService (กัน cron sync job ยิงงานเขียนข้อมูลจริงใน PRD ระหว่างรัน
 * — ดู pcms2-harness-context.xml) แล้วเทียบผลลัพธ์ bean-level กับ baseline ที่แช่แข็งไว้
 *
 * <b>DB เป้าหมายคือ PRD จริง (10.11.44.101/PCMS)</b> — รันแบบ sequential เท่านั้น (ไม่ parallel)
 * เพื่อไม่แย่ง connection pool กับ user จริง และไม่ query queryTimeout ป้องกันไว้ (ยังไม่ทำ Phase 1c)
 * ดังนั้นแนะนำรันช่วง off-peak และเริ่มจาก case เบา (userStatus LotNo คงที่) ก่อนเปิด case TODO
 * ที่ยังไม่รู้ขนาดผลลัพธ์จริง
 *
 * วิธีรัน (ไม่ต้องพึ่ง plugin เพิ่มใน pom — ใช้ surefire ที่มีอยู่แล้ว ต้องเปิด enable flag เสมอ):
 *   mvn test -Dtest=PCMSRegressionHarness -Dpcms.harness.enabled=true -Dpcms.harness.mode=capture
 *       (ครั้งแรก / ทุกครั้งที่ต้อง re-freeze — แช่แข็ง baseline ใหม่ทับของเดิม)
 *   mvn test -Dtest=PCMSRegressionHarness -Dpcms.harness.enabled=true
 *       (mode=verify เป็นค่า default — ใช้หลังแก้โค้ดทุกครั้ง ต้อง diff-clean)
 *
 * หรือรันผ่าน main() ตรงๆ จาก IDE ก็ได้ (Eclipse: Run As > Java Application) โดยส่ง args[0]
 * เป็น "capture"/"verify" — ไม่ต้อง set system property ใดๆ ในกรณีนี้
 */
public class PCMSRegressionHarness {

	private static final String ENABLE_FLAG = "pcms.harness.enabled";
	private static final String MODE_PROPERTY = "pcms.harness.mode";
	private static final Path CASES_DIR = Paths.get("src/test/resources/pcms2/regression/cases");
	private static final Path BASELINE_DIR = Paths.get("src/test/resources/pcms2/regression/baseline");
	private static final String SPRING_CONTEXT = "pcms2-harness-context.xml";

	public static void main(String[] args) throws Exception {
		String mode = args.length > 0 ? args[0] : "verify";
		if (!mode.equals("capture") && !mode.equals("verify")) {
			throw new IllegalArgumentException("mode ต้องเป็น capture หรือ verify เท่านั้น, ได้ '" + mode + "'");
		}
		List<String> failures = run(mode);
		if (!failures.isEmpty()) {
			System.out.println("\n=== " + failures.size() + " case ล้มเหลว ===");
			failures.forEach(System.out::println);
			System.exit(1);
		}
		System.out.println("\n=== harness ผ่านทั้งหมด (mode=" + mode + ") ===");
	}

	/**
	 * JUnit4 wrapper — ข้าม (ไม่ fail) ถ้าไม่ได้เปิด -Dpcms.harness.enabled=true อย่างชัดเจน
	 * กัน `mvn test` ธรรมดารันชน PRD โดยไม่ตั้งใจ (ปกติ build ใช้ -DskipTests อยู่แล้ว แต่กันไว้อีกชั้น)
	 * mode อ่านจาก -Dpcms.harness.mode (default "verify") — ใส่ "capture" เพื่อแช่แข็ง baseline ใหม่
	 */
	@Test
	public void verifyAgainstBaseline() throws Exception {
		Assume.assumeTrue(
				"ข้าม PCMSRegressionHarness เพราะไม่ได้เปิด -D" + ENABLE_FLAG + "=true "
						+ "(harness ต่อ PRD จริง ต้องเปิดใช้อย่างตั้งใจเท่านั้น)",
				Boolean.getBoolean(ENABLE_FLAG));
		String mode = System.getProperty(MODE_PROPERTY, "verify");
		List<String> failures = run(mode);
		if (!failures.isEmpty()) {
			fail(failures.size() + " case diff จาก baseline:\n" + String.join("\n", failures));
		}
	}

	private static List<String> run(String mode) throws Exception {
		List<String> failures = new ArrayList<>();
		System.out.println("[harness] boot Spring context (exclude scheduler) จาก " + SPRING_CONTEXT + " ...");
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(SPRING_CONTEXT);
		try {
			PCMSMainDao mainDao = ctx.getBean(PCMSMainDao.class);
			PCMSDetailDao detailDao = ctx.getBean(PCMSDetailDao.class);

			List<PCMSHarnessCase> allCases = PCMSHarnessCase.loadAll(CASES_DIR);
			List<PCMSHarnessCase> cases = new ArrayList<>();
			for (PCMSHarnessCase c : allCases) {
				if (c.enabled) {
					cases.add(c);
				} else {
					System.out.println("[harness] ข้าม case '" + c.name + "' (enabled=false ใน JSON)");
				}
			}
			if (cases.isEmpty()) {
				failures.add("ไม่มี case ที่ enabled=true ใน " + CASES_DIR + " — ไม่มีอะไรให้ตรวจ");
				return failures;
			}

			// เก็บแถวตัวแทนต่อ TypePrd จากทุก static case (case แรกที่เจอ TypePrd นั้นชนะ)
			// ใช้เป็น input สำหรับ 8 senario ของ 6 expansion method ด้านล่าง (discovery)
			Map<String, ProbeRow> firstRowByTypePrd = new LinkedHashMap<>();

			for (PCMSHarnessCase c : cases) {
				List<PCMSTableDetail> summary = callAndVerify(mode,
						"getPCMSSumaryDetail__" + c.name, failures,
						() -> mainDao.getPCMSSumaryDetail(singleton(c.bean)));
				harvest(firstRowByTypePrd, summary);

				List<PCMSSecondTableDetail> detail = callAndVerify(mode,
						"searchByDetail__" + c.name, failures,
						() -> detailDao.searchByDetail(singleton(c.bean)));
				harvestSecond(firstRowByTypePrd, detail);
			}

			runExpansionMethods(mode, detailDao, firstRowByTypePrd, failures);

			return failures;
		} finally {
			ctx.close();
		}
	}

	// =====================================================================
	// 6 expansion methods — ใช้แถวตัวแทนที่เก็บจาก static case ด้านบน (discovery)
	// ไม่ได้ query แบบกว้างแยกต่างหาก เพื่อไม่เพิ่มโหลดให้ PRD เกินจำเป็น
	// =====================================================================
	private static void runExpansionMethods(String mode, PCMSDetailDao detailDao,
			Map<String, ProbeRow> firstRowByTypePrd, List<String> failures) throws Exception {

		ProbeRow main = firstRowByTypePrd.get("Main");
		ProbeRow orderPuang = firstRowByTypePrd.get("OrderPuang");
		ProbeRow switchType = firstRowByTypePrd.get("Switch");
		ProbeRow replaced = firstRowByTypePrd.get("Replaced");
		ProbeRow waitLot = firstRowByTypePrd.get("WaitLot");
		ProbeRow anyNormal = main != null ? main : (orderPuang != null ? orderPuang : switchType);

		if (anyNormal != null) {
			PCMSSecondTableDetail probe = secondDetailWithProductionOrder(anyNormal.productionOrder);
			callAndVerify(mode, "getNormalCaseByProdOrder_ProductionOrder", failures,
					() -> detailDao.getNormalCaseByProdOrder("ProductionOrder", singletonSecond(probe)));
		} else {
			skip(failures, "getNormalCaseByProdOrder_ProductionOrder",
					"ไม่มีแถว Main/OrderPuang/Switch จาก static case ให้ discover ProductionOrder");
		}

		if (replaced != null) {
			PCMSSecondTableDetail probeRP = new PCMSSecondTableDetail();
			probeRP.setProductionOrderRP(replaced.productionOrder);
			callAndVerify(mode, "getNormalCaseByProdOrder_ProductionOrderRP", failures,
					() -> detailDao.getNormalCaseByProdOrder("ProductionOrderRP", singletonSecond(probeRP)));

			PCMSSecondTableDetail probeRP2 = new PCMSSecondTableDetail();
			probeRP2.setProductionOrderRP(replaced.productionOrder);
			callAndVerify(mode, "getReplacedCaseByProdOrder_ProductionOrderRP", failures,
					() -> detailDao.getReplacedCaseByProdOrder("ProductionOrderRP", singletonSecond(probeRP2)));
		} else {
			skip(failures, "getNormalCaseByProdOrder_ProductionOrderRP",
					"ไม่มีแถว Replaced จาก static case ให้ discover ProductionOrderRP");
			skip(failures, "getReplacedCaseByProdOrder_ProductionOrderRP",
					"ไม่มีแถว Replaced จาก static case ให้ discover ProductionOrderRP");
		}

		if (anyNormal != null && anyNormal.saleOrder != null && anyNormal.saleLine != null) {
			PCMSSecondTableDetail probe = secondDetailWithProductionOrder(anyNormal.productionOrder);
			probe.setSaleOrder(anyNormal.saleOrder);
			probe.setSaleLine(anyNormal.saleLine);
			callAndVerify(mode, "getReplacedCaseByProdOrder_ProductionOrder", failures,
					() -> detailDao.getReplacedCaseByProdOrder("ProductionOrder", singletonSecond(probe)));
		} else {
			skip(failures, "getReplacedCaseByProdOrder_ProductionOrder",
					"ไม่มีแถว Main/OrderPuang/Switch พร้อม SaleOrder/SaleLine ให้ discover");
		}

		if (switchType != null) {
			PCMSSecondTableDetail probe = secondDetailWithProductionOrder(switchType.productionOrder);
			callAndVerify(mode, "getSwitchProdOrderListByPrd", failures,
					() -> detailDao.getSwitchProdOrderListByPrd(singletonSecond(probe)));
		} else {
			skip(failures, "getSwitchProdOrderListByPrd", "ไม่มีแถว Switch จาก static case ให้ discover");
		}

		if (orderPuang != null) {
			PCMSSecondTableDetail probe1 = secondDetailWithProductionOrder(orderPuang.productionOrder);
			callAndVerify(mode, "getOrderPuangListByPrd", failures,
					() -> detailDao.getOrderPuangListByPrd(singletonSecond(probe1)));

			// หมายเหตุ: หลัง UNION แล้ว OrderPuang กับ OrderPuang+Switch ใช้ TypePrd='OrderPuang'
			// เหมือนกัน แยกกันไม่ได้จากผลลัพธ์ summary/detail grid เฉยๆ — ใช้แถวเดียวกัน probe
			// ถ้า PO นี้ไม่ได้อยู่ใน #tempSPO จริง ผลลัพธ์ที่ถูกต้องคือ "ว่าง" ก็ยังนับเป็น
			// regression signal ที่ใช้ได้ (ว่าง=ว่าง เทียบ baseline ได้ปกติ)
			PCMSSecondTableDetail probe2 = secondDetailWithProductionOrder(orderPuang.productionOrder);
			callAndVerify(mode, "getOrderPuangSWListByPrd", failures,
					() -> detailDao.getOrderPuangSWListByPrd(singletonSecond(probe2)));
		} else {
			skip(failures, "getOrderPuangListByPrd", "ไม่มีแถว OrderPuang จาก static case ให้ discover");
			skip(failures, "getOrderPuangSWListByPrd", "ไม่มีแถว OrderPuang จาก static case ให้ discover");
		}

		if (waitLot != null && waitLot.saleOrder != null) {
			PCMSSecondTableDetail probe = new PCMSSecondTableDetail();
			probe.setSaleOrder(waitLot.saleOrder);
			probe.setSaleLine(waitLot.saleLine);
			ArrayList<PCMSSecondTableDetail> listRP = new ArrayList<>();
			listRP.add(probe);
			callAndVerify(mode, "getWaitLotCaseBySaleOrder", failures,
					() -> detailDao.getWaitLotCaseBySaleOrder(listRP));
		} else {
			skip(failures, "getWaitLotCaseBySaleOrder", "ไม่มีแถว WaitLot จาก static case ให้ discover");
		}
	}

	private static PCMSSecondTableDetail secondDetailWithProductionOrder(String productionOrder) {
		PCMSSecondTableDetail bean = new PCMSSecondTableDetail();
		bean.setProductionOrder(productionOrder);
		return bean;
	}

	private static void skip(List<String> failures, String caseLabel, String reason) {
		// ไม่ใช่ failure — แค่ log ให้เห็นชัดว่าข้ามอะไรไปเพราะอะไร (ไม่ silent cap)
		System.out.println("[harness] SKIP " + caseLabel + " — " + reason);
	}

	// =====================================================================
	// Harvest แถวตัวแทนจาก summary/detail grid เพื่อป้อน 6 expansion methods
	// =====================================================================

	private static final class ProbeRow {
		final String saleOrder;
		final String saleLine;
		final String productionOrder;

		ProbeRow(String saleOrder, String saleLine, String productionOrder) {
			this.saleOrder = saleOrder;
			this.saleLine = saleLine;
			this.productionOrder = productionOrder;
		}
	}

	/**
	 * เลือกแถวตัวแทนแรกต่อ TypePrd แบบ <b>deterministic</b> — sort candidate ด้วย key คงที่
	 * (ProductionOrder, SaleOrder, SaleLine) ก่อนเลือก มิฉะนั้น "แถวแรก" จะขึ้นกับลำดับ physical ที่
	 * tie-reorder ได้ (ดู SqlOutputCanonicalizer) → probe input ของ 6 expansion method จะไม่แน่นอน
	 * และทำให้ baseline ของ method เหล่านั้น flaky
	 */
	private static void harvest(Map<String, ProbeRow> bucket, List<PCMSTableDetail> rows) {
		List<PCMSTableDetail> sorted = new ArrayList<>(rows);
		Collections.sort(sorted, new Comparator<PCMSTableDetail>() {
			@Override
			public int compare(PCMSTableDetail a, PCMSTableDetail b) {
				int c = safe(a.getProductionOrder()).compareTo(safe(b.getProductionOrder()));
				if (c != 0) return c;
				c = safe(a.getSaleOrder()).compareTo(safe(b.getSaleOrder()));
				if (c != 0) return c;
				return safe(a.getSaleLine()).compareTo(safe(b.getSaleLine()));
			}
		});
		for (PCMSTableDetail row : sorted) {
			String typePrd = row.getTypePrd();
			if (typePrd == null || bucket.containsKey(typePrd)) {
				continue;
			}
			if (row.getProductionOrder() == null || row.getProductionOrder().trim().isEmpty()) {
				continue;
			}
			bucket.put(typePrd, new ProbeRow(row.getSaleOrder(), row.getSaleLine(), row.getProductionOrder()));
		}
	}

	private static String safe(String s) {
		return s == null ? "" : s;
	}

	private static void harvestSecond(Map<String, ProbeRow> bucket, List<PCMSSecondTableDetail> rows) {
		// PCMSSecondTableDetail ไม่มี TypePrd ในโค้ดที่ตรวจ (ไม่ได้ map field นี้จาก selectAll ใน
		// PCMSDetailDaoImpl) จึงไม่ harvest จากผลของ searchByDetail — ใช้ผลจาก
		// getPCMSSumaryDetail (harvest ด้านบน) เป็นแหล่งเดียวสำหรับ discovery ก็เพียงพอแล้ว
	}

	// =====================================================================
	// เรียก DAO method 1 ครั้ง + capture/verify เทียบ baseline + log เวลาที่ใช้
	// =====================================================================

	@FunctionalInterface
	private interface DaoCall<T> {
		List<T> call() throws Exception;
	}

	private static <T> List<T> callAndVerify(String mode, String caseLabel, List<String> failures,
			DaoCall<T> call) throws Exception {
		long start = System.currentTimeMillis();
		List<T> result;
		try {
			result = call.call();
		} catch (Exception e) {
			failures.add(caseLabel + " — DAO call throw exception: " + e);
			System.out.println("[harness] FAIL " + caseLabel + " (exception, ดู failures ท้าย log)");
			return new ArrayList<>();
		}
		long elapsedMs = System.currentTimeMillis() - start;
		System.out.printf("[harness] %-55s rows=%-5d %6dms%n", caseLabel, result.size(), elapsedMs);

		Path baselineFile = BASELINE_DIR.resolve(caseLabel + ".txt");
		String actual = SqlOutputCanonicalizer.canonicalize(result);

		if (mode.equals("capture")) {
			if (SqlOutputCanonicalizer.exists(baselineFile)) {
				System.out.println("[harness]   (เขียนทับ baseline เดิม: " + baselineFile + ")");
			}
			SqlOutputCanonicalizer.writeTo(baselineFile, result);
		} else {
			if (!SqlOutputCanonicalizer.exists(baselineFile)) {
				failures.add(caseLabel + " — ไม่มี baseline ที่ " + baselineFile + " (รัน mode capture ก่อน)");
			} else {
				String expected = SqlOutputCanonicalizer.readFrom(baselineFile);
				if (!expected.equals(actual)) {
					failures.add(caseLabel + " — ผล diff จาก baseline (" + baselineFile + ")");
				}
			}
		}
		return result;
	}

	private static ArrayList<PCMSTableDetail> singleton(PCMSTableDetail bean) {
		ArrayList<PCMSTableDetail> list = new ArrayList<>();
		list.add(bean);
		return list;
	}

	private static ArrayList<PCMSSecondTableDetail> singletonSecond(PCMSSecondTableDetail bean) {
		ArrayList<PCMSSecondTableDetail> list = new ArrayList<>();
		list.add(bean);
		return list;
	}
}
