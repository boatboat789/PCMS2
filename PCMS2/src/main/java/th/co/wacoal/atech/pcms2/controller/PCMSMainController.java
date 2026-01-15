package th.co.wacoal.atech.pcms2.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import th.co.wacoal.atech.pcms2.entities.ColumnHiddenDetail;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.EncryptedDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSAllDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.entities.UserDetail;
import th.co.wacoal.atech.pcms2.service.PCMSMainService;
import th.co.wacoal.atech.pcms2.service.master.ColumnSettingService;
import th.co.wacoal.atech.pcms2.service.master.ConfigCustomerUserService;
import th.co.wacoal.atech.pcms2.service.master.FromSapMainSaleService;
import th.co.wacoal.atech.pcms2.service.master.PermitsService;
import th.co.wacoal.atech.pcms2.service.master.PPMM.UserStatusDetailService; 
@Controller
@RequestMapping(value = { "/Main", "/" ,"" })
public class PCMSMainController {
//	private String myPassword = "PCMSDISPLAY";
	@SuppressWarnings("unused")
	@Autowired
	private ServletContext context;
	@SuppressWarnings("unused")
	private String LOCAL_DIRECTORY;
	@SuppressWarnings("unused")
	private String FTP_DIRECTORY;
	private PCMSMainService pcmsMainService;
	private UserStatusDetailService usdService;
	

	private ConfigCustomerUserService configCustomerUserService  ;
	private ColumnSettingService columnSettingService  ;
	private FromSapMainSaleService fromSapMainSaleService  ;  
	private PermitsService permitsService ;  
	private ModelAndView mv = new ModelAndView();
    @Autowired
	public PCMSMainController(PCMSMainService pcmsMainService 
			,UserStatusDetailService usdService
			,ConfigCustomerUserService configCustomerUserService  
			, ColumnSettingService columnSettingService  
			, FromSapMainSaleService fromSapMainSaleService  
			, PermitsService permitsService) { 
    	this.pcmsMainService = pcmsMainService;
		this.usdService = usdService;

		this.configCustomerUserService = configCustomerUserService;
		this.columnSettingService = columnSettingService;
		this.fromSapMainSaleService = fromSapMainSaleService;
		this.permitsService = permitsService; 
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
					String OS = System.getProperty("os.name").toLowerCase();
					ArrayList<ConfigCustomerUserDetail> listConfigCus = configCustomerUserService.getConfigCustomerUserDetail(user); 
					if(listConfigCus.isEmpty()) {
						ConfigCustomerUserDetail ccuDetail = new ConfigCustomerUserDetail();
						ccuDetail.setUserId(user);
						listConfigCus.add(ccuDetail);
						
						cusNameList = fromSapMainSaleService.getCustomerNameDetail();
						cusShortNameList = fromSapMainSaleService.getCustomerShortNameDetail();
					}
					else {
						cusNameList = fromSapMainSaleService.getCustomerNameDetail(listConfigCus);
						cusShortNameList = fromSapMainSaleService.getCustomerShortNameDetail(listConfigCus);
					}
					boolean isCustomer = false ;
					if(userObject != null) {
						isCustomer = userObject.isCustomer();
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
					mv.addObject("ConfigCusListTest", listConfigCus );
					mv.addObject("ConfigCusList", g.toJson(listConfigCus));
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
	@RequestMapping(value ="/fakeSubmit",  method = RequestMethod.POST)
    public void submitForm(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
    		@Validated @ModelAttribute("PCMSTable") PCMSTableDetail pd, BindingResult br) throws IOException
    {
//		fake pass value for submit
//		PCMSMainModel model = new PCMSMainModel();
//		Gson g = new Gson();
//		ArrayList<PCMSTableDetail> poList = new ArrayList<PCMSTableDetail>();
//		poList.add(pd); 
//        response.setContentType("application/json");
//		PrintWriter out = response.getWriter();
//		out.println(g.toJson(model.getPrdDetailByRow( poList)));

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
		out.println(g.toJson(pcmsMainService.searchByDetail( poList,isCustomer)));
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
		out.println(g.toJson(columnSettingService.upsertColumnVisibleSummary( pd)));
	}
	@RequestMapping(  value = "/saveDefault",  method = RequestMethod.POST )
	public void doGetSaveDefault(HttpSession session,HttpServletRequest request, HttpServletResponse response 
			, @RequestBody ArrayList<PCMSTableDetail> poList  ) throws IOException {
		Gson g = new Gson();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(pcmsMainService.saveDefault( poList)));
	}
	@RequestMapping(  value = "/loadDefault",  method = RequestMethod.POST )
	public void doGetLoadDefault(HttpSession session,HttpServletRequest request, HttpServletResponse response ) throws IOException {
		Gson g = new Gson();
		ArrayList<PCMSTableDetail> poList = new ArrayList<>();
		String user = (String) session.getAttribute("user"); 
			PCMSTableDetail pd = new PCMSTableDetail(); 
			pd.setUserId(user);
			poList.add(pd);
//		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(pcmsMainService.loadDefault( poList)));
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
