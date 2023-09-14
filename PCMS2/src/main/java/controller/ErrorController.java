package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController { 

	@RequestMapping(value = { "/error" }, method = { RequestMethod.GET })
	public ModelAndView customError(HttpServletRequest request, HttpServletResponse response, Model model) {
		// retrieve some useful information from the request
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
//		// String servletName = (String)
		// request.getAttribute("javax.servlet.error.servlet_name");
//		String exceptionMessage = getExceptionMessage(throwable, statusCode);
//
		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
//		if (requestUri == null) {
//			requestUri = "Unknown";
//		}
//
//		String message = MessageFormat.format("{0} returned for {1} with message {3}", statusCode, requestUri,
//				exceptionMessage);
//		System.out.println(statusCode+" "+throwable.toString()+" "+requestUri);
		System.out.println(statusCode+" " +requestUri);
		ModelAndView mv = new ModelAndView(); 
		mv.setViewName("error/error"); 
//		mv.addAttribute("errorMessage", message);
		return mv;
	}  

//	private String getExceptionMessage(Throwable throwable, Integer statusCode) {
//		if (throwable != null) {
//			return Throwables.getRootCause(throwable).getMessage();
//		}
//		HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
//		return httpStatus.getReasonPhrase();
//	}
}
