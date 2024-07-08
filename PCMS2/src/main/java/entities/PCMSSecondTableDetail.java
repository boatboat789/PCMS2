package entities;

public class PCMSSecondTableDetail {
	private String division;
	private String saleOrder;
	private String saleLine;
	private String customerShortName;
	private String saleCreateDate;
	private String purchaseOrder;
	private String materialNo;
	private String customerMaterialBase;
	private String customerMaterial;
	private String price;
	private String saleUnit;
	private String saleQuantity;
	private String orderAmount;
	private String remainQuantity;
	private String remainAmount;
	private String totalQuantity;
	private String grade;
	private String billSendWeightQuantity;
	private String billSendMRQuantity;
	private String billSendYDQuantity;
	private String billSendQuantity;
	private String customerDue;
	private String dueDate;
	private String lotNo;
	private String labNo;
	private String labStatus;
	private String cfmPlanLabDate;
	private String cfmActualLabDate;
	private String cfmCusAnsLabDate;
	private String userStatus ;
	private String tkCFM;
	private String cfmDateActual;
	private String cfmPlanDate;
	private String cfmSendDate;
	private String cfmAnswerDate;
	private String cfmNumber;
	private String cfmStatus;
	private String cfmRemark;
	private String deliveryDate;
	private String shipDate;
	private String lotShipping;
	private String remarkOne;
	private String remarkTwo;
	private String remarkThree;
	private String remark;
	private String cfmLastest;
	private String userId;
	private String productionOrder;
	private String caseSave;
	private String volumn;
	private String replacedRemark;
	private String iconStatus;
	private String systemStatus;
	private String stockRemark;

	private String grQuantityKG;
	private String grQuantityMR;
	private String grQuantityYD;
	
	private String grQuantity;
	private String volumnFGAmount;

	private String dyePlan;
	private String dyeActual;
	private String dyeStatus;


	private String sendCFMCusDate;
	private String causeOfDelay;
	private String delayedDepartment;

