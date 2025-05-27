package th.co.wacoal.atech.pcms2.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.entities.erp.atech.Z_ATT_CustomerConfirm2Detail;
import th.co.wacoal.atech.pcms2.model.master.Z_ATT_CustomerConfirm2Model;
import th.co.wacoal.atech.pcms2.service.CustomDateDeserializer;
import th.co.wacoal.atech.pcms2.service.ExportExcelCFMReportService; 

@Controller
@RequestMapping({ "/Report" })
public class ReportController {
	@SuppressWarnings("unused") 
	private ApplicationContext appContext;
//	private ReportSplitWorkModel rswModel;
//	private UserStatusDetailModel usModel;
//	private LabStatusDetailModel lsModel;
//    @Autowired
//    public ReportController(
//    		ReportSplitWorkModel rswModel,
//    		UserStatusDetailModel usModel,
//    		LabStatusDetailModel lsModel) {
//        this.rswModel = rswModel;
//        this.usModel = usModel;
//        this.lsModel = lsModel;
//    } 
    @Autowired
	public ReportController( ) { 
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
					mv.setViewName("Report/CFMReport"); 
					mv.addObject("OS", g.toJson(OS));
					mv.addObject("HeaderName", reportType);
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

//	@RequestMapping(value = "/{ReportType}/getReportDetail", method = RequestMethod.POST)
	@RequestMapping(value = "/{ReportType}/getReportDetail", method = RequestMethod.GET)
	public void doGetReportDetail(HttpSession session, HttpServletRequest request,
			HttpServletResponse response
			, @PathVariable("ReportType") String reportType
//			, @RequestBody String data
			, @RequestParam("data") String data 
			) throws IOException
	{ 
//		System.out.println(data);
		Gson g = new Gson(); 
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = null;
		ArrayList<Z_ATT_CustomerConfirm2Detail> resultList = null;
		if(reportType.equals("CFM")) { 
			Z_ATT_CustomerConfirm2Model zccModel = new Z_ATT_CustomerConfirm2Model();
	        // กำหนด Type สำหรับ ArrayList
	        Type listType = new TypeToken<ArrayList<Z_ATT_CustomerConfirm2Detail>>() {}.getType(); 
	        // แปลง JSON Array เป็น ArrayList
	        list = new Gson().fromJson(data, listType); 
	        Z_ATT_CustomerConfirm2Detail bean = list.get(0);
	        resultList = zccModel.getZ_ATT_CustomerConfirm2DetailByProductionOrder(
	        				bean.getProdID()
	        				, bean.getLotNo()
	        				, bean.getReplyDateRange()
	        				,bean.getCustomerName()
	        				,bean.getSo()
	        				,bean.getSendDateRange());
		} 
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(resultList));
	} 

 
	@RequestMapping(value = "/{ReportType}/createExcelReportDetail", method = { RequestMethod.POST }) 
	public void exportExcel(HttpSession session, HttpServletRequest request, HttpServletResponse response
			, @RequestBody String data
			, @PathVariable("ReportType") String reportType
//			,@RequestParam("data") String data
			) throws Exception {  
		Gson g = new GsonBuilder()
		        .registerTypeAdapter(Date.class, new CustomDateDeserializer())
		        .create(); 
		ArrayList<Z_ATT_CustomerConfirm2Detail> list = null;  
		if (reportType.equals("CFM")) {
		    Type listType = new TypeToken<ArrayList<Z_ATT_CustomerConfirm2Detail>>() {}.getType();
		    list = g.fromJson(data, listType);
		} 
		ExportExcelCFMReportService ex = new ExportExcelCFMReportService(); 
		ex.export(response, list);
	} 
}