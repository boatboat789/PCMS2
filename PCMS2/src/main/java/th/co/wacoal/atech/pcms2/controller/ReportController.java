package th.co.wacoal.atech.pcms2.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.Z_ATT_CustomerConfirm2Detail;
import th.co.wacoal.atech.pcms2.service.ExportExcelCFMReportService;
import th.co.wacoal.atech.pcms2.service.master.Z_ATT_CustomerConfirm2Service;

@Controller
@RequestMapping({ "/Report" })
public class ReportController {
	@SuppressWarnings("unused") 
	private ApplicationContext appContext;
	private Z_ATT_CustomerConfirm2Service zccService; 
    @Autowired
	public ReportController(
			Z_ATT_CustomerConfirm2Service zccService   ) { 
    	
    	this.zccService = zccService;
	}
	@RequestMapping(value = "/{ReportType}/Detail", method = { RequestMethod.GET })
	public ModelAndView createReportModel(
			HttpSession session, 
			@PathVariable("ReportType") String reportType)
	{
		ModelAndView mv = new ModelAndView(); 
		Gson g = new Gson();
		String user = (String) session.getAttribute("user");  
		if (user != null) {
			PermitDetail permit = (PermitDetail) session.getAttribute("permit");
			if (permit == null) {
				mv.setViewName("error/AccessDenied"); // Redirect to an access-denied view
				mv.addObject("errorMsg", "Contact IT for set permission first.");
			} else {
				if (permit.isReport()) {  
					String OS = System.getProperty("os.name").toLowerCase();  
					String headerName = "CFM Report";
					if(reportType.equals("CFM")) {
						headerName = "CFM Report";
					}
					mv.setViewName("Report/CFMReport"); 
					mv.addObject("OS", g.toJson(OS));
					mv.addObject("HeaderName", headerName);
					mv.addObject("ReportType", reportType);
					mv.addObject("UserID", g.toJson(user));
				} else {
					mv.setViewName("error/AccessDenied"); // Redirect to an access-denied view
					mv.addObject("errorMsg", "You do not have permission to access this page.");
				}
			}
		} else {
			mv.setViewName("login"); // Redirect to a login page if no user is found
			mv.addObject("alertmsg", "Please log in to access this page.");
			mv.addObject("alerttyp", "User Session Not Found.");
		} 
		return mv;
	}

	/** ต้อง login และมีสิทธิ์ Report เหมือนหน้า HTML — endpoint ข้อมูลเรียกตรงได้ จึงต้องเช็คซ้ำ */
	private boolean canViewReport(HttpSession session)
	{
		if (session.getAttribute("user") == null) {
			return false;
		}
		PermitDetail permit = (PermitDetail) session.getAttribute("permit");
		return permit != null && permit.isReport();
	}

	private void sendText(HttpServletResponse response, int status, String message) throws IOException
	{
		response.setStatus(status);
		response.setContentType("text/plain; charset=UTF-8");
		response.getWriter().print(message);
	}

	private static String nz(String v)
	{
		return v == null ? "" : v.trim();
	}

	@RequestMapping(value = "/{ReportType}/getReportDetail", method = RequestMethod.GET)
	public void doGetReportDetail(HttpSession session, HttpServletRequest request,
			HttpServletResponse response
			, @PathVariable("ReportType") String reportType
			, @RequestParam("data") String data 
			) throws IOException
	{ 
		if ( ! canViewReport(session)) {
			sendText(response, HttpServletResponse.SC_FORBIDDEN, "คุณไม่มีสิทธิ์ใช้รายงานนี้");
			return;
		}
		if ( ! reportType.equals("CFM")) {
			sendText(response, HttpServletResponse.SC_BAD_REQUEST, "ไม่รู้จักประเภทรายงาน");
			return;
		}
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = null;
		try {
			Type listType = new TypeToken<ArrayList<Z_ATT_CustomerConfirm2Detail>>() {}.getType(); 
			list = new Gson().fromJson(data, listType); 
		} catch (RuntimeException e) {
			list = null;
		}
		if (list == null || list.isEmpty() || list.get(0) == null) {
			sendText(response, HttpServletResponse.SC_BAD_REQUEST, "เงื่อนไขค้นหาไม่ถูกต้อง");
			return;
		}
		Z_ATT_CustomerConfirm2Detail bean = list.get(0);
		ArrayList<Z_ATT_CustomerConfirm2Detail> resultList;
		try {
			resultList = zccService.getZ_ATT_CustomerConfirm2DetailByProductionOrder(
					nz(bean.getProdID())
					, nz(bean.getLotNo())
					, nz(bean.getReplyDateRange())
					, nz(bean.getCustomerName())
					, nz(bean.getSo())
					, nz(bean.getSendDateRange()));
		} catch (IllegalArgumentException e) {
			// buildDateClause โยนเมื่อรูปแบบวันที่ไม่ใช่ dd/MM/yyyy [- dd/MM/yyyy]
			sendText(response, HttpServletResponse.SC_BAD_REQUEST, "รูปแบบวันที่ไม่ถูกต้อง (ต้องเป็น dd/MM/yyyy หรือช่วง dd/MM/yyyy - dd/MM/yyyy)");
			return;
		}
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(new Gson().toJson(resultList));
	} 

