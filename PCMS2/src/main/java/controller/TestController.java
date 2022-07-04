package controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = { "/Test" }) 
public class TestController {
	@Autowired
	private ServletContext context;  
	private String LOCAL_DIRECTORY;
	private String FTP_DIRECTORY; 
	@RequestMapping(method = { RequestMethod.GET })
	public ModelAndView test(HttpSession session) {
		ModelAndView mv = new ModelAndView(); 
		mv.setViewName("Test/Test");  
		return mv;
	}    
	
	 
}
