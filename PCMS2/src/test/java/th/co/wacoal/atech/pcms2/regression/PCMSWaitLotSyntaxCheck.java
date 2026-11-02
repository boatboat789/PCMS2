package th.co.wacoal.atech.pcms2.regression;

import java.util.ArrayList;

import org.junit.Assume;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import th.co.wacoal.atech.pcms2.dao.PCMSDetailDao;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;

import static org.junit.Assert.assertNotNull;

/**
 * เช็คว่า SQL ของ getWaitLotCaseBySaleOrder parse/รันผ่าน (ไม่ error) แม้ไม่มีข้อมูล WaitLot —
 * ใช้ SaleOrder/SaleLine ที่ไม่มีจริง คาดว่าได้ 0 แถว · SELECT ล้วน (สร้างแค่ #temp)
 * รัน: mvn test -Dtest=PCMSWaitLotSyntaxCheck -Dpcms.harness.enabled=true
 */
public class PCMSWaitLotSyntaxCheck {

	@Test
	public void waitLotSqlParses() {
		Assume.assumeTrue(Boolean.getBoolean("pcms.harness.enabled"));
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("pcms2-harness-context.xml");
		try {
			PCMSDetailDao dao = ctx.getBean(PCMSDetailDao.class);
			PCMSSecondTableDetail probe = new PCMSSecondTableDetail();
			probe.setSaleOrder("9999999999");
			probe.setSaleLine("000010");
			ArrayList<PCMSSecondTableDetail> in = new ArrayList<>();
			in.add(probe);
			ArrayList<PCMSSecondTableDetail> out = dao.getWaitLotCaseBySaleOrder(in);
			assertNotNull(out);
			System.out.println("[WaitLotSyntaxCheck] OK rows=" + out.size());
		} finally {
			ctx.close();
		}
	}
}
