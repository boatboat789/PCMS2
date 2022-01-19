package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = { "/SapToWeb" })
public class SapToWebController {
	@Autowired
	private ServletContext context;  
	private String LOCAL_DIRECTORY;
	private String FTP_DIRECTORY; 
	@RequestMapping(method = { RequestMethod.GET })
	public ModelAndView test(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Test/SapToWeb");
		return mv;
	}    
	
	@RequestMapping(value = { "/test3" }, method = { RequestMethod.GET })
	public void testGoPPMM(HttpSession session, HttpServletRequest req) {
//		ModelAndView mv = new ModelAndView(); 
//		mv.setViewName("redirect:"+"http://pcms.a-tech.co.th:8080/PPMM/PlanningDyeing");
//		mv.setViewName("redirect:"+"http://localhost:8080/InspectSystem/search/home.html");
//		return mv;   
	}  
}
