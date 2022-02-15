package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import entities.ColumnHiddenDetail;
import entities.PCMSTableDetail;
import model.PCMSDetailModel;
import model.PCMSMainModel;

@Controller
@RequestMapping(value = { "/PCMSMain" }) 
public class PCMSMainController {
	@Autowired
	private ServletContext context;  
	private String LOCAL_DIRECTORY;
	private String FTP_DIRECTORY; 
	@RequestMapping(method = { RequestMethod.GET })
	public ModelAndView test(HttpSession session) {       
		ModelAndView mv = new ModelAndView();
		Gson g = new Gson();
		PCMSMainModel model = new PCMSMainModel();
		String user = (String) session.getAttribute("user");
		 ArrayList<ColumnHiddenDetail> list = model.getColVisibleDetail(user);
		 String[] arrayCol = null  ;
		 if(list.size() == 0) { arrayCol = null  ;} 
		 else {  arrayCol = list.get(0).getColVisibleSummary().split(","); }   
		mv.setViewName("PCMSMain/PCMSMain");  
		mv.addObject("ColList", g.toJson(arrayCol));
		mv.addObject("SaleNumberList", g.toJson(model.getSaleNumberList()));
		mv.addObject("UserStatusList", g.toJson(model.getUserStatusList()));    
		mv.addObject("CusNameList", g.toJson(model.getCustomerNameList()));
		mv.addObject("CusShortNameList", g.toJson(model.getCustomerShortNameList()));
		return mv;
	}    
	
