package th.co.wacoal.atech.pcms2.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;
 
public class FilterLogin extends OncePerRequestFilter { 
//    @Autowired
//    public FilterLogin(EmployeeDetailModel empModel) {
//        this.empModel = empModel;
//    }

	@Override
	public void destroy()
	{
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession ses = req.getSession(false);
		String context = req.getContextPath();
		String uri = req.getRequestURI().substring(context.length());
		String loginURL = req.getContextPath() + "/login";
		String loginAuthURL = req.getContextPath() + "/login/loginAuth";
		boolean loggedIn = (ses != null && ses.getAttribute("user") != null && ses.getAttribute("userObject") != null);
		boolean loginRequest = loginURL.equals(req.getRequestURI());
		boolean loginAuthRequest = loginAuthURL.equals(req.getRequestURI());
		boolean isStaticResource = uri.startsWith("/resources/");
		if (loggedIn || loginRequest || loginAuthRequest || isStaticResource) {
			filterChain.doFilter(request, response);
		} else {

			HttpSession session = req.getSession();
			session.setAttribute("originalURI", uri);
			res.sendRedirect(loginURL); // CREATE NEW LINK FOR REDIRECT
		}
	}

}