	private static final int MAX_EXPORT_ROWS = 20000;

	/**
	 * ส่งออก Excel — client ส่งมาเฉพาะ id ของแถวที่ต้องการ (แถวที่เห็นบนจอหลังกรอง) แล้ว server ดึงข้อมูลจริงจาก DB ตาม id เอง
	 * ไม่รับตัวเลขที่ client ส่งกลับมาเขียนลงไฟล์ (กันแก้ตัวเลขก่อนดาวน์โหลด)
	 */
	@RequestMapping(value = "/{ReportType}/createExcelReportDetail", method = { RequestMethod.POST }) 
	public void exportExcel(HttpSession session, HttpServletRequest request, HttpServletResponse response
			, @RequestBody String data
			, @PathVariable("ReportType") String reportType
			) throws Exception {  
		if ( ! canViewReport(session)) {
			sendText(response, HttpServletResponse.SC_FORBIDDEN, "คุณไม่มีสิทธิ์ใช้รายงานนี้");
			return;
		}
		if ( ! reportType.equals("CFM")) {
			sendText(response, HttpServletResponse.SC_BAD_REQUEST, "ไม่รู้จักประเภทรายงาน");
			return;
		}
		ArrayList<Z_ATT_CustomerConfirm2Detail> requested = null;
		try {
			Type listType = new TypeToken<ArrayList<Z_ATT_CustomerConfirm2Detail>>() {}.getType();
			requested = new Gson().fromJson(data, listType);
		} catch (RuntimeException e) {
			requested = null;
		}
		if (requested == null || requested.isEmpty()) {
			sendText(response, HttpServletResponse.SC_BAD_REQUEST, "ไม่มีข้อมูลให้ส่งออก");
			return;
		}
		if (requested.size() > MAX_EXPORT_ROWS) {
			sendText(response, HttpServletResponse.SC_BAD_REQUEST, "ส่งออกได้ไม่เกิน " + MAX_EXPORT_ROWS + " แถวต่อครั้ง กรุณาแคบเงื่อนไขค้นหา");
			return;
		}
		// เก็บเฉพาะ id ที่ไม่ซ้ำและเป็นบวก
		java.util.LinkedHashSet<Integer> ids = new java.util.LinkedHashSet<>();
		for (Z_ATT_CustomerConfirm2Detail r : requested) {
			if (r != null && r.getId() > 0) {
				ids.add(r.getId());
			}
		}
		ArrayList<Z_ATT_CustomerConfirm2Detail> idList = new ArrayList<>();
		for (Integer id : ids) {
			Z_ATT_CustomerConfirm2Detail b = new Z_ATT_CustomerConfirm2Detail();
			b.setId(id);
			idList.add(b);
		}
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = idList.isEmpty() ? new ArrayList<>() : zccService.getZ_ATT_CustomerConfirm2DetailById(idList);
		if (list.isEmpty()) {
			sendText(response, HttpServletResponse.SC_NOT_FOUND, "ไม่พบข้อมูลที่ต้องการส่งออก (อาจถูกปิดไปแล้ว) กรุณาค้นหาใหม่");
			return;
		}
		ExportExcelCFMReportService ex = new ExportExcelCFMReportService(); 
		ex.export(response, list);
	} 
}
