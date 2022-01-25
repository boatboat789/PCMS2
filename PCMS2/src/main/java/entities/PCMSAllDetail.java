package entities;

import java.util.ArrayList;

public class PCMSAllDetail {
	private String ProductionOrder;
	  private String LotNo;
	  private String Batch;
	  private String LabNo;
	  private String PrdCreateDate;
	  private String DueDate;
      private String SaleOrder;
      private String SaleLine; 
	  private String PurchaseOrder;
	  private String ArticleFG;
	  private String DesignFG;
	  private String CustomerName;
	  private String CustomerShortName;
	  private String Shade;
	  private String BookNo;
	  private String Center;
	  private String MaterialNo;
	  private String Volumn;
	  private String SaleUnit;
	  private String STDUnit;
	  private String Color;
	  private String ColorCustomer;
	  private String PlanGreigeDate;
	  private String RefPrd;
	  private String GreigeInDate;
	  private String BCAware;
	  private String OrderPuang;
	  private String UserStatus;
	  private String LabStatus;
	  private String CFMDatePlan;
	  private String DeliveryDate;
	  private String BCDate;
	  private String RemarkOne;
	  private String RemarkTwo;
	  private String RemarkThree;
	  private String RemAfterCloseOne;
	  private String RemAfterCloseTwo;
	  private String RemAfterCloseThree;
	  private String GreigeArticle;
	  private String GreigeDesign;
	  private ArrayList<PODetail> poDetailList;
	  private ArrayList<PresetDetail> presetDetailList;
	  private ArrayList<DyeingDetail> dyeingDetailList;
	  private ArrayList<SendTestQCDetail> sendTestQCDetailList;
	  private ArrayList<FinishingDetail> finishingDetailList; 
	  private ArrayList<InspectDetail> inspectDetailList;
	  private ArrayList<PackingDetail> packingDetailList;
	  private ArrayList<WorkInLabDetail> workInLabDetailList;
	  private ArrayList<WaitTestDetail> waitTestDetailList;
	  private ArrayList<CFMDetail> cfmDetailList;
	  private ArrayList<SaleDetail> saleDetailList; 
	  private ArrayList<SaleInputDetail> saleInputDetailList; 
	  private ArrayList<InputDateDetail> submitDateDetailList ;
	  private ArrayList<NCDetail> ncDetailList;
	  private ArrayList<ReceipeDetail> receipeDetailList;
	  
