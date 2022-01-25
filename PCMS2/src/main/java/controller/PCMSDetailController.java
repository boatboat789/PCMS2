package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import entities.ColumnHiddenDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import model.PCMSDetailModel;
import model.PCMSMainModel;
import model.PCMSDetailModel;

@Controller
@RequestMapping(value = { "/PCMSDetail" }) 
public class PCMSDetailController {
	@Autowired
	private ServletContext context;  
	private String LOCAL_DIRECTORY;
	private String FTP_DIRECTORY; 
	@RequestMapping(method = { RequestMethod.GET })
	public ModelAndView test(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		Gson g = new Gson();
		String user = (String) session.getAttribute("user");
		PCMSDetailModel model = new PCMSDetailModel();
		 ArrayList<ColumnHiddenDetail> list = model.getColVisibleDetail(user);
		 String[] arrayCol = null  ;      
		 if(list.size() == 0) { 
			 ColumnHiddenDetail bean = new ColumnHiddenDetail();
			 arrayCol = bean.getColVisibleDetail().split(",");
			 } 
		 else {  arrayCol = list.get(0).getColVisibleDetail().split(","); }   
		mv.setViewName("PCMSDetail/PCMSDetail"); 
		mv.addObject("SaleNumberList", g.toJson(model.getSaleNumberList()));
		mv.addObject("ColList", g.toJson(arrayCol));
		mv.addObject("UserStatusList", g.toJson(model.getUserStatusList()));
		return mv;
	}    
	
