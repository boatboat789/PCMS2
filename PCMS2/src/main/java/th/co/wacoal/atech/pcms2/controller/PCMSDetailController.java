package th.co.wacoal.atech.pcms2.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import th.co.wacoal.atech.pcms2.entities.ColumnHiddenDetail;
import th.co.wacoal.atech.pcms2.entities.ConfigCustomerUserDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.model.PCMSDetailModel;
import th.co.wacoal.atech.pcms2.model.master.ColumnSettingModel;
import th.co.wacoal.atech.pcms2.model.master.ConfigCustomerUserModel;
import th.co.wacoal.atech.pcms2.model.master.ConfigDepartmentModel;
import th.co.wacoal.atech.pcms2.model.master.FromSapMainSaleModel;
import th.co.wacoal.atech.pcms2.model.master.PermitsModel;
import th.co.wacoal.atech.pcms2.model.master.PlanCFMDateModel;
import th.co.wacoal.atech.pcms2.model.master.PlanCFMLabDateModel;
import th.co.wacoal.atech.pcms2.model.master.PlanSendCFMCusDateModel;
import th.co.wacoal.atech.pcms2.model.master.PPMM.UserStatusDetailModel;

@Controller
@RequestMapping(value = { "/Detail" })
public class PCMSDetailController {
	@SuppressWarnings("unused")
	@Autowired
	private ServletContext context;
	@SuppressWarnings("unused")
	private String LOCAL_DIRECTORY;
	@SuppressWarnings("unused")
	private String FTP_DIRECTORY;

	@Autowired
	public PCMSDetailController() {
	}

	@RequestMapping(method = { RequestMethod.GET })
	public ModelAndView getModelAndView(HttpSession session)
	{
		ModelAndView mv = new ModelAndView();
		ColumnSettingModel csModel = new ColumnSettingModel();
		FromSapMainSaleModel fsmsModel = new FromSapMainSaleModel();
		ConfigDepartmentModel cdmModel = new ConfigDepartmentModel();
		UserStatusDetailModel usdModel = new UserStatusDetailModel();
		ConfigCustomerUserModel ccuModel = new ConfigCustomerUserModel();
		PermitsModel permitsModel = new PermitsModel();
		Gson g = new Gson();
		String user = (String) session.getAttribute("user");
		if (user != null) {
			PermitDetail permit = (PermitDetail) session.getAttribute("permit");
			if (permit == null) {
				mv.setViewName("error/AccessDenied"); // Redirect to an access-denied view
				mv.addObject("errorMsg", "Contact IT for set permission first.");
			} else {
				if (permit.isPCMSMain()) {
					ArrayList<ColumnHiddenDetail> list = csModel.getColumnVisibleDetail(user);
					String[] arrayCol = null;
					if (list.size() == 0) {
						arrayCol = null;
					} else {
						arrayCol = list.get(0).getColVisibleDetail().split(",");
					}

					String OS = System.getProperty("os.name").toLowerCase();
					ArrayList<ConfigCustomerUserDetail> listConfigCus = ccuModel.getConfigCustomerUserDetail(user);
					if (listConfigCus.isEmpty()) {
						ConfigCustomerUserDetail ccuDetail = new ConfigCustomerUserDetail();
						ccuDetail.setUserId(user);
						listConfigCus.add(ccuDetail);
					}

					mv.setViewName("PCMSDetail/PCMSDetail");
					mv.addObject("PermitIdList", g.toJson(permitsModel.getPermitsDetail()));
					mv.addObject("OS", g.toJson(OS));
					mv.addObject("UserID", g.toJson(user));
					mv.addObject("OS", g.toJson(OS));
					mv.addObject("ColList", g.toJson(arrayCol));
					mv.addObject("ConfigCusListTest", listConfigCus);
					mv.addObject("ConfigCusList", g.toJson(listConfigCus));
					mv.addObject("DepList", g.toJson(cdmModel.getDelayedDepartmentList()));
					mv.addObject("DivisionList", g.toJson(fsmsModel.getDivisionDetail()));
					mv.addObject("SaleNumberList", g.toJson(fsmsModel.getSaleNumberDetail()));
					mv.addObject("UserStatusList", g.toJson(usdModel.getUserStatusDetail()));
					mv.addObject("CusNameList", g.toJson(fsmsModel.getCustomerNameDetail()));
					mv.addObject("CusShortNameList", g.toJson(fsmsModel.getCustomerShortNameDetail()));
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

	@RequestMapping(value = "/searchByDetail", method = RequestMethod.POST)
	public void doGetSearchByDetail(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestBody ArrayList<PCMSTableDetail> poList) throws IOException
	{
		PCMSDetailModel model = new PCMSDetailModel();
		Gson g = new Gson();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(model.searchByDetail(poList)));
	}

	@RequestMapping(value = "/saveInputDate", method = RequestMethod.POST)
	public void doGetSaveInputDate(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestBody ArrayList<PCMSSecondTableDetail> poList) throws IOException
	{
		PCMSDetailModel model = new PCMSDetailModel();
		String user = (String) session.getAttribute("user");
		Gson g = new Gson();
		for (PCMSSecondTableDetail bean : poList) {
			bean.setUserId(user);
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(model.saveInputDate(poList)));
	}

	@RequestMapping(value = "/saveInputDetail", method = RequestMethod.POST)
	public void doGetSaveInputDetail(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestBody ArrayList<PCMSSecondTableDetail> poList) throws IOException
	{
		PCMSDetailModel model = new PCMSDetailModel();
		String user = (String) session.getAttribute("user");
		Gson g = new Gson();
		for (PCMSSecondTableDetail bean : poList) {
			bean.setUserId(user);
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(model.saveInputDetail(poList)));
	}

	@RequestMapping(value = "/getCFMPlanDateDetail", method = RequestMethod.POST)
	public void doGetCFMPlanDate(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestBody ArrayList<PCMSSecondTableDetail> poList) throws IOException
	{
		PlanCFMDateModel model = new PlanCFMDateModel();
		@SuppressWarnings("unused") String user = (String) session.getAttribute("user");
		Gson g = new Gson();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(model.getCFMPlanDateDetail(poList)));
	}

	@RequestMapping(value = "/getCFMPlanLabDateDetail", method = RequestMethod.POST)
	public void doGetCFMPlanLabDate(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestBody ArrayList<PCMSSecondTableDetail> poList) throws IOException
	{

		PlanCFMLabDateModel pcfmldModel = new PlanCFMLabDateModel();
		@SuppressWarnings("unused") String user = (String) session.getAttribute("user");
		Gson g = new Gson();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(pcfmldModel.getCFMPlanLabDateDetail(poList)));
	}

	@RequestMapping(value = "/getDeliveryPlanDateDetail", method = RequestMethod.POST)
	public void doGetDeliveryDate(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestBody ArrayList<PCMSSecondTableDetail> poList) throws IOException
	{
		PCMSDetailModel model = new PCMSDetailModel();
		@SuppressWarnings("unused") String user = (String) session.getAttribute("user");
		Gson g = new Gson();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(model.getDeliveryPlanDateDetail(poList)));
	}