	@RequestMapping(value ="/fakeSubmit",  method = RequestMethod.POST)        
    public void submitForm(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
    		@Validated @ModelAttribute("PCMSTable") PCMSTableDetail pd, BindingResult br) throws IOException  
    {  
//		fake pass value for submit
//		PCMSMainModel model = new PCMSMainModel();   
//		Gson g = new Gson(); 
//		ArrayList<PCMSTableDetail> poList = new ArrayList<PCMSTableDetail>();
//		poList.add(pd);   
//        System.out.println(pd.getSaleOrder()+"d");
//        response.setContentType("application/json");
//		PrintWriter out = response.getWriter(); 
//		out.println(g.toJson(model.getPrdDetailByRow( poList)));  
		
    }  
	@RequestMapping(  value = "/searchByDetail",  method = RequestMethod.POST )
	public void doGetSearchByDetail(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data ) throws IOException {
		PCMSMainModel model = new PCMSMainModel();  
//		 System.out.println(data);          
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
//			pd.setUserStatus(userArray[i].getUserStatus());
			pd.setUserStatusList(userArray[i].getUserStatusList());
			pd.setCustomerNameList(userArray[i].getCustomerNameList());
			pd.setCustomerShortNameList(userArray[i].getCustomerShortNameList());
			pd.setDeliveryStatus(userArray[i].getDeliveryStatus());
			pd.setDistChannel(userArray[i].getDistChannel());
			pd.setSaleStatus(userArray[i].getSaleStatus());
			pd.setDueDate(userArray[i].getDueDate());
			poList.add(pd);   
		} 
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
//		System.out.println("he");
		out.println(g.toJson(model.searchByDetail( poList)));  
	} 
	@RequestMapping(  value = "/getPrdDetailByRow",  method = RequestMethod.POST )
	public void doGetPrdDetailByRow(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PCMSMainModel model = new PCMSMainModel();
		Gson g = new Gson();  
		PCMSTableDetail[] userArray = (PCMSTableDetail[]) g.fromJson(data, PCMSTableDetail[].class);
		ArrayList<PCMSTableDetail> poList = new ArrayList<PCMSTableDetail>();
		
		int i = 0;  
		for (i = 0; i < userArray.length; i++) {
			PCMSTableDetail pd = new PCMSTableDetail();
			pd.setCustomerShortName(userArray[i].getCustomerShortName());
			pd.setSaleNumber(userArray[i].getSaleNumber());
			pd.setSaleOrder(userArray[i].getSaleOrder());  
			pd.setProductionOrder(userArray[i].getProductionOrder()); 
			pd.setArticleFG(userArray[i].getArticleFG()); 
			pd.setDesignFG(userArray[i].getDesignFG()); 
			pd.setSaleLine(userArray[i].getSaleLine()); 
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
		out.println(g.toJson(model.getPrdDetailByRow( poList)));  
//		out.println(g.toJson(null ));  
	}
	@RequestMapping(  value = "/saveColSettingToServer",  method = RequestMethod.POST )
	public void doSaveColSettingToServer(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PCMSMainModel model = new PCMSMainModel();
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
//		System.out.println(colVisible);
		ColumnHiddenDetail pd = new ColumnHiddenDetail(); 
		pd.setUserId(user);
		pd.setColVisibleSummary(colVisible);
		poList.add(pd);   
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
//		System.out.println("he");
		out.println(g.toJson(model.saveColSettingToServer( pd)));  
//		out.println();  
	}
	@RequestMapping(  value = "/saveDefault",  method = RequestMethod.POST )
	public void doGetSaveDefault(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data ) throws IOException {
		PCMSMainModel model = new PCMSMainModel();            
		Gson g = new Gson(); 
		PCMSTableDetail[] userArray = (PCMSTableDetail[]) g.fromJson(data, PCMSTableDetail[].class);
		ArrayList<PCMSTableDetail> poList = new ArrayList<PCMSTableDetail>();
		int i = 0; 
		String user = (String) session.getAttribute("user"); 
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
			pd.setUserStatusList(userArray[i].getUserStatusList());
			pd.setCustomerNameList(userArray[i].getCustomerNameList());
			pd.setCustomerShortNameList(userArray[i].getCustomerShortNameList());
			pd.setDeliveryStatus(userArray[i].getDeliveryStatus());
			pd.setDistChannel(userArray[i].getDistChannel());
			pd.setSaleStatus(userArray[i].getSaleStatus());
			pd.setDueDate(userArray[i].getDueDate());
			pd.setUserId(user);
			poList.add(pd);   
		}  
//		System.out.println(user);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(model.saveDefault( poList)));  
	}
	@RequestMapping(  value = "/loadDefault",  method = RequestMethod.POST )
	public void doGetLoadDefault(HttpSession session,HttpServletRequest request, HttpServletResponse response ) throws IOException {
		PCMSMainModel model = new PCMSMainModel();            
		Gson g = new Gson(); 
//		PCMSTableDetail[] userArray = (PCMSTableDetail[]) g.fromJson(data, PCMSTableDetail[].class);
		ArrayList<PCMSTableDetail> poList = new ArrayList<PCMSTableDetail>();
		String user = (String) session.getAttribute("user"); 
		int i = 0; 
//		for (i = 0; i < userArray.length; i++) {
			PCMSTableDetail pd = new PCMSTableDetail();
//			pd.setCustomerName(userArray[i].getCustomerName());
//			pd.setCustomerShortName(userArray[i].getCustomerShortName());
//			pd.setSaleNumber(userArray[i].getSaleNumber());
//			pd.setSaleOrder(userArray[i].getSaleOrder());  
//			pd.setProductionOrder(userArray[i].getProductionOrder());  
//			pd.setArticleFG(userArray[i].getArticleFG()); 
//			pd.setDesignFG(userArray[i].getDesignFG()); 
//			pd.setSaleOrder(userArray[i].getSaleOrder()); 
//			pd.setSaleOrderCreateDate(userArray[i].getSaleOrderCreateDate()) ;
//			pd.setProductionOrderCreateDate(userArray[i].getProductionOrderCreateDate());
//			pd.setMaterialNo(userArray[i].getMaterialNo());
//			pd.setLabNo(userArray[i].getLabNo()); 
//			pd.setUserStatus(userArray[i].getUserStatus());
//			pd.setUserStatusList(userArray[i].getUserStatusList());
//			pd.setCustomerNameList(userArray[i].getCustomerNameList());
//			pd.setCustomerShortNameList(userArray[i].getCustomerShortNameList());
//			pd.setDeliveryStatus(userArray[i].getDeliveryStatus());
//			pd.setDistChannel(userArray[i].getDistChannel());
//			pd.setSaleStatus(userArray[i].getSaleStatus());
//			pd.setDueDate(userArray[i].getDueDate());
			pd.setUserId(user);
			poList.add(pd);   
//		} 
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(model.loadDefault( poList)));  
	}
}
