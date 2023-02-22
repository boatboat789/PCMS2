package entities;

import java.util.List;

public class PCMSTableDetail {
	private String SaleOrder;
	private String SaleLine;
	
	private String ArticleFG;
	private String DesignFG;
	private String DistChannel ;
	private String Color;
	private String ColorCustomer;
	private String SaleQuantity;
	private String BillQuantity;
	private String SaleUnit;
	private String ProductionOrder;
	private String TotalQuantity;  
	private String GreigeInDate;
	private String UserStatus;
	private String LabStatus;
	private String DueDate;
	
	private String Prepare;
	private String Preset;
	private String DyePlan;
	private String DyeActual;
	private String DyeStatus;
	private String Dryer;
	private String Finishing;
	private String Inspectation;
	private String CFMPlanDate;    // Input on web
	private String CFMDateActual;  // 
	private String DeliveryDate;   // Input on web 
	private String LotShipping;  
	
	private String LabNo; 
	private String CustomerName; 
	private String CustomerShortName;
	private String SaleNumber;
	private String SaleFullName;
	
	private String SaleOrderCreateDate;
	private String ProductionOrderCreateDate;
	private String MaterialNo;
	private String DeliveryStatus; 
	private String SaleStatus ;
	private String LotNo;
	private String ShipDate ;  
	private String UserId ;   
	private List<String> UserStatusList; 
	private List<String> CustomerNameList; 
	private List<String> CustomerShortNameList; 
	private List<String> DivisionList; 
	private String IconStatus;
	private String SystemStatus;
	private String Relax;
	private int No;
	private String Division; 
	private String TypePrd;  
	private String TypePrdRemark ;  
	private String SendCFMCusDate;
	private String PurchaseOrder;
	public PCMSTableDetail(String saleOrder, String saleLine, String designFG, String articleFG, String distChannel,
			String color, String colorCustomer, String saleQuantity, String billQuantity, String saleUnit,
			String productionOrder, String totalQuantity, String greigeInDate, String userStatus, String LabStatus,
			String dueDate, String prepare, String preset, String dyePlan, String dyeActual, String Dryer,
			String finishing, String inspectation, String CFMPlanDate, String cFMDateActual, String DeliveryDate,
			String lotShipping, String labNo, String CustomerShortName, String saleNumber, String saleFullName,
			String saleOrderCreateDate, String productionOrderCreateDate, String materialNo, String deliveryStatus,
			String saleStatus,String LotNo,String ShipDate,String Relax, String customerName,String Division,
			String dyeStatus,String typePrd,String typePrdRemark,String SendCFMCusDate,String PurchaseOrder) {
		super();
		this.PurchaseOrder  =PurchaseOrder;
		this.SendCFMCusDate  =SendCFMCusDate;
		this.ShipDate = ShipDate;
		SaleOrder = saleOrder;
		SaleLine = saleLine;
		DesignFG = designFG;
		ArticleFG = articleFG;
		DistChannel = distChannel;
		Color = color;
		ColorCustomer = colorCustomer;
		SaleQuantity = saleQuantity;
		BillQuantity = billQuantity;
		SaleUnit = saleUnit;
		ProductionOrder = productionOrder;
		TotalQuantity = totalQuantity;
		GreigeInDate = greigeInDate;
		UserStatus = userStatus;
		this.LabStatus = LabStatus;
		DueDate = dueDate;
		Prepare = prepare;
		Preset = preset;
		DyePlan = dyePlan;
		DyeActual = dyeActual;
		this.Dryer = Dryer;
		Finishing = finishing;
		Inspectation = inspectation;  
		this.CFMPlanDate = CFMPlanDate;
		CFMDateActual = cFMDateActual;
		this.DeliveryDate = DeliveryDate;
		LotShipping = lotShipping;
		LabNo = labNo;
		this.CustomerShortName = CustomerShortName;
		SaleNumber = saleNumber;
		SaleFullName = saleFullName;
		SaleOrderCreateDate = saleOrderCreateDate;
		ProductionOrderCreateDate = productionOrderCreateDate;
		MaterialNo = materialNo;
		DeliveryStatus = deliveryStatus;
		SaleStatus = saleStatus;
		this.LotNo = LotNo;
		this.CustomerName = customerName;
		this.Relax = Relax;
		this.Division = Division;
		this.DyeStatus = dyeStatus;
		this.TypePrd = typePrd;
		this.TypePrdRemark = typePrdRemark;
	}
	public PCMSTableDetail() {
		// TODO Auto-generated constructor stub
	} 
	public PCMSTableDetail(String saleOrder, String designFG, String articleFG, String distChannel,
			String productionOrder, String userStatus, String dueDate, String labNo, String customerShortName,
			String saleNumber, String saleCreateDate, String prdCreateDate, String materialNo, String deliveryStatus,
			String saleStatus, String customerName,int no,String userId, String division) { 
		this.No = no;
		this.UserId = userId;
		SaleOrder = saleOrder; 
		DesignFG = designFG;
		ArticleFG = articleFG;
		DistChannel = distChannel; 
		ProductionOrder = productionOrder; 
		UserStatus = userStatus; 
		DueDate = dueDate;  
		LabNo = labNo;
		this.CustomerName = customerName;
		this.CustomerShortName = customerShortName;
		SaleNumber = saleNumber; 
		SaleOrderCreateDate = saleCreateDate;
		ProductionOrderCreateDate = prdCreateDate;
		MaterialNo = materialNo;
		DeliveryStatus = deliveryStatus;
		SaleStatus = saleStatus; 
		this.Division = division;
	} 
	public String getPurchaseOrder() {
		return PurchaseOrder;
	}
	public void setPurchaseOrder(String purchaseOrder) {
		PurchaseOrder = purchaseOrder;
	}
	public String getSendCFMCusDate() {
		return SendCFMCusDate;
	}
	public void setSendCFMCusDate(String sendCFMCusDate) {
		SendCFMCusDate = sendCFMCusDate;
	}
	public String getTypePrdRemark() {
		return TypePrdRemark;
	}
	public void setTypePrdRemark(String typePrdRemark) {
		TypePrdRemark = typePrdRemark;
	}
	public String getTypePrd() {
		return TypePrd;
	}
	public void setTypePrd(String typePrd) {
		TypePrd = typePrd;
	}
	public String getDyeStatus() {
		return DyeStatus;
	}
	public void setDyeStatus(String dyeStatus) {
		DyeStatus = dyeStatus;
	}
	public int getNo() {
		return No;
	}
	public void setNo(int no) {
		No = no;
	}
	public String getIconStatus() {
		return IconStatus;
	}
	public void setIconStatus(String iconStatus) {
		IconStatus = iconStatus;
	}
	public String getSystemStatus() {
		return SystemStatus;
	}
	public void setSystemStatus(String systemStatus) {
		SystemStatus = systemStatus;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getRelax() {
		return Relax;
	}
	public void setRelax(String relax) {
		Relax = relax;
	}
	public List<String> getCustomerNameList() {
		return CustomerNameList;
	}
	public void setCustomerNameList(List<String> customerNameList) {
		CustomerNameList = customerNameList;
	}
	public List<String> getCustomerShortNameList() {
		return CustomerShortNameList;
	}
	public void setCustomerShortNameList(List<String> CustomerShortNameList) {
		this.CustomerShortNameList = CustomerShortNameList;
	}
	public List<String>  getUserStatusList() {
		return UserStatusList;
	}
	public void setUserStatusList(List<String>  userStatusList) {
		UserStatusList = userStatusList;
	}
	public String getShipDate() {
		return ShipDate;
	}
	public void setShipDate(String shipDate) {
		ShipDate = shipDate;
	}
	public String getLotNo() {
		return LotNo;
	}
	public void setLotNo(String lotNo) {
		LotNo = lotNo;
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
	public String getDesignFG() {
		return DesignFG;
	}
	public void setDesignFG(String designFG) {
		DesignFG = designFG;
	}
	public String getArticleFG() {
		return ArticleFG;
	}
	public void setArticleFG(String articleFG) {
		ArticleFG = articleFG;
	}
	public String getDistChannel() {
		return DistChannel;
	}
	public void setDistChannel(String distChannel) {
		DistChannel = distChannel;
	}
	public String getColor() {
		return Color;
	}
	public void setColor(String color) {
		Color = color;
	}
	public String getColorCustomer() {
		return ColorCustomer;
	}
	public void setColorCustomer(String colorCustomer) {
		ColorCustomer = colorCustomer;
	}
	public String getSaleQuantity() {
		return SaleQuantity;
	}
	public void setSaleQuantity(String saleQuantity) {
		SaleQuantity = saleQuantity;
	}
	public String getBillQuantity() {
		return BillQuantity;
	}
	public void setBillQuantity(String billQuantity) {
		BillQuantity = billQuantity;
	}
	public String getSaleUnit() {
		return SaleUnit;
	}
	public void setSaleUnit(String saleUnit) {
		SaleUnit = saleUnit;
	}
	public String getProductionOrder() {
		return ProductionOrder;
	}
	public void setProductionOrder(String productionOrder) {
		ProductionOrder = productionOrder;
	}
	public String getTotalQuantity() {
		return TotalQuantity;
	}
	public void setTotalQuantity(String totalQuantity) {
		TotalQuantity = totalQuantity;
	}
	public String getGreigeInDate() {
		return GreigeInDate;
	}
	public void setGreigeInDate(String greigeInDate) {
		GreigeInDate = greigeInDate;
	}
	public String getUserStatus() {
		return UserStatus;
	}
	public void setUserStatus(String userStatus) {
		UserStatus = userStatus;
	}
	public String getDivision() {
		return Division;
	}
	public void setDivision(String division) {
		Division = division;
	}
	public String getLabStatus() {
		return LabStatus;
	}
	public void setLabStatus(String LabStatus) {
		this.LabStatus = LabStatus;
	}
	public String getDueDate() {
		return DueDate;
	}
	public void setDueDate(String dueDate) {
		DueDate = dueDate;
	}
	public String getPrepare() {
		return Prepare;
	}
	public void setPrepare(String prepare) {
		Prepare = prepare;
	}
	public String getPreset() {
		return Preset;
	}
	public void setPreset(String preset) {
		Preset = preset;
	}
	public String getDyePlan() {
		return DyePlan;
	}
	public void setDyePlan(String dyePlan) {
		DyePlan = dyePlan;
	}
	public String getDyeActual() {
		return DyeActual;
	}
	public void setDyeActual(String dyeActual) {
		DyeActual = dyeActual;
	}
	public String getDryer() {
		return Dryer;
	}
	public void setDryer(String Dryer) {
		this.Dryer = Dryer;
	}
	public String getFinishing() {
		return Finishing;
	}
	public void setFinishing(String finishing) {
		Finishing = finishing;
	}
	public String getInspectation() {
		return Inspectation;
	}
	public void setInspectation(String inspectation) {
		Inspectation = inspectation;
	}
	public String getCFMPlanDate() {
		return CFMPlanDate;
	}
	public void setCFMPlanDate(String CFMPlanDate) {
		this.CFMPlanDate = CFMPlanDate;
	}
	public String getCFMDateActual() {
		return CFMDateActual;
	}
	public void setCFMDateActual(String cFMDateActual) {
		CFMDateActual = cFMDateActual;
	}
	public String getDeliveryDate() {
		return DeliveryDate;
	}
	public void setDeliveryDate(String DeliveryDate) {
		this.DeliveryDate = DeliveryDate;
	}
	public String getLotShipping() {
		return LotShipping;
	}
	public void setLotShipping(String lotShipping) {
		LotShipping = lotShipping;
	}
	public String getLabNo() {
		return LabNo;
	}
	public void setLabNo(String labNo) {
		LabNo = labNo;
	}
	public String getCustomerShortName() {
		return CustomerShortName;
	}
	public void setCustomerShortName(String CustomerShortName) {
		this.CustomerShortName = CustomerShortName;
	}
	public String getSaleNumber() {
		return SaleNumber;
	}
	public void setSaleNumber(String saleNumber) {
		SaleNumber = saleNumber;
	}
	public String getSaleFullName() {
		return SaleFullName;
	}
	public void setSaleFullName(String saleFullName) {
		SaleFullName = saleFullName;
	}
	public String getSaleOrderCreateDate() {
		return SaleOrderCreateDate;
	}
	public void setSaleOrderCreateDate(String saleOrderCreateDate) {
		SaleOrderCreateDate = saleOrderCreateDate;
	}
	public String getProductionOrderCreateDate() {
		return ProductionOrderCreateDate;
	}
	public void setProductionOrderCreateDate(String productionOrderCreateDate) {
		ProductionOrderCreateDate = productionOrderCreateDate;
	}
	public String getMaterialNo() {
		return MaterialNo;
	}
	public void setMaterialNo(String materialNo) {
		MaterialNo = materialNo;
	}
	public String getDeliveryStatus() {
		return DeliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		DeliveryStatus = deliveryStatus;
	}
	public String getSaleStatus() {
		return SaleStatus;
	}
	public void setSaleStatus(String saleStatus) {
		SaleStatus = saleStatus;
	} 
	public String getCustomerName() {
		return CustomerName;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	public List<String> getDivisionList() {
		return DivisionList;
	}
	public void setDivisionList(List<String> divisionList) {
		DivisionList = divisionList;
	} 
	
	
	
}