	@RequestMapping(  value = "/searchByDetail",  method = RequestMethod.POST )
	public void doGetSearchByDetail(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PCMSDetailModel model = new PCMSDetailModel();
		 
		Gson g = new Gson(); 
		PCMSTableDetail[] userArray = (PCMSTableDetail[]) g.fromJson(data, PCMSTableDetail[].class);
		ArrayList<PCMSTableDetail> poList = new ArrayList<PCMSTableDetail>();
		int i = 0; 
		for (i = 0; i < userArray.length; i++) {
			PCMSTableDetail pd = new PCMSTableDetail();
			pd.setCustomerName(userArray[i].getCustomerName());
			pd.setCustomerShortName(userArray[i].getCustomerShortName());
			pd.setSaleNumber(userArray[i].getSaleNumber());
			pd.setSaleOrder(userArray[i].getSaleOrder());  
			pd.setProductionOrder(userArray[i].getProductionOrder());  
			pd.setArticleFG(userArray[i].getArticleFG()); 
			pd.setDesignFG(userArray[i].getDesignFG()); 
			pd.setSaleOrder(userArray[i].getSaleOrder()); 
			pd.setSaleOrderCreateDate(userArray[i].getSaleOrderCreateDate()) ;
			pd.setProductionOrderCreateDate(userArray[i].getProductionOrderCreateDate());
			pd.setMaterialNo(userArray[i].getMaterialNo());
			pd.setLabNo(userArray[i].getLabNo());
			pd.setUserStatus(userArray[i].getUserStatus());
			pd.setDeliveryStatus(userArray[i].getDeliveryStatus());
			pd.setDistChannel(userArray[i].getDistChannel());
			pd.setSaleStatus(userArray[i].getSaleStatus());
			poList.add(pd);   
		} 
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
//		System.out.println("he");
		out.println(g.toJson(model.searchByDetail( poList)));  
	}
	@RequestMapping(  value = "/saveInputDate",  method = RequestMethod.POST )
	public void doGetSaveInputDate(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PCMSDetailModel model = new PCMSDetailModel();
		String user = (String) session.getAttribute("user");
		Gson g = new Gson(); 
		PCMSSecondTableDetail[] userArray = (PCMSSecondTableDetail[]) g.fromJson(data, PCMSSecondTableDetail[].class);
		ArrayList<PCMSSecondTableDetail> poList = new ArrayList<PCMSSecondTableDetail>();
		int i = 0; 
		for (i = 0; i < userArray.length; i++) {
			PCMSSecondTableDetail pd = new PCMSSecondTableDetail();
			pd.setProductionOrder(userArray[i].getProductionOrder());
			pd.setSaleOrder(userArray[i].getSaleOrder());
			pd.setSaleLine(userArray[i].getSaleLine());
			pd.setCFMPlanLabDate(userArray[i].getCFMPlanLabDate());
			pd.setCFMPlanDate(userArray[i].getCFMPlanDate());  
			pd.setDeliveryDate(userArray[i].getDeliveryDate());   
			pd.setCaseSave(userArray[i].getCaseSave());   
			pd.setUserId(user);
			poList.add(pd);   
		}  
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
//		System.out.println("he");
		out.println(g.toJson(model.saveInputDate( poList)));  
	}
	@RequestMapping(  value = "/getCFMPlanDateDetail",  method = RequestMethod.POST )
	public void doGetCFMPlanDate(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PCMSDetailModel model = new PCMSDetailModel();
		String user = (String) session.getAttribute("user");
		Gson g = new Gson(); 
		PCMSSecondTableDetail[] userArray = (PCMSSecondTableDetail[]) g.fromJson(data, PCMSSecondTableDetail[].class);
		ArrayList<PCMSSecondTableDetail> poList = new ArrayList<PCMSSecondTableDetail>();
		int i = 0; 
		for (i = 0; i < userArray.length; i++) {
			PCMSSecondTableDetail pd = new PCMSSecondTableDetail();
			pd.setProductionOrder(userArray[i].getProductionOrder());
			pd.setSaleOrder(userArray[i].getSaleOrder());
			pd.setSaleLine(userArray[i].getSaleLine());
			pd.setCFMPlanLabDate(userArray[i].getCFMPlanLabDate());
			pd.setCFMPlanDate(userArray[i].getCFMPlanDate());  
			pd.setDeliveryDate(userArray[i].getDeliveryDate());   
			pd.setCaseSave(userArray[i].getCaseSave());   
			pd.setUserId(user);
			poList.add(pd);   
		}  
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
//		System.out.println("he");
		out.println(g.toJson(model.getCFMPlanDateDetail( poList)));  
	}
	@RequestMapping(  value = "/getCFMPlanLabDateDetail",  method = RequestMethod.POST )
	public void doGetCFMPlanLabDate(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PCMSDetailModel model = new PCMSDetailModel();
		String user = (String) session.getAttribute("user");
		Gson g = new Gson(); 
		PCMSSecondTableDetail[] userArray = (PCMSSecondTableDetail[]) g.fromJson(data, PCMSSecondTableDetail[].class);
		ArrayList<PCMSSecondTableDetail> poList = new ArrayList<PCMSSecondTableDetail>();
		int i = 0; 
		for (i = 0; i < userArray.length; i++) {
			PCMSSecondTableDetail pd = new PCMSSecondTableDetail();
			pd.setProductionOrder(userArray[i].getProductionOrder());
			pd.setSaleOrder(userArray[i].getSaleOrder());
			pd.setSaleLine(userArray[i].getSaleLine());
			pd.setCFMPlanLabDate(userArray[i].getCFMPlanLabDate());
			pd.setCFMPlanDate(userArray[i].getCFMPlanDate());  
			pd.setDeliveryDate(userArray[i].getDeliveryDate());   
			pd.setCaseSave(userArray[i].getCaseSave());   
			pd.setUserId(user);
			poList.add(pd);   
		}  
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
//		System.out.println("he");
		out.println(g.toJson(model.getCFMPlanLabDateDetail( poList)));  
	}
	@RequestMapping(  value = "/getDeliveryPlanDateDetail",  method = RequestMethod.POST )
	public void doGetDeliveryDate(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PCMSDetailModel model = new PCMSDetailModel();
		String user = (String) session.getAttribute("user");
		Gson g = new Gson(); 
		PCMSSecondTableDetail[] userArray = (PCMSSecondTableDetail[]) g.fromJson(data, PCMSSecondTableDetail[].class);
		ArrayList<PCMSSecondTableDetail> poList = new ArrayList<PCMSSecondTableDetail>();
		int i = 0; 
		for (i = 0; i < userArray.length; i++) {
			PCMSSecondTableDetail pd = new PCMSSecondTableDetail();
			pd.setProductionOrder(userArray[i].getProductionOrder());
			pd.setSaleOrder(userArray[i].getSaleOrder());
			pd.setSaleLine(userArray[i].getSaleLine());
			pd.setCFMPlanLabDate(userArray[i].getCFMPlanLabDate());
			pd.setCFMPlanDate(userArray[i].getCFMPlanDate());  
			pd.setDeliveryDate(userArray[i].getDeliveryDate());   
			pd.setCaseSave(userArray[i].getCaseSave());   
			pd.setUserId(user);
			poList.add(pd);   
		}  
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
//		System.out.println("he");
		out.println(g.toJson(model.getDeliveryPlanDateDetail( poList)));  
	}
	@RequestMapping(  value = "/saveColSettingToServer",  method = RequestMethod.POST )
	public void doSaveColSettingToServer(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PCMSDetailModel model = new PCMSDetailModel();
		String user = (String) session.getAttribute("user");
		Gson g = new Gson(); 
		String [] userArray = (String[]) g.fromJson(data, String[].class);
		ArrayList<ColumnHiddenDetail> poList = new ArrayList<ColumnHiddenDetail>();
		int i = 0; 
		String colVisible = "";
		for (i = 0; i < userArray.length; i++) { 
			colVisible += userArray[i];
			if(i!= userArray.length - 1) {
				colVisible +=",";
			} 
		}  
		ColumnHiddenDetail pd = new ColumnHiddenDetail(); 
		pd.setUserId(user);
		pd.setColVisibleDetail(colVisible);
		poList.add(pd);   
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
//		System.out.println("he");
		out.println(g.toJson(model.saveColSettingToServer( pd)));  
//		out.println();  
	}
}
