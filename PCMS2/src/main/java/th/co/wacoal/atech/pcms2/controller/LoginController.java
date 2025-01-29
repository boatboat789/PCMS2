package th.co.wacoal.atech.pcms2.controller;

import java.text.ParseException;
import java.util.ArrayList;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.UserDetail;
import th.co.wacoal.atech.pcms2.info.AdInfo;
import th.co.wacoal.atech.pcms2.model.LogInModel;
import th.in.totemplate.core.authen.ActiveDirectory;
import th.in.totemplate.core.authen.AuthenAttributes;

@Controller
public class LoginController {
//	private String wrongUP = "Username	 or Password is incorrect.";
	private String alertmsgText = "";
	private String alerttypText = "";

    @Autowired
	public LoginController(HttpSession session) throws ParseException {
	}

	@RequestMapping(value = { "/login" }, method = { RequestMethod.GET })
	public String getLoginModel(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response)
	{
		if (session != null) {
			session.removeAttribute("user");
			session.removeAttribute("userName");
			session.removeAttribute("userObject");
		}
		model.addAttribute("alertmsg", alertmsgText);
		model.addAttribute("alerttyp", alerttypText);
		return "login";
	}

	@RequestMapping(value = { "/logout" }, method = { RequestMethod.GET })
	public String getLogout(HttpSession session, HttpServletRequest req)
	{
//		HttpSession ses = req.getSession(false);
		if (session != null) {
			session.removeAttribute("user");
			session.removeAttribute("userName");
			session.removeAttribute("userObject");
			alertmsgText = "";
			alerttypText = "";
		}
		return "redirect:/login";
	}