	private String pcRemark;
	private String switchRemark ;
	private String typePrd ;
	private String typePrdRemark ;
	private int countInSW;
	private String saleOrderSW;
	private String saleLineSW;
	private String productionOrderSW;
	private String productionOrderRP;
	private String stockLoad;
	private String cfmDetailAll;
	private String cfmNumberAll;
	private String cfmRemarkAll;
	private String rollNoRemarkAll;
	private String customerType;
	private String customerDivision;
		public PCMSSecondTableDetail(String division, String saleOrder, String saleLine, String customerShortName,
				String saleCreateDate, String purchaseOrder, String materialNo, String customerMaterial, String price,
				String saleUnit, String saleQuantity,String OrderAmount, String remainQuantity, String remainAmount, String totalQuantity,
				String grade, String BillSendWeightQuantity, String billSendMRQuantity,String billSendYDQuantity, String billSendQuantity,
				String customerDue, String dueDate,
				String lotNo, String labNo, String labStatus, String cFMPlanLabDate, String cFMActualLabDate,
				String cFMCusAnsLabDate, String userStatus, String tKCFM, String cFMPlanDate, String cFMSendDate,
				String cFMAnswerDate, String cFMNumber, String cFMStatus, String cFMRemark, String deliveryDate,
				String shipDate, String remarkOne, String remarkTwo, String remarkThree,String Remark,String CFMLastest,
				String ProductionOrder,String Volumn,String ReplacedRemark,String StockRemark,String GRQuantity ,
				String VolumnFGAmount ,String DyePlan,String DyeActual,String PCRemark,String SwitchRemark,
				String TypePrd , String StockLoad ,String SendCFMCusDate,String CauseOfDelay,
				String DelayedDepartment ,String CFMDetailAll ,String CFMNumberAll, String CFMRemarkAll,
				String RollNoRemarkAll , String CFMDateActual,String CustomerType,String CustomerDivision,
				String LotShipping,String dyeStatus,String customerMaterialBase,
				String grQuantityKG,String grQuantityMR,String grQuantityYD
				) {
 
			super();
			this.grQuantityKG = grQuantityKG;
			this.grQuantityMR = grQuantityMR;
			this.grQuantityYD = grQuantityYD;
			this.customerMaterialBase = customerMaterialBase;
			this.dyeStatus = dyeStatus;
			this.lotShipping = LotShipping;
			this.customerDivision = CustomerDivision;
			this.customerType = CustomerType;
			this.cfmDateActual= CFMDateActual;
			this.rollNoRemarkAll = RollNoRemarkAll;
			this.cfmDetailAll = CFMDetailAll;
			this.cfmNumberAll = CFMNumberAll;
			this.cfmRemarkAll = CFMRemarkAll;
			this.sendCFMCusDate = SendCFMCusDate;
			this.causeOfDelay = CauseOfDelay;
			this.delayedDepartment = DelayedDepartment;
			this.dyePlan = DyePlan;
			this.dyeActual = DyeActual;
			this.orderAmount = OrderAmount;
			this.productionOrder = ProductionOrder;
			this.remark = Remark;
			this.cfmLastest = CFMLastest;
			this.division = division;
			this.saleOrder = saleOrder;
			this.saleLine = saleLine;
			this.customerShortName = customerShortName;
			this.saleCreateDate = saleCreateDate;
			this.purchaseOrder = purchaseOrder;
			this.materialNo = materialNo;
			this.customerMaterial = customerMaterial;
			this.price = price;
			this.saleUnit = saleUnit;
			this.saleQuantity = saleQuantity;
			this.remainQuantity = remainQuantity;
			this.remainAmount = remainAmount;
			this.totalQuantity = totalQuantity;
			this.grade = grade;
			this.billSendWeightQuantity = BillSendWeightQuantity;
			this.billSendQuantity = billSendQuantity;
			this.customerDue = customerDue;
			this.dueDate = dueDate;
			this.lotNo = lotNo;
			this.labNo = labNo;
			this.labStatus = labStatus;
			this.cfmPlanLabDate = cFMPlanLabDate;
			this.cfmActualLabDate = cFMActualLabDate;
			this.cfmCusAnsLabDate = cFMCusAnsLabDate;
			this.userStatus = userStatus;
			this.tkCFM = tKCFM;
			this.cfmPlanDate = cFMPlanDate;
			this.cfmSendDate = cFMSendDate;
			this.cfmAnswerDate = cFMAnswerDate;
			this.cfmNumber = cFMNumber;
			this.cfmStatus = cFMStatus;
			this.cfmRemark = cFMRemark;
			this.deliveryDate = deliveryDate;
			this.shipDate = shipDate;
			this.remarkOne = remarkOne;
			this.remarkTwo = remarkTwo;
			this.remarkThree = remarkThree;
			this.volumn = Volumn;
			this.replacedRemark = ReplacedRemark;
			this.stockRemark = StockRemark;
			this.grQuantity = GRQuantity;
			this.volumnFGAmount = VolumnFGAmount;
			this.pcRemark = PCRemark;
			this.switchRemark = SwitchRemark;
			this.typePrd = TypePrd;
			this.stockLoad = StockLoad;
		}
		public PCMSSecondTableDetail() {
			// TODO Auto-generated constructor stub
		}
		public String getGrQuantityKG()
		{
			return grQuantityKG;
		}
		public void setGrQuantityKG(String grQuantityKG)
		{
			this.grQuantityKG = grQuantityKG;
		}
		public String getGrQuantityMR()
		{
			return grQuantityMR;
		}
		public void setGrQuantityMR(String grQuantityMR)
		{
			this.grQuantityMR = grQuantityMR;
		}
		public String getGrQuantityYD()
		{
			return grQuantityYD;
		}
		public void setGrQuantityYD(String grQuantityYD)
		{
			this.grQuantityYD = grQuantityYD;
		}
		public String getCustomerMaterialBase()
		{
			return customerMaterialBase;
		}
		public void setCustomerMaterialBase(String customerMaterialBase)
		{
			this.customerMaterialBase = customerMaterialBase;
		}
		public String getDyeStatus() {
			return dyeStatus;
		}
		public void setDyeStatus(String dyeStatus) {
			this.dyeStatus = dyeStatus;
		}
		public String getDivision() {
			return division;
		}
		public void setDivision(String division) {
			this.division = division;
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
		public String getCustomerShortName() {
			return customerShortName;
		}
		public void setCustomerShortName(String customerShortName) {
			this.customerShortName = customerShortName;
		}
		public String getSaleCreateDate() {
			return saleCreateDate;
		}
		public void setSaleCreateDate(String saleCreateDate) {
			this.saleCreateDate = saleCreateDate;
		}
		public String getPurchaseOrder() {
			return purchaseOrder;
		}
		public void setPurchaseOrder(String purchaseOrder) {
			this.purchaseOrder = purchaseOrder;
		}
		public String getMaterialNo() {
			return materialNo;
		}
		public void setMaterialNo(String materialNo) {
			this.materialNo = materialNo;
		}
		public String getCustomerMaterial() {
			return customerMaterial;
		}
		public void setCustomerMaterial(String customerMaterial) {
			this.customerMaterial = customerMaterial;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getSaleUnit() {
			return saleUnit;
		}
		public void setSaleUnit(String saleUnit) {
			this.saleUnit = saleUnit;
		}
		public String getSaleQuantity() {
			return saleQuantity;
		}
		public void setSaleQuantity(String saleQuantity) {
			this.saleQuantity = saleQuantity;
		}
		public String getOrderAmount() {
			return orderAmount;
		}
		public void setOrderAmount(String orderAmount) {
			this.orderAmount = orderAmount;
		}
		public String getRemainQuantity() {
			return remainQuantity;
		}
		public void setRemainQuantity(String remainQuantity) {
			this.remainQuantity = remainQuantity;
		}
		public String getRemainAmount() {
			return remainAmount;
		}
		public void setRemainAmount(String remainAmount) {
			this.remainAmount = remainAmount;
		}
		public String getTotalQuantity() {
			return totalQuantity;
		}
		public void setTotalQuantity(String totalQuantity) {
			this.totalQuantity = totalQuantity;
		}
		public String getGrade() {
			return grade;
		}
		public void setGrade(String grade) {
			this.grade = grade;
		}
		public String getBillSendWeightQuantity() {
			return billSendWeightQuantity;
		}
		public void setBillSendWeightQuantity(String billSendWeightQuantity) {
			this.billSendWeightQuantity = billSendWeightQuantity;
		}
		public String getBillSendMRQuantity() {
			return billSendMRQuantity;
		}
		public void setBillSendMRQuantity(String billSendMRQuantity) {
			this.billSendMRQuantity = billSendMRQuantity;
		}
		public String getBillSendYDQuantity() {
			return billSendYDQuantity;
		}
		public void setBillSendYDQuantity(String billSendYDQuantity) {
			this.billSendYDQuantity = billSendYDQuantity;
		}
		public String getBillSendQuantity() {
			return billSendQuantity;
		}
		public void setBillSendQuantity(String billSendQuantity) {
			this.billSendQuantity = billSendQuantity;
		}
		public String getCustomerDue() {
			return customerDue;
		}
		public void setCustomerDue(String customerDue) {
			this.customerDue = customerDue;
		}
		public String getDueDate() {
			return dueDate;
		}
		public void setDueDate(String dueDate) {
			this.dueDate = dueDate;
		}
		public String getLotNo() {
			return lotNo;
		}
		public void setLotNo(String lotNo) {
			this.lotNo = lotNo;
		}
		public String getLabNo() {
			return labNo;
		}
		public void setLabNo(String labNo) {
			this.labNo = labNo;
		}
		public String getLabStatus() {
			return labStatus;
		}
		public void setLabStatus(String labStatus) {
			this.labStatus = labStatus;
		}
		public String getCfmPlanLabDate() {
			return cfmPlanLabDate;
		}
		public void setCfmPlanLabDate(String cfmPlanLabDate) {
			this.cfmPlanLabDate = cfmPlanLabDate;
		}
		public String getCfmActualLabDate() {
			return cfmActualLabDate;
		}
		public void setCfmActualLabDate(String cfmActualLabDate) {
			this.cfmActualLabDate = cfmActualLabDate;
		}
		public String getCfmCusAnsLabDate() {
			return cfmCusAnsLabDate;
		}
		public void setCfmCusAnsLabDate(String cfmCusAnsLabDate) {
			this.cfmCusAnsLabDate = cfmCusAnsLabDate;
		}
		public String getUserStatus() {
			return userStatus;
		}
		public void setUserStatus(String userStatus) {
			this.userStatus = userStatus;
		}
		public String getTkCFM() {
			return tkCFM;
		}
		public void setTkCFM(String tkCFM) {
			this.tkCFM = tkCFM;
		}
		public String getCfmDateActual() {
			return cfmDateActual;
		}
		public void setCfmDateActual(String cfmDateActual) {
			this.cfmDateActual = cfmDateActual;
		}
		public String getCfmPlanDate() {
			return cfmPlanDate;
		}
		public void setCfmPlanDate(String cfmPlanDate) {
			this.cfmPlanDate = cfmPlanDate;
		}
		public String getCfmSendDate() {
			return cfmSendDate;
		}
		public void setCfmSendDate(String cfmSendDate) {
			this.cfmSendDate = cfmSendDate;
		}
		public String getCfmAnswerDate() {
			return cfmAnswerDate;
		}
		public void setCfmAnswerDate(String cfmAnswerDate) {
			this.cfmAnswerDate = cfmAnswerDate;
		}
		public String getCfmNumber() {
			return cfmNumber;
		}
		public void setCfmNumber(String cfmNumber) {
			this.cfmNumber = cfmNumber;
		}
		public String getCfmStatus() {
			return cfmStatus;
		}
		public void setCfmStatus(String cfmStatus) {
			this.cfmStatus = cfmStatus;
		}
		public String getCfmRemark() {
			return cfmRemark;
		}
		public void setCfmRemark(String cfmRemark) {
			this.cfmRemark = cfmRemark;
		}
		public String getDeliveryDate() {
			return deliveryDate;
		}
		public void setDeliveryDate(String deliveryDate) {
			this.deliveryDate = deliveryDate;
		}
		public String getShipDate() {
			return shipDate;
		}
		public void setShipDate(String shipDate) {
			this.shipDate = shipDate;
		}
		public String getLotShipping() {
			return lotShipping;
		}
		public void setLotShipping(String lotShipping) {
			this.lotShipping = lotShipping;
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
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getCfmLastest() {
			return cfmLastest;
		}
		public void setCfmLastest(String cfmLastest) {
			this.cfmLastest = cfmLastest;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getProductionOrder() {
			return productionOrder;
		}
		public void setProductionOrder(String productionOrder) {
			this.productionOrder = productionOrder;
		}
		public String getCaseSave() {
			return caseSave;
		}
		public void setCaseSave(String caseSave) {
			this.caseSave = caseSave;
		}
		public String getVolumn() {
			return volumn;
		}
		public void setVolumn(String volumn) {
			this.volumn = volumn;
		}
		public String getReplacedRemark() {
			return replacedRemark;
		}
		public void setReplacedRemark(String replacedRemark) {
			this.replacedRemark = replacedRemark;
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
		public String getStockRemark() {
			return stockRemark;
		}
		public void setStockRemark(String stockRemark) {
			this.stockRemark = stockRemark;
		}
		public String getGrQuantity() {
			return grQuantity;
		}
		public void setGrQuantity(String grQuantity) {
			this.grQuantity = grQuantity;
		}
		public String getVolumnFGAmount() {
			return volumnFGAmount;
		}
		public void setVolumnFGAmount(String volumnFGAmount) {
			this.volumnFGAmount = volumnFGAmount;
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
		public String getSendCFMCusDate() {
			return sendCFMCusDate;
		}
		public void setSendCFMCusDate(String sendCFMCusDate) {
			this.sendCFMCusDate = sendCFMCusDate;
		}
		public String getCauseOfDelay() {
			return causeOfDelay;
		}
		public void setCauseOfDelay(String causeOfDelay) {
			this.causeOfDelay = causeOfDelay;
		}
		public String getDelayedDepartment() {
			return delayedDepartment;
		}
		public void setDelayedDepartment(String delayedDepartment) {
			this.delayedDepartment = delayedDepartment;
		}
		public String getPcRemark() {
			return pcRemark;
		}
		public void setPcRemark(String pcRemark) {
			this.pcRemark = pcRemark;
		}
		public String getSwitchRemark() {
			return switchRemark;
		}
		public void setSwitchRemark(String switchRemark) {
			this.switchRemark = switchRemark;
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
		public int getCountInSW() {
			return countInSW;
		}
		public void setCountInSW(int countInSW) {
			this.countInSW = countInSW;
		}
		public String getSaleOrderSW() {
			return saleOrderSW;
		}
		public void setSaleOrderSW(String saleOrderSW) {
			this.saleOrderSW = saleOrderSW;
		}
		public String getSaleLineSW() {
			return saleLineSW;
		}
		public void setSaleLineSW(String saleLineSW) {
			this.saleLineSW = saleLineSW;
		}
		public String getProductionOrderSW() {
			return productionOrderSW;
		}
		public void setProductionOrderSW(String productionOrderSW) {
			this.productionOrderSW = productionOrderSW;
		}
		public String getProductionOrderRP() {
			return productionOrderRP;
		}
		public void setProductionOrderRP(String productionOrderRP) {
			this.productionOrderRP = productionOrderRP;
		}
		public String getStockLoad() {
			return stockLoad;
		}
		public void setStockLoad(String stockLoad) {
			this.stockLoad = stockLoad;
		}
		public String getCfmDetailAll() {
			return cfmDetailAll;
		}
		public void setCfmDetailAll(String cfmDetailAll) {
			this.cfmDetailAll = cfmDetailAll;
		}
		public String getCfmNumberAll() {
			return cfmNumberAll;
		}
		public void setCfmNumberAll(String cfmNumberAll) {
			this.cfmNumberAll = cfmNumberAll;
		}
		public String getCfmRemarkAll() {
			return cfmRemarkAll;
		}
		public void setCfmRemarkAll(String cfmRemarkAll) {
			this.cfmRemarkAll = cfmRemarkAll;
		}
		public String getRollNoRemarkAll() {
			return rollNoRemarkAll;
		}
		public void setRollNoRemarkAll(String rollNoRemarkAll) {
			this.rollNoRemarkAll = rollNoRemarkAll;
		}
		public String getCustomerType() {
			return customerType;
		}
		public void setCustomerType(String customerType) {
			this.customerType = customerType;
		}
		public String getCustomerDivision() {
			return customerDivision;
		}
		public void setCustomerDivision(String customerDivision) {
			this.customerDivision = customerDivision;
		}

}
