package controller;

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

import entities.ColumnHiddenDetail;
import entities.ConfigCustomerUserDetail;
import entities.EncryptedDetail;
import entities.PCMSAllDetail;
import entities.PCMSTableDetail;
import model.LogInModel;
import model.PCMSMainModel;

@Controller
@RequestMapping(value = { "/Main" }) 
public class PCMSMainController {
//	private String myPassword = "PCMSDISPLAY";
	@Autowired 
	private ServletContext context;  
	private String LOCAL_DIRECTORY;
	private String FTP_DIRECTORY;  
	@RequestMapping(method = { RequestMethod.GET })
	public ModelAndView test(HttpSession session) {       
		ModelAndView mv = new ModelAndView();
		Gson g = new Gson();
		PCMSMainModel model = new PCMSMainModel(); 
		LogInModel logInModel = new LogInModel( );	   
		String user = (String) session.getAttribute("user");
		 ArrayList<ColumnHiddenDetail> list = model.getColVisibleDetail(user);
		 String[] arrayCol = null  ; 
		 if(list.size() == 0) { arrayCol = null  ;} 
		 else {  arrayCol = list.get(0).getColVisibleSummary().split(","); }    
		String OS = System.getProperty("os.name").toLowerCase();	  

		ArrayList<ConfigCustomerUserDetail> listConfigCus = logInModel.getConfigCustomerUserDetail(user);
		ArrayList<PCMSAllDetail> cusNameList = null ;
		ArrayList<PCMSAllDetail> cusShortNameList = null ;
//		System.out.println(listConfigCus.size());
		if(listConfigCus.size() > 0) {
			cusNameList = model.getCustomerNameList(listConfigCus);
			cusShortNameList = model.getCustomerShortNameList(listConfigCus);
		}
		else { 
			cusNameList = model.getCustomerNameList();
			cusShortNameList = model.getCustomerShortNameList();
		}
		mv.setViewName("PCMSMain/PCMSMain");  
		mv.addObject("OS", g.toJson(OS));
		mv.addObject("UserID", g.toJson(user));
		mv.addObject("ColList", g.toJson(arrayCol));
		mv.addObject("ConfigCusList", g.toJson(listConfigCus));
		mv.addObject("DivisionList", g.toJson(model.getDivisionList()));
		mv.addObject("SaleNumberList", g.toJson(model.getSaleNumberList()));
		mv.addObject("UserStatusList", g.toJson(model.getUserStatusList()));    
		mv.addObject("CusNameList", g.toJson(cusNameList));
		mv.addObject("CusShortNameList", g.toJson(cusShortNameList));
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
//        System.out.println(pd.getSaleOrder()+"d");
//        response.setContentType("application/json");
//		PrintWriter out = response.getWriter(); 
//		out.println(g.toJson(model.getPrdDetailByRow( poList)));  
		
    }  
	@RequestMapping(  value = "/searchByDetail",  method = RequestMethod.POST )
	public void doGetSearchByDetail(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data ) throws IOException {
		PCMSMainModel model = new PCMSMainModel();  
//		 System.out.println(data);          
		Gson g = new Gson(); 
		PCMSTableDetail[] userArray = (PCMSTableDetail[]) g.fromJson(data, PCMSTableDetail[].class);
		ArrayList<PCMSTableDetail> poList = new ArrayList<PCMSTableDetail>();
		int i = 0; 
		for (i = 0; i < userArray.length; i++) {
			PCMSTableDetail pd = new PCMSTableDetail();
			pd.setCustomerName(userArray[i].getCustomerName());
			pd.setCustomerShortName(userArray[i].getCustomerShortName());
			pd.setSaleNumber(userArray[i].getSaleNumber());
			pd.setSaleOrder(userArray[i].getSaleOrder());  
			pd.setProductionOrder(userArray[i].getProductionOrder());  
			pd.setArticleFG(userArray[i].getArticleFG()); 
			pd.setDesignFG(userArray[i].getDesignFG()); 
			pd.setSaleOrder(userArray[i].getSaleOrder()); 
			pd.setSaleOrderCreateDate(userArray[i].getSaleOrderCreateDate()) ;
			pd.setProductionOrderCreateDate(userArray[i].getProductionOrderCreateDate());
			pd.setMaterialNo(userArray[i].getMaterialNo());
			pd.setLabNo(userArray[i].getLabNo()); 
//			pd.setUserStatus(userArray[i].getUserStatus());
			pd.setUserStatusList(userArray[i].getUserStatusList());
			pd.setCustomerNameList(userArray[i].getCustomerNameList());
			pd.setDivisionList(userArray[i].getDivisionList());
			pd.setCustomerShortNameList(userArray[i].getCustomerShortNameList());
			pd.setDeliveryStatus(userArray[i].getDeliveryStatus());
			pd.setDistChannel(userArray[i].getDistChannel());
			pd.setSaleStatus(userArray[i].getSaleStatus());
			pd.setDueDate(userArray[i].getDueDate());
			poList.add(pd);   
		} 
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
//		System.out.println("he");
		out.println(g.toJson(model.searchByDetail( poList)));  
	} 
	@RequestMapping(  value = "/getPrdDetailByRow",  method = RequestMethod.POST )
	public void doGetPrdDetailByRow(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PCMSMainModel model = new PCMSMainModel();
		Gson g = new Gson();  
		PCMSTableDetail[] userArray = (PCMSTableDetail[]) g.fromJson(data, PCMSTableDetail[].class);
		ArrayList<PCMSTableDetail> poList = new ArrayList<PCMSTableDetail>();
		
		int i = 0;  
		for (i = 0; i < userArray.length; i++) {
			PCMSTableDetail pd = new PCMSTableDetail();
			pd.setCustomerShortName(userArray[i].getCustomerShortName());
			pd.setSaleNumber(userArray[i].getSaleNumber());
			pd.setSaleOrder(userArray[i].getSaleOrder());  
			pd.setProductionOrder(userArray[i].getProductionOrder()); 
			pd.setArticleFG(userArray[i].getArticleFG()); 
			pd.setDesignFG(userArray[i].getDesignFG()); 
			pd.setSaleLine(userArray[i].getSaleLine()); 
			pd.setSaleOrderCreateDate(userArray[i].getSaleOrderCreateDate()) ;
			pd.setProductionOrderCreateDate(userArray[i].getProductionOrderCreateDate());
			pd.setMaterialNo(userArray[i].getMaterialNo());
			pd.setLabNo(userArray[i].getLabNo());
			pd.setUserStatus(userArray[i].getUserStatus());
			pd.setDeliveryStatus(userArray[i].getDeliveryStatus());
			pd.setDistChannel(userArray[i].getDistChannel());
			pd.setSaleStatus(userArray[i].getSaleStatus());
			poList.add(pd);   
		} 
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(model.getPrdDetailByRow( poList)));  
//		out.println(g.toJson(null ));  
	}
	@RequestMapping(  value = "/saveColSettingToServer",  method = RequestMethod.POST )
	public void doSaveColSettingToServer(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data) throws IOException {
		PCMSMainModel model = new PCMSMainModel();
		String user = (String) session.getAttribute("user");
		Gson g = new Gson(); 
		String [] userArray = (String[]) g.fromJson(data, String[].class);
		ArrayList<ColumnHiddenDetail> poList = new ArrayList<ColumnHiddenDetail>();
		int i = 0; 
		String colVisible = "";
		for (i = 0; i < userArray.length; i++) { 
			colVisible += userArray[i];
			if(i!= userArray.length - 1) {
				colVisible +=",";   
			} 
		}  
//		System.out.println(colVisible);
		ColumnHiddenDetail pd = new ColumnHiddenDetail(); 
		pd.setUserId(user);
		pd.setColVisibleSummary(colVisible);
		poList.add(pd);   
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
//		System.out.println("he");
		out.println(g.toJson(model.saveColSettingToServer( pd)));  
//		out.println();  
	}
	@RequestMapping(  value = "/saveDefault",  method = RequestMethod.POST )
	public void doGetSaveDefault(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data ) throws IOException {
		PCMSMainModel model = new PCMSMainModel();            
		Gson g = new Gson(); 
		PCMSTableDetail[] userArray = (PCMSTableDetail[]) g.fromJson(data, PCMSTableDetail[].class);
		ArrayList<PCMSTableDetail> poList = new ArrayList<PCMSTableDetail>();
		int i = 0; 
		String user = (String) session.getAttribute("user"); 
		for (i = 0; i < userArray.length; i++) {
			PCMSTableDetail pd = new PCMSTableDetail();
			pd.setCustomerName(userArray[i].getCustomerName());
			pd.setCustomerShortName(userArray[i].getCustomerShortName());
			pd.setSaleNumber(userArray[i].getSaleNumber());
			pd.setSaleOrder(userArray[i].getSaleOrder());  
			pd.setProductionOrder(userArray[i].getProductionOrder());  
			pd.setArticleFG(userArray[i].getArticleFG()); 
			pd.setDesignFG(userArray[i].getDesignFG()); 
			pd.setSaleOrder(userArray[i].getSaleOrder()); 
			pd.setSaleOrderCreateDate(userArray[i].getSaleOrderCreateDate()) ;
			pd.setProductionOrderCreateDate(userArray[i].getProductionOrderCreateDate());
			pd.setMaterialNo(userArray[i].getMaterialNo());
			pd.setLabNo(userArray[i].getLabNo()); 
			pd.setUserStatus(userArray[i].getUserStatus());
			pd.setUserStatusList(userArray[i].getUserStatusList());
			pd.setCustomerNameList(userArray[i].getCustomerNameList());
			pd.setCustomerShortNameList(userArray[i].getCustomerShortNameList());
			pd.setDeliveryStatus(userArray[i].getDeliveryStatus());
			pd.setDistChannel(userArray[i].getDistChannel());
			pd.setSaleStatus(userArray[i].getSaleStatus());
			pd.setDueDate(userArray[i].getDueDate());
			pd.setDivisionList(userArray[i].getDivisionList());
			pd.setUserId(user);
			poList.add(pd);   
		}  
//		System.out.println(user);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(model.saveDefault( poList)));  
	}
	@RequestMapping(  value = "/loadDefault",  method = RequestMethod.POST )
	public void doGetLoadDefault(HttpSession session,HttpServletRequest request, HttpServletResponse response ) throws IOException {
		PCMSMainModel model = new PCMSMainModel();            
		Gson g = new Gson(); 
//		PCMSTableDetail[] userArray = (PCMSTableDetail[]) g.fromJson(data, PCMSTableDetail[].class);
		ArrayList<PCMSTableDetail> poList = new ArrayList<PCMSTableDetail>();
		String user = (String) session.getAttribute("user"); 
		int i = 0; 
//		for (i = 0; i < userArray.length; i++) {
			PCMSTableDetail pd = new PCMSTableDetail();
//			pd.setCustomerName(userArray[i].getCustomerName());
//			pd.setCustomerShortName(userArray[i].getCustomerShortName());
//			pd.setSaleNumber(userArray[i].getSaleNumber());
//			pd.setSaleOrder(userArray[i].getSaleOrder());  
//			pd.setProductionOrder(userArray[i].getProductionOrder());  
//			pd.setArticleFG(userArray[i].getArticleFG()); 
//			pd.setDesignFG(userArray[i].getDesignFG()); 
//			pd.setSaleOrder(userArray[i].getSaleOrder()); 
//			pd.setSaleOrderCreateDate(userArray[i].getSaleOrderCreateDate()) ;
//			pd.setProductionOrderCreateDate(userArray[i].getProductionOrderCreateDate());
//			pd.setMaterialNo(userArray[i].getMaterialNo());
//			pd.setLabNo(userArray[i].getLabNo()); 
//			pd.setUserStatus(userArray[i].getUserStatus());
//			pd.setUserStatusList(userArray[i].getUserStatusList());
//			pd.setCustomerNameList(userArray[i].getCustomerNameList());
//			pd.setCustomerShortNameList(userArray[i].getCustomerShortNameList());
//			pd.setDeliveryStatus(userArray[i].getDeliveryStatus());
//			pd.setDistChannel(userArray[i].getDistChannel());
//			pd.setSaleStatus(userArray[i].getSaleStatus());
//			pd.setDueDate(userArray[i].getDueDate());
			pd.setUserId(user);
			poList.add(pd);   
//		} 
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(model.loadDefault( poList)));  
	}
	@RequestMapping(  value = "/getEncrypted/{userId}",  method = RequestMethod.POST )
	public void doGetEncrypted(HttpSession session,HttpServletRequest request, HttpServletResponse response ,
			@RequestBody String data ,@PathVariable(value="userId") String id ) throws Exception {   
		Gson g = new Gson(); 
//		PCMSTableDetail[] userArray = (PCMSTableDetail[]) g.fromJson(data, PCMSTableDetail[].class);
//		ArrayList<PCMSTableDetail> poList = new ArrayList<PCMSTableDetail>();
//		int i = 0;  
//
//        String ciphertextFromJavascript = "";
//		for (i = 0; i < userArray.length; i++) {
//			PCMSTableDetail pd = new PCMSTableDetail();    
//			pd.setSaleOrder(userArray[i].getSaleOrder());
//			System.out.println(userArray[i].getSaleOrder());
//
//	         ciphertextFromJavascript = userArray[i].getSaleOrder();    
//			
//		}  
//		String secret = "PCMSDISPLAY";  
//		byte[] cipherData = Base64.getDecoder().decode(ciphertextFromJavascript);
//		byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);
//
//		MessageDigest md5 = MessageDigest.getInstance("MD5");
//		final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8), md5);
//		SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");   
//		IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);
//
//		byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
//		Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
//		aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
//		byte[] decryptedData = aesCBC.doFinal(encrypted);   
//		String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);
//
//		System.out.println(decryptedText);  
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
                    
//                    System.out.println(Base64.getEncoder().encodeToString(cipher.doFinal(toEncrypt.getBytes(StandardCharsets.UTF_8))));
                    toEncrypt = Base64.getEncoder().encodeToString(cipher.doFinal(toEncrypt.getBytes(StandardCharsets.UTF_8)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }  
            }
        }
