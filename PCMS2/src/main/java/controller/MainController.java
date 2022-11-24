package controller;

import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.google.gson.Gson;

import entities.ColumnHiddenDetail;
import entities.ConfigCustomerUserDetail;
import model.LogInModel;
import model.PCMSMainModel; 
@Controller
@RequestMapping({ "/" ,""})
public class MainController { 
	@Autowired
	public ServletContext context;
   
	public MainController() { 
	}   
//    @RequestMapping(value="/{url}", method = RequestMethod.GET)
//    public String method(@PathVariable("url") String url) {
//    	String redirectUrl = url;
//    	return "redirect:" + redirectUrl;
//    }   
//    @RequestMapping(method = { RequestMethod.GET })
//	public ModelAndView test(HttpSession session) {
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("PCMSMain/PCMSMain");
//		return mv;    
//	}     
	@RequestMapping(value = { "/" }, method = { RequestMethod.GET })
	public ModelAndView home1(HttpSession session, HttpServletResponse response,HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		Gson g = new Gson();    
		LogInModel logInModel = new LogInModel( );	   
		PCMSMainModel model = new PCMSMainModel();
		String user = (String) session.getAttribute("user");  
		ArrayList<ColumnHiddenDetail> list = model.getColVisibleDetail(user);
		String[] arrayCol = null  ;
		if(list.size() == 0) { }    
		else {  arrayCol = list.get(0).getColVisibleSummary().split(","); }   
		ArrayList<ConfigCustomerUserDetail> listConfigCus = logInModel.getConfigCustomerUserDetail(user);
		String OS = System.getProperty("os.name").toLowerCase();	   
		mv.setViewName("PCMSMain/PCMSMain");  
		mv.addObject("OS", g.toJson(OS));
		mv.addObject("UserID", g.toJson(user));
		mv.addObject("ColList", g.toJson(arrayCol));
		mv.addObject("ConfigCusList", g.toJson(listConfigCus));
		mv.addObject("DivisionList", g.toJson(model.getDivisionList()));
		mv.addObject("SaleNumberList", g.toJson(model.getSaleNumberList()));
		mv.addObject("UserStatusList", g.toJson(model.getUserStatusList()));
		mv.addObject("CusNameList", g.toJson(model.getCustomerNameList()));
		mv.addObject("CusShortNameList", g.toJson(model.getCustomerShortNameList()));
		return mv;   
	}   
}
