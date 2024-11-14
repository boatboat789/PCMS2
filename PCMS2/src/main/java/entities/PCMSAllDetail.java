package entities;

import java.util.ArrayList;

public class PCMSAllDetail {
	private String productionOrder;
	  private String lotNo;
	  private String batch;
	  private String labNo;
	  private String productionOrderCreateDate;
	  private String dueDate;
      private String saleOrder;
      private String saleLine;
	  private String purchaseOrder;
	  private String articleFG;
	  private String designFG;
	  private String customerName;
	  private String customerShortName;
	  private String shade;
	  private String bookNo;
	  private String center;
	  private String materialNo;
	  private String volumn;
	  private String saleUnit;
	  private String stdUnit;
	  private String color;
	  private String colorCustomer;
	  private String planGreigeDate;
	  private String refPrd;
	  private String greigeInDate;
	  private String bcAware;
	  private String orderPuang;
	  private String userStatus;
	  private String labStatus;
	  private String cfmDatePlan;
	  private String deliveryDate;
	  private String bcDate;
	  private String remarkOne;
	  private String remarkTwo;
	  private String remarkThree;
	  private String remAfterCloseOne;
	  private String remAfterCloseTwo;
	  private String remAfterCloseThree;
	  private String greigeArticle;
	  private String greigeDesign;
	  private ArrayList<PODetail> poDetailList;
	  private ArrayList<PresetDetail> presetDetailList;
	  private ArrayList<DyeingDetail> dyeingDetailList;
	  private ArrayList<SendTestQCDetail> sendTestQCDetailList;
	  private ArrayList<FinishingDetail> finishingDetailList;
	  private ArrayList<InspectDetail> inspectDetailList;
	  private ArrayList<PackingDetail> packingDetailList;
	  private ArrayList<WorkInLabDetail> workInLabDetailList;
//	  private ArrayList<WaitTestDetail> waitTestDetailList;
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
		this.customerShortName = customerShortName;
		this.productionOrder = productionOrder;
		this.lotNo = lotNo;
		this.batch = batch;
		this.labNo = labNo;
		this.productionOrderCreateDate = prdCreateDate;
		this.dueDate = dueDate;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.purchaseOrder = purchaseOrder;
		this.articleFG = articleFG;
		this.designFG = designFG;
		this.customerName = customerName;
		this.shade = shade;
		this.bookNo = bookNo;
		this.center = center;
		this.materialNo = materialNo;
		this.volumn = volumn;
		this.saleUnit = saleUnit;
		this.stdUnit = sTDUnit;
		this.color = color;
		this.planGreigeDate = planGreigeDate;
		this.refPrd = refPrd;
		this.greigeInDate = greigeInDate;
		this.bcAware = bCAware;
		this.orderPuang = orderPuang;
		this.userStatus = userStatus;
		this.labStatus = labStatus;
		this.cfmDatePlan = cFMDatePlan;
		this.deliveryDate = DeliveryDate;
		this.bcDate = bCDate;
		this.remarkOne = remarkOne;
		this.remarkTwo = remarkTwo;
		this.remarkThree = remarkThree;
		this.remAfterCloseOne = remAfterCloseOne;
		this.remAfterCloseTwo = remAfterCloseTwo;
		this.remAfterCloseThree = remAfterCloseThree;
		this.greigeArticle = GreigeArticle;
		this.greigeDesign = GreigeDesign;
		this.colorCustomer = ColorCustomer;
	}
	public PCMSAllDetail() {
		// TODO Auto-generated constructor stub
	}
	public String getProductionOrder() {
		return productionOrder;
	}
	public void setProductionOrder(String productionOrder) {
		this.productionOrder = productionOrder;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getLabNo() {
		return labNo;
	}
	public void setLabNo(String labNo) {
		this.labNo = labNo;
	}
	public String getProductionOrderCreateDate() {
		return productionOrderCreateDate;
	}
	public void setProductionOrderCreateDate(String productionOrderCreateDate) {
		this.productionOrderCreateDate = productionOrderCreateDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getSaleOrder() {
		return saleOrder;
	}
	public void setSaleOrder(String saleOrder) {
		this.saleOrder = saleOrder;
	}
	public String getSaleLine() {
		return saleLine;
	}
	public void setSaleLine(String saleLine) {
		this.saleLine = saleLine;
	}
	public String getPurchaseOrder() {
		return purchaseOrder;
	}
	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	public String getArticleFG() {
		return articleFG;
	}
	public void setArticleFG(String articleFG) {
		this.articleFG = articleFG;
	}
	public String getDesignFG() {
		return designFG;
	}
	public void setDesignFG(String designFG) {
		this.designFG = designFG;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerShortName() {
		return customerShortName;
	}
	public void setCustomerShortName(String customerShortName) {
		this.customerShortName = customerShortName;
	}
	public String getShade() {
		return shade;
	}
	public void setShade(String shade) {
		this.shade = shade;
	}
	public String getBookNo() {
		return bookNo;
	}
	public void setBookNo(String bookNo) {
		this.bookNo = bookNo;
	}
	public String getCenter() {
		return center;
	}
	public void setCenter(String center) {
		this.center = center;
	}
	public String getMaterialNo() {
		return materialNo;
	}
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	public String getVolumn() {
		return volumn;
	}
	public void setVolumn(String volumn) {
		this.volumn = volumn;
	}
	public String getSaleUnit() {
		return saleUnit;
	}
	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}
	public String getStdUnit() {
		return stdUnit;
	}
	public void setStdUnit(String stdUnit) {
		this.stdUnit = stdUnit;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getColorCustomer() {
		return colorCustomer;
	}
	public void setColorCustomer(String colorCustomer) {
		this.colorCustomer = colorCustomer;
	}
	public String getPlanGreigeDate() {
		return planGreigeDate;
	}
	public void setPlanGreigeDate(String planGreigeDate) {
		this.planGreigeDate = planGreigeDate;
	}
	public String getRefPrd() {
		return refPrd;
	}
	public void setRefPrd(String refPrd) {
		this.refPrd = refPrd;
	}
	public String getGreigeInDate() {
		return greigeInDate;
	}
	public void setGreigeInDate(String greigeInDate) {
		this.greigeInDate = greigeInDate;
	}
	public String getBcAware() {
		return bcAware;
	}
	public void setBcAware(String bcAware) {
		this.bcAware = bcAware;
	}
	public String getOrderPuang() {
		return orderPuang;
	}
	public void setOrderPuang(String orderPuang) {
		this.orderPuang = orderPuang;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getLabStatus() {
		return labStatus;
	}
	public void setLabStatus(String labStatus) {
		this.labStatus = labStatus;
	}
	public String getCfmDatePlan() {
		return cfmDatePlan;
	}
	public void setCfmDatePlan(String cfmDatePlan) {
		this.cfmDatePlan = cfmDatePlan;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getBcDate() {
		return bcDate;
	}
	public void setBcDate(String bcDate) {
		this.bcDate = bcDate;
	}
	public String getRemarkOne() {
		return remarkOne;
	}
	public void setRemarkOne(String remarkOne) {
		this.remarkOne = remarkOne;
	}
	public String getRemarkTwo() {
		return remarkTwo;
	}
	public void setRemarkTwo(String remarkTwo) {
		this.remarkTwo = remarkTwo;
	}
	public String getRemarkThree() {
		return remarkThree;
	}
	public void setRemarkThree(String remarkThree) {
		this.remarkThree = remarkThree;
	}
	public String getRemAfterCloseOne() {
		return remAfterCloseOne;
	}
	public void setRemAfterCloseOne(String remAfterCloseOne) {
		this.remAfterCloseOne = remAfterCloseOne;
	}
	public String getRemAfterCloseTwo() {
		return remAfterCloseTwo;
	}
	public void setRemAfterCloseTwo(String remAfterCloseTwo) {
		this.remAfterCloseTwo = remAfterCloseTwo;
	}
	public String getRemAfterCloseThree() {
		return remAfterCloseThree;
	}
	public void setRemAfterCloseThree(String remAfterCloseThree) {
		this.remAfterCloseThree = remAfterCloseThree;
	}
	public String getGreigeArticle() {
		return greigeArticle;
	}
	public void setGreigeArticle(String greigeArticle) {
		this.greigeArticle = greigeArticle;
	}
	public String getGreigeDesign() {
		return greigeDesign;
	}
	public void setGreigeDesign(String greigeDesign) {
		this.greigeDesign = greigeDesign;
	}
	public ArrayList<PODetail> getPoDetailList() {
		return poDetailList;
	}
	public void setPoDetailList(ArrayList<PODetail> poDetailList) {
		this.poDetailList = poDetailList;
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
	public ArrayList<WorkInLabDetail> getWorkInLabDetailList() {
		return workInLabDetailList;
	}
	public void setWorkInLabDetailList(ArrayList<WorkInLabDetail> workInLabDetailList) {
		this.workInLabDetailList = workInLabDetailList;
	}
//	public ArrayList<WaitTestDetail> getWaitTestDetailList() {
//		return waitTestDetailList;
//	}
//	public void setWaitTestDetailList(ArrayList<WaitTestDetail> waitTestDetailList) {
//		this.waitTestDetailList = waitTestDetailList;
//	}
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
	public ArrayList<InputDateDetail> getSubmitDateDetailList() {
		return submitDateDetailList;
	}
	public void setSubmitDateDetailList(ArrayList<InputDateDetail> submitDateDetailList) {
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
}
