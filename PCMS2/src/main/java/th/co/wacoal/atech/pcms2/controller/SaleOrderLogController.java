package th.co.wacoal.atech.pcms2.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import th.co.wacoal.atech.pcms2.entities.ApiResponse; 
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
	@RequestMapping(  value = "/{dataType}/getSaleOrderLogBySearch",  method = RequestMethod.GET )
	public ResponseEntity<ApiResponse<List<SaleOrderLogDetail>>> doGetProdOperationLogBySearch(HttpSession session,HttpServletRequest request, HttpServletResponse response   
			,@RequestParam("saleOrder") String saleOrder 
			,@RequestParam("changeDateStart") String changeDateStart 
			,@RequestParam("changeDateEnd") String changeDateEnd 
//			, @RequestBody List<SearchCriteria> list 
			,@PathVariable("dataType") String dataType) throws IOException {  
        ArrayList<SaleOrderLogDetail> resultList;
		if(dataType.equals("ERP365")) { 
        	resultList = erpService.getFromErpMainSaleDetailWithRangeOfChangeDate( changeDateStart,changeDateEnd,saleOrder);
        }
        else { 
        	resultList = fsmpModel.getFromSapMainSaleDetailWithRangeOfChangeDate(changeDateStart,changeDateEnd,saleOrder);
        }
//	    boolean isSuccess = result.stream().allMatch(bean -> "I".equals(bean.getIconStatus()));
	    boolean isSuccess = true;
	    String status = isSuccess ? "success" : "error";
	    String message = isSuccess ? "บันทึกข้อมูลสำเร็จ" : "บางรายการบันทึกล้มเหลว";

	    ApiResponse<List<SaleOrderLogDetail>> apiResponse = new ApiResponse<>(status, message, resultList);
	    return ResponseEntity.ok(apiResponse);
	}
	 
}
