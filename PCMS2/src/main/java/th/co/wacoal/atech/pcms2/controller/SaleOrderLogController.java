package th.co.wacoal.atech.pcms2.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import th.co.wacoal.atech.pcms2.entities.ApiResponse;
import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.entities.SaleOrderLogDetail;
import th.co.wacoal.atech.pcms2.service.master.FromSapMainSaleService;
import th.co.wacoal.atech.pcms2.service.master.erp.atech.ERPAtechService;  
 
    
@Controller
@RequestMapping(value = { "/Log/SaleOrderLog"  })
//@RequestMapping(value = { "/Log/ATT/ProdOperationLog" })
public class SaleOrderLogController {
	@SuppressWarnings("unused") 
	private ServletContext context;
	private FromSapMainSaleService fsmpModel;  
	private ERPAtechService erpService;  
    @Autowired
    public SaleOrderLogController(ERPAtechService erpService
    		,FromSapMainSaleService fsmpModel) { 
    	this.fsmpModel = fsmpModel;
    	this.erpService = erpService;
    }
	@RequestMapping(  value = "/{dataType}",  method = RequestMethod.GET )
	public ModelAndView getModelAndView(HttpSession session,@PathVariable("dataType") String dataType) {
		ModelAndView mv = new ModelAndView();
		if ( ! canViewLog(session)) {
			mv.setViewName("error/AccessDenied");
			mv.addObject("errorMsg", "You do not have permission to access this page.");
			return mv;
		}
		Gson g = new Gson();
		String user = (String) session.getAttribute("user");
		String titleName = "";
		if(dataType.equals("ERP365")) {titleName = "Log - ข้อมูล SaleOrder จาก View บน ERP365";}
		else {titleName = "Log - ข้อมูล SaleOrder ที่รับค่ามาอัพเดทแล้ว";}
		mv.setViewName("LogInformation/SaleOrderLog");
		mv.addObject("UserID", g.toJson(user));
		mv.addObject("titleName", g.toJson(titleName));
		mv.addObject("dataType", g.toJson(dataType));
//		mv.addObject("UserID", g.toJson());'Log - ข้อมูล Prod/Opertation ที่อัพเดทจาก ERP365'
		return mv;
	}  
	/** Log ใช้สิทธิ์ Report เดียวกับเมนู (ซ่อนอยู่ใต้ Report) — endpoint เรียกตรงได้ จึงต้องเช็คซ้ำ */
	private boolean canViewLog(HttpSession session)
	{
		PermitDetail permit = (PermitDetail) session.getAttribute("permit");
		return session.getAttribute("user") != null && permit != null && permit.isReport();
	}
	private static final int MAX_LOG_ROWS = 50000;
	@RequestMapping(  value = "/{dataType}/getSaleOrderLogBySearch",  method = RequestMethod.GET )
	public ResponseEntity<ApiResponse<List<SaleOrderLogDetail>>> doGetProdOperationLogBySearch(HttpSession session,HttpServletRequest request, HttpServletResponse response   
			,@RequestParam("saleOrder") String saleOrder 
			,@RequestParam("changeDateStart") String changeDateStart 
			,@RequestParam("changeDateEnd") String changeDateEnd 
//			, @RequestBody List<SearchCriteria> list 
			,@PathVariable("dataType") String dataType) throws IOException {  
		if ( ! canViewLog(session)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<List<SaleOrderLogDetail>>("error", "ไม่มีสิทธิ์ใช้งานหน้านี้", new ArrayList<SaleOrderLogDetail>()));
		}
        ArrayList<SaleOrderLogDetail> resultList;
	    String status = "success";
	    String message = "ค้นหาสำเร็จ";
	    try {
			if(dataType.equals("ERP365")) { 
	        	resultList = erpService.getFromErpMainSaleDetailWithRangeOfChangeDate( changeDateStart,changeDateEnd,saleOrder);
	        }
	        else { 
	        	resultList = fsmpModel.getFromSapMainSaleDetailWithRangeOfChangeDate(changeDateStart,changeDateEnd,saleOrder);
	        }
		    if (resultList != null && resultList.size() > MAX_LOG_ROWS) {
		    	resultList = new ArrayList<>(resultList.subList(0, MAX_LOG_ROWS));
		    	message = "แสดงเพียง " + MAX_LOG_ROWS + " แถวแรก กรุณาแคบเงื่อนไขค้นหา";
		    }
	    } catch (RuntimeException e) {
	    	resultList = new ArrayList<>();
	    	status = "error";
	    	message = "ค้นหาไม่สำเร็จ กรุณาตรวจรูปแบบวันที่ (dd/MM/yyyy) หรือติดต่อทีม IT";
	    }

	    ApiResponse<List<SaleOrderLogDetail>> apiResponse = new ApiResponse<>(status, message, resultList);
	    return ResponseEntity.ok(apiResponse);
	}
	 
}
