package th.co.wacoal.atech.pcms2.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import th.co.wacoal.atech.pcms2.entities.ColumnHiddenDetail;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.EncryptedDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.entities.UserDetail;
import th.co.wacoal.atech.pcms2.logic.PCMSMainProcess;
import th.co.wacoal.atech.pcms2.service.PCMSMainService;
import th.co.wacoal.atech.pcms2.service.master.ColumnSettingService;
import th.co.wacoal.atech.pcms2.service.master.ConfigCustomerUserService;
import th.co.wacoal.atech.pcms2.service.master.CustomerService;
import th.co.wacoal.atech.pcms2.service.master.FromSapMainSaleService;
import th.co.wacoal.atech.pcms2.service.master.PermitsService;
import th.co.wacoal.atech.pcms2.service.master.PPMM.UserStatusDetailService; 
@Controller
@RequestMapping(value = { "/Main", "/" ,"" })
public class PCMSMainController { 
	private final PCMSMainService pcmsMainService;
	private final UserStatusDetailService usdService;
	

	private final PCMSMainProcess pCMSMainProcess;
	private final ConfigCustomerUserService configCustomerUserService  ;
	private final ColumnSettingService columnSettingService  ;
	private final FromSapMainSaleService fromSapMainSaleService  ;  
	private final PermitsService permitsService ;  
	private final CustomerService customerService ;  
	private ModelAndView mv = new ModelAndView();

	private final Gson g = new Gson();
    @Autowired
	public PCMSMainController(PCMSMainService pcmsMainService 
			,UserStatusDetailService usdService
			,ConfigCustomerUserService configCustomerUserService  
			, ColumnSettingService columnSettingService  
			, FromSapMainSaleService fromSapMainSaleService  
			, PermitsService permitsService, PCMSMainProcess pCMSMainProcess, CustomerService customerService) { 
    	this.pcmsMainService = pcmsMainService;
		this.usdService = usdService;
		this.pCMSMainProcess = pCMSMainProcess;

		this.configCustomerUserService = configCustomerUserService;
		this.columnSettingService = columnSettingService;
		this.fromSapMainSaleService = fromSapMainSaleService;
		this.permitsService = permitsService;
		this.customerService = customerService; 
    }