	public PCMSAllDetail(String productionOrder, String lotNo, String batch, String labNo, String prdCreateDate,
			String dueDate, String saleOrder, String saleLine, String purchaseOrder, String articleFG, String designFG,
			String customerName,String customerShortName ,String shade, String bookNo, String center, String materialNo, String volumn,
			String saleUnit, String sTDUnit, String color, String planGreigeDate, String refPrd, String greigeInDate,
			String bCAware, String orderPuang, String userStatus, String labStatus, String cFMDatePlan,
			String DeliveryDate, String bCDate, String remarkOne, String remarkTwo, String remarkThree,
			String remAfterCloseOne, String remAfterCloseTwo, String remAfterCloseThree,
			String GreigeArticle ,String GreigeDesign,String ColorCustomer) {
		super();
		CustomerShortName = customerShortName;
		ProductionOrder = productionOrder;
		LotNo = lotNo;
		Batch = batch;
		LabNo = labNo;
		PrdCreateDate = prdCreateDate;
		DueDate = dueDate;
		SaleOrder = saleOrder;
		SaleLine = saleLine;
		PurchaseOrder = purchaseOrder;
		ArticleFG = articleFG;
		DesignFG = designFG;
		CustomerName = customerName;
		Shade = shade;
		BookNo = bookNo;
		Center = center;
		MaterialNo = materialNo;
		Volumn = volumn;
		SaleUnit = saleUnit;
		STDUnit = sTDUnit;
		Color = color;
		PlanGreigeDate = planGreigeDate;
		RefPrd = refPrd;
		GreigeInDate = greigeInDate;
		BCAware = bCAware;
		OrderPuang = orderPuang;
		UserStatus = userStatus;
		LabStatus = labStatus;
		CFMDatePlan = cFMDatePlan;
		this.DeliveryDate = DeliveryDate;
		BCDate = bCDate;
		RemarkOne = remarkOne;
		RemarkTwo = remarkTwo;
		RemarkThree = remarkThree;
		RemAfterCloseOne = remAfterCloseOne;
		RemAfterCloseTwo = remAfterCloseTwo;
		RemAfterCloseThree = remAfterCloseThree;
		this.GreigeArticle = GreigeArticle;
		this.GreigeDesign = GreigeDesign;
		this.ColorCustomer = ColorCustomer;
	}
	public String getColorCustomer() {
		return ColorCustomer;
	}
	public void setColorCustomer(String colorCustomer) {
		ColorCustomer = colorCustomer;
	}
	public ArrayList<InputDateDetail> getSubmitDateDetailList() {
		return submitDateDetailList;
	}
	public void setSubmitDateDetailList(ArrayList<InputDateDetail> submitDateDetailList) {
		this.submitDateDetailList = submitDateDetailList;
	}
	public String getCustomerShortName() {
		return CustomerShortName;
	}
	public void setCustomerShortName(String customerShortName) {
		CustomerShortName = customerShortName;
	}
	public String getGreigeArticle() {
		return GreigeArticle;
	}
	public void setGreigeArticle(String greigeArticle) {
		GreigeArticle = greigeArticle;
	}
	public String getGreigeDesign() {
		return GreigeDesign;
	}
	public void setGreigeDesign(String greigeDesign) {
		GreigeDesign = greigeDesign;
	}
	public ArrayList<InputDateDetail> getInputDateDetailList() {
		return submitDateDetailList;
	}
	public void setInputDateDetailList(ArrayList<InputDateDetail> submitDateDetailList) {
		this.submitDateDetailList = submitDateDetailList;
	}
	public ArrayList<WorkInLabDetail> getWorkInLabDetailList() {
		return workInLabDetailList;
	}
	public void setWorkInLabDetailList(ArrayList<WorkInLabDetail> workInLabDetailList) {
		this.workInLabDetailList = workInLabDetailList;
	}
	public ArrayList<WaitTestDetail> getWaitTestDetailList() {
		return waitTestDetailList;
	}
	public void setWaitTestDetailList(ArrayList<WaitTestDetail> waitTestDetailList) {
		this.waitTestDetailList = waitTestDetailList;
	}
	public ArrayList<CFMDetail> getCfmDetailList() {
		return cfmDetailList;
	}
	public void setCfmDetailList(ArrayList<CFMDetail> cfmDetailList) {
		this.cfmDetailList = cfmDetailList;
	}
	public ArrayList<SaleDetail> getSaleDetailList() {
		return saleDetailList;
	}
	public void setSaleDetailList(ArrayList<SaleDetail> saleDetailList) {
		this.saleDetailList = saleDetailList;
	}
	public ArrayList<SaleInputDetail> getSaleInputDetailList() {
		return saleInputDetailList;
	}
	public void setSaleInputDetailList(ArrayList<SaleInputDetail> saleInputDetailList) {
		this.saleInputDetailList = saleInputDetailList;
	}
	public ArrayList<InputDateDetail> getSubmitdatDetailList() {
		return submitDateDetailList;
	}
	public void setSubmitdatDetailList(ArrayList<InputDateDetail> submitDateDetailList) {
		this.submitDateDetailList = submitDateDetailList;
	}
	public ArrayList<NCDetail> getNcDetailList() {
		return ncDetailList;
	}
	public void setNcDetailList(ArrayList<NCDetail> ncDetailList) {
		this.ncDetailList = ncDetailList;
	}
	public ArrayList<ReceipeDetail> getReceipeDetailList() {
		return receipeDetailList;
	}
	public void setReceipeDetailList(ArrayList<ReceipeDetail> receipeDetailList) {
		this.receipeDetailList = receipeDetailList;
	}
	public ArrayList<SendTestQCDetail> getSendTestQCDetailList() {
		return sendTestQCDetailList;
	}
	public void setSendTestQCDetailList(ArrayList<SendTestQCDetail> sendTestQCDetailList) {
		this.sendTestQCDetailList = sendTestQCDetailList;
	}
	public ArrayList<FinishingDetail> getFinishingDetailList() {
		return finishingDetailList;
	}
	public void setFinishingDetailList(ArrayList<FinishingDetail> finishingDetailList) {
		this.finishingDetailList = finishingDetailList;
	}
	public ArrayList<InspectDetail> getInspectDetailList() {
		return inspectDetailList;
	}
	public void setInspectDetailList(ArrayList<InspectDetail> inspectDetailList) {
		this.inspectDetailList = inspectDetailList;
	}
	public ArrayList<PackingDetail> getPackingDetailList() {
		return packingDetailList;
	}
	public void setPackingDetailList(ArrayList<PackingDetail> packingDetailList) {
		this.packingDetailList = packingDetailList;
	}
	public ArrayList<PresetDetail> getPresetDetailList() {
		return presetDetailList;
	}
	public void setPresetDetailList(ArrayList<PresetDetail> presetDetailList) {
		this.presetDetailList = presetDetailList;
	}
	public ArrayList<DyeingDetail> getDyeingDetailList() {
		return dyeingDetailList;
	}
	public void setDyeingDetailList(ArrayList<DyeingDetail> dyeingDetailList) {
		this.dyeingDetailList = dyeingDetailList;
	}
	public ArrayList<PODetail> getPoDetailList() {
		return poDetailList;
	}
	public void setPoDetailList(ArrayList<PODetail> poDetailList) {
		this.poDetailList = poDetailList;
	}
	public String getProductionOrder() {
		return ProductionOrder;
	}
	public void setProductionOrder(String productionOrder) {
		ProductionOrder = productionOrder;
	}
	public String getLotNo() {
		return LotNo;
	}
	public void setLotNo(String lotNo) {
		LotNo = lotNo;
	}
	public String getBatch() {
		return Batch;
	}
	public void setBatch(String batch) {
		Batch = batch;
	}
	public String getLabNo() {
		return LabNo;
	}
	public void setLabNo(String labNo) {
		LabNo = labNo;
	}
	public String getPrdCreateDate() {
		return PrdCreateDate;
	}
	public void setPrdCreateDate(String prdCreateDate) {
		PrdCreateDate = prdCreateDate;
	}
	public String getDueDate() {
		return DueDate;
	}
	public void setDueDate(String dueDate) {
		DueDate = dueDate;
	}
	public String getSaleOrder() {
		return SaleOrder;
	}
	public void setSaleOrder(String saleOrder) {
		SaleOrder = saleOrder;
	}
	public String getSaleLine() {
		return SaleLine;
	}
	public void setSaleLine(String saleLine) {
		SaleLine = saleLine;
	}
	public String getPurchaseOrder() {
		return PurchaseOrder;
	}
	public void setPurchaseOrder(String purchaseOrder) {
		PurchaseOrder = purchaseOrder;
	}
	public String getArticleFG() {
		return ArticleFG;
	}
	public void setArticleFG(String articleFG) {
		ArticleFG = articleFG;
	}
	public String getDesignFG() {
		return DesignFG;
	}
	public void setDesignFG(String designFG) {
		DesignFG = designFG;
	}
	public String getCustomerName() {
		return CustomerName;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	public String getShade() {
		return Shade;
	}
	public void setShade(String shade) {
		Shade = shade;
	}
	public String getBookNo() {
		return BookNo;
	}
	public void setBookNo(String bookNo) {
		BookNo = bookNo;
	}
	public String getCenter() {
		return Center;
	}
	public void setCenter(String center) {
		Center = center;
	}
	public String getMaterialNo() {
		return MaterialNo;
	}
	public void setMaterialNo(String materialNo) {
		MaterialNo = materialNo;
	}
	public String getVolumn() {
		return Volumn;
	}
	public void setVolumn(String volumn) {
		Volumn = volumn;
	}
	public String getSaleUnit() {
		return SaleUnit;
	}
	public void setSaleUnit(String saleUnit) {
		SaleUnit = saleUnit;
	}
	public String getSTDUnit() {
		return STDUnit;
	}
	public void setSTDUnit(String sTDUnit) {
		STDUnit = sTDUnit;
	}
	public String getColor() {
		return Color;
	}
	public void setColor(String color) {
		Color = color;
	}
	public String getPlanGreigeDate() {
		return PlanGreigeDate;
	}
	public void setPlanGreigeDate(String planGreigeDate) {
		PlanGreigeDate = planGreigeDate;
	}
	public String getRefPrd() {
		return RefPrd;
	}
	public void setRefPrd(String refPrd) {
		RefPrd = refPrd;
	}
	public String getGreigeInDate() {
		return GreigeInDate;
	}
	public void setGreigeInDate(String greigeInDate) {
		GreigeInDate = greigeInDate;
	}
	public String getBCAware() {
		return BCAware;
	}
	public void setBCAware(String bCAware) {
		BCAware = bCAware;
	}
	public String getOrderPuang() {
		return OrderPuang;
	}
	public void setOrderPuang(String orderPuang) {
		OrderPuang = orderPuang;
	}
	public String getUserStatus() {
		return UserStatus;
	}
	public void setUserStatus(String userStatus) {
		UserStatus = userStatus;
	}
	public String getLabStatus() {
		return LabStatus;
	}
	public void setLabStatus(String labStatus) {
		LabStatus = labStatus;
	}
	public String getCFMDatePlan() {
		return CFMDatePlan;
	}
	public void setCFMDatePlan(String cFMDatePlan) {
		CFMDatePlan = cFMDatePlan;
	}
	public String getDeliveryDate() {
		return DeliveryDate;
	}
	public void setDeliveryDate(String DeliveryDate) {
		this.DeliveryDate = DeliveryDate;
	}
	public String getBCDate() {
		return BCDate;
	}
	public void setBCDate(String bCDate) {
		BCDate = bCDate;
	}
	public String getRemarkOne() {
		return RemarkOne;
	}
	public void setRemarkOne(String remarkOne) {
		RemarkOne = remarkOne;
	}
	public String getRemarkTwo() {
		return RemarkTwo;
	}
	public void setRemarkTwo(String remarkTwo) {
		RemarkTwo = remarkTwo;
	}
	public String getRemarkThree() {
		return RemarkThree;
	}
	public void setRemarkThree(String remarkThree) {
		RemarkThree = remarkThree;
	}
	public String getRemAfterCloseOne() {
		return RemAfterCloseOne;
	}
	public void setRemAfterCloseOne(String remAfterCloseOne) {
		RemAfterCloseOne = remAfterCloseOne;
	}
	public String getRemAfterCloseTwo() {
		return RemAfterCloseTwo;
	}
	public void setRemAfterCloseTwo(String remAfterCloseTwo) {
		RemAfterCloseTwo = remAfterCloseTwo;
	}
	public String getRemAfterCloseThree() {
		return RemAfterCloseThree;
	}
	public void setRemAfterCloseThree(String remAfterCloseThree) {
		RemAfterCloseThree = remAfterCloseThree;
	}
}
