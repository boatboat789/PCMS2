package th.co.wacoal.atech.pcms2.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FilterLogin implements Filter {
	@Override
   public void init(FilterConfig config) throws ServletException {
   }
	@Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	      HttpServletRequest req = (HttpServletRequest)request; 
	      HttpServletResponse res = (HttpServletResponse)response;
	      HttpSession ses = req.getSession(false);
	      String context = req.getContextPath();
	      String uri = req.getRequestURI().substring(context.length());
	      String loginURL =  req.getContextPath() +"/login";
	      String loginAuthURL =  req.getContextPath() +"/login/loginAuth";
	      boolean loggedIn = ( ses != null && ses.getAttribute("user") != null && ses.getAttribute("userObject") != null );
	      boolean loginRequest = loginURL.equals(req.getRequestURI());
	      boolean loginAuthRequest = loginAuthURL.equals(req.getRequestURI());
	      boolean isStaticResource = uri.startsWith("/resources/");

//	      boolean passAuthen = false;
//          boolean isResource = req.getRequestURI().matches(".*(css|jpg|png|gif|js)$");
//	      String comeFrom = req.getParameter("comeFrom");
	      if (loggedIn||loginRequest||loginAuthRequest||isStaticResource) {
	    	  chain.doFilter(request, response);
	      }
//	      else if(comeFrom != null  ){
//	    	  ses = req.getSession();
//	    	  if(comeFrom.equals("PCMS")){
//				EmployeeDetail user = new EmployeeDetail( );
//				user.setId("PCMSDISPLAY");
//                ses.setAttribute("user", user.getId());
//                ses.setAttribute("userObject", user); 
//	        	}
//	        }
	      else {

				HttpSession session = req.getSession();
			    session.setAttribute("originalURI", uri);
				res.sendRedirect(loginURL);             //CREATE NEW LINK FOR REDIRECT 
//    		 RequestDispatcher dispatcher = request.getRequestDispatcher("login");
//             dispatcher.forward(request, response);  //GET OLD LINK FOR REDIRECT
   			}
   }
	@Override
   public void destroy() {
   }

}
