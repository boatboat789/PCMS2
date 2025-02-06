package th.co.wacoal.atech.pcms2.entities;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class PCMSTableDetail {
	private String saleOrder;
	private String saleLine;

	private String articleFG;
	private String designFG;
	private String distChannel ;
	private String color;
	private String colorCustomer;
	private String saleQuantity;
	private String billQuantity;
	private String saleUnit;
	private String productionOrder;
	private String totalQuantity;
	private String planGreigeDate;
	private String greigeInDate;
	private String userStatus;
	private String labStatus;
	private String dueDate;

	private String prepare;
	private String preset;
	private String dyePlan;
	private String dyeActual;
	private String dyeStatus;
	private String dryer;
	private String finishing;
	private String inspectation;
	private String cfmPlanDate;    // Input on web
	private String cfmDateActual;  //
	private String cfmDetailAll ;
	private String rollNoRemarkAll ;
	private String deliveryDate;   // Input on web
	private String lotShipping;

	private String labNo;
	private String customerName;
	private String customerShortName;
	private String saleNumber;
	private String saleFullName;

	private String saleOrderCreateDate;
	private String productionOrderCreateDate;
	private String materialNo;
	private String deliveryStatus;
	private String saleStatus ;
	private String lotNo;
	private String shipDate ;
	private String userId ;
	private List<String> userStatusList;
	private List<String> customerNameList;
	private List<String> customerShortNameList;
	private List<String> divisionList;
	private String iconStatus;
	private String systemStatus;
	private String relax;
	private int no;
	private String division;
	private String typePrd;
	private String typePrdRemark ;
	private String sendCFMCusDate;
	private String purchaseOrder;
	private String customerDivision;
	private String forPage ;
	public PCMSTableDetail(String saleOrder, String saleLine, String designFG, String articleFG, String distChannel,
			String color, String colorCustomer, String saleQuantity, String billQuantity, String saleUnit,
			String productionOrder, String totalQuantity, String greigeInDate, String userStatus, String LabStatus,
			String dueDate, String prepare, String preset, String dyePlan, String dyeActual, String Dryer,
			String finishing, String inspectation, String CFMPlanDate, String cFMDateActual, String DeliveryDate,
			String lotShipping, String labNo, String CustomerShortName, String saleNumber, String saleFullName,
			String saleOrderCreateDate, String productionOrderCreateDate, String materialNo, String deliveryStatus,
			String saleStatus,String LotNo,String ShipDate,String Relax, String customerName,String Division,
			String dyeStatus,String typePrd,String typePrdRemark,String SendCFMCusDate,String PurchaseOrder,
			String CustomerDivision,String PlanGreigeDate,

			String cfmDetailAll,
			String rollNoRemarkAll  ) {
		super();

		this. cfmDetailAll = cfmDetailAll;
		this. rollNoRemarkAll = rollNoRemarkAll;
		this.planGreigeDate = PlanGreigeDate;
		this.customerDivision = CustomerDivision;
		this.purchaseOrder  =PurchaseOrder;
		this.sendCFMCusDate  =SendCFMCusDate;
		this.shipDate = ShipDate;
		this.saleOrder = saleOrder;
		this.saleLine = saleLine;
		this.designFG = designFG;
		this.articleFG = articleFG;
		this.distChannel = distChannel;
		this.color = color;
		this.colorCustomer = colorCustomer;
		this.saleQuantity = saleQuantity;
		this.billQuantity = billQuantity;
		this.saleUnit = saleUnit;
		this.productionOrder = productionOrder;
		this.totalQuantity = totalQuantity;
		this.greigeInDate = greigeInDate;
		this.userStatus = userStatus;
		this.labStatus = LabStatus;
		this.dueDate = dueDate;
		this.prepare = prepare;
		this.preset = preset;
		this.dyePlan = dyePlan;
		this.dyeActual = dyeActual;
		this.dryer = Dryer;
		this.finishing = finishing;
		this.inspectation = inspectation;
		this.cfmPlanDate = CFMPlanDate;
		this.cfmDateActual = cFMDateActual;
		this.deliveryDate = DeliveryDate;
		this.lotShipping = lotShipping;
		this.labNo = labNo;
		this.customerShortName = CustomerShortName;
		this.saleNumber = saleNumber;
		this.saleFullName = saleFullName;
		this.saleOrderCreateDate = saleOrderCreateDate;
		this.productionOrderCreateDate = productionOrderCreateDate;
		this.materialNo = materialNo;
		this.deliveryStatus = deliveryStatus;
		this.saleStatus = saleStatus;
		this.lotNo = LotNo;
		this.customerName = customerName;
		this.relax = Relax;
		this.division = Division;
		this.dyeStatus = dyeStatus;
		this.typePrd = typePrd;
		this.typePrdRemark = typePrdRemark;
	}
	public PCMSTableDetail() {
		// TODO Auto-generated constructor stub
	}
	public PCMSTableDetail(String saleOrder, String designFG, String articleFG, String distChannel,
			String productionOrder, String userStatus, String dueDate, String labNo, String customerShortName,
			String saleNumber, String saleCreateDate, String prdCreateDate, String materialNo, String deliveryStatus,
			String saleStatus, String customerName,int no,String userId, String division,String PurchaseOrder) {
		this.no = no;
		this.purchaseOrder  =PurchaseOrder;
		this.userId = userId;
		this.saleOrder = saleOrder;
		this.designFG = designFG;
		this.articleFG = articleFG;
		this.distChannel = distChannel;
		this.productionOrder = productionOrder;
		this.userStatus = userStatus;
		this.dueDate = dueDate;
		this.labNo = labNo;
		this.customerName = customerName;
		this.customerShortName = customerShortName;
		this.saleNumber = saleNumber;
		this.saleOrderCreateDate = saleCreateDate;
		this.productionOrderCreateDate = prdCreateDate;
		this.materialNo = materialNo;
		this.deliveryStatus = deliveryStatus;
		this.saleStatus = saleStatus;
		this.division = division;
	}
	public String getCfmDetailAll() {
		return cfmDetailAll;
	}
	public void setCfmDetailAll(String cfmDetailAll) {
		this.cfmDetailAll = cfmDetailAll;
	}
	public String getRollNoRemarkAll() {
		return rollNoRemarkAll;
	}
	public void setRollNoRemarkAll(String rollNoRemarkAll) {
		this.rollNoRemarkAll = rollNoRemarkAll;
	}
	public String getForPage() {
		return forPage;
	}
	public void setForPage(String forPage) {
		this.forPage = forPage;
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
	public String getDistChannel() {
		return distChannel;
	}
	public void setDistChannel(String distChannel) {
		this.distChannel = distChannel;
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
	public String getSaleQuantity() {
		return saleQuantity;
	}
	public void setSaleQuantity(String saleQuantity) {
		this.saleQuantity = saleQuantity;
	}
	public String getBillQuantity() {
		return billQuantity;
	}
	public void setBillQuantity(String billQuantity) {
		this.billQuantity = billQuantity;
	}
	public String getSaleUnit() {
		return saleUnit;
	}
	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}
	public String getProductionOrder() {
		return productionOrder;
	}
	public void setProductionOrder(String productionOrder) {
		this.productionOrder = productionOrder;
	}
	public String getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(String totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public String getPlanGreigeDate() {
		return planGreigeDate;
	}
	public void setPlanGreigeDate(String planGreigeDate) {
		this.planGreigeDate = planGreigeDate;
	}
	public String getGreigeInDate() {
		return greigeInDate;
	}
	public void setGreigeInDate(String greigeInDate) {
		this.greigeInDate = greigeInDate;
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
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getPrepare() {
		return prepare;
	}
	public void setPrepare(String prepare) {
		this.prepare = prepare;
	}
	public String getPreset() {
		return preset;
	}
	public void setPreset(String preset) {
		this.preset = preset;
	}
	public String getDyePlan() {
		return dyePlan;
	}
	public void setDyePlan(String dyePlan) {
		this.dyePlan = dyePlan;
	}
	public String getDyeActual() {
		return dyeActual;
	}
	public void setDyeActual(String dyeActual) {
		this.dyeActual = dyeActual;
	}
	public String getDyeStatus() {
		return dyeStatus;
	}
	public void setDyeStatus(String dyeStatus) {
		this.dyeStatus = dyeStatus;
	}
	public String getDryer() {
		return dryer;
	}
	public void setDryer(String dryer) {
		this.dryer = dryer;
	}
	public String getFinishing() {
		return finishing;
	}
	public void setFinishing(String finishing) {
		this.finishing = finishing;
	}
	public String getInspectation() {
		return inspectation;
	}
	public void setInspectation(String inspectation) {
		this.inspectation = inspectation;
	}
	public String getCfmPlanDate() {
		return cfmPlanDate;
	}
	public void setCfmPlanDate(String cfmPlanDate) {
		this.cfmPlanDate = cfmPlanDate;
	}
	public String getCfmDateActual() {
		return cfmDateActual;
	}
	public void setCfmDateActual(String cfmDateActual) {
		this.cfmDateActual = cfmDateActual;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getLotShipping() {
		return lotShipping;
	}
	public void setLotShipping(String lotShipping) {
		this.lotShipping = lotShipping;
	}
	public String getLabNo() {
		return labNo;
	}
	public void setLabNo(String labNo) {
		this.labNo = labNo;
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
	public String getSaleNumber() {
		return saleNumber;
	}
	public void setSaleNumber(String saleNumber) {
		this.saleNumber = saleNumber;
	}
	public String getSaleFullName() {
		return saleFullName;
	}
	public void setSaleFullName(String saleFullName) {
		this.saleFullName = saleFullName;
	}
	public String getSaleOrderCreateDate() {
		return saleOrderCreateDate;
	}
	public void setSaleOrderCreateDate(String saleOrderCreateDate) {
		this.saleOrderCreateDate = saleOrderCreateDate;
	}
	public String getProductionOrderCreateDate() {
		return productionOrderCreateDate;
	}
	public void setProductionOrderCreateDate(String productionOrderCreateDate) {
		this.productionOrderCreateDate = productionOrderCreateDate;
	}
	public String getMaterialNo() {
		return materialNo;
	}
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public String getSaleStatus() {
		return saleStatus;
	}
	public void setSaleStatus(String saleStatus) {
		this.saleStatus = saleStatus;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<String> getUserStatusList() {
		return userStatusList;
	}
	public void setUserStatusList(List<String> userStatusList) {
		this.userStatusList = userStatusList;
	}
	public List<String> getCustomerNameList() {
		return customerNameList;
	}
	public void setCustomerNameList(List<String> customerNameList) {
		this.customerNameList = customerNameList;
	}
	public List<String> getCustomerShortNameList() {
		return customerShortNameList;
	}
	public void setCustomerShortNameList(List<String> customerShortNameList) {
		this.customerShortNameList = customerShortNameList;
	}
	public List<String> getDivisionList() {
		return divisionList;
	}
	public void setDivisionList(List<String> divisionList) {
		this.divisionList = divisionList;
	}
	public String getIconStatus() {
		return iconStatus;
	}
	public void setIconStatus(String iconStatus) {
		this.iconStatus = iconStatus;
	}
	public String getSystemStatus() {
		return systemStatus;
	}
	public void setSystemStatus(String systemStatus) {
		this.systemStatus = systemStatus;
	}
	public String getRelax() {
		return relax;
	}
	public void setRelax(String relax) {
		this.relax = relax;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getTypePrd() {
		return typePrd;
	}
	public void setTypePrd(String typePrd) {
		this.typePrd = typePrd;
	}
	public String getTypePrdRemark() {
		return typePrdRemark;
	}
	public void setTypePrdRemark(String typePrdRemark) {
		this.typePrdRemark = typePrdRemark;
	}
	public String getSendCFMCusDate() {
		return sendCFMCusDate;
	}
	public void setSendCFMCusDate(String sendCFMCusDate) {
		this.sendCFMCusDate = sendCFMCusDate;
	}
	public String getPurchaseOrder() {
		return purchaseOrder;
	}
	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	public String getCustomerDivision() {
		return customerDivision;
	}
	public void setCustomerDivision(String customerDivision) {
		this.customerDivision = customerDivision;
	}



}