	@RequestMapping(method = { RequestMethod.GET })
	public ModelAndView getModelAndView(HttpSession session) {  
		Gson g = new Gson();
		String user = (String) session.getAttribute("user");
		if (user != null) {
			PermitDetail permit = (PermitDetail) session.getAttribute("permit");
			if (permit == null) {
				mv.setViewName("error/AccessDenied"); // Redirect to an access-denied view
				mv.addObject("errorMsg", "Contact IT for set permission first.");
			} else {
				if (permit.isPCMSMain()) { 

					ArrayList<PCMSAllDetail> cusNameList = null ; 
					ArrayList<PCMSAllDetail> cusShortNameList = null ;
					String[] arrayCol = null  ;
					UserDetail userObject = (UserDetail) session.getAttribute("userObject");
					boolean isCustomer = false ;
					if(userObject != null) {
						isCustomer = userObject.isCustomer();
					}
					String OS = System.getProperty("os.name").toLowerCase();
					ArrayList<ConfigCustomerUserDetail> listConfigCus = configCustomerUserService.getConfigCustomerUserDetail(user); 
					if(listConfigCus.isEmpty()) {
						ConfigCustomerUserDetail ccuDetail = new ConfigCustomerUserDetail();
						ccuDetail.setUserId(user);
						listConfigCus.add(ccuDetail); 
						cusNameList = this.fromSapMainSaleService.getCustomerNameDetail(); 
						cusShortNameList = this.fromSapMainSaleService.getCustomerShortNameDetail(); 
					}
					else {
						cusNameList = this.fromSapMainSaleService.getCustomerNameDetail(listConfigCus); 
						cusShortNameList = this.fromSapMainSaleService.getCustomerShortNameDetail(listConfigCus); 
					} 
					ArrayList<ColumnHiddenDetail> list = columnSettingService.getColumnVisibleDetail(user);
					if(list.size() == 0) { arrayCol = null  ;}
					else {
						arrayCol = list.get(0).getColVisibleSummary().split(",");
					}
					
					
					mv.setViewName("PCMSMain/PCMSMain");
					mv.addObject("PermitIdList", g.toJson(permitsService.getPermitsDetail()));
					mv.addObject("OS", g.toJson(OS));
					mv.addObject("UserID", g.toJson(user));
					mv.addObject("IsCustomer", g.toJson(isCustomer ));
					mv.addObject("ColList", g.toJson(arrayCol));  
					mv.addObject("DivisionList", g.toJson(fromSapMainSaleService.getDivisionDetail()));
					mv.addObject("SaleNumberList", g.toJson(fromSapMainSaleService.getSaleNumberDetail()));
					mv.addObject("UserStatusList", g.toJson(usdService.getUserStatusDetail()));
					mv.addObject("CusNameList", g.toJson(cusNameList)); 
					mv.addObject("CusShortNameList", g.toJson(cusShortNameList)); 
					mv.addObject("UserID", g.toJson(user));
				} else {
					mv.setViewName("error/AccessDenied"); // Redirect to an access-denied view
					mv.addObject("errorMsg", "You do not have permission to access this page.");
				}
			}
		} else {
			mv.setViewName("login"); // Redirect to a login page if no user is found
			mv.addObject("alertmsg", "Please log in to access this page.");
			mv.addObject("alerttyp", "User Session Not Found.");
		}
		return mv;
	}  
	@GetMapping("/getInitData")
	@ResponseBody
	public String getInitData(HttpSession session) {
		String user = (String) session.getAttribute("user");
	    Map<String, Object> data = new HashMap<>();
		String OS = System.getProperty("os.name").toLowerCase(); 
		UserDetail userObject = (UserDetail) session.getAttribute("userObject");
		boolean isCustomer = false ;
		if(userObject != null) {
			isCustomer = userObject.isCustomer();
		}
	    data.put("os", OS); // ข้อมูลเดิมของโบ๊ท
	    data.put("userId", user);
	    data.put("isCustomer", isCustomer);
	    
	    return g.toJson(data); // ส่งก้อนใหญ่ก้อนเดียวไปเลย
	}
	@RequestMapping(value ="/fakeSubmit",  method = RequestMethod.POST)
    public void submitForm(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
    		@Validated @ModelAttribute("PCMSTable") PCMSTableDetail pd, BindingResult br) throws IOException
    { 
    }
	@RequestMapping(  value = "/searchByDetail",  method = RequestMethod.POST )
	public void doGetSearchByDetail(HttpSession session,HttpServletRequest request, HttpServletResponse response 
			, @RequestBody ArrayList<PCMSTableDetail> poList 
			) throws IOException { 
		Gson g = new Gson();
		UserDetail userObject = (UserDetail) session.getAttribute("userObject");
		boolean isCustomer = false ;
		if(userObject != null) {
			isCustomer = userObject.isCustomer();
		} 
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(pCMSMainProcess.searchByDetail( poList,isCustomer)));
	}
	@RequestMapping(  value = "/getPrdDetailByRow",  method = RequestMethod.POST )
	public void doGetPrdDetailByRow(HttpSession session
			,HttpServletRequest request
			, HttpServletResponse response
			, @RequestBody ArrayList<PCMSTableDetail> poList 
			) throws IOException { 
		Gson g = new Gson();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(pcmsMainService.getPrdDetailByRow( poList)));
	}
	@RequestMapping(  value = "/saveColSettingToServer",  method = RequestMethod.POST )
	public void doSaveColSettingToServer(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		String user = (String) session.getAttribute("user");
		Gson g = new Gson();
		String [] userArray = g.fromJson(data, String[].class);
		ArrayList<ColumnHiddenDetail> poList = new ArrayList<>();
		int i = 0;
		String colVisible = "";
		for (i = 0; i < userArray.length; i++) {
			colVisible += userArray[i];
			if(i!= userArray.length - 1) {
				colVisible +=",";
			}
		}
		ColumnHiddenDetail pd = new ColumnHiddenDetail();
		pd.setUserId(user);
		pd.setColVisibleSummary(colVisible);
		poList.add(pd);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(columnSettingService.upsertColumnVisibleSummary(user, pd)));
	}
	@RequestMapping(  value = "/saveDefault",  method = RequestMethod.POST )
	public void doGetSaveDefault(HttpSession session,HttpServletRequest request, HttpServletResponse response 
			, @RequestBody ArrayList<PCMSTableDetail> poList  ) throws IOException {
		Gson g = new Gson();
		String user = (String) session.getAttribute("user"); 
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(pCMSMainProcess.saveDefault( user,poList)));
	}

	@RequestMapping(value = "/loadDefault", method = RequestMethod.POST)
	public void doGetLoadDefault(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		Gson g = new Gson();
		String user = (String) session.getAttribute("user"); 
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(pCMSMainProcess.loadDefault(user)));
	}
	@RequestMapping(  value = "/getEncrypted/{userId}",  method = RequestMethod.POST )
	public void doGetEncrypted(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data ,@PathVariable(value="userId") String id ) throws Exception {
		Gson g = new Gson();
	    String toEncrypt = id;
		if(toEncrypt != null) {
            if(toEncrypt.length() > 0) {
                try {
                    toEncrypt = Base64.getEncoder().encodeToString(toEncrypt.getBytes(StandardCharsets.UTF_8)) + "=";

                    String secretPass = "PCMSDISPLAY";
                    String secretSalt = "OHSHIT";
                    byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0,
                                  0, 0, 0, 0, 0, 0, 0, 0 };
                    IvParameterSpec ivspec = new IvParameterSpec(iv);

                    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                    KeySpec spec = new PBEKeySpec(secretPass.toCharArray(), secretSalt.getBytes(), 65536, 256);
                    SecretKey secretTemp = factory.generateSecret(spec);
                    SecretKeySpec secretKey = new SecretKeySpec(secretTemp.getEncoded(), "AES");

                    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
 
                    toEncrypt = Base64.getEncoder().encodeToString(cipher.doFinal(toEncrypt.getBytes(StandardCharsets.UTF_8)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  
		EncryptedDetail bean = new EncryptedDetail();
		bean.setEncrypted(toEncrypt);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(bean));
	}
}
