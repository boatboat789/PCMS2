package entities;

public class PCMSSecondTableDetail {
	private String Division;
	private String SaleOrder;
	private String SaleLine;
	private String CustomerShortName;	
	private String SaleCreateDate;
	private String PurchaseOrder;
	private String MaterialNo;
	private String CustomerMaterial;
	private String Price;
	private String SaleUnit;
	private String SaleQuantity;
	private String OrderAmount;
	private String RemainQuantity;
	private String RemainAmount;
	private String TotalQuantity;
	private String Grade;
	private String BillSendWeightQuantity;
	private String BillSendMRQuantity;
	private String BillSendYDQuantity;
	private String BillSendQuantity;
	private String CustomerDue;
	private String DueDate;
	private String LotNo;
	private String LabNo;
	private String LabStatus;
	private String CFMPlanLabDate;
	private String CFMActualLabDate;
	private String CFMCusAnsLabDate;
	private String UserStatus ;
	private String TKCFM;
	private String CFMDateActual;
	private String CFMPlanDate;
	private String CFMSendDate;
	private String CFMAnswerDate;
	private String CFMNumber;
	private String CFMStatus;
	private String CFMRemark;
	private String DeliveryDate;
	private String ShipDate;
	private String RemarkOne;
	private String RemarkTwo;
	private String RemarkThree; 
	private String Remark;
	private String CFMLastest;
	private String UserId;
	private String ProductionOrder;
	private String CaseSave;
	private String Volumn;
	private String ReplacedRemark;
	private String IconStatus;
	private String SystemStatus;
	private String StockRemark;
	private String GRQuantity;
	private String VolumnFGAmount;
	
	private String DyePlan;
	private String DyeActual;
	

	private String SendCFMCusDate;
	private String CauseOfDelay;
	private String DelayedDepartment;
	