//		System.out.println(toEncrypt); 
//		 String secretPass = "PCMSDISPLAY";
//         String secretSalt = "OHSHIT";
//         String plaintext = "";
//         try {
//             byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0,
//                           0, 0, 0, 0, 0, 0, 0, 0 };
//             IvParameterSpec ivspec = new IvParameterSpec(iv); 
//             SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//             KeySpec keySpec = new PBEKeySpec(secretPass.toCharArray(), secretSalt.getBytes(), 65536, 256);
//             SecretKey secretTemp = factory.generateSecret(keySpec);
//             SecretKeySpec secretKey = new SecretKeySpec(secretTemp.getEncoded(), "AES"); 
//             Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//             cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec); 
//             plaintext = new String(cipher.doFinal(Base64.getDecoder().decode(toEncrypt)));
//             plaintext = plaintext.substring(0, plaintext.length() - 1);
//             plaintext = new String(Base64.getDecoder().decode(plaintext));  
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//		System.out.println(plaintext);
		
		
		EncryptedDetail bean = new EncryptedDetail();
		bean.setEncrypted(toEncrypt);
		
		
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter(); 
		out.println(g.toJson(bean));  
//		out.println( toEncrypt );  
	}  
//	public byte[][] GenerateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password, MessageDigest md) {
//
//	    int digestLength = md.getDigestLength();
//	    int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
//	    byte[] generatedData = new byte[requiredLength];
//	    int generatedLength = 0; 
//	    try {
//	        md.reset();
//
//	        // Repeat process until sufficient data has been generated
//	        while (generatedLength < keyLength + ivLength) {
//
//	            // Digest data (last digest if available, password data, salt if available)
//	            if (generatedLength > 0)
//	                md.update(generatedData, generatedLength - digestLength, digestLength);
//	            md.update(password);
//	            if (salt != null)
//	                md.update(salt, 0, 8);
//	            md.digest(generatedData, generatedLength, digestLength);
//
//	            // additional rounds
//	            for (int i = 1; i < iterations; i++) {
//	                md.update(generatedData, generatedLength, digestLength);
//	                md.digest(generatedData, generatedLength, digestLength);
//	            }
//
//	            generatedLength += digestLength;
//	        }
//
//	        // Copy key and IV into separate byte arrays
//	        byte[][] result = new byte[2][];
//	        result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
//	        if (ivLength > 0)
//	            result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);
//
//	        return result;
//
//	    } catch (DigestException e) {
//	        throw new RuntimeException(e);
//
//	    } finally {
//	        // Clean out temporary data
//	        Arrays.fill(generatedData, (byte)0);
//	    }
//	}
}
