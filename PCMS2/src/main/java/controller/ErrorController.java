package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    @Autowired
	public ErrorController( ) { 
	}
	@RequestMapping(value = { "/handleError" }, method = { RequestMethod.GET })
	public ModelAndView customError(HttpServletRequest request, HttpServletResponse response, Model model)
	{ 
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code"); 
		@SuppressWarnings("unused")
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception"); 
		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");  
        // Save it to session for access after login
        HttpSession session = request.getSession();
        session.setAttribute("originalUrl", requestUri);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        String errorMsg = "";
		ModelAndView mv = new ModelAndView();
        if (isLoggedIn) {
            // Redirect to 404.jsp if the user is logged in
            mv.setViewName("error/404");
            errorMsg = "Wrong Path.";
        } else {
            // Redirect to error.jsp if the user is not logged in
            mv.setViewName("error/error");
            errorMsg = "Need to login.";
        } 
		mv.addObject("statusCode", statusCode.toString()); 
		mv.addObject("errorMsg", errorMsg); 
		return mv;
	} 
}
