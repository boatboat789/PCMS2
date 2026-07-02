package th.co.wacoal.atech.pcms2.regression;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;

/**
 * โหลด search case ของ harness จาก JSON ใน src/test/resources/pcms2/regression/cases/
 *
 * แต่ละไฟล์ = 1 case: {"name","description","enabled","bean": {...PCMSTableDetail fields...}}
 * "bean" ไม่ต้องระบุครบทุก field — {@link #normalize} จะเติมค่า default ("" / list ว่าง) ให้
 * field ที่ PCMSSqlService.buildWhereClauses ต้องการแบบ non-null เสมอ (มิฉะนั้น NPE) ก่อนคืนค่า
 */
public final class PCMSHarnessCase {

	private static final Gson GSON = new GsonBuilder().create();

	public final String name;
	public final String description;
	public final boolean enabled;
	public final PCMSTableDetail bean;

	private PCMSHarnessCase(String name, String description, boolean enabled, PCMSTableDetail bean) {
		this.name = name;
		this.description = description;
		this.enabled = enabled;
		this.bean = bean;
	}

	/** shape ของ JSON — mapping ตรงตัวผ่าน Gson (field name ต้องตรงกับ PCMSTableDetail) */
	private static final class Raw {
		String name;
		String description;
		Boolean enabled;
		PCMSTableDetail bean;
	}

	/** โหลดทุก case จากโฟลเดอร์ เรียงตามชื่อไฟล์ (ตั้งชื่อไฟล์มี prefix ตัวเลขไว้คุมลำดับ) */
	public static List<PCMSHarnessCase> loadAll(Path casesDir) throws IOException {
		List<PCMSHarnessCase> cases = new ArrayList<>();
		if (!Files.isDirectory(casesDir)) {
			return cases;
		}
		List<Path> files = new ArrayList<>();
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(casesDir, "*.json")) {
			for (Path p : ds) {
				files.add(p);
			}
		}
		Collections.sort(files);
		for (Path file : files) {
			try (Reader r = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
				Raw raw = GSON.fromJson(r, Raw.class);
				if (raw == null || raw.name == null || raw.name.trim().isEmpty()) {
					throw new IllegalStateException("case file ไม่มี \"name\": " + file);
				}
				PCMSTableDetail bean = raw.bean != null ? raw.bean : new PCMSTableDetail();
				normalize(bean);
				boolean enabled = raw.enabled == null || raw.enabled;
				cases.add(new PCMSHarnessCase(raw.name, raw.description, enabled, bean));
			}
		}
		return cases;
	}

	/**
	 * เติม default ให้ field ที่ PCMSSqlService.buildWhereClauses เรียก .isEmpty()/.size()/.split()
	 * ตรงๆ โดยไม่เช็ค null ก่อน — ถ้าเว้นว่างไว้ใน JSON จะ NPE ตอนรันจริง จึงเติมให้ปลอดภัยที่นี่
	 */
	static void normalize(PCMSTableDetail bean) {
		if (bean.getSaleNumber() == null) bean.setSaleNumber("");
		if (bean.getMaterialNo() == null) bean.setMaterialNo("");
		if (bean.getSaleOrder() == null) bean.setSaleOrder("");
		if (bean.getSaleOrderCreateDate() == null) bean.setSaleOrderCreateDate("");
		if (bean.getLabNo() == null) bean.setLabNo("");
		if (bean.getArticleFG() == null) bean.setArticleFG("");
		if (bean.getDesignFG() == null) bean.setDesignFG("");
		if (bean.getPurchaseOrder() == null) bean.setPurchaseOrder("");
		if (bean.getProductionOrder() == null) bean.setProductionOrder("");
		if (bean.getProductionOrderCreateDate() == null) bean.setProductionOrderCreateDate("");
		if (bean.getDueDate() == null) bean.setDueDate("");
		if (bean.getDeliveryStatus() == null) bean.setDeliveryStatus("");
		if (bean.getSaleStatus() == null) bean.setSaleStatus("");
		if (bean.getDistChannel() == null) bean.setDistChannel("DM|EX|HW");
		if (bean.getUserStatusList() == null) bean.setUserStatusList(new ArrayList<>());
		if (bean.getDivisionList() == null) bean.setDivisionList(new ArrayList<>());
		if (bean.getCustomerNameList() == null) bean.setCustomerNameList(new ArrayList<>());
		if (bean.getCustomerShortNameList() == null) bean.setCustomerShortNameList(new ArrayList<>());
	}
}