	@RequestMapping(value = "/getSendCFMCusDateDetail", method = RequestMethod.POST)
	public void doGetSendCFMCusDateDetail(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestBody ArrayList<PCMSSecondTableDetail> poList) throws IOException
	{
		PlanSendCFMCusDateModel model = new PlanSendCFMCusDateModel();
		@SuppressWarnings("unused") String user = (String) session.getAttribute("user");
		Gson g = new Gson();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(model.getSendCFMCusDateDetail(poList)));
	}

	@RequestMapping(value = "/saveColSettingToServer", method = RequestMethod.POST)
	public void doSaveColSettingToServer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestBody String data) throws IOException
	{
		ColumnSettingModel model = new ColumnSettingModel();
		String user = (String) session.getAttribute("user");
		Gson g = new Gson();
		String[] userArray = g.fromJson(data, String[].class);
		ArrayList<ColumnHiddenDetail> poList = new ArrayList<>();
		int i = 0;
		String colVisible = "";
		for (i = 0; i < userArray.length; i ++ ) {
			colVisible += userArray[i];
			if (i != userArray.length-1) {
				colVisible += ",";
			}
		}
		ColumnHiddenDetail pd = new ColumnHiddenDetail();
		pd.setUserId(user);
		pd.setColVisibleDetail(colVisible);
		poList.add(pd);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(model.upsertColumnSettingDetail(pd)));
	}

	@RequestMapping(value = "/saveDefault", method = RequestMethod.POST)
	public void doGetSaveDefault(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestBody ArrayList<PCMSTableDetail> poList) throws IOException
	{
		PCMSDetailModel model = new PCMSDetailModel();
		Gson g = new Gson();
		String user = (String) session.getAttribute("user");
		for (PCMSTableDetail bean : poList) {
			bean.setUserId(user);
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(model.saveDefault(poList)));
	}

	@RequestMapping(value = "/loadDefault", method = RequestMethod.POST)
	public void doGetLoadDefault(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		PCMSDetailModel model = new PCMSDetailModel();
		Gson g = new Gson();
		ArrayList<PCMSTableDetail> poList = new ArrayList<>();
		String user = (String) session.getAttribute("user");
		PCMSTableDetail pd = new PCMSTableDetail();
		pd.setUserId(user);
		poList.add(pd);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(model.loadDefault(poList)));
	}

	@RequestMapping(value = "/getSwitchProdOrderListByPrd", method = RequestMethod.POST)
	public void doGetSwitchProdOrderListByPrd(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestBody ArrayList<PCMSSecondTableDetail> poList) throws IOException
	{
		PCMSDetailModel model = new PCMSDetailModel();
		@SuppressWarnings("unused") String user = (String) session.getAttribute("user");
		Gson g = new Gson();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(model.getSwitchProdOrderListByPrd(poList)));
	}

	@RequestMapping(value = "/getSwitchProdOrderListByRowProd", method = RequestMethod.POST)
	public void doGetSwitchProdOrderListByRowProd(HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestBody ArrayList<PCMSSecondTableDetail> poList) throws IOException
	{
		PCMSDetailModel model = new PCMSDetailModel();
		@SuppressWarnings("unused") String user = (String) session.getAttribute("user");
		Gson g = new Gson();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(g.toJson(model.getSwitchProdOrderListByRowProd(poList)));
	}
}
