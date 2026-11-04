package th.co.wacoal.atech.pcms2.regression;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

/**
 * แปลงผลลัพธ์ของ DAO method (ArrayList ของ entity bean เช่น PCMSTableDetail /
 * PCMSSecondTableDetail) เป็นข้อความ canonical แบบ deterministic เพื่อ diff เทียบ baseline
 *
 * ระดับที่ diff: bean-level (หลัง BeanCreateService._gen* map แล้ว) ไม่ใช่ raw
 * List&lt;Map&lt;String,Object&gt;&gt; จาก SqlStatementHandler.queryList เพราะ DAO method
 * ปัจจุบันไม่มี seam ให้ดึงค่า raw map ออกมาโดยไม่แก้ production code (ตัดสินใจร่วมกับ user
 * แล้วว่าไม่แตะ production code ใน Phase 0) — bean-level ยังจับ field ที่ map ไว้ได้ครบทุกตัว
 * จุดบอดเดียว: ถ้า SELECT มี column ที่ไม่มี field รองรับเปลี่ยนไป จะไม่เจอ (ความเสี่ยงต่ำ)
 *
 * <b>เปรียบเทียบแบบ MULTISET (sort rows ก่อน diff) — ตั้งใจ ไม่ใช่คงลำดับแถว</b>
 * เหตุผล: final SELECT ORDER BY ของ query พวกนี้ (CustomerShortName, DueDate, SaleOrder,
 * SaleLine, TypePrdRemark, ProductionOrder) <b>ไม่เป็น total order</b> — แถวที่ค่าทั้ง 6 คอลัมน์
 * เท่ากันจะถูก SQL Server คืนมาในลำดับ physical ที่ไม่แน่นอน (โดยเฉพาะ result set ใหญ่ที่เจอ
 * parallel scan) พิสูจน์แล้ว: รัน query เดิม 2 ครั้ง sorted-diff = 0 (multiset เท่ากันเป๊ะ) แต่
 * unsorted-diff ≠ 0 (แค่ลำดับ tie ต่างกัน) → ถ้า diff แบบคงลำดับจะได้ false failure เพราะ
 * non-determinism ที่มีอยู่แล้วใน production ไม่ใช่ regression
 * refactor คง ORDER BY clause ตัวเดิมเป๊ะ → ความถูกต้องของลำดับรับประกันโดย construction อยู่แล้ว
 * harness จึงไม่ต้อง police ลำดับ หน้าที่ harness = จับว่าแถว/ค่า add/remove/change ไหม (multiset จับครบ)
 *
 * - field order ของแต่ละ bean deterministic เพราะ Gson reflect field ตามลำดับประกาศใน class
 *   เดียวกันทั้งตอน capture และตอน verify (ไม่ได้เปลี่ยน entity class ใน refactor นี้)
 * - BigDecimal ใช้ toPlainString() รักษา scale เป๊ะ (scale เปลี่ยน = สัญญาณจริงที่ต้องจับ ไม่ mask)
 * - java.util.Date/Timestamp/java.sql.Date ใช้ toString() ตรงๆ (deterministic ไม่พึ่ง locale)
 * - 1 bean = 1 บรรทัด (compact JSON) แล้ว sort บรรทัด → total order คงที่สำหรับ multiset diff
 */
public final class SqlOutputCanonicalizer {

	private static final Gson GSON = new GsonBuilder()
			.serializeNulls()
			.registerTypeHierarchyAdapter(BigDecimal.class,
					(JsonSerializer<BigDecimal>) (src, type, ctx) -> new JsonPrimitive(src.toPlainString()))
			.registerTypeHierarchyAdapter(Date.class,
					(JsonSerializer<Date>) (src, type, ctx) -> new JsonPrimitive(src.toString()))
			.create();

	private SqlOutputCanonicalizer() {}

	/**
	 * แปลงผลทั้งหมด (list ของ bean) เป็นข้อความ canonical แบบ multiset:
	 * บรรทัดแรก "#rows=N" แล้วตามด้วย compact-JSON ของแต่ละ bean ที่ <b>sort แล้ว</b>
	 * (order-independent — ดู class javadoc ว่าทำไม)
	 */
	public static String canonicalize(List<?> beans) {
		List<String> lines = new ArrayList<>(beans.size());
		for (Object bean : beans) {
			lines.add(GSON.toJson(bean));
		}
		Collections.sort(lines);
		StringBuilder sb = new StringBuilder();
		sb.append("#rows=").append(beans.size()).append('\n');
		for (String line : lines) {
			sb.append(line).append('\n');
		}
		return sb.toString();
	}

	public static void writeTo(Path file, List<?> beans) throws IOException {
		Files.createDirectories(file.getParent());
		Files.write(file, canonicalize(beans).getBytes(StandardCharsets.UTF_8));
	}

	public static boolean exists(Path file) {
		return Files.exists(file);
	}

	public static String readFrom(Path file) throws IOException {
		return new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
	}
}