	private String PCRemark;
	private String SwitchRemark ;
	private String TypePrd ; 
	private String TypePrdRemark ;  
	private int CountInSW;
	private String SaleOrderSW;
	private String SaleLineSW;
	private String ProductionOrderSW;
	private String ProductionOrderRP;
	private String StockLoad;
	private String CFMDetailAll;
	private String CFMNumberAll;
	private String CFMRemarkAll;
	private String RollNoRemarkAll;
	private String CustomerType;
	private String CustomerDivision;
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
				String RollNoRemarkAll , String CFMDateActual,String CustomerType,String CustomerDivision
				) {
			super();
			this.CustomerDivision = CustomerDivision;
			this.CustomerType = CustomerType;
			this.CFMDateActual= CFMDateActual;
			this.RollNoRemarkAll = RollNoRemarkAll;
			this.CFMDetailAll = CFMDetailAll;
			this.CFMNumberAll = CFMNumberAll;
			this.CFMRemarkAll = CFMRemarkAll;
			this.SendCFMCusDate = SendCFMCusDate;
			this.CauseOfDelay = CauseOfDelay;
			this.DelayedDepartment = DelayedDepartment;
			this.DyePlan = DyePlan;
			this.DyeActual = DyeActual;
			this.OrderAmount = OrderAmount;
			this.ProductionOrder = ProductionOrder;
			this.Remark = Remark;
			this.CFMLastest = CFMLastest;
			Division = division;
			SaleOrder = saleOrder;
			SaleLine = saleLine;
			CustomerShortName = customerShortName;
			SaleCreateDate = saleCreateDate;
			PurchaseOrder = purchaseOrder;
			MaterialNo = materialNo;
			CustomerMaterial = customerMaterial;
			Price = price;
			SaleUnit = saleUnit;
			SaleQuantity = saleQuantity;
			RemainQuantity = remainQuantity;
			RemainAmount = remainAmount;
			TotalQuantity = totalQuantity;
			Grade = grade;
			this.BillSendWeightQuantity = BillSendWeightQuantity;
			BillSendQuantity = billSendQuantity;
			CustomerDue = customerDue;
			DueDate = dueDate;
			LotNo = lotNo;
			LabNo = labNo;
			LabStatus = labStatus;
			CFMPlanLabDate = cFMPlanLabDate;
			CFMActualLabDate = cFMActualLabDate;
			CFMCusAnsLabDate = cFMCusAnsLabDate;
			UserStatus = userStatus;
			TKCFM = tKCFM;
			CFMPlanDate = cFMPlanDate;
			CFMSendDate = cFMSendDate;
			CFMAnswerDate = cFMAnswerDate;
			CFMNumber = cFMNumber;
			CFMStatus = cFMStatus;
			CFMRemark = cFMRemark;
			DeliveryDate = deliveryDate;
			ShipDate = shipDate;
			RemarkOne = remarkOne;
			RemarkTwo = remarkTwo;
			RemarkThree = remarkThree;
			this.Volumn = Volumn;
			this.ReplacedRemark = ReplacedRemark;
			this.StockRemark = StockRemark;
			this.GRQuantity = GRQuantity;
			this.VolumnFGAmount = VolumnFGAmount;
			this.PCRemark = PCRemark;
			this.SwitchRemark = SwitchRemark;
			this.TypePrd = TypePrd;
			this.StockLoad = StockLoad;
		}
		
		public String getCustomerDivision() {
			return CustomerDivision;
		}

		public void setCustomerDivision(String customerDivision) {
			CustomerDivision = customerDivision;
		}

		public String getCustomerType() {
			return CustomerType;
		}

		public void setCustomerType(String customerType) {
			CustomerType = customerType;
		}

		public String getCFMDateActual() {
			return CFMDateActual;
		}

		public void setCFMDateActual(String cFMDateActual) {
			CFMDateActual = cFMDateActual;
		}

		public String getRollNoRemarkAll() {
			return RollNoRemarkAll;
		}

		public void setRollNoRemarkAll(String rollNoRemarkAll) {
			RollNoRemarkAll = rollNoRemarkAll;
		}

		public String getCFMDetailAll() {
			return CFMDetailAll;
		}

		public void setCFMDetailAll(String cFMDetailAll) {
			CFMDetailAll = cFMDetailAll;
		}

		public String getCFMNumberAll() {
			return CFMNumberAll;
		}

		public void setCFMNumberAll(String cFMNumberAll) {
			CFMNumberAll = cFMNumberAll;
		}

		public String getCFMRemarkAll() {
			return CFMRemarkAll;
		}

		public void setCFMRemarkAll(String cFMRemarkAll) {
			CFMRemarkAll = cFMRemarkAll;
		}

		public String getSendCFMCusDate() {
			return SendCFMCusDate;
		}

		public void setSendCFMCusDate(String sendCFMCusDate) {
			SendCFMCusDate = sendCFMCusDate;
		}

		public String getCauseOfDelay() {
			return CauseOfDelay;
		}

		public void setCauseOfDelay(String causeOfDelay) {
			CauseOfDelay = causeOfDelay;
		}

		public String getDelayedDepartment() {
			return DelayedDepartment;
		}

		public void setDelayedDepartment(String delayedDepartment) {
			DelayedDepartment = delayedDepartment;
		}

		public String getStockLoad() {
			return StockLoad;
		}

		public void setStockLoad(String stockLoad) {
			StockLoad = stockLoad;
		}

		public String getProductionOrderRP() {
			return ProductionOrderRP;
		}

		public void setProductionOrderRP(String productionOrderRP) {
			ProductionOrderRP = productionOrderRP;
		}

		public String getTypePrdRemark() {
			return TypePrdRemark;
		}

		public void setTypePrdRemark(String typePrdRemark) {
			TypePrdRemark = typePrdRemark;
		}

		public String getSaleOrderSW() {
			return SaleOrderSW;
		}

		public void setSaleOrderSW(String saleOrderSW) {
			SaleOrderSW = saleOrderSW;
		}

		public String getSaleLineSW() {
			return SaleLineSW;
		}

		public void setSaleLineSW(String saleLineSW) {
			SaleLineSW = saleLineSW;
		}

		public String getProductionOrderSW() {
			return ProductionOrderSW;
		}

		public void setProductionOrderSW(String productionOrderSW) {
			ProductionOrderSW = productionOrderSW;
		}

		public PCMSSecondTableDetail() {
			// TODO Auto-generated constructor stub
		}

		public int getCountInSW() {
			return CountInSW;
		}

		public void setCountInSW(int countInSW) {
			CountInSW = countInSW;
		}

		public String getTypePrd() {
			return TypePrd;
		}

		public void setTypePrd(String typePrd) {
			TypePrd = typePrd;
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

		public String getVolumnFGAmount() {
			return VolumnFGAmount;
		}

		public void setVolumnFGAmount(String volumnFGAmount) {
			VolumnFGAmount = volumnFGAmount;
		}

		public String getGRQuantity() {
			return GRQuantity;
		}

		public void setGRQuantity(String gRQuantity) {
			GRQuantity = gRQuantity;
		}

		public String getStockRemark() {
			return StockRemark;
		}

		public void setStockRemark(String stockRemark) {
			StockRemark = stockRemark;
		}

		public String getBillSendMRQuantity() {
			return BillSendMRQuantity;
		}

		public String getReplacedRemark() {
			return ReplacedRemark;
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

		public void setReplacedRemark(String ReplacedRemark) {
			this.ReplacedRemark = ReplacedRemark;
		}

		public void setBillSendMRQuantity(String billSendMRQuantity) {
			BillSendMRQuantity = billSendMRQuantity;
		}

		public String getBillSendYDQuantity() {
			return BillSendYDQuantity;
		}

		public void setBillSendYDQuantity(String billSendYDQuantity) {
			BillSendYDQuantity = billSendYDQuantity;
		}

		public String getVolumn() {
			return Volumn;
		}

		public void setVolumn(String volumn) {
			Volumn = volumn;
		}

		public String getOrderAmount() {
			return OrderAmount;
		}

		public void setOrderAmount(String orderAmount) {
			OrderAmount = orderAmount;
		}

		public String getCaseSave() {
			return CaseSave;
		}

		public void setCaseSave(String caseSave) {
			CaseSave = caseSave;
		}

		public String getProductionOrder() {
			return ProductionOrder;
		}

		public void setProductionOrder(String productionOrder) {
			ProductionOrder = productionOrder;
		}

		public String getUserId() {
			return UserId;
		}
		public void setUserId(String userId) {
			UserId = userId;
		}
		public String getRemark() {
			return Remark;
		}
		public void setRemark(String remark) {
			Remark = remark;
		}
		public String getCFMLastest() {
			return CFMLastest;
		}
		public void setCFMLastest(String cFMLastest) {
			CFMLastest = cFMLastest;
		}
		public String getDivision() {
			return Division;
		}
		public void setDivision(String division) {
			Division = division;
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
		public String getCustomerShortName() {
			return CustomerShortName;
		}
		public void setCustomerShortName(String customerShortName) {
			CustomerShortName = customerShortName;
		}
		public String getSaleCreateDate() {
			return SaleCreateDate;
		}
		public void setSaleCreateDate(String saleCreateDate) {
			SaleCreateDate = saleCreateDate;
		}
		public String getPurchaseOrder() {
			return PurchaseOrder;
		}
		public void setPurchaseOrder(String purchaseOrder) {
			PurchaseOrder = purchaseOrder;
		}
		public String getMaterialNo() {
			return MaterialNo;
		}
		public void setMaterialNo(String materialNo) {
			MaterialNo = materialNo;
		}
		public String getCustomerMaterial() {
			return CustomerMaterial;
		}
		public void setCustomerMaterial(String customerMaterial) {
			CustomerMaterial = customerMaterial;
		}
		public String getPrice() {
			return Price;
		}
		public void setPrice(String price) {
			Price = price;
		}
		public String getSaleUnit() {
			return SaleUnit;
		}
		public void setSaleUnit(String saleUnit) {
			SaleUnit = saleUnit;
		}
		public String getSaleQuantity() {
			return SaleQuantity;
		}
		public void setSaleQuantity(String saleQuantity) {
			SaleQuantity = saleQuantity;
		}
		public String getRemainQuantity() {
			return RemainQuantity;
		}
		public void setRemainQuantity(String remainQuantity) {
			RemainQuantity = remainQuantity;
		}
		public String getRemainAmount() {
			return RemainAmount;
		}
		public void setRemainAmount(String remainAmount) {
			RemainAmount = remainAmount;
		}
		public String getTotalQuantity() {
			return TotalQuantity;
		}
		public void setTotalQuantity(String totalQuantity) {
			TotalQuantity = totalQuantity;
		}
		public String getGrade() {
			return Grade;
		}
		public void setGrade(String grade) {
			Grade = grade;
		}
		public String getBillSendWeightQuantity() {
			return BillSendWeightQuantity;
		}
		public void setBillSendWeightQuantity(String BillSendWeightQuantity) {
			this.BillSendWeightQuantity = BillSendWeightQuantity;
		}
		public String getBillSendQuantity() {
			return BillSendQuantity;
		}
		public void setBillSendQuantity(String billSendQuantity) {
			BillSendQuantity = billSendQuantity;
		}
		public String getCustomerDue() {
			return CustomerDue;
		}
		public void setCustomerDue(String customerDue) {
			CustomerDue = customerDue;
		}
		public String getDueDate() {
			return DueDate;
		}
		public void setDueDate(String dueDate) {
			DueDate = dueDate;
		}
		public String getLotNo() {
			return LotNo;
		}
		public void setLotNo(String lotNo) {
			LotNo = lotNo;
		}
		public String getLabNo() {
			return LabNo;
		}
		public void setLabNo(String labNo) {
			LabNo = labNo;
		}
		public String getLabStatus() {
			return LabStatus;
		}
		public void setLabStatus(String labStatus) {
			LabStatus = labStatus;
		}
		public String getCFMPlanLabDate() {
			return CFMPlanLabDate;
		}
		public void setCFMPlanLabDate(String cFMPlanLabDate) {
			CFMPlanLabDate = cFMPlanLabDate;
		}
		public String getCFMActualLabDate() {
			return CFMActualLabDate;
		}
		public void setCFMActualLabDate(String cFMActualLabDate) {
			CFMActualLabDate = cFMActualLabDate;
		}
		public String getCFMCusAnsLabDate() {
			return CFMCusAnsLabDate;
		}
		public void setCFMCusAnsLabDate(String cFMCusAnsLabDate) {
			CFMCusAnsLabDate = cFMCusAnsLabDate;
		}
		public String getUserStatus() {
			return UserStatus;
		}
		public void setUserStatus(String userStatus) {
			UserStatus = userStatus;
		}
		public String getTKCFM() {
			return TKCFM;
		}
		public void setTKCFM(String tKCFM) {
			TKCFM = tKCFM;
		}
		public String getCFMPlanDate() {
			return CFMPlanDate;
		}
		public void setCFMPlanDate(String cFMPlanDate) {
			CFMPlanDate = cFMPlanDate;
		}
		public String getCFMSendDate() {
			return CFMSendDate;
		}
		public void setCFMSendDate(String cFMSendDate) {
			CFMSendDate = cFMSendDate;
		}
		public String getCFMAnswerDate() {
			return CFMAnswerDate;
		}
		public void setCFMAnswerDate(String cFMAnswerDate) {
			CFMAnswerDate = cFMAnswerDate;
		}
		public String getCFMNumber() {
			return CFMNumber;
		}
		public void setCFMNumber(String cFMNumber) {
			CFMNumber = cFMNumber;
		}
		public String getCFMStatus() {
			return CFMStatus;
		}
		public void setCFMStatus(String cFMStatus) {
			CFMStatus = cFMStatus;
		}
		public String getCFMRemark() {
			return CFMRemark;
		}
		public void setCFMRemark(String cFMRemark) {
			CFMRemark = cFMRemark;
		}
		public String getDeliveryDate() {
			return DeliveryDate;
		}
		public void setDeliveryDate(String deliveryDate) {
			DeliveryDate = deliveryDate;
		}
		public String getShipDate() {
			return ShipDate;
		}
		public void setShipDate(String shipDate) {
			ShipDate = shipDate;
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

		public String getPCRemark() {
			return PCRemark;
		}

		public void setPCRemark(String pCRemark) {
			PCRemark = pCRemark;
		}

		public String getSwitchRemark() {
			return SwitchRemark;
		}

		public void setSwitchRemark(String switchRemark) {
			SwitchRemark = switchRemark;
		}
		
}
