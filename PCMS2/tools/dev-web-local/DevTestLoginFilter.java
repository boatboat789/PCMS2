package devtest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;

import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.entities.UserDetail;
import th.co.wacoal.atech.pcms2.service.TaskService;
import th.co.wacoal.atech.pcms2.service.master.PermitsService;

/**
 * ตัวช่วยทดสอบบน DEV เท่านั้น — ไม่ใช่โค้ดของแอป (jar ใน WEB-INF/lib ของสำเนาที่ explode นอก repo
 * และใส่ filter นี้ก่อน FilterLogin ใน web.xml ของสำเนา)
 *  1) ข้ามล็อกอิน AD: ตั้ง session user/userObject/permit เอง (ผู้ใช้ = ZZTEST, สิทธิ์ตาม init-param permitId)
 *  2) ปิด scheduled jobs (TaskService.setScheduleEnabled(false)) ทันทีที่ Spring context พร้อม
 * ปฏิเสธถ้าไม่ได้เรียกจาก localhost
 */
public class DevTestLoginFilter implements Filter {

	private String userId = "ZZTEST";
	private String permitId = "VIEWONLY"; // DEV ของ PCMS2 มีแค่ CUST01/CUST02/VIEWONLY
	private volatile boolean scheduleOff = false;
	private ServletContext ctx;

	@Override
	public void init(FilterConfig cfg) throws ServletException
	{
		ctx = cfg.getServletContext();
		if (cfg.getInitParameter("userId") != null) userId = cfg.getInitParameter("userId");
		if (cfg.getInitParameter("permitId") != null) permitId = cfg.getInitParameter("permitId");
		Thread t = new Thread(() -> {
			for (int i = 0; i < 300 && !scheduleOff; i++) {
				try {
					Thread.sleep(500);
					ApplicationContext ac = springContext();
					if (ac != null) {
						ac.getBean(TaskService.class).setScheduleEnabled(false);
						scheduleOff = true;
						System.out.println("[DevTest] scheduled jobs OFF (TaskService.scheduleEnabled=false)");
					}
				} catch (Exception e) {
					// ยังไม่พร้อม ลองใหม่
				}
			}
		}, "devtest-schedule-off");
		t.setDaemon(true);
		t.start();
	}

	private ApplicationContext springContext()
	{
		// controller/service beans อยู่ใน context ของ DispatcherServlet "PCMS2" ไม่ใช่ root context
		Object o = ctx.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.PCMS2");
		return (o instanceof ApplicationContext) ? (ApplicationContext) o : null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		String addr = req.getRemoteAddr();
		if (!("127.0.0.1".equals(addr) || "0:0:0:0:0:0:0:1".equals(addr) || "::1".equals(addr))) {
			((javax.servlet.http.HttpServletResponse) response).sendError(403, "dev-test filter: localhost only");
			return;
		}
		HttpSession ses = req.getSession(true);
		ApplicationContext ac = springContext();
		if ((ses.getAttribute("user") == null || ses.getAttribute("userObject") == null || ses.getAttribute("permit") == null) && ac != null) {
			UserDetail u = new UserDetail();
			u.setUserId(userId);
			u.setFirstName("DevTest");
			u.setLastName("Tester");
			u.setUserType("USER");
			u.setPermitId(permitId);
			ses.setAttribute("user", userId);
			ses.setAttribute("userObject", u);
			java.util.List<PermitDetail> pm = ac.getBean(PermitsService.class).getPermitsDetailByPermitId(permitId);
			if (pm.isEmpty()) {
				System.out.println("[DevTest] ไม่พบ permitId=" + permitId + " ใน DEV");
			} else {
				ses.setAttribute("permit", pm.get(0));
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{
	}
}
