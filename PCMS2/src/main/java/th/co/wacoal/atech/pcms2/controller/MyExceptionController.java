package th.co.wacoal.atech.pcms2.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
//@ControllerAdvice
public class MyExceptionController {
	@Autowired
	public ServletContext context;
    @Autowired
	public MyExceptionController( ) { 
	}
	 @ExceptionHandler(NoHandlerFoundException.class)
	public ModelAndView handleError404(HttpServletRequest request, Exception e) {
		ModelAndView mv = new ModelAndView();
//        mav.addObject("exception", e);
		mv.setViewName("404");
		return mv;
	}
}
