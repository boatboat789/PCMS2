package controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import model.PCMSMainModel; 
@Controller
@RequestMapping({ "/" ,""})
public class MainController { 
	@Autowired
	public ServletContext context;
   
	public MainController() {
//      System.out.println("im main");
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
		PCMSMainModel model = new PCMSMainModel();
		mv.setViewName("PCMSMain/PCMSMain");
		mv.addObject("SaleNumberList", g.toJson(model.getSaleNumberList()));
		return mv;   
	}   
}