	@RequestMapping(value = { "/login/loginAuth" }, method = { RequestMethod.POST })
	public String getLoginAuthen(Model model, final String userId, String userPassword, final HttpSession session,
			RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response)
	{
		LogInModel logInModel = new LogInModel();
		ArrayList<ConfigCustomerUserDetail> listConfigCus = logInModel.getConfigCustomerUserDetail(userId);
//		final Gson g = new Gson();
		final UserDetail user = new UserDetail();
		final TempLogin temp = new TempLogin();
		String redirect = ""; 
		String originalURI = (String) session.getAttribute("originalURI");
		ConfigCustomerUserDetail bean = null;
		String homePath = "/Main";
		if (session.getAttribute("user") != null) {
			alertmsgText = "";
			alerttypText = "";
			UserDetail userTMP = logInModel.getUserDetail(userId, userPassword);
			if (userTMP != null) {
				user.setId(userTMP.getId());
				user.setFirstName(userTMP.getFirstName().trim());
				user.setUserId(userTMP.getUserId().trim());
				user.setIsSystem(userTMP.getIsSystem());
				user.setIsAdmin(userTMP.getIsAdmin());
				user.setPermitId(userTMP.getPermitId());
				user.setResponsible(userTMP.getResponsible());
				user.setChangeBy(userTMP.getChangeBy());
				user.setChangeDate(userTMP.getChangeDate());
				user.setRegistBy(userTMP.getRegistBy());
				user.setRegistDate(userTMP.getRegistDate());
				user.setCustomer(userTMP.isCustomer());
				user.setUserType("USER");
				temp.setStatus(true);
				session.setAttribute("userObject", user);
			}
			if (listConfigCus.size() == 0) {
				session.removeAttribute("originalURI");
				if (originalURI == null) {
					redirect = "redirect:" + homePath;
				} else {
					if (originalURI.equals("")) {
						redirect = "redirect:" + homePath;
					} else {
						redirect = "redirect:" + originalURI;
					}
				}
				return redirect;
			} else {
				bean = listConfigCus.get(0);
//				if(bean.getIsPCMSSumPage() &&  (oriPath.equals("")||oriPath.equals("/login") )     ){
				if (bean.getIsPCMSSumPage() && originalURI == null) {
					redirect = "redirect:" + homePath;
				} else if (bean.getIsPCMSSumPage() && originalURI.equals("")) {
					redirect = "redirect:" + homePath;
				} else {
					if (bean.getIsPCMSDetailPage() == true && (originalURI.equals("") || originalURI.contains("Detail"))) {
						redirect = "redirect:/Detail";
					} else {
						redirect = "redirect:" + originalURI;
					}
				}
			}
			return redirect;
		} else { 
			user.setUserId(userId);
			user.setPassword(userPassword);
			try {
				AdInfo info = AdInfo.getInstance();
				ActiveDirectory.getAttributes(ActiveDirectory.getContext(info, user.getUserId(), user.getPassword()),
						info.getSearchName(), info.getSearchFilter(user.getUserId()), new AuthenAttributes() {
							@Override
							public void set(Attributes attribute)
							{
								try {
									user.setDepartment(((attribute.get("department") == null) ? ""
											: attribute.get("department").get().toString()));
									user.setFirstName(((attribute.get("givenName") == null) ? ""
											: attribute.get("givenName").get().toString()));
									user.setLastName(((attribute.get("sn") == null) ? "" : attribute.get("sn").get().toString()));
									user.setEmail(
											((attribute.get("mail") == null) ? "" : attribute.get("mail").get().toString()));
									temp.setStatus(true);
									session.setAttribute("user", userId);
									session.setAttribute("userObject", user);
//		                 				session.setAttribute("userObject", g.toJson(user));
								} catch (NamingException e) {
									e.printStackTrace();
								}
							}
						});
			} catch (NamingException e) {
				System.err.println(this.getClass().getName() + " - " + e.getMessage());
				e.printStackTrace();
			}
			// if authen pass
			if ( ! temp.getStatus()) {
				UserDetail userTMP = logInModel.getUserDetail(userId, userPassword);
				if (userTMP != null) {
					user.setId(userTMP.getId());
					user.setFirstName(userTMP.getFirstName().trim());
					user.setUserId(userTMP.getUserId().trim());
					user.setIsSystem(userTMP.getIsSystem());
					user.setIsAdmin(userTMP.getIsAdmin());
					user.setPermitId(userTMP.getPermitId());
					user.setResponsible(userTMP.getResponsible());
					user.setChangeBy(userTMP.getChangeBy());
					user.setChangeDate(userTMP.getChangeDate());
					user.setRegistBy(userTMP.getRegistBy());
					user.setRegistDate(userTMP.getRegistDate());
					user.setCustomer(userTMP.isCustomer());
					user.setUserType("USER");
					temp.setStatus(true);
					session.setAttribute("user", userId);
					session.setAttribute("userObject", user);
				}
			} else {
				UserDetail userTMP = logInModel.getUserDetail(userId);
				if (userTMP != null) {
					user.setId(userTMP.getId());
					user.setUserId(userTMP.getUserId().trim());
					user.setIsSystem(userTMP.getIsSystem());
					user.setIsAdmin(userTMP.getIsAdmin());
					user.setPermitId(userTMP.getPermitId());
					user.setResponsible(userTMP.getResponsible());
					user.setChangeBy(userTMP.getChangeBy());
					user.setChangeDate(userTMP.getChangeDate());
					user.setRegistBy(userTMP.getRegistBy());
					user.setRegistDate(userTMP.getRegistDate());
					user.setCustomer(userTMP.isCustomer());
					user.setUserType("USER");
					temp.setStatus(true);
					session.setAttribute("user", userId);
					session.setAttribute("userObject", user);
				}
			}
		} 
		if (user.getFirstName() != null || user.getUserType() != null) {
			alertmsgText = "";
			alerttypText = "";
			if (listConfigCus.size() == 0) {
				if (originalURI == null) {
					redirect = "redirect:" + homePath;
				} else {
					if (originalURI.equals("")) {
						redirect = "redirect:" + homePath;
					} else {
						redirect = "redirect:" + originalURI;
					}
				}
			} else {
				bean = listConfigCus.get(0); 
				if (bean.getIsPCMSSumPage() && originalURI == null) {
					redirect = "redirect:" + homePath;
				} else if (bean.getIsPCMSSumPage() && originalURI.equals("")) {
					redirect = "redirect:" + homePath;
				} else {
					if (bean.getIsPCMSDetailPage() == true && (originalURI.equals("") || originalURI.contains("Detail"))) {
						redirect = "redirect:/Detail";
					} else {
						redirect = "redirect:" + originalURI;
					}
				}
			}
		} else {
			redirect = "redirect:/login";
//			redirect = "redirect:login" ;
			if (temp.getStatus()) {
				alertmsgText = "Unauthorized Access Prohibited";
				alerttypText = "error";
//	        	model.addAttribute("alerttyp",  "error"  );
//	        	model.addAttribute("alertmsg", "Username or Password is incorrect."  ); 
			} else {
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

		public TempLogin() {
			this.status = false;
		}

		public void setStatus(boolean status)
		{
			this.status = status;
		}

		public boolean getStatus()
		{
			return this.status;
		}
	}
}
