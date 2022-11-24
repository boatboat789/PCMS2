package controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import entities.CFMDetail;
import entities.ColumnHiddenDetail;
import entities.ConfigCustomerUserDetail;
import entities.UserDetail;
import info.AdInfo;
import info.SqlInfo;
import model.LogInModel;
import th.in.totemplate.core.authen.ActiveDirectory;
import th.in.totemplate.core.authen.AuthenAttributes;
import th.in.totemplate.core.sql.Database;

@Controller
public class LoginController { 
//	private String wrongUP = "Username	 or Password is incorrect."; 
	private String alertmsgText = "";
	private String alerttypText = "";
	public LoginController(HttpSession session) throws ParseException { } 
	@RequestMapping(value = { "/login" }, method = { RequestMethod.GET })
	public String getLoginModel(Model  model, HttpSession session, HttpServletRequest request, 
			HttpServletResponse response ) { 
		model.addAttribute("alertmsg", alertmsgText);
		model.addAttribute("alerttyp", alerttypText);  
		return "login";
	} 
	@RequestMapping(value = { "/logout" }, method = { RequestMethod.GET })
	public 	String getLogout(HttpSession session, HttpServletRequest req) { 
//		HttpSession ses = req.getSession(false);
	     if(session != null) {
	         session.removeAttribute("user");  
		     session.removeAttribute("userName");  
		     session.removeAttribute("userObject");  
		     alertmsgText = "";
		     alerttypText = "";
	     }
		return "redirect:/login";
	}
//	@RequestMapping(value = { "/login" }, method = { RequestMethod.GET })
//	public ModelAndView getLoginModel(Model  model, HttpSession session, HttpServletRequest request, 
//			HttpServletResponse response, String errormsg) { 
//		ModelAndView mv = new ModelAndView();   
//		mv.setViewName("login");    
//		return mv;
//	} 
//	@GetMapping(value = { "/login/loginAuth" } ) 
	@RequestMapping(value = { "/login/loginAuth" }, method = { RequestMethod.POST }) 
	public String getLoginAuthen(Model model, final String userId, String userPassword, final HttpSession session,
			RedirectAttributes redirectAttributes,HttpServletRequest request, 
			HttpServletResponse response) { 
		LogInModel logInModel = new LogInModel( );	  
		ArrayList<ConfigCustomerUserDetail> listConfigCus = logInModel.getConfigCustomerUserDetail(userId);
		final Gson g = new Gson(); 
		final UserDetail user = new UserDetail( );	
		final TempLogin temp = new TempLogin();
		String redirect = "";
		String path = null;
		try {
			path = new URL(request.getHeader("referer")).getPath();
//			path = path.replaceFirst("/", "");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String oriPath = path.substring(request.getContextPath().length());
//		String userName = ""; 
//		System.out.println(listConfigCus.size());    
		ConfigCustomerUserDetail bean = null;
//		System.out.println(bean.getIsPCMSSumPage());
//		System.out.println(bean.getIsPCMSDetailPage());
		if(session.getAttribute("user") != null) {  
			alertmsgText = "";
			alerttypText ="";
			if( listConfigCus.size() == 0) {
				if(oriPath.equals("")||oriPath.equals("/login")) { 
					redirect = "redirect:/Main" ;   
				}
				else {  redirect = "redirect:"+oriPath ;  } 
			}
			else { 
				bean = listConfigCus.get(0);
				if(bean.getIsPCMSSumPage() == true &&  (oriPath.equals("")||oriPath.equals("/login") )     ){
					redirect = "redirect:/Main" ;  
				} 
				else if(bean.getIsPCMSDetailPage() == true &&  oriPath.equals("/Detail")   ) { 
					redirect = "redirect:/Detail" ;   
				}
				else {  redirect = "redirect:"+oriPath ;    } 
			}
			return redirect;   
		}
		else {  
//			if(userId.equals("test")) {
//				user.setFirstName("Test");
//				user.setUserId(userId)  ;    
//				user.setPassword(userPassword);
//				temp.setStatus(true); 
//             	session.setAttribute("user", userId);     	   
////  				session.setAttribute("userOB",   g.toJsonTree(user)    );    
// 				session.setAttribute("userObject", g.toJson(user));   
//			}
//			else { 
				user.setUserId(userId);
				user.setPassword(userPassword);
		        try {  
	                AdInfo info = AdInfo.getInstance();
	                ActiveDirectory.getAttributes(
	                    ActiveDirectory.getContext(
	                        info,
	                        user.getUserId(),
	                        user.getPassword()),
		                    info.getSearchName(),
		                    info.getSearchFilter(user.getUserId()),
		                    new AuthenAttributes() {
		                        @Override
		                        public void set(Attributes attribute) {
		                            try {  
		                                user.setDepartment( ((attribute.get("department") == null) ? "" : attribute.get("department").get().toString()) );
		                                user.setFirstName(  ((attribute.get("givenName")  == null) ? "" : attribute.get("givenName").get().toString())  );
		                                user.setLastName(   ((attribute.get("sn")         == null) ? "" : attribute.get("sn").get().toString())         );
		                                user.setEmail(      ((attribute.get("mail")       == null) ? "" : attribute.get("mail").get().toString())       );
		                                temp.setStatus(true); 
		                             	session.setAttribute("user", userId);      
		                  				session.setAttribute("userObject",  user  );  
//		                 				session.setAttribute("userObject", g.toJson(user));   
		                            } catch(NamingException e) { e.printStackTrace(); }
		                        }
		                    }
		                ); 
		        } catch(NamingException e) {
		            System.err.println(this.getClass().getName()+" - "+e.getMessage());
		            e.printStackTrace();
		        }   
//			}
			// if authen pass
			if(temp.getStatus() != true){
				UserDetail userTMP = logInModel.getUserDetail(userId);
				if (userTMP != null) {  
	                 user.setId(userTMP.getId());
	                 user.setFirstName(userTMP.getFirstName().trim());
	                 user.setUserId(userTMP.getUserId(). trim());   
	                 user.setIsSystem(userTMP.getIsSystem());
	                 user.setIsAdmin(userTMP.getIsAdmin());
	                 user.setPermitId(userTMP.getPermitId());
	                 user.setResponsible(userTMP.getResponsible()); 
	                 user.setChangeBy(userTMP.getChangeBy());
	                 user.setChangeDate(userTMP.getChangeDate());
	                 user.setRegistBy(userTMP.getRegistBy());
	                 user.setRegistDate(userTMP.getRegistDate()); 
	                 user.setIsCustomer(userTMP.getIsCustomer()) ;
	                 user.setUserType("USER");
	                 temp.setStatus(true); 

                  	session.setAttribute("user", userId);      
      				session.setAttribute("userObject",  user    ); 
//      				session.setAttribute("userObject", g.toJson(user));   
				}  
            } 
			else {
				UserDetail userTMP = logInModel.getUserDetail(userId);
				if (userTMP != null) {  
	                 user.setId(userTMP.getId());
//	                 user.setFirstName(userTMP.getFirstName().trim());
	                 user.setUserId(userTMP.getUserId(). trim());   
	                 user.setIsSystem(userTMP.getIsSystem());
	                 user.setIsAdmin(userTMP.getIsAdmin());
	                 user.setPermitId(userTMP.getPermitId());
	                 user.setResponsible(userTMP.getResponsible()); 
	                 user.setChangeBy(userTMP.getChangeBy());   
	                 user.setChangeDate(userTMP.getChangeDate());
	                 user.setRegistBy(userTMP.getRegistBy());
	                 user.setRegistDate(userTMP.getRegistDate()); 
	                 user.setIsCustomer(userTMP.getIsCustomer()) ;
	                 user.setUserType("USER"); 
	                 temp.setStatus(true);  
                  	session.setAttribute("user", userId);       
      				session.setAttribute("userObject", user  );  
//      				session.setAttribute("userObject", g.toJson(user));    
				}  
			} 
		}          
   
//		System.out.println("/login/loginAuth : "+request.getHeader("referer"));
//		System.out.println("/login/loginAuth : "+request.getRequestURI());   
//		System.out.println("/login/loginAuth : "+request.getParameter("from"));
//		System.out.println("/login/loginAuth : "+session.getAttribute("SPRING_SECURITY_SAVED_REQUEST"));
//		System.out.println("/login/loginAuth path : "+ path ); 
//		System.out.println("/login/loginAuth : "+ request.getRequestURL() ); 
//		System.out.println("/login/loginAuth : "+ request.getContextPath() ); 
//		System.out.println("/login/loginAuth : "+ request.getRequestURI().substring(request.getContextPath().length())); 
//		System.out.println("/login/loginAuth : "+ path.substring(request.getContextPath().length())); 
//		System.out.println(oriPath);     
		if(user.getFirstName() != null || user.getUserType() != null) {  
			alertmsgText = "";
			alerttypText ="";
			if( listConfigCus.size() == 0) {
				if(oriPath.equals("")||oriPath.equals("/login")) { 
					redirect = "redirect:/Main" ;   
				}
				else {  redirect = "redirect:"+oriPath ;  } 
			}
			else {
				bean = listConfigCus.get(0);
				if(bean.getIsPCMSSumPage() == true &&  (oriPath.equals("")||oriPath.equals("/login") )     ){
					redirect = "redirect:/Main" ;  
				} 
				else if(bean.getIsPCMSDetailPage() == true &&  oriPath.equals("/Detail")   ) { 
					redirect = "redirect:/Detail" ;   
				}
				else {  redirect = "redirect:"+oriPath ;    } 
			}
		 } 
		else {
			redirect = "redirect:/login" ;  
//			redirect = "redirect:login" ;       
			 if(temp.getStatus()) {
				 alertmsgText = "Unauthorized Access Prohibited";
				 alerttypText = "error";
//	        	model.addAttribute("alerttyp",  "error"  );   
//	        	model.addAttribute("alertmsg", "Username or Password is incorrect."  );  
//	        	System.out.println("hi1"); 
			 }
			else { 
				alertmsgText = "Username or Password is incorrect";
				alerttypText = "warning";
//	            model.addObject("alerttyp", g.toJson("warning") );   
//	        	model.addObject("alertmsg", g.toJson("Username or Password is incorrect") );   
//	        	model.addAttribute("alerttyp", "warning"  );   
//	        	model.addAttribute("alertmsg", "Username or Password is incorrect"  );   
	        }
		}   
		return redirect;  
	}  
	class TempLogin {
	    private boolean status;
	    public TempLogin() { this.status = false; } 
	    public void setStatus(boolean status) { this.status = status; }
	    public boolean getStatus() { return this.status; }
	}  
}
