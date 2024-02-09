package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import entities.ConfigCustomerUserDetail;
import entities.PCMSSecondTableDetail;
import entities.PCMSTableDetail;
import model.LogInModel;
import model.PCMSDetailModel;
import model.master.ColumnSettingModel;
import model.master.ConfigDepartmentModel;
import model.master.FromSapMainSaleModel;
import model.master.PlanCFMDateModel;
import model.master.PlanCFMLabDateModel;
import model.master.PlanSendCFMCusDateModel;

@Controller
@RequestMapping(value = { "/Detail" }) 
public class PCMSDetailController {
	@Autowired
	private ServletContext context;  
	private String LOCAL_DIRECTORY;
	private String FTP_DIRECTORY; 
	@RequestMapping(method = { RequestMethod.GET })
	public ModelAndView test(HttpSession session) {
		ModelAndView mv = new ModelAndView(); 
		PCMSDetailModel model = new PCMSDetailModel(); 
		ColumnSettingModel csModel = new ColumnSettingModel(); 
		FromSapMainSaleModel fsmsModel = new FromSapMainSaleModel(); 
		Gson g = new Gson();
		String user = (String) session.getAttribute("user"); 
		ConfigDepartmentModel cdmModel = new ConfigDepartmentModel(); 
		ArrayList<ColumnHiddenDetail> list = csModel.getColumnVisibleDetail(user);
		String[] arrayCol = null  ;    
		if(list.size() == 0) {      
			arrayCol = null; 
		} 
		else {  arrayCol = list.get(0).getColVisibleDetail().split(","); }  

		String OS = System.getProperty("os.name").toLowerCase();	  
		LogInModel logInModel = new LogInModel( );	   
		ArrayList<ConfigCustomerUserDetail> listConfigCus = logInModel.getConfigCustomerUserDetail(user); 
		mv.setViewName("PCMSDetail/PCMSDetail");  
		mv.addObject("UserID", g.toJson(user));   
		mv.addObject("OS", g.toJson(OS));
		mv.addObject("ColList", g.toJson(arrayCol));
		mv.addObject("ConfigCusListTest", listConfigCus );
		mv.addObject("ConfigCusList", g.toJson(listConfigCus));
		mv.addObject("DepList", g.toJson(cdmModel.getDelayedDepartmentList()));
		mv.addObject("DivisionList", g.toJson(fsmsModel.getDivisionDetail()));
		mv.addObject("SaleNumberList", g.toJson(fsmsModel.getSaleNumberDetail()));
		mv.addObject("UserStatusList", g.toJson(model.getUserStatusList()));
		mv.addObject("CusNameList", g.toJson(fsmsModel.getCustomerNameDetail()));
		mv.addObject("CusShortNameList", g.toJson(fsmsModel.getCustomerShortNameDetail()));
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
//			pd.setCustomerName(userArray[i].getCustomerName());
//			pd.setCustomerShortName(userArray[i].getCustomerShortName());
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
			pd.setDivisionList(userArray[i].getDivisionList());
			pd.setCustomerNameList(userArray[i].getCustomerNameList());
			pd.setCustomerShortNameList(userArray[i].getCustomerShortNameList());
			pd.setDeliveryStatus(userArray[i].getDeliveryStatus());
			pd.setDistChannel(userArray[i].getDistChannel());
			pd.setSaleStatus(userArray[i].getSaleStatus());
			pd.setDueDate(userArray[i].getDueDate());
			pd.setDivisionList(userArray[i].getDivisionList());
			pd.setCustomerDivision(userArray[i].getCustomerDivision());
			pd.setPurchaseOrder(userArray[i].getPurchaseOrder());
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
			pd.setCfmPlanLabDate(userArray[i].getCfmPlanLabDate());
			pd.setCfmPlanDate(userArray[i].getCfmPlanDate());  
			pd.setDeliveryDate(userArray[i].getDeliveryDate());   
			pd.setCaseSave(userArray[i].getCaseSave());   
			pd.setLotNo(userArray[i].getLotNo());
			pd.setUserId(user);
			poList.add(pd);   
		}  
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
//		System.out.println("he");
		out.println(g.toJson(model.saveInputDate( poList)));  
	}
	@RequestMapping(  value = "/saveInputDetail",  method = RequestMethod.POST )
	public void doGetSaveInputDetail(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
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
			pd.setReplacedRemark(userArray[i].getReplacedRemark()) ; 
			pd.setStockRemark(userArray[i].getStockRemark()) ; 
			pd.setGrade(userArray[i].getGrade()) ;
			pd.setCaseSave(userArray[i].getCaseSave()) ;
			pd.setLotNo(userArray[i].getLotNo());
			pd.setPcRemark(userArray[i].getPcRemark()) ;
			pd.setSwitchRemark(userArray[i].getSwitchRemark());
			pd.setStockLoad(userArray[i].getStockLoad()); 
			pd.setDelayedDepartment(userArray[i].getDelayedDepartment());
			pd.setCauseOfDelay(userArray[i].getCauseOfDelay());
			pd.setSendCFMCusDate(userArray[i].getSendCFMCusDate());
			pd.setUserId(user);
			poList.add(pd);   
		}  
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(model.saveInputDetail( poList)));  
	}
	@RequestMapping(  value = "/getCFMPlanDateDetail",  method = RequestMethod.POST )
	public void doGetCFMPlanDate(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PlanCFMDateModel model = new PlanCFMDateModel();
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
			pd.setCfmPlanLabDate(userArray[i].getCfmPlanLabDate());
			pd.setCfmPlanDate(userArray[i].getCfmPlanDate());  
			pd.setDeliveryDate(userArray[i].getDeliveryDate());   
			pd.setSendCFMCusDate(userArray[i].getSendCFMCusDate()) ;
			pd.setCaseSave(userArray[i].getCaseSave());    
			pd.setPcRemark(userArray[i].getPcRemark()) ;
			pd.setSwitchRemark(userArray[i].getSwitchRemark());
			pd.setStockLoad(userArray[i].getStockLoad());
			pd.setUserId(user);
			poList.add(pd);   
		}  
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(model.getCFMPlanDateDetail( poList)));  
	}
	@RequestMapping(  value = "/getCFMPlanLabDateDetail",  method = RequestMethod.POST )
	public void doGetCFMPlanLabDate(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {

		PlanCFMLabDateModel pcfmldModel = new PlanCFMLabDateModel( );
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
			pd.setCfmPlanLabDate(userArray[i].getCfmPlanLabDate());
			pd.setCfmPlanDate(userArray[i].getCfmPlanDate());  
			pd.setDeliveryDate(userArray[i].getDeliveryDate());   
			pd.setSendCFMCusDate(userArray[i].getSendCFMCusDate()) ;
			pd.setCaseSave(userArray[i].getCaseSave());   
			pd.setSwitchRemark(userArray[i].getSwitchRemark());
			pd.setPcRemark(userArray[i].getPcRemark()) ;
			pd.setUserId(user);
			poList.add(pd);   
		}  
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(pcfmldModel.getCFMPlanLabDateDetail( poList)));  
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
			pd.setCfmPlanLabDate(userArray[i].getCfmPlanLabDate());
			pd.setCfmPlanDate(userArray[i].getCfmPlanDate());  
			pd.setDeliveryDate(userArray[i].getDeliveryDate());   
			pd.setSendCFMCusDate(userArray[i].getSendCFMCusDate()) ;
			pd.setCaseSave(userArray[i].getCaseSave());   
			pd.setPcRemark(userArray[i].getPcRemark()) ;
			pd.setUserId(user);
			poList.add(pd);   
		}  
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(model.getDeliveryPlanDateDetail( poList)));  
	}
	@RequestMapping(  value = "/getSendCFMCusDateDetail",  method = RequestMethod.POST )
	public void doGetSendCFMCusDateDetail(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PlanSendCFMCusDateModel model = new PlanSendCFMCusDateModel();
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
			pd.setCfmPlanLabDate(userArray[i].getCfmPlanLabDate());
			pd.setCfmPlanDate(userArray[i].getCfmPlanDate());  
			pd.setDeliveryDate(userArray[i].getDeliveryDate());   
			pd.setSendCFMCusDate(userArray[i].getSendCFMCusDate()) ;
			pd.setCaseSave(userArray[i].getCaseSave());   
			pd.setPcRemark(userArray[i].getPcRemark()) ;
			pd.setUserId(user);
			poList.add(pd);   
		}  
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(model.getSendCFMCusDateDetail( poList)));  
	}
	@RequestMapping(  value = "/saveColSettingToServer",  method = RequestMethod.POST )
	public void doSaveColSettingToServer(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		ColumnSettingModel model = new ColumnSettingModel();
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
		out.println(g.toJson(model.upsertColumnSettingDetail( pd)));   
	}
 
	@RequestMapping(  value = "/saveDefault",  method = RequestMethod.POST )
	public void doGetSaveDefault(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data ) throws IOException {
		PCMSDetailModel model = new PCMSDetailModel();            
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
			pd.setDivisionList(userArray[i].getDivisionList());
			pd.setPurchaseOrder(userArray[i].getPurchaseOrder());
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
		PCMSDetailModel model = new PCMSDetailModel();            
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
	@RequestMapping(  value = "/getSwitchProdOrderListByPrd",  method = RequestMethod.POST )
	public void doGetSwitchProdOrderListByPrd(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
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
			pd.setReplacedRemark(userArray[i].getReplacedRemark()) ; 
			pd.setStockRemark(userArray[i].getStockRemark()) ; 
			pd.setGrade(userArray[i].getGrade()) ;
			pd.setCaseSave(userArray[i].getCaseSave()) ;
			pd.setLotNo(userArray[i].getLotNo());
			pd.setPcRemark(userArray[i].getPcRemark()) ;
			pd.setSwitchRemark(userArray[i].getSwitchRemark());
			pd.setUserId(user);
			poList.add(pd);   
		}  
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(model.getSwitchProdOrderListByPrd(poList)));  
	}
	@RequestMapping(  value = "/getSwitchProdOrderListByRowProd",  method = RequestMethod.POST )
	public void doGetSwitchProdOrderListByRowProd(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
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
			pd.setReplacedRemark(userArray[i].getReplacedRemark()) ; 
			pd.setStockRemark(userArray[i].getStockRemark()) ; 
			pd.setGrade(userArray[i].getGrade()) ;
			pd.setCaseSave(userArray[i].getCaseSave()) ;
			pd.setLotNo(userArray[i].getLotNo());
			pd.setPcRemark(userArray[i].getPcRemark()) ;
			pd.setSwitchRemark(userArray[i].getSwitchRemark());
			pd.setUserId(user);
			poList.add(pd);   
		}  
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(model.getSwitchProdOrderListByRowProd(poList)));  
	}
}
