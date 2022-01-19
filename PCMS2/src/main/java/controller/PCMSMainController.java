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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

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
		mv.setViewName("PCMSMain/PCMSMain");
		mv.addObject("SaleNumberList", g.toJson(model.getSaleNumberList()));
		mv.addObject("UserStatusList", g.toJson(model.getUserStatusList()));
		return mv;
	}    
	
	@RequestMapping(  value = "/searchByDetail",  method = RequestMethod.POST )
	public void doGetSearchByDetail(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PCMSMainModel model = new PCMSMainModel();
		 
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
}
