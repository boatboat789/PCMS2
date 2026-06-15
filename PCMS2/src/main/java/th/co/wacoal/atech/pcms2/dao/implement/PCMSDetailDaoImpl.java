package th.co.wacoal.atech.pcms2.dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import th.co.wacoal.atech.pcms2.dao.PCMSDetailDao;
import th.co.wacoal.atech.pcms2.entities.InputDateDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSSecondTableDetail;
import th.co.wacoal.atech.pcms2.entities.PCMSTableDetail;
import th.co.wacoal.atech.pcms2.service.BackGroundJobService;
import th.co.wacoal.atech.pcms2.service.BeanCreateService;
import th.co.wacoal.atech.pcms2.service.PCMSSearchService;
import th.co.wacoal.atech.pcms2.service.PCMSSqlService;
import th.co.wacoal.atech.pcms2.service.master.FromSapMainProdService;
import th.co.wacoal.atech.pcms2.service.master.PlanCFMDateService;
import th.co.wacoal.atech.pcms2.service.master.PlanCFMLabDateService;
import th.co.wacoal.atech.pcms2.service.master.PlanDeliveryDateService;
import th.co.wacoal.atech.pcms2.service.master.ReplacedProdOrderService;
import th.co.wacoal.atech.pcms2.service.master.SearchSettingService;
import th.co.wacoal.atech.pcms2.service.master.SwitchProdOrderService;
import th.co.wacoal.atech.pcms2.service.master.TEMP_UserStatusAutoService;
import th.co.wacoal.atech.pcms2.utilities.SqlStatementHandler;
import th.in.totemplate.core.sql.Database;

@Repository // Spring annotation to mark this as a DAO component
public class PCMSDetailDaoImpl implements PCMSDetailDao {
	// PC - Lab-ReLab
	// Dye,QA - Lab-ReDye
	// Sale - Lab-New
	private PCMSSqlService pss = new PCMSSqlService();
	private SqlStatementHandler sshUtl = new SqlStatementHandler();
	private String C_PRODORDER = "ProductionOrder";
	private String C_PRODORDERRP = "ProductionOrderRP";
	private BeanCreateService bcModel = new BeanCreateService();
	private String selectWaitLot = ""
			+ "   a.SaleOrder \r\n"
			+ "	, a.[SaleLine] \r\n"
			+ "   , a.Division\r\n"
			+ "   , a.CustomerShortName\r\n"
			+ "   , a.SaleCreateDate\r\n"
			+ "   , a.PurchaseOrder\r\n"
			+ "   , a.MaterialNo\r\n"
			+ "   , a.CustomerMaterial\r\n"
			+ "   , a.Price\r\n"
			+ "   , a.SaleUnit\r\n"
			+ "   , a.OrderAmount\r\n"
			+ "   , a.SaleQuantity\r\n"
			+ "   , a.RemainQuantity\r\n"
			+ "   , a.RemainAmount \r\n"
			+ "   , b.TotalQuantity \r\n"
			+ "   , b.Grade \r\n"
			+ "   , b.BillSendWeightQuantity \r\n"
			+ "   , b.BillSendQuantity  \r\n"
			+ "   , b.BillSendMRQuantity \r\n"
			+ "   , b.BillSendYDQuantity \r\n"
			+ "   , a.CustomerDue\r\n"
			+ "   , a.DueDate \r\n"
			+ "   , b.ProductionOrder\r\n"
			+ "   , b.LotNo \r\n"
			+ "   , b.LabNo\r\n"
			+ "   , b.LabStatus\r\n"
			+ "   , b.CFMPlanLabDate\r\n"
			+ "   , b.CFMActualLabDate \r\n"
			+ "   , b.CFMCusAnsLabDate \r\n"
			+ "   , b.UserStatus \r\n"
			+ "   , b.TKCFM \r\n"
			+ "   , b.CFMPlanDate \r\n"
			+ "   , b.SendCFMCusDate\r\n"
			+ "   , b.DeliveryDate  \r\n"
			+ "   , b.CFMDateActual\r\n"
			+ "   , b.CFMDetailAll \r\n"
			+ "   , b.CFMNumberAll  \r\n"
			+ "   , b.CFMRemarkAll \r\n"
			+ "   , b.RollNoRemarkAll \r\n"
			+ "   , a.ShipDate \r\n"
			+ "   , b.RemarkOne \r\n"
			+ "   , b.RemarkTwo \r\n"
			+ "   , b.RemarkThree \r\n"
			+ "   , CAST(NULL AS NVARCHAR(MAX)) AS ReplacedRemark \r\n"
			+ "   , b.StockRemark\r\n"
			+ "   , b.GRSumKG \r\n"
			+ "   , b.GRSumYD \r\n"
			+ "   , b.GRSumMR \r\n"
			+ "   , b.DyePlan \r\n"
			+ "   , b.DyeActual \r\n"
			+ "   , CAST(NULL AS NVARCHAR(MAX)) AS PCRemark\r\n"
			+ "   , CAST(NULL AS NVARCHAR(MAX)) AS [DelayedDep] \r\n"
			+ "   , CAST(NULL AS NVARCHAR(MAX)) AS [CauseOfDelay] \r\n"
			+ "   , b.[SwitchRemark]\r\n"
			+ "   , CAST(NULL AS NVARCHAR(MAX)) AS [StockLoad] \r\n"
			+ "   , b.[PrdCreateDate]\r\n"
			+ "   , b.LotShipping \r\n"
			+ "   , b.Volumn \r\n"
			+ "   , b.VolumnFGAmount  \r\n"
			+ "   , 'WaitLot' as TypePrd \r\n"
			+ "   , 'WaitLot' AS TypePrdRemark  \r\n"
			+ "   , CAST(null AS VARCHAR(10) ) as [DyeStatus]\r\n"
			+ "   , a.[CustomerMaterialBase]\r\n";
	private String selectOPV2 = ""
			+ "   a.SaleOrder ,\r\n"
			+ "   a.[SaleLine], \r\n"
			+ "   a.Division,\r\n"
			+ "   a.CustomerName,--ADD\r\n"
			+ "   a.SaleStatus,--ADD\r\n"
			+ "   a.DistChannel,--ADD\r\n"
			+ "   a.CustomerShortName,	\r\n"
			+ "   a.SaleCreateDate,\r\n"
			+ "   a.PurchaseOrder,\r\n"
			+ "   a.MaterialNo,\r\n"
			+ "   a.CustomerMaterial,\r\n"
			+ "   a.Price,\r\n"
			+ "   a.SaleUnit,\r\n"
			+ "   a.OrderAmount,\r\n"
			+ "   a.SaleQuantity,\r\n"
			+ "   a.RemainQuantity,\r\n"
			+ "   a.RemainAmount,\r\n"
			+ "   b.TotalQuantity,\r\n"
			+ "   a.Grade,\r\n"
			+ "   a.BillSendWeightQuantity,\r\n"
			+ "   a.BillSendQuantity,\r\n"
			+ "   a.BillSendMRQuantity,\r\n"
			+ "   a.BillSendYDQuantity,\r\n"
			+ "   a.CustomerDue,\r\n"
			+ "   a.DueDate,\r\n"
			+ "   b.ProductionOrder,\r\n"
			+ "   b.LotNo,\r\n"
			+ "   b.LabNo,\r\n"
			+ "   b.LabStatus,\r\n"
			+ "   CAST(NULL AS date) AS CFMPlanLabDate,\r\n"
			+ "   a.CFMActualLabDate,\r\n"
			+ "   a.CFMCusAnsLabDate,\r\n"
			+ "   a.UserStatusCal as UserStatus,\r\n"
			+ "   CAST(NULL AS date) AS TKCFM, \r\n"
			+ "   a.CFMPlanDate AS CFMPlanDate ,  \r\n"
			+ "   a.SendCFMCusDate, \r\n"
			+ "   CASE \r\n"
			+ "		WHEN h.[ProductionOrder] is not null THEN H.DeliveryDate \r\n"
			+ "		ELSE b.CFTYPE \r\n"
			+ "		END AS DeliveryDate , \r\n"
			+ "   a.CFMDateActual, \r\n"
			+ "   a.CFMDetailAll, \r\n"
			+ "   a.CFMNumberAll, \r\n"
			+ "   a.CFMRemarkAll, \r\n"
			+ "   a.RollNoRemarkAll , \r\n"
			+ "   a.ShipDate,\r\n"
			+ "   b.RemarkOne,\r\n"
			+ "   b.RemarkTwo,\r\n"
			+ "   b.RemarkThree ,\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS ReplacedRemark ,\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS StockRemark,\r\n"
			+ "   a.GRSumKG,\r\n"
			+ "   a.GRSumYD,\r\n"
			+ "   a.GRSumMR,\r\n"
			+ "   a.DyePlan, \r\n"
			+ "   a.DyeActual, \r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS PCRemark, \r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [DelayedDep], \r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [CauseOfDelay], \r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [SwitchRemark],\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [StockLoad], \r\n"
			+ "   b.[PrdCreateDate],\r\n"
			+ "   a.LotShipping \r\n";
	private String selectSW = ""
			+ "   a.SaleOrder, \r\n"
			+ "	  a.[SaleLine], \r\n"
			+ "   a.Division,\r\n"
			+ "   a.CustomerShortName,	\r\n"
			+ "   a.SaleCreateDate,\r\n"
			+ "   a.PurchaseOrder,\r\n"
			+ "   a.MaterialNo,\r\n"
			+ "   a.CustomerMaterial,\r\n"
			+ "   a.Price,\r\n"
			+ "   a.SaleUnit,\r\n"
			+ "   a.OrderAmount,\r\n"
			+ "   a.SaleQuantity,\r\n"
			+ "   a.RemainQuantity,\r\n"
			+ "   a.RemainAmount,\r\n"
			+ "   b.TotalQuantity,\r\n"
			+ "   m.Grade,\r\n"
			+ "   FSMBB.BillSendWeightQuantity,\r\n"
			+ "   case\r\n"
			+ "		 WHEN a.SaleUnit  = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
			+ "		 WHEN a.SaleUnit  = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
			+ "		 ELSE FSMBB.BillSendMRQuantity\r\n"
			+ "	  end AS BillSendQuantity ,\r\n"
			+ "   FSMBB.BillSendMRQuantity,\r\n"
			+ "   FSMBB.BillSendYDQuantity,\r\n"
			+ "   a.CustomerDue,\r\n"
			+ "   a.DueDate,\r\n"
			+ "   b.ProductionOrder,\r\n"
			+ "   b.LotNo,\r\n"
			+ "   b.LabNo,\r\n"
			+ "   b.LabStatus,\r\n"
			+ "   CAST(NULL AS date) AS CFMPlanLabDate,\r\n"
			+ "   g.CFMActualLabDate,\r\n"
			+ "   g.CFMCusAnsLabDate,\r\n"
			+ "   UCAL.UserStatusCal as UserStatus,\r\n"
			+ "   CAST(NULL AS date) AS TKCFM, \r\n"
			+ "   g.CFMPlanDate AS CFMPlanDate ,  \r\n"
			+ "   coalesce ( SCC.SendCFMCusDate ,g.SendCFMCusDate ) AS SendCFMCusDate, \r\n"
			+ "   CASE \r\n"
			+ "		WHEN h.[ProductionOrder] is not null \r\n"
			+ "		THEN H.DeliveryDate ELSE b.CFTYPE \r\n"
			+ "		END AS DeliveryDate , \r\n"
			+ "   CFMDateActual,\r\n"
			+ "   g.CFMDetailAll, \r\n"
			+ "   g.CFMNumberAll,  \r\n"
			+ "   g.CFMRemarkAll, \r\n"
			+ "   g.RollNoRemarkAll , \r\n"
			+ "   a.ShipDate,\r\n"
			+ "   b.RemarkOne,\r\n"
			+ "   b.RemarkTwo,\r\n"
			+ "   b.RemarkThree ,\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS ReplacedRemark ,\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS StockRemark,\r\n"
			+ "   m.GRSumKG,\r\n"
			+ "   m.GRSumYD,\r\n"
			+ "   m.GRSumMR,\r\n"
			+ "   g.DyePlan , \r\n"
			+ "   g.DyeActual, \r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS PCRemark, \r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [DelayedDep], \r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [CauseOfDelay], \r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [SwitchRemark],\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [StockLoad], \r\n"
			+ "   b.[PrdCreateDate],\r\n"
			+ "   g.LotShipping,\r\n"
			+ "   g.PlanGreigeDate\r\n ";
	private String selectMainV2 = ""
			+ "   b.SaleOrder, \r\n"
			+ "   b.[SaleLine], \r\n"
			+ "   b.Division,\r\n"
			+ "   b.CustomerShortName,\r\n"
			+ "   b.SaleCreateDate,\r\n"
			+ "   b.PurchaseOrder,\r\n"
			+ "   b.MaterialNo,\r\n"
			+ "   b.CustomerMaterial,\r\n"
			+ "   b.Price,\r\n"
			+ "   b.SaleUnit,\r\n"
			+ "   b.OrderAmount,\r\n"
			+ "   b.SaleQuantity,\r\n"
			+ "   b.RemainQuantity,\r\n"
			+ "   b.RemainAmount,\r\n"
			+ "   b.TotalQuantity,\r\n"
			+ "   b.Grade,\r\n"
			+ "   b.BillSendWeightQuantity,\r\n"
			+ "   b.BillSendQuantity,\r\n"
			+ "   b.BillSendMRQuantity,\r\n"
			+ "   b.BillSendYDQuantity,\r\n"
			+ "   b.CustomerDue,\r\n"
			+ "   b.DueDate,\r\n"
			+ "   b.ProductionOrder,\r\n"
			+ "   b.LotNo,\r\n"
			+ "   b.LabNo,\r\n"
			+ "   b.LabStatus,\r\n"
			+ "   CAST(NULL AS date) AS CFMPlanLabDate,\r\n"
			+ "   b.CFMActualLabDate,\r\n"
			+ "   b.CFMCusAnsLabDate,\r\n"
			+ "   b.UserStatus,\r\n"
			+ "   CAST(NULL AS date) AS TKCFM, \r\n"
			+ "   b.CFMPlanDate AS CFMPlanDate ,  \r\n"
			+ "   b.SendCFMCusDate,\r\n"
			+ "   CASE \r\n"
			+ "		WHEN b.[ProductionOrder] is not null THEN b.DeliveryDate \r\n"
			+ "		ELSE b.CFTYPE \r\n"
			+ "		END AS DeliveryDate , \r\n"
			+ "   b.CFMDateActual,\r\n"
			+ "   b.CFMDetailAll,\r\n"
			+ "   b.CFMNumberAll,\r\n"
			+ "   b.CFMRemarkAll,\r\n"
			+ "   b.RollNoRemarkAll , \r\n"
			+ "   b.ShipDate,\r\n"
			+ "   b.RemarkOne,\r\n"
			+ "   b.RemarkTwo,\r\n"
			+ "   b.RemarkThree ,\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS ReplacedRemark ,\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS StockRemark,\r\n"
			+ "   b.GRSumKG,\r\n"
			+ "   b.GRSumYD,\r\n"
			+ "   b.GRSumMR,\r\n"
			+ "   b.DyePlan , \r\n"
			+ "   b.DyeActual, \r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS PCRemark,\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [DelayedDep],\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [CauseOfDelay],\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [SwitchRemark],\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [StockLoad],\r\n"
			+ "   b.[PrdCreateDate], \r\n"
			+ "   b.LotShipping\r\n";
	private String selectRPV2 = ""
			+ "   a.SaleOrder,\r\n"
			+ "	  a.[SaleLine], \r\n"
			+ "   a.Division,\r\n"
			+ "   a.CustomerName,--ADD\r\n"
			+ "   a.SaleStatus,--ADD\r\n"
			+ "   a.DistChannel,--ADD\r\n"
			+ "   a.CustomerShortName,\r\n"
			+ "   a.SaleCreateDate,\r\n"
			+ "   a.PurchaseOrder,\r\n"
			+ "   a.MaterialNo,\r\n"
			+ "   a.CustomerMaterial,\r\n"
			+ "   a.Price,\r\n"
			+ "   a.SaleUnit,\r\n"
			+ "   a.OrderAmount,\r\n"
			+ "   a.SaleQuantity,\r\n"
			+ "   a.RemainQuantity,\r\n"
			+ "   a.RemainAmount,\r\n"
			+ "   b.TotalQuantity,\r\n"
			+ "   m.Grade,\r\n"
			+ "   FSMBB.BillSendWeightQuantity, \r\n"
			+ "   case\r\n"
			+ "		WHEN a.SaleUnit  = 'KG' THEN FSMBB.BillSendWeightQuantity   \r\n"
			+ "		WHEN a.SaleUnit  = 'YD' THEN FSMBB.BillSendYDQuantity  \r\n"
			+ "		ELSE FSMBB.BillSendMRQuantity\r\n"
			+ "	  end AS BillSendQuantity, \r\n"
			+ "   FSMBB.BillSendMRQuantity,\r\n"
			+ "   FSMBB.BillSendYDQuantity, \r\n"
			+ "   a.CustomerDue,\r\n"
			+ "   a.DueDate,\r\n"
			+ "   b.ProductionOrder,\r\n"
			+ "   b.LotNo,\r\n"
			+ "   b.LabNo,\r\n"
			+ "   b.LabStatus,\r\n"
			+ "   CAST(NULL AS date) AS CFMPlanLabDate,\r\n"
			+ "   g.CFMActualLabDate,\r\n"
			+ "   g.CFMCusAnsLabDate,\r\n"
			+ "   UCALRP.UserStatusCalRP as UserStatus,\r\n"
			+ "   CAST(NULL AS date) AS TKCFM, \r\n"
			+ "   g.CFMPlanDate AS CFMPlanDate ,  \r\n"
			+ "   coalesce ( SCC.SendCFMCusDate ,g.SendCFMCusDate ) AS SendCFMCusDate, \r\n"
			+ "   CASE \r\n"
			+ "		WHEN h.[ProductionOrder] is not null THEN H.DeliveryDate \r\n"
			+ "		ELSE b.CFTYPE \r\n"
			+ "		END AS DeliveryDate , \r\n"
			+ "   g.CFMDateActual,\r\n"
			+ "   g.CFMDetailAll, \r\n"
			+ "   g.CFMNumberAll,  \r\n"
			+ "   g.CFMRemarkAll, \r\n"
			+ "   g.RollNoRemarkAll , \r\n"
			+ "   a.ShipDate,\r\n"
			+ "   b.RemarkOne,\r\n"
			+ "   b.RemarkTwo,\r\n"
			+ "   b.RemarkThree ,\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS ReplacedRemark,\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS StockRemark,\r\n"
			+ "   m.GRSumKG,\r\n"
			+ "   m.GRSumYD,\r\n"
			+ "   m.GRSumMR,\r\n"
			+ "   g.DyePlan , \r\n"
			+ "   g.DyeActual, \r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS PCRemark,\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [DelayedDep],\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [CauseOfDelay],\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [SwitchRemark],\r\n"
			+ "   CAST(NULL AS NVARCHAR(MAX)) AS [StockLoad],\r\n"
			+ "   b.[PrdCreateDate],\r\n"
			+ "   g.LotShipping, \r\n"
			+ "   g.PlanGreigeDate\r\n";
	private String selectAll = ""
			+ "   a.SaleOrder,\r\n"
			+ "   a.[SaleLine] ,\r\n"
			+ "   a.Division,\r\n"
			+ "   a.CustomerShortName,	 \r\n"
			+ "   a.SaleCreateDate,\r\n"
			+ "   a.PurchaseOrder,\r\n"
			+ "   a.MaterialNo,\r\n"
			+ "   a.CustomerMaterial,\r\n"
			+ "   a.Price,\r\n"
			+ "   a.SaleUnit,\r\n"
			+ "   a.OrderAmount,\r\n"
			+ "   a.SaleQuantity,\r\n"
			+ "   a.RemainQuantity,\r\n"
			+ "   a.RemainAmount,\r\n"
			+ "   a.TotalQuantity,\r\n"
			+ "   a.Grade,\r\n"
			+ "   a.BillSendWeightQuantity, \r\n"
			+ "   a.BillSendQuantity, \r\n"
			+ "   a.BillSendMRQuantity,\r\n"
			+ "   a.BillSendYDQuantity, \r\n"
			+ "   a.CustomerDue,\r\n"
			+ "   a.DueDate,\r\n"
			+ "   a.ProductionOrder,\r\n"
			+ "   a.LotNo,\r\n"
			+ "   a.LabNo,\r\n"
			+ "   a.LabStatus,\r\n"
			+ "   a.CFMPlanLabDate,\r\n"
			+ "   a.CFMActualLabDate,\r\n"
			+ "   a.CFMCusAnsLabDate,\r\n"
			+ "   a.UserStatus,\r\n"
			+ "   a.TKCFM,\r\n"
			+ "   a.CFMPlanDate ,  \r\n"
			+ "   a.SendCFMCusDate,\r\n"
			+ "   a.DeliveryDate , \r\n"
			+ "   a.CFMDateActual,\r\n"
			+ "   a.CFMDetailAll, \r\n"
			+ "   a.CFMNumberAll,  \r\n"
			+ "   a.CFMRemarkAll, \r\n"
			+ "   a.RollNoRemarkAll , \r\n"
			+ "   a.ShipDate,\r\n"
			+ "   a.RemarkOne,\r\n"
			+ "   a.RemarkTwo,\r\n"
			+ "   a.RemarkThree ,\r\n"
			+ "   a.ReplacedRemark ,\r\n"
			+ "   a.StockRemark,\r\n"
			+ "   a.GRSumKG,\r\n"
			+ "   a.GRSumYD,\r\n"
			+ "   a.GRSumMR,\r\n"
			+ "   a.DyePlan, \r\n"
			+ "   a.DyeActual, \r\n"
			+ "   a.PCRemark,\r\n"
			+ "   a.[DelayedDep],\r\n"
			+ "   a.[CauseOfDelay],\r\n"
			+ "   a.[SwitchRemark],\r\n"
			+ "   a.[StockLoad],\r\n"
			+ "   a.[PrdCreateDate]\r\n"
			+ "   , a.LotShipping\r\n"
			+ "   , a.Volumn  \r\n"
			+ "   , a.VolumnFGAmount \r\n"
			+ "   , a.TypePrd \r\n"
			+ "   , a.TypePrdRemark \r\n"
			+ "   , a.[DyeStatus]\r\n"
			+ "   , a.[CustomerMaterialBase]\r\n";
	private String selectOP = ""
			+ "	a.SaleOrder,\r\n"
			+ "    a.[SaleLine],\r\n"
			+ "    a.Division,\r\n"
			+ "    a.CustomerShortName,\r\n"
			+ "    a.SaleCreateDate,\r\n"
			+ "    a.PurchaseOrder,\r\n"
			+ "    a.MaterialNo,\r\n"
			+ "    a.CustomerMaterial,\r\n"
			+ "    a.Price,\r\n"
			+ "    a.SaleUnit,\r\n"
			+ "    a.OrderAmount,\r\n"
			+ "    a.SaleQuantity,\r\n"
			+ "    a.RemainQuantity,\r\n"
			+ "    a.RemainAmount,\r\n"
			+ "    b.TotalQuantity,\r\n"
			+ "    a.Grade,\r\n"
			+ "    a.BillSendWeightQuantity,\r\n"
			+ "    a.BillSendQuantity,\r\n"
			+ "    a.BillSendMRQuantity,\r\n"
			+ "    a.BillSendYDQuantity,\r\n"
			+ "    a.CustomerDue,\r\n"
			+ "    a.DueDate,\r\n"
			+ "    b.ProductionOrder,\r\n"
			+ "    b.LotNo,\r\n"
			+ "    b.LabNo,\r\n"
			+ "    b.LabStatus,\r\n"
			+ "    e.CFMPlanLabDate, \r\n"
			+ "    a.CFMActualLabDate,\r\n"
			+ "    a.CFMCusAnsLabDate,\r\n"
			+ "    a.UserStatusCal                      AS UserStatus,\r\n"
			+ "    COALESCE(TAPP.SORCFMDate, j.CFMDate) AS TKCFM,\r\n"
			+ "    a.CFMPlanDate,\r\n"
			+ "    a.SendCFMCusDate,\r\n"
			+ "    CASE\r\n"
			+ "        WHEN h.[ProductionOrder] IS NOT NULL THEN h.DeliveryDate\r\n"
			+ "        ELSE b.CFTYPE\r\n"
			+ "    END                                  AS DeliveryDate,\r\n"
			+ "    a.CFMDateActual,\r\n"
			+ "    a.CFMDetailAll,\r\n"
			+ "    a.CFMNumberAll,\r\n"
			+ "    a.CFMRemarkAll,\r\n"
			+ "    a.RollNoRemarkAll,\r\n"
			+ "    a.ShipDate,\r\n"
			+ "    b.RemarkOne,\r\n"
			+ "    b.RemarkTwo,\r\n"
			+ "    b.RemarkThree,\r\n"
			+ "    k.ReplacedRemark,-- ดึงจาก InputReplacedRemark\r\n"
			+ "    l.StockRemark,-- ดึงจาก InputStockRemark\r\n"
			+ "    a.GRSumKG,\r\n"
			+ "    a.GRSumYD,\r\n"
			+ "    a.GRSumMR,\r\n"
			+ "    a.DyePlan,\r\n"
			+ "    a.DyeActual,\r\n"
			+ "    p.PCRemark,\r\n"
			+ "    InputDD.[DelayedDep],\r\n"
			+ "    InputCOD.[CauseOfDelay],\r\n"
			+ "    q.[SwitchRemark],\r\n"
			+ "    SL.[StockLoad],\r\n"
			+ "    b.[PrdCreateDate],\r\n"
			+ "    a.LotShipping,\r\n"
			+ "    CASE\r\n"
			+ "        WHEN a.Grade = 'A'\r\n"
			+ "            OR a.Grade is null THEN a.Volumn\r\n"
			+ "        ELSE NULL\r\n"
			+ "    END                                  AS Volumn,\r\n"
			+ "    CASE\r\n"
			+ "        WHEN a.Grade = 'A'\r\n"
			+ "            OR a.Grade is null THEN a.Price * a.Volumn\r\n"
			+ "        ELSE NULL\r\n"
			+ "    END                                  AS VolumnFGAmount,\r\n"
			+ "    'OrderPuang'                         as TypePrd,\r\n"
			+ "    a.TypePrdRemark,\r\n"
			+ "    a.[DyeStatus],\r\n"
			+ "    a.[CustomerMaterialBase] \n";

	private String leftJoinBSelect = ""
			+ "                 a.[SaleOrder]\r\n"
			+ "                ,a.[Saleline]\r\n"
			+ "                ,a.[TotalQuantity]\r\n"
			+ "                ,a.[Unit]\r\n"
			+ "                ,a.[RemAfterCloseOne]\r\n"
			+ "                ,a.[RemAfterCloseTwo]\r\n"
			+ "                ,a.[RemAfterCloseThree]\r\n"
			+ "                ,a.[LabStatus]\r\n"
			+ "                ,a.[DesignFG]\r\n"
			+ "                ,a.[ArticleFG]\r\n"
			+ "                ,a.[BookNo]\r\n"
			+ "                ,a.[Center]\r\n"
			+ "                ,a.[Batch]\r\n"
			+ "                ,a.[LabNo]\r\n"
			+ "                ,a.[RemarkOne]\r\n"
			+ "                ,a.[RemarkTwo]\r\n"
			+ "                ,a.[RemarkThree]\r\n"
			+ "                ,a.[BCAware]\r\n"
			+ "                ,a.[OrderPuang]\r\n"
			+ "                ,a.[RefPrd]\r\n"
			+ "                ,a.[GreigeInDate]\r\n"
			+ "                ,a.[BCDate]\r\n"
			+ "                ,a.[Volumn]\r\n"
			+ "                ,a.[CFdate]\r\n"
			+ "                ,a.[CFType]\r\n"
			+ "                ,a.[Shade]\r\n"
			+ "                ,g.[LotShipping]\r\n"
			+ "                ,m.[Grade]\r\n"
			+ "                ,a.[PrdCreateDate]\r\n"
			+ "                ,a.[GreigeArticle]\r\n"
			+ "                ,a.[GreigeDesign]\r\n"
			+ "                ,a.[GreigeMR]\r\n"
			+ "                ,a.[GreigeKG]\r\n"
			+ "                ,a.[ProductionOrder]\r\n"
			+ "                ,a.[LotNo]\r\n\r\n"
			+ "			       , adjVol AS SumVol\r\n"
			+ "				   , a.Price * adjVol AS SumVolFGAmount"
			+ "                ,a.Volumn as RealVolumn\r\n"
			+ "                ,g.[DyePlan]\r\n"
			+ "                ,g.[DyeActual]\r\n"
			+ "                ,g.[Dryer]\r\n"
			+ "                ,g.[Finishing]\r\n"
			+ "                ,g.[Inspectation]\r\n"
			+ "                ,g.[Prepare]\r\n"
			+ "                ,g.[Preset]\r\n"
			+ "                ,g.[Relax]\r\n"
			+ "                ,g.[CFMDateActual]\r\n"
			+ "                ,g.[CFMPlanDate]\r\n"
			+ "                ,g.[DyeStatus]\r\n"
			+ "                ,h.DeliveryDate\r\n"
			+ "                ,UCAL.UserStatusCal as UserStatus\r\n"
			+ "  			   ,coalesce ( SCC.SendCFMCusDate ,g.SendCFMCusDate ) AS SendCFMCusDate \r\n"
			+ "                ,GRSumKG\r\n"
			+ "                ,GRSumYD\r\n"
			+ "                ,GRSumMR\r\n"
			+ "                ,g.CFMDetailAll\r\n"
			+ "                ,g.CFMNumberAll\r\n"
			+ "                ,g.CFMRemarkAll\r\n"
			+ "                ,g.RollNoRemarkAll\r\n"
			+ "                ,g.CFMActualLabDate\r\n"
			+ "                ,g.CFMCusAnsLabDate\r\n"
			+ "                ,FSMBB.BillSendWeightQuantity\r\n"
			+ "                ,case\r\n"
			+ "                   WHEN a.SaleUnit = 'KG' THEN FSMBB.BillSendWeightQuantity\r\n"
			+ "                   WHEN a.SaleUnit = 'YD' THEN FSMBB.BillSendYDQuantity\r\n"
			+ "                   ELSE FSMBB.BillSendMRQuantity\r\n"
			+ "                 end                AS BillSendQuantity\r\n"
			+ "                ,FSMBB.BillSendMRQuantity\r\n"
			+ "                ,FSMBB.BillSendYDQuantity\r\n"
			+ "                ,g.PlanGreigeDate\r\n"
			+ "                ,a.Division\r\n"
			+ "                ,a.CustomerShortName\r\n"
			+ "                ,a.SaleCreateDate\r\n"
			+ "                ,a.PurchaseOrder\r\n"
			+ "                ,a.MaterialNo\r\n"
			+ "                ,a.CustomerMaterial\r\n"
			+ "                ,a.CustomerMaterialBase\r\n"
			+ "                ,a.Price\r\n"
			+ "                ,a.SaleUnit\r\n"
			+ "                ,a.OrderAmount\r\n"
			+ "                ,a.SaleQuantity\r\n"
			+ "                ,a.RemainQuantity\r\n"
			+ "                ,a.RemainAmount\r\n"
			+ "                ,a.CustomerDue\r\n"
			+ "                ,a.DueDate\r\n"
			+ "                ,a.ShipDate\r\n"
			+ "                ,a.[SaleNumber]\r\n"
			+ "                ,a.[SaleFullName]\r\n"
			+ "                ,a.DistChannel\r\n"
			+ "                ,a.Color\r\n"
			+ "                ,a.ColorCustomer\r\n"
			+ "                ,a.CustomerName\r\n"
			+ "                ,a.DeliveryStatus\r\n"
			+ "                ,a.SaleStatus\r\n"
			+ pss.passFilterExpr;
	private String createTempMainFirst = ""
			+ " SELECT \r\n"
			+ this.selectMainV2
			+ "   ,CASE  \r\n"
			+ "     	WHEN ( b.SumVol is not null and ( b.Grade = 'A' or b.Grade is null  or b.Grade  = '') ) THEN b.SumVol \r\n"
			+ "			ELSE  NULL\r\n"
			+ "			END AS Volumn \r\n"
			+ "   , CASE  \r\n"
			+ "     	WHEN ( b.SumVolFGAmount is not null and ( b.Grade = 'A' or b.Grade is null  or b.Grade  = '') ) THEN b.SumVolFGAmount \r\n"
			+ "			ELSE  NULL\r\n"
			+ "			END AS VolumnFGAmount  \r\n"
			+ "   , 'Main' as TypePrd \r\n"
			+ "   , 'Main' AS TypePrdRemark \r\n"
			+ "   , b.[DyeStatus]\r\n"
			+ "   , b.[CustomerMaterialBase]\r\n"
			+ " INTO #tempMain  \r\n";
	private String createTempMainSecond = ""
			+ this.pss.getLeftJoinSwitchProdOrder("b")
			+ " where b.PassFilter = 1\r\n"
			+ "    AND SPO.ProductionOrderSW IS NULL ";
	private String createTempPrdOPA = ""
			+ " If(OBJECT_ID('tempdb..#tempPrdOPA') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempPrdOPA\r\n"
			+ "	end ;\r\n"
			+ " SELECT   \r\n"
			+ "				a.SaleOrder\r\n"
			+ "                ,a.[SaleLine]\r\n"
			+ "                ,a.DistChannel\r\n"
			+ "                ,a.Color\r\n"
			+ "                ,a.ColorCustomer\r\n"
			+ "                ,a.SaleQuantity\r\n"
			+ "                ,a.RemainQuantity\r\n"
			+ "                ,a.SaleUnit\r\n"
			+ "                ,a.DueDate\r\n"
			+ "                ,a.CustomerShortName\r\n"
			+ "                ,a.[SaleFullName]\r\n"
			+ "                ,a.[SaleNumber]\r\n"
			+ "                ,a.SaleCreateDate\r\n"
			+ "                ,a.MaterialNo\r\n"
			+ "                ,a.DeliveryStatus\r\n"
			+ "                ,a.SaleStatus\r\n"
			+ "                ,b.ProductionOrder\r\n"
			+ "                ,a.CustomerName\r\n"
			+ "                ,a.DesignFG\r\n"
			+ "                ,a.OrderAmount\r\n"
			+ "                ,'SUB'         as TypePrdRemark\r\n"
			+ "                ,a.ArticleFG\r\n"
			+ "                ,a.ShipDate\r\n"
			+ "                ,a.Division\r\n"
			+ "                ,a.PurchaseOrder\r\n"
			+ "                ,a.CustomerMaterial\r\n"
			+ "                ,a.Price\r\n"
			+ "                ,RemainAmount\r\n"
			+ "                ,CustomerDue\r\n"
			+ "                ,CASE\r\n"
			+ "                   WHEN b.Volumn <> 0 THEN b.Volumn\r\n"
			+ "                   ELSE 0\r\n"
			+ "                 END           AS Volumn\r\n"
			+ "                ,m.[Grade]\r\n"
			+ "                ,null as [PriceSTD]\r\n"
			+ "                ,GRSumMR\r\n"
			+ "                ,GRSumKG\r\n"
			+ "                ,GRSumYD\r\n"
			+ "                ,UCAL.UserStatusCal\r\n"
			+ "                ,g.CFMActualLabDate\r\n"
			+ "                ,g.CFMCusAnsLabDate\r\n"
			+ "                ,g.CFMPlanDate AS CFMPlanDate\r\n"
			+ "   			   ,coalesce ( SCC.SendCFMCusDate ,g.SendCFMCusDate ) AS SendCFMCusDate  \r\n"
			+ "                ,g.CFMDateActual\r\n"
			+ "                ,g.CFMDetailAll\r\n"
			+ "                ,g.CFMNumberAll\r\n"
			+ "                ,g.CFMRemarkAll\r\n"
			+ "                ,g.RollNoRemarkAll\r\n"
			+ "                ,g.LotShipping\r\n"
			+ "                ,g.DyePlan\r\n"
			+ "                ,g.DyeActual\r\n"
			+ "                ,FSMBB.BillSendWeightQuantity\r\n"
			+ "                ,case\r\n"
			+ "                   WHEN a.SaleUnit = 'KG' THEN FSMBB.BillSendWeightQuantity\r\n"
			+ "                   WHEN a.SaleUnit = 'YD' THEN FSMBB.BillSendYDQuantity\r\n"
			+ "                   ELSE FSMBB.BillSendMRQuantity\r\n"
			+ "                 end AS BillSendQuantity\r\n"
			+ "                ,FSMBB.BillSendMRQuantity\r\n"
			+ "                ,FSMBB.BillSendYDQuantity\r\n"
			+ "                ,g.PlanGreigeDate\r\n"
			+ "                ,g.[DyeStatus]\r\n"
			+ "                ,a.[CustomerMaterialBase]\r\n"
			+ "         into #tempPrdOPA\r\n"
			+ "		 	from #tempMainSale as a  \r\n"
			+ "		 	inner join [PCMS].[dbo].[FromSapMainProdSale] as b on a.SaleOrder = b.SaleOrder  and \r\n"
			+ "                                                               a.SaleLine = b.SaleLine and \r\n"
			+ "                                                               b.[DataStatus] = 'O' \n"
			+ this.pss.buildLeftJoinTempProdWorkDate("b")
			+ this.pss.buildLeftJoinTempSumGR("b")
			+ this.pss.buildLeftJoinUserStatusAuto("UCAL", "b", "m")
			+ this.pss.getLeftJoinTempSumBill("b", "a", "M")
			+ this.pss.buildLeftJoinSCC("b")
			+ this.pss.buildInnerJoinTempUSMSpecial1("UCAL", "UserStatusCal")
			+ this.pss.getLeftJoinSwitchProdOrder("b")
			+ "		 	WHERE 1 = 1 AND SPO.ProductionOrderSW IS NULL\r\n";
	private String createTempOP = ""
			+ " If(OBJECT_ID('tempdb..#tempPrdOP') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempPrdOP\r\n"
			+ "	end ; \r\n "
			+ " SELECT   \r\n"
			+ this.selectOPV2
			+ "   , CASE\r\n"
			+ "			WHEN a.Grade = 'A' OR a.Grade is null THEN  a.Volumn\r\n"
			+ "			ELSE  NULL\r\n"
			+ "			END AS Volumn\r\n"
			+ "   , CASE\r\n"
			+ "			WHEN a.Grade = 'A' OR a.Grade is null THEN a.Price *  a.Volumn \r\n"
			+ "			ELSE  NULL\r\n"
			+ "			END AS VolumnFGAmount  \r\n"
			+ "   , 'OrderPuang' as TypePrd \r\n"
			+ "   , a.TypePrdRemark \r\n"
			+ "   , a.[DyeStatus]\r\n"
			+ "   , a.[CustomerMaterialBase]\r\n"
			+ " into #tempPrdOP\r\n"
			+ " FROM #tempPrdOPA as a  \r\n "
			+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
			+ this.pss.getLeftJoinTempPlandeliveryDate("a", "a");

	private String createTempOPSWFirst = ""
			+ " If(OBJECT_ID('tempdb..#tempPrdOPSW') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempPrdOPSW\r\n"
			+ "	end ; \r\n"
			+ " SELECT   \r\n"
			+ this.selectSW
			+ "	  , CASE  \r\n"
			+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN  a.Volumn\r\n"
			+ "			ELSE  NULL\r\n"
			+ "			END AS Volumn   \r\n"
			+ "	  , CASE  \r\n"
			+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN a.Price *  a.Volumn \r\n"
			+ "			ELSE  NULL\r\n"
			+ "			END AS VolumnFGAmount  \r\n"
			+ "	  , 'OrderPuang' as TypePrd \r\n"
			+ "	  , a.TypePrdRemark \r\n"
			+ "   , g.[DyeStatus]\r\n"
			+ "   , a.[CustomerMaterialBase]\r\n"
			+ " INTO #tempPrdOPSW  \r\n"
			+ " FROM (  SELECT   \r\n"
			+ "			    a.SaleOrder\r\n"
			+ "                ,a.[SaleLine]\r\n"
			+ "                ,a.DistChannel\r\n"
			+ "                ,a.Color\r\n"
			+ "                ,a.ColorCustomer\r\n"
			+ "                ,a.SaleQuantity\r\n"
			+ "                ,a.RemainQuantity\r\n"
			+ "                ,a.SaleUnit\r\n"
			+ "                ,a.DueDate\r\n"
			+ "                ,a.CustomerShortName\r\n"
			+ "                ,a.[SaleFullName]\r\n"
			+ "                ,a.[SaleNumber]\r\n"
			+ "                ,a.SaleCreateDate\r\n"
			+ "                ,a.MaterialNo\r\n"
			+ "                ,a.DeliveryStatus\r\n"
			+ "                ,a.SaleStatus\r\n"
			+ "                ,b.ProductionOrder\r\n"
			+ "                ,a.CustomerName\r\n"
			+ "                ,a.DesignFG\r\n"
			+ "                ,a.OrderAmount\r\n"
			+ "                ,'SUB' as TypePrdRemark\r\n"
			+ "                ,a.ArticleFG\r\n"
			+ "                ,a.ShipDate\r\n"
			+ "                ,a.Division\r\n"
			+ "                ,a.PurchaseOrder\r\n"
			+ "                ,a.CustomerMaterial\r\n"
			+ "                ,a.Price\r\n"
			+ "                ,a.RemainAmount\r\n"
			+ "                ,a.CustomerDue\r\n"
			+ "                ,CASE\r\n"
			+ "                   WHEN b.Volumn <> 0 THEN b.Volumn\r\n"
			+ "                   ELSE 0\r\n"
			+ "                 END   AS Volumn\r\n"
			+ "                ,a.[CustomerMaterialBase]\r\n"
			+ "		 	from #tempMainSale as a  \r\n"
			+ "		 	inner join #tempSPOSale as b on a.SaleOrder = b.SaleOrder and \r\n"
			+ "                                         a.SaleLine = b.SaleLine \r\n"
			+ "			where b.SaleLine <> ''\r\n";
	private String createTempOPSWSecond = ""
			+ "	) as a  \r\n "
			+ " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
			+ this.pss.buildLeftJoinTempProdWorkDate("b")
			+ this.pss.buildLeftJoinSCC("b")
			+ this.pss.getLeftJoinTempPlandeliveryDate("a", "a")
			+ this.pss.buildLeftJoinTempSumGR("b")
			+ this.pss.buildLeftJoinUserStatusAuto("UCAL", "b", "m")
			+ this.pss.getLeftJoinTempSumBill("b", "a", "M");

	private String createTempPrdSWFirst = ""
			+ " If(OBJECT_ID('tempdb..#tempPrdSW') Is Not Null)\r\n"
			+ "	begin\r\n"
			+ "		Drop Table #tempPrdSW\r\n"
			+ "	end ;\r\n "
			+ " SELECT   \r\n"
			+ this.selectSW
			+ "   , CASE  \r\n"
			+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN  ( b.Volumn - a.SumVol )\r\n"
			+ "			ELSE  NULL\r\n"
			+ "			END AS Volumn   \r\n"
			+ "   , CASE  \r\n"
			+ "			WHEN m.Grade = 'A' OR m.Grade is null THEN a.Price * ( b.Volumn - a.SumVol ) \r\n"
			+ "			ELSE  NULL\r\n"
			+ "			END AS VolumnFGAmount  \r\n"
			+ "   , 'Switch' as TypePrd \r\n"
			+ "   , TypePrdRemark \r\n"
			+ "   , g.[DyeStatus]\r\n"
			+ "   , a.[CustomerMaterialBase]\r\n"
			+ " INTO #tempPrdSW  \r\n"
			+ " FROM (\r\n"
			+ "       SELECT  \r\n"
			+ "             a.SaleOrder\r\n"
			+ "                ,a.[SaleLine]\r\n"
			+ "                ,a.DistChannel\r\n"
			+ "                ,a.Color\r\n"
			+ "                ,a.ColorCustomer\r\n"
			+ "                ,a.SaleQuantity\r\n"
			+ "                ,a.RemainQuantity\r\n"
			+ "                ,a.SaleUnit\r\n"
			+ "                ,a.DueDate\r\n"
			+ "                ,a.CustomerShortName\r\n"
			+ "                ,a.[SaleFullName]\r\n"
			+ "                ,a.[SaleNumber]\r\n"
			+ "                ,a.SaleCreateDate\r\n"
			+ "                ,a.MaterialNo\r\n"
			+ "                ,a.DeliveryStatus\r\n"
			+ "                ,a.SaleStatus\r\n"
			+ "                ,b.ProductionOrderSW as ProductionOrder\r\n"
			+ "                ,a.CustomerName\r\n"
			+ "                ,a.DesignFG\r\n"
			+ "                ,a.OrderAmount\r\n"
			+ "                ,a.ArticleFG\r\n"
			+ "                ,a.ShipDate\r\n"
			+ "                ,a.Division\r\n"
			+ "                ,a.PurchaseOrder\r\n"
			+ "                ,a.CustomerMaterial\r\n"
			+ "                ,a.Price\r\n"
			+ "                ,a.RemainAmount\r\n"
			+ "                ,a.CustomerDue\r\n"
			+ "                ,CASE\r\n"
			+ "                   when b.ProductionOrder = b.ProductionOrderSW then 'MAIN'\r\n"
			+ "                   ELSE 'SUB'\r\n"
			+ "                 END                 TypePrdRemark\r\n"
			+ "                ,C.SumVol\r\n"
			+ "                ,a.[CustomerMaterialBase]\r\n"
			+ "		 	from #tempMainSale as a  \r\n"
			+ "		 	inner join [PCMS].[dbo].[SwitchProdOrder]  as b on a.SaleOrder = b.SaleOrderSW and \r\n"
			+ "                                                            a.SaleLine = b.SaleLineSW  \r\n \r\n"
			+ "		 	LEFT JOIN ( \r\n"
			+ "				SELECT ProductionOrder AS PRDORDERSW, SUM(Volumn) AS SumVol\r\n"
			+ "				FROM #tempSPOSale\r\n"
			+ "				GROUP BY ProductionOrder\r\n"
			+ "		    ) AS C ON B.ProductionOrderSW = C.PRDORDERSW \r\n"
			+ "		    where b.DataStatus = 'O' \r\n";

	private String createTempPrdSWSecond =
			"" + " ) as a  \r\n " + " left join [PCMS].[dbo].[FromSapMainProd] as b on a.ProductionOrder = b.ProductionOrder \r\n"
					+ this.pss.buildLeftJoinTempProdWorkDate("b")
					+ this.pss.buildLeftJoinSCC("b")
					+ this.pss.getLeftJoinTempPlandeliveryDate("a", "a")
					+ this.pss.buildLeftJoinTempSumGR("b")
					+ this.pss.buildLeftJoinUserStatusAuto("UCAL", "b", "m")
					+ this.pss.getLeftJoinTempSumBill("b", "a", "M");

	private String createTempPrdReplacedFirst = ""
			+ " SELECT    \r\n"
			+ this.selectRPV2
			+ "    , CASE \r\n"
			+ "			WHEN m.Grade = 'A' OR m.Grade is null and b.Volume <> 0 THEN b.Volume \r\n"
			+ "			ELSE NULL\r\n"
			+ "			END AS Volumn  \r\n"
			+ "   , CASE \r\n"
			+ "			WHEN m.Grade = 'A' OR m.Grade is null and b.Volume <> 0  THEN a.Price * b.Volume  \r\n"
			+ "			ELSE NULL\r\n"
			+ "			END AS VolumnFGAmount \r\n"
			+ "   ,'Replaced' as TypePrd \r\n"
			+ "   ,'SUB' as TypePrdRemark  \r\n"
			+ "   , g.[DyeStatus]\r\n"
			+ "   , a.[CustomerMaterialBase]\r\n"
			+ " from #tempMainSale as a  \r\n"
			+ " inner join ( \r\n"
			+ "		select \r\n"
			+ "			a.SaleOrder , \r\n"
			+ "			a.SaleLine, \r\n"
			+ "			CASE WHEN a.Volume = 0 THEN b.Volumn ELSE a.Volume END as [Volume] ,\r\n"
			+ "			a.[ProductionOrderRP] AS ProductionOrder , \r\n"
			+ "			b.TotalQuantity,\r\n"
			+ "			b.LotNo,\r\n"
			+ "			b.LabNo,\r\n"
			+ "			b.LabStatus,\r\n"
			+ "			b.CFTYPE ,\r\n"
			+ "			b.RemarkOne,\r\n"
			+ "			b.RemarkTwo,\r\n"
			+ "			b.RemarkThree ,\r\n"
			+ "			b.[PrdCreateDate]\r\n"
			+ "		from [PCMS].[dbo].[ReplacedProdOrder]  as a\r\n"
			+ this.pss.buildInnerJoinFromSapMainProd("b", "ProductionOrder", "a", "ProductionOrderRP")
			+ this.pss.buildInnerJoinTempUSMSpecial1("b", "UserStatus")
			+ "		WHERE a.[DataStatus] = 'O'  \r\n";
	private String createTempPrdReplacedSecond = ""
			+ " )  as b on a.SaleOrder = b.SaleOrder \n"
			+ "         and a.SaleLine = b.SaleLine \r\n"
			+ this.pss.buildLeftJoinTempProdWorkDate("b")
			+ this.pss.buildLeftJoinSCC("b")
			+ this.pss.getLeftJoinTempPlandeliveryDate("b", "a")
			+ this.pss.buildLeftJoinTempSumGR("b")
			+ this.pss.buildLeftJoinUserStatusAuto("UCALRP", "b", "m")
			+ this.pss.getLeftJoinTempSumBill("b", "a", "M")
			+ " where 1 = 1 \r\n";
	private final Database database;

	private final PCMSSearchService psService;

	private final PlanCFMDateService planCFMDateService;
	private final PlanDeliveryDateService planDeliveryDateService;
	private final PlanCFMLabDateService planCFMLabDateService;

	@Autowired
	public PCMSDetailDaoImpl(@Qualifier("pcmsDatabase") Database database, BackGroundJobService bgjService,
			PCMSSearchService psService,

			// Services เพิ่มเติมที่เคย new ไว้
			FromSapMainProdService fromSapMainProdService, SearchSettingService searchSettingService,
			SwitchProdOrderService switchProdOrderService, ReplacedProdOrderService replacedProdOrderService,
			PlanCFMDateService planCFMDateService, PlanDeliveryDateService planDeliveryDateService,
			PlanCFMLabDateService planCFMLabDateService, TEMP_UserStatusAutoService tusaService) {

		this.database = database;
		this.psService = psService;
		this.planCFMDateService = planCFMDateService;
		this.planDeliveryDateService = planDeliveryDateService;
		this.planCFMLabDateService = planCFMLabDateService;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> searchByDetail(ArrayList<PCMSTableDetail> poList)
	{
		ArrayList<PCMSSecondTableDetail> list = null;
		PCMSTableDetail bean = poList.get(0);

		// ---- Build WHERE clauses from search criteria ----
		Map<String, String> whereMap      = pss.buildWhereClauses(bean);
		String whereCaseTry               = whereMap.get("whereCaseTry");
		String whereCaseTryRP             = whereMap.get("whereCaseTryRP");
		String tmpWhereNoLotUCAL          = whereMap.get("tmpWhereNoLotUCAL");
		String whereSale                  = whereMap.get("whereSale");
		String whereWaitLot               = whereMap.get("whereWaitLot");
		String whereBase                  = whereMap.get("whereBase").replace("b.", "a.");

		String sql = "SET NOCOUNT ON;\r\n"
				+ buildSqlGuardDrops()
				+ buildSqlCommonTables(bean, whereSale)
				+ buildSqlTypedTables(tmpWhereNoLotUCAL, whereCaseTry, whereCaseTryRP)
				+ buildSqlMainAndWaitLot(whereWaitLot, whereBase)
				+ buildSqlCleanup()
				+ buildSqlPrepareFinalResult()   // materialize UNION ALL + CREATE INDEX ตาม ORDER BY
				+ buildSqlFinalSelect();

//		System.out.println(sql);
		List<Map<String, Object>> datas = SqlStatementHandler.queryList(this.database, PCMSSqlService.dropAllTemp, sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

	// =========================================================================
	// SQL batch builders — searchByDetail
	// =========================================================================

	/** Drop output temp tables ก่อนเริ่ม batch เพื่อป้องกัน error จาก session เก่า */
	private String buildSqlGuardDrops() {
		return pss.buildIfTempTableDrop("#tempFinalResult")
				+ pss.buildIfTempTableDrop("#tempWaitLot")
				+ pss.buildIfTempTableDrop("#tempMain")
				+ pss.buildIfTempTableDrop("#tempOP")
				+ pss.buildIfTempTableDrop("#tempOPSW")
				+ pss.buildIfTempTableDrop("#tempSW")
				+ pss.buildIfTempTableDrop("#tempRP")
				+ pss.buildIfTempTableDrop("#tempSPOSale")
				+ pss.buildIfTempTableDrop("#tempProdData")
				+ pss.buildIfTempTableDrop("#tempProdMain")
				+ pss.buildIfTempTableDrop("#tempUCAL")
				+ pss.buildIfTempTableDrop("#tempUCALBest")
				+ pss.buildIfTempTableDrop("#tempUSMSPE")
				+ pss.buildIfTempTableDrop("#tempUSMSpecial1")
				+ pss.buildIfTempTableDrop("#tempSumGRMain");
	}

	/** Lookup tables ที่ใช้ร่วมกันทุก type (#tempMainSale, #tempSumGR, #tempSPO, Input tables ฯลฯ) */
	private String buildSqlCommonTables(PCMSTableDetail bean, String whereSale) {
		String createUserStatus  = psService.handlerTempTableUserStatusList(bean.getUserStatusList());
		String createCusList     = psService.handlerTempTableCustomerSearchList(
				bean.getCustomerNameList(), bean.getCustomerShortNameList());
		String createTempMainSale = createUserStatus + createCusList
				+ pss.createTempMainSaleWithJoinCustomer + whereSale;

		return createTempMainSale
				+ pss.createTempMainSaleIndex
				+ pss.createTempPlanDeliveryDate
				+ pss.createTempSumGR
				+ pss.createTempSumBill
				+ pss.createTempSCC
				+ pss.createTempProdWorkDateFiltered   // filtered by #tempMainSale — was full scan (35% cost)
				+ pss.createTempFromSORCFM
				+ pss.createTempPlanCFMLabDate
				+ pss.createTempSPO
				+ pss.createTempSPOSale
				+ pss.createTempTAPP
				+ pss.createTempInputRR
				+ pss.createTempInputPCR
				+ pss.createTempInputCOD
				+ pss.createTempInputDD
				+ pss.createTempInputSR
				+ pss.createTempInputSL
				+ pss.createTempInputStockRemark
				+ pss.createTempUSMSpecial1;           // pre-materialize viewUserStatusMappingPCMS WHERE Special=1
	}

	/** สร้าง temp table ตาม type: OrderPuang (OP), Replaced (RP), Switch (OPSW, SW) */
	private String buildSqlTypedTables(String tmpWhereNoLotUCAL, String whereCaseTry, String whereCaseTryRP) {
		return buildSqlOPTables(tmpWhereNoLotUCAL, whereCaseTry)
				+ buildSqlRPTable(whereCaseTryRP, whereCaseTry)
				+ buildSqlOPSWTable(whereCaseTry)
				+ buildSqlSWTable(whereCaseTry);
	}

	/** OrderPuang: #tempPrdOPA → #tempPrdOP → #tempOP */
	private String buildSqlOPTables(String tmpWhereNoLotUCAL, String whereCaseTry) {
		String createOPA = this.createTempPrdOPA
				+ "         " + tmpWhereNoLotUCAL
				+ " AND SPO.ProductionOrderSW IS NULL"
				+ this.createTempOP;
		String insertOP = " SELECT\r\n" + this.selectAll
				+ " INTO #tempOP\r\n"
				+ " FROM #tempPrdOP as a\r\n"
				+ " WHERE 1 = 1 " + whereCaseTry;
		return createOPA
				+ insertOP
				+ pss.buildIfTempTableDrop("#tempPrdOP");
	}

	/** Replaced: PRD_REPLACED CTE → #tempRP  (ใช้แล้ว drop #tempPrdOPA) */
	private String buildSqlRPTable(String whereCaseTryRP, String whereCaseTry) {
		String insertRP = "; WITH PRD_REPLACED AS (\r\n"
				+ this.createTempPrdReplacedFirst
				+ this.createTempPrdReplacedSecond
				+ whereCaseTryRP
				+ " )\r\n"
				+ " SELECT\r\n" + this.selectAll
				+ " INTO #tempRP\r\n"
				+ " FROM PRD_REPLACED as a\r\n"
				+ pss.buildInnerJoinTempUSMSpecial1("a", "UserStatus")
				+ " WHERE 1 = 1 " + whereCaseTry;
		return insertRP
				+ pss.buildIfTempTableDrop("#tempPrdOPA");
	}

	/** OrderPuang+Switch: #tempPrdOPSW → #tempOPSW */
	private String buildSqlOPSWTable(String whereCaseTry) {
		String insertOPSW = " SELECT\r\n" + this.selectAll
				+ " INTO #tempOPSW\r\n"
				+ " FROM #tempPrdOPSW as a\r\n"
				+ pss.buildInnerJoinTempUSMSpecial1("a", "UserStatus")
				+ " WHERE 1 = 1 " + whereCaseTry;
		return this.createTempOPSWFirst
				+ this.createTempOPSWSecond
				+ insertOPSW
				+ pss.buildIfTempTableDrop("#tempPrdOPSW");
	}

	/** Switch: #tempPrdSW → #tempSW */
	private String buildSqlSWTable(String whereCaseTry) {
		String insertSW = " SELECT\r\n" + this.selectAll
				+ " INTO #tempSW\r\n"
				+ " FROM #tempPrdSW as a\r\n"
				+ pss.buildInnerJoinTempUSMSpecial1("a", "UserStatus")
				+ " WHERE 1 = 1 " + whereCaseTry;
		return this.createTempPrdSWFirst
				+ this.createTempPrdSWSecond
				+ insertSW
				+ pss.buildIfTempTableDrop("#tempPrdSW");
	}

	/**
	 * Main + WaitLot:
	 *   aggregate filter → #tempWaitLot
	 *   #tempProdData → #tempUCAL, #tempUSMSPE → #tempProdMain → #tempMain
	 */
	private String buildSqlMainAndWaitLot(String whereWaitLot, String whereBase) {
		String insertWaitLot = pss.createTempPrepWaitLot
				+ " SELECT\r\n" + this.selectWaitLot
				+ " INTO #tempWaitLot\r\n"
				+ " FROM #tempMainSale as a\r\n"
				+ pss.innerJoinWaitLotB
				+ whereWaitLot
				+ " AND ( SumVol = 'B' OR countProdRP > 0 )\r\n";

		String insertProdMain = pss.buildIfTempTableDrop("#tempProdMain")
				+ " SELECT\r\n" + this.leftJoinBSelect
				+ " INTO #tempProdMain\r\n"
				+ " FROM #tempProdData as a\r\n"
				+ pss.getLeftJoinTempPlandeliveryDate("A", "a")
				+ pss.buildLeftJoinTempProdWorkDate("a")
				+ pss.buildLeftJoinSCC("a")
				+ pss.buildLeftJoinTempSumGR("a")
				+ pss.getLeftJoinTempUCALBest("a", "m")   // pre-computed — แทน OUTER APPLY TOP 1
				+ pss.getLeftJoinTempUSMSPE()              // ยังคงต้องใช้สำหรับ PassFilter (viewUSM_SPE.Special)
				+ pss.getLeftJoinCRP("a")
				+ pss.getLeftJoinTempSumBill("a", "a", "M")
				+ pss.getLeftJoinSwitchProdOrder("a")      // ย้าย SPO anti-join มาที่นี่ — #tempProdMain จะไม่มี switch-target rows
				+ whereBase
				+ " AND SPO.ProductionOrderSW IS NULL\r\n" // กรองออกก่อน — ลดจำนวน row ใน #tempProdMain
				+ ";\r\n"
				+ "CREATE CLUSTERED INDEX IX_tempProdMain_PO ON #tempProdMain(ProductionOrder);\r\n";

		// #tempProdMain ไม่มี SPO switch-target rows แล้ว → insertMain ไม่ต้อง JOIN #tempSPO อีก
		String insertMain = this.createTempMainFirst
				+ " FROM #tempProdMain as b\r\n"
				+ " WHERE b.PassFilter = 1\r\n";

		return pss.createTempForMainAndWaitLotFiltered
				+ insertWaitLot
				+ pss.createTempProdData
				+ pss.createTempUCAL
				+ pss.createTempUSMSPE
				+ pss.createTempUCALBest                   // ต้องรันหลัง UCAL + USMSE + SumGR พร้อมแล้ว
				+ insertProdMain
				+ insertMain
				+ pss.buildIfTempTableDrop("#tempProdMain")
				+ pss.buildIfTempTableDrop("#tempUCALBest")
				+ pss.buildIfTempTableDrop("#tempUCAL")
				+ pss.buildIfTempTableDrop("#tempUSMSPE")
				+ pss.createDropTempForMainAndWaitLot
				+ pss.buildIfTempTableDrop("#tempProdData");
	}

	/** Drop lookup tables ที่ไม่ต้องใช้แล้วก่อน final SELECT */
	private String buildSqlCleanup() {
		return pss.buildIfTempTableDrop("#tempSumBill")
				+ pss.buildIfTempTableDrop("#tempSumGR")
				+ pss.buildIfTempTableDrop("#tempPlandeliveryDate")
				+ pss.buildIfTempTableDrop("#tempUSMSpecial1")
				+ pss.buildIfTempTableDrop("#tempMainSale");
	}

	/**
	 * Materialize UNION ALL 6 tables → #tempFinalResult + clustered index ตาม ORDER BY
	 *
	 * แยกขั้นตอนนี้ออกจาก finalSelect เพื่อให้:
	 * 1. SQL Server รู้ row count จริงของ #tempFinalResult ก่อน compile 10 LEFT JOINs
	 * 2. Clustered index ตาม ORDER BY → ไม่มี sort operator ใน finalSelect
	 * 3. Optimizer เลือก Nested Loop seek เข้า lookup tables ได้ถูกต้อง
	 */
	private String buildSqlPrepareFinalResult() {
		return pss.buildIfTempTableDrop("#tempFinalResult")
				+ " SELECT a.*\r\n"
				+ " INTO #tempFinalResult\r\n"
				+ " FROM #tempWaitLot AS a\r\n"
				+ " LEFT JOIN #tempMain AS b ON a.SaleOrder = b.SaleOrder AND a.SaleLine = b.SaleLine\r\n"
				+ " WHERE b.SaleOrder IS NULL\r\n"
				+ " UNION ALL SELECT * FROM #tempMain\r\n"
				+ " UNION ALL SELECT * FROM #tempOP\r\n"
				+ " UNION ALL SELECT * FROM #tempOPSW\r\n"
				+ " UNION ALL SELECT * FROM #tempSW\r\n"
				+ " UNION ALL SELECT * FROM #tempRP;\r\n"
				// Clustered index ตาม ORDER BY → eliminates sort operator ใน final SELECT
				+ "CREATE CLUSTERED INDEX IX_tempFinalResult\r\n"
				+ "    ON #tempFinalResult(CustomerShortName, DueDate, SaleOrder, SaleLine, TypePrdRemark, ProductionOrder);\r\n";
	}

	/** Final SELECT: FROM #tempFinalResult + 10 lookup JOINs (compiled หลัง materialize — รู้ row count จริง) */
	private String buildSqlFinalSelect() {
		return " SELECT\r\n"
				+ "   u.SaleOrder, u.[SaleLine], u.Division, u.CustomerShortName, u.SaleCreateDate,\r\n"
				+ "   u.PurchaseOrder, u.MaterialNo, u.CustomerMaterial, u.Price, u.SaleUnit,\r\n"
				+ "   u.OrderAmount, u.SaleQuantity, u.RemainQuantity, u.RemainAmount, u.TotalQuantity,\r\n"
				+ "   u.Grade, u.BillSendWeightQuantity, u.BillSendQuantity, u.BillSendMRQuantity,\r\n"
				+ "   u.BillSendYDQuantity, u.CustomerDue, u.DueDate, u.ProductionOrder, u.LotNo,\r\n"
				+ "   u.LabNo, u.LabStatus,\r\n"
				+ "   e.CFMPlanLabDate,\r\n"
				+ "   u.CFMActualLabDate, u.CFMCusAnsLabDate, u.UserStatus,\r\n"
				+ "   COALESCE(TAPP.SORCFMDate, J.CFMDate) AS TKCFM,\r\n"
				+ "   u.CFMPlanDate, u.SendCFMCusDate, u.DeliveryDate, u.CFMDateActual,\r\n"
				+ "   u.CFMDetailAll, u.CFMNumberAll, u.CFMRemarkAll, u.RollNoRemarkAll, u.ShipDate,\r\n"
				+ "   u.RemarkOne, u.RemarkTwo, u.RemarkThree,\r\n"
				+ "   K.ReplacedRemark,\r\n"
				+ "   l.StockRemark,\r\n"
				+ "   u.GRSumKG, u.GRSumYD, u.GRSumMR, u.DyePlan, u.DyeActual,\r\n"
				+ "   P.PCRemark,\r\n"
				+ "   InputDD.[DelayedDep],\r\n"
				+ "   InputCOD.[CauseOfDelay],\r\n"
				+ "   q.[SwitchRemark],\r\n"
				+ "   SL.[StockLoad],\r\n"
				+ "   u.[PrdCreateDate], u.LotShipping, u.Volumn, u.VolumnFGAmount, u.TypePrd,\r\n"
				+ "   u.TypePrdRemark, u.[DyeStatus], u.[CustomerMaterialBase]\r\n"
				+ " FROM #tempFinalResult AS u\r\n"   // ← materialized แล้ว ไม่ใช่ inline UNION ALL
				+ pss.getLeftJoinPlanCFMLabDate("u", "u")
				+ pss.getLeftJoinFromSORCFM("u")
				+ pss.getLeftJoinTAPP("u")
				+ pss.getLeftJoinInputReplacedRemark("u", "u")
				+ pss.getLeftJoinInputStockRemark("u", "u", "u")
				+ pss.getLeftJoinInputPCRemark("u", "u")
				+ pss.getLeftJoinSimpleTable("u", "InputCauseOfDelay", "InputCOD",
						"ProductionOrder, CauseOfDelay", "ProductionOrder", "ProductionOrder")
				+ pss.getLeftJoinSimpleTable("u", "InputDelayedDep", "InputDD",
						"ProductionOrder, DelayedDep", "ProductionOrder", "ProductionOrder")
				+ pss.getLeftJoinSimpleTable("u", "InputSwitchRemark", "q",
						"ProductionOrder, SwitchRemark", "ProductionOrder", "ProductionOrder")
				+ pss.getLeftJoinInputStockLoad("u", "u")
				+ " ORDER BY u.CustomerShortName, u.DueDate, u.[SaleOrder], u.[SaleLine], u.TypePrdRemark, u.[ProductionOrder]";
	}

	@Override
	public ArrayList<InputDateDetail> saveInputDate(ArrayList<PCMSSecondTableDetail> poList)
	{

		ArrayList<InputDateDetail> list = new ArrayList<>();
		ArrayList<InputDateDetail> listCount = new ArrayList<>();
		String fromTable = "";
		int check = 0;
		PCMSSecondTableDetail bean = poList.get(0);
		String caseSave = bean.getCaseSave();
		String planDate = "";
//		java.util.Date today = new java.util.Date();
//		String todayString=sdf3.format(today);
		if (caseSave.equals("cfmPlanLabDate")) {
			planDate = bean.getCfmPlanLabDate();
			fromTable = " [PCMS].[dbo].[PlanCFMLabDate] ";
			list = planCFMLabDateService.getMaxCFMPlanLabDateDetail(poList);
			listCount = planCFMLabDateService.getCountCFMPlanLabDateDetail(poList);
			check = list.size();
		} else if (caseSave.equals("cfmPlanDate")) {
			planDate = bean.getCfmPlanDate();
			fromTable = "[PCMS].[dbo].[PlanCFMDate] ";
			list = planCFMDateService.getMaxCFMPlanDateDetail(poList);
			listCount = planCFMDateService.getCountCFMPlanDateDetail(poList);
			check = list.size();
		} else if (caseSave.equals("deliveryDate")) {
			planDate = bean.getDeliveryDate();
			fromTable = "[PCMS].[dbo].[PlanDeliveryDate] ";
			list = planDeliveryDateService.getMaxDeliveryPlanDateDetail(poList);
			listCount = planDeliveryDateService.getCountDeliveryPlanDateDetail(poList);
			check = list.size();
		}
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String saleLine = bean.getSaleLine();
		ArrayList<InputDateDetail> listInput = new ArrayList<>();
		InputDateDetail beanInput = new InputDateDetail();
		if (check > 0) {
			beanInput.setIconStatus("I");
			beanInput.setSystemStatus("Date : " + planDate + " already confirm.Try to refresh again.");
		} else {
			String sql = "";
			sql = " insert into "
					+ fromTable
					+ " ( "
					+ "		[ProductionOrder] ,[SaleOrder] ,[SaleLine] ,[PlanDate]  ,[CreateBy]  , " // 5
					+ "		[CreateDate] ,[LotNo] "
					+ "     ) "// 24
					+ " 	values(? , ? , ? , ? , ?"// 1
					+ "			  ,? , ? "
					+ " ) ;";


			Connection connection = this.database.getConnection();
			try (PreparedStatement prepared = connection.prepareStatement(sql)) { 
				int index = 1;
				prepared.setString(index ++ , bean.getProductionOrder());
				prepared.setString(index ++ , bean.getSaleOrder());
				prepared.setString(index ++ , saleLine);
				this.sshUtl.setSqlDate(prepared, planDate, index ++ );
				prepared.setString(index ++ , bean.getUserId());
				prepared.setTimestamp(index ++ , new Timestamp(time));
				prepared.setString(index ++ , bean.getLotNo());
				prepared.executeUpdate();
				prepared.close();
				if (caseSave.equals("CFMPlanDate")) {
					beanInput.setIconStatus("I0");
				} else {
					beanInput.setIconStatus("I1");
				}
				beanInput.setSystemStatus("Update Success.");
				if (listCount.size() > 0) {
					beanInput.setCountPlanDate(listCount.get(0).getCountPlanDate()+1);
				} else {
					beanInput.setCountPlanDate(1);
				}
//				}
			} catch (SQLException e) {
//				System.err.println(e.getMessage());
				e.printStackTrace();
				beanInput.setIconStatus("E");
				beanInput.setSystemStatus("Something happen, Please contact IT.");
			}
		}
		listInput.add(beanInput);
		return listInput;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getWaitLotCaseBySaleOrder(ArrayList<PCMSSecondTableDetail> listRP)
	{
		ArrayList<PCMSSecondTableDetail> list = null;
		String where = " where  1 = 1 \r\n";
		if (listRP.size() > 0) {
			String saleOrder = "";
			String saleLine = "";
			int sizeList = listRP.size();
			where += " AND ( \r\n";
			for (int i = 0; i < sizeList; i ++ ) {
				PCMSSecondTableDetail bean = listRP.get(i);
				saleOrder = bean.getSaleOrder();
				saleLine = bean.getSaleLine();
				where = where + " ( a.SaleOrder = '" + saleOrder + "' and a.SaleLine = '" + saleLine + "' ) ";
				if (i != sizeList-1) {
					where += " or ";
				}
			}
			where += " ) \r\n";
		}

		String sql = ""
//				+ this.declareTempApproved

				+ this.pss.createTempForMainAndWaitLot
				+ this.pss.createTempPrepWaitLot
				+ this.pss.createTempMainSale
				+ this.pss.createTempFromSORCFM
				+ where
				+ " SELECT DISTINCT  \r\n"
				+ this.selectWaitLot
				+ " FROM #tempMainSale as a \r\n "
				+ this.pss.innerJoinWaitLotB
				+ this.pss.getLeftJoinFromSORCFM("a")
				+ this.pss.getLeftJoinTAPP("b")
				+ this.pss.getLeftJoinInputReplacedRemark("b", "a")
				+ this.pss.getLeftJoinInputPCRemark("b", "a")
				+ this.pss.getLeftJoinSimpleTable("b", "InputCauseOfDelay", "InputCOD", "ProductionOrder, CauseOfDelay",
						"ProductionOrder", "ProductionOrder")
				+ this.pss.getLeftJoinSimpleTable("b", "InputDelayedDep", "InputDD", "ProductionOrder, DelayedDep",
						"ProductionOrder", "ProductionOrder")
				+ this.pss.getLeftJoinInputStockLoad("b", "a")
				+ where
				+ " and ( SumVol = 'B' OR countProdRP > 0 ) ";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<InputDateDetail> getDeliveryPlanDateDetail(ArrayList<PCMSSecondTableDetail> poList)
	{
		ArrayList<InputDateDetail> list = null;
		PCMSSecondTableDetail bean = poList.get(0);
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String sql = ""
				+ " SET NOCOUNT ON; ;\r\n"
				+ " SELECT \r\n"
				+ "		 [ProductionOrder]\r\n"
				+ "     ,[SaleOrder]\r\n"
				+ "     ,[SaleLine]\r\n"
				+ "     ,[PlanDate]\r\n"
				+ "     ,[CreateBy]\r\n"
				+ "     ,[CreateDate]\r\n"
				+ "	  	,'0:PCMS' as InputFrom \r\n"
				+ "     ,LotNo \r\n"
				+ " FROM [PCMS].[dbo].[PlanDeliveryDate] as a\r\n"
				+ " where a.[ProductionOrder] = ? and \r\n"
				+ "       a.[SaleOrder] = ? and \r\n"
				+ "       a.[SaleLine] = ? \r\n"
				+ " union ALL  \r\n "
				+ " SELECT \r\n"
				+ "      [ProductionOrder]\r\n"
				+ "      ,[SaleOrder]\r\n"
				+ "      ,[SaleLine]\r\n"
				+ "      ,[CFType] as [PlanDate]\r\n"
				+ "      ,'' AS [CreateBy]\r\n"
				+ "      ,null AS [CreateDate]\r\n"
				+ "	     , '1:SAP' as InputFrom \r\n"
				+ "      ,'' AS LotNo \r\n"
				+ " FROM [PCMS].[dbo].[FromSapMainProd] as a\r\n"
				+ " where a.[ProductionOrder] = ? "
				+ " and CFType is not null  \r\n"
				+ " ORDER BY InputFrom ,CreateDate desc ";

		List<Map<String, Object>> datas = this.database.queryList(sql, bean.getProductionOrder(), bean.getSaleOrder(),
				bean.getSaleLine(), bean.getProductionOrder());
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genInputDateDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getNormalCaseByProdOrder(String prdOrderType, ArrayList<PCMSSecondTableDetail> poList)
	{
		ArrayList<PCMSSecondTableDetail> list = null;
		String where = " where 1 = 1 ";
		String orderBy = " Order by  CustomerShortName,  DueDate, [SaleOrder], [SaleLine], [ProductionOrder]";
		if (poList.size() > 0) {
			where += " and a.ProductionOrder in ( \r\n";
			String prodOrder = "";
			for (int i = 0; i < poList.size(); i ++ ) {
				if (prdOrderType.equals(this.C_PRODORDER)) {
					prodOrder = poList.get(i).getProductionOrder();
				} else if (prdOrderType.equals(this.C_PRODORDERRP)) {
					prodOrder = poList.get(i).getProductionOrderRP();
				}
				where = where + "'" + prodOrder + "' ";
				if (i != poList.size()-1) {
					where += " , ";
				}
			}
			where += " ) \r\n";
		}

		String fromMainB = ""
				+ " from (  "
				+ " SELECT distinct \r\n"
				+ this.leftJoinBSelect
				+ this.pss.fromProdA
				+ this.pss.getLeftJoinTempPlandeliveryDate("A", "a")
				+ this.pss.buildLeftJoinTempProdWorkDate("a")
				+ this.pss.buildLeftJoinSCC("a")
				+ this.pss.buildLeftJoinTempSumGR("a")
				+ this.pss.buildLeftJoinUserStatusAuto("UCAL", "a", "m")
				+ this.pss.buildLeftJoinViewUserStatusMappingPCMS("UCAL", "UserStatusCal", 0)
				+ this.pss.getLeftJoinTempSumBill("a", "a", "M")
				+ this.pss.getLeftJoinCRP("a")
				+ where
				+ " ) as b \r\n";
		String sqlMain = ""
				+ this.pss.createTempMainSale
				+ this.pss.createTempPlanDeliveryDate
				+ this.pss.createTempSumBill
				+ this.pss.createTempSumGR
				+ this.pss.createTempSCC
				+ this.pss.createTempProdWorkDate
				+ this.pss.createTempFromSORCFM
				+ this.pss.createTempPlanCFMLabDate
				+ this.pss.createTempSPO
				+ this.pss.createTempTAPP
				+ this.pss.createTempInputRR
				+ this.pss.createTempInputPCR
				+ this.pss.createTempInputCOD
				+ this.pss.createTempInputDD
				+ this.pss.createTempInputSR
				+ this.pss.createTempInputSL
				+ this.pss.createTempInputStockRemark
				+ this.pss.createTempBillBatchFlag
				+ this.pss.createTempSumVolOP
				+ this.pss.createTempSumVolRP
				+ this.pss.createTempCRP
				+ this.pss.withProdData
				+ this.createTempMainFirst
				+ fromMainB
				+ this.createTempMainSecond
				+ " SELECT * \r\n"
				+ "	FROM #tempMain\r\n"
				+ orderBy;
		List<Map<String, Object>> datas = this.database.queryList(sqlMain);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getReplacedCaseByProdOrder(String prdOrderType,
			ArrayList<PCMSSecondTableDetail> poList)
	{
		ArrayList<PCMSSecondTableDetail> list = null;
		String where = " and ( \r\n";
		String prodOrder = "";
		for (int i = 0; i < poList.size(); i ++ ) {
			if (prdOrderType.equals(this.C_PRODORDER)) {
				prodOrder = poList.get(i).getProductionOrder();
				String saleLine = poList.get(i).getSaleLine();
				where = where
						+ " ( a."
						+ prdOrderType
						+ " = '"
						+ prodOrder
						+ "' and\r\n"
						+ "    a.[SaleOrder] = '"
						+ poList.get(i).getSaleOrder()
						+ "' and\r\n"
						+ "    a.[SaleLine] = '"
						+ saleLine
						+ "' \r\n"
						+ " ) \r\n";
			} else if (prdOrderType.equals(this.C_PRODORDERRP)) {
				prodOrder = poList.get(i).getProductionOrderRP();
				where = where + " " + prdOrderType + " = '" + prodOrder + "' ";
			}
			if (i != poList.size()-1) {
				where += " or ";
			}
		}
		where += " ) \r\n";

		String sqlRP = ""
				+ this.pss.createTempMainSale
				+ this.pss.createTempPlanDeliveryDate
				+ this.pss.createTempSumBill
				+ this.pss.createTempSumGR
				+ this.pss.createTempSCC
				+ this.pss.createTempProdWorkDate
				+ this.pss.createTempFromSORCFM
				+ this.pss.createTempPlanCFMLabDate
				+ this.pss.createTempSPO
				+ this.pss.createTempTAPP
				+ this.pss.createTempInputRR
				+ this.pss.createTempInputPCR
				+ this.pss.createTempInputCOD
				+ this.pss.createTempInputDD
				+ this.pss.createTempInputSR
				+ this.pss.createTempInputSL
				+ this.pss.createTempInputStockRemark
				+ " ; WITH PRD_REPLACED as ( \n "
				+ this.createTempPrdReplacedFirst
				+ where
				+ this.createTempPrdReplacedSecond
				+ " ) \r\n"
				+ " select \r\n"
				+ this.selectAll
				+ " from PRD_REPLACED as a \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sqlRP);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getSwitchProdOrderListByPrd(ArrayList<PCMSSecondTableDetail> poList)
	{
		ArrayList<PCMSSecondTableDetail> list = null;
		String where = " and  ( b.ProductionOrder in ( \r\n";
		for (int i = 0; i < poList.size(); i ++ ) {
			String ProductionOrder = poList.get(i).getProductionOrder();
			where = where + " '" + ProductionOrder + "' ";
			if (i != poList.size()-1) {
				where += " , ";
			}
		}
		where += " ) " + " ) \r\n";
		String createTempSWFromA = ""

				+ this.createTempPrdSWFirst
				+ where
				+ this.createTempPrdSWSecond;
		String sqlSW = ""
				+ this.pss.createTempMainSale
				+ this.pss.createTempPlanDeliveryDate
				+ this.pss.createTempSumBill
				+ this.pss.createTempSumGR
				+ this.pss.createTempSCC
				+ this.pss.createTempProdWorkDate
				+ this.pss.createTempFromSORCFM
				+ this.pss.createTempPlanCFMLabDate
				+ this.pss.createTempSPO
				+ this.pss.createTempTAPP
				+ this.pss.createTempInputRR
				+ this.pss.createTempInputPCR
				+ this.pss.createTempInputCOD
				+ this.pss.createTempInputDD
				+ this.pss.createTempInputSR
				+ this.pss.createTempInputSL
				+ this.pss.createTempInputStockRemark
				+ createTempSWFromA
				+ " select distinct\r\n"
				+ this.selectAll
				+ " from #tempPrdSW as a \r\n"
				+ this.pss.buildInnerJoinViewUSM_SPE("a", 1, "UserStatus");
		List<Map<String, Object>> datas = this.database.queryList(sqlSW);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getOrderPuangListByPrd(ArrayList<PCMSSecondTableDetail> poList)
	{
		ArrayList<PCMSSecondTableDetail> list = null;
		String where = " and  ( b.ProductionOrder in ( \r\n";
		for (int i = 0; i < poList.size(); i ++ ) {
			String ProductionOrder = poList.get(i).getProductionOrder();
			where = where + " '" + ProductionOrder + "' ";
			if (i != poList.size()-1) {
				where += " , ";
			}
		}
		where += " ) " + " ) \r\n";
		String createTempOPFromA = "" + this.createTempPrdOPA + "         " + where + this.createTempOP;
		String sqlOP = ""
				+ this.pss.createTempMainSale
				+ this.pss.createTempPlanDeliveryDate
				+ this.pss.createTempSumBill
				+ this.pss.createTempSumGR
				+ this.pss.createTempSCC
				+ this.pss.createTempProdWorkDate
				+ this.pss.createTempFromSORCFM
				+ this.pss.createTempPlanCFMLabDate
				+ this.pss.createTempSPO
				+ this.pss.createTempTAPP
				+ this.pss.createTempInputRR
				+ this.pss.createTempInputPCR
				+ this.pss.createTempInputCOD
				+ this.pss.createTempInputDD
				+ this.pss.createTempInputSR
				+ this.pss.createTempInputSL
				+ this.pss.createTempInputStockRemark
				+ createTempOPFromA
				+ " select distinct \r\n"
				+ this.selectAll
				+ " from #tempPrdOP as a \r\n"
				+ this.pss.getLeftJoinSwitchProdOrder("A")
				+ this.pss.buildInnerJoinViewUSM_SPE("a", 1, "UserStatus")
				+ " where 1 = 1 "
				+ "    AND SPO.ProductionOrderSW IS NULL ";
		List<Map<String, Object>> datas = this.database.queryList(sqlOP);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

	@Override
	public ArrayList<PCMSSecondTableDetail> getOrderPuangSWListByPrd(ArrayList<PCMSSecondTableDetail> poList)
	{
		ArrayList<PCMSSecondTableDetail> list = null;
		String where = " ";
		if ( ! poList.isEmpty()) {
			where = " and b.ProductionOrder IN (";
			List<String> productionOrders = new ArrayList<>();

			for (PCMSSecondTableDetail detail : poList) {
				productionOrders.add("'" + detail.getProductionOrder() + "'");
			}

			where += String.join(", ", productionOrders) + ") \r\n";
		}
		String sql = ""
				+ this.pss.createTempMainSale
				+ this.pss.createTempPlanDeliveryDate
				+ this.pss.createTempSumBill
				+ this.pss.createTempSumGR
				+ this.pss.createTempSCC
				+ this.pss.createTempProdWorkDate
				+ this.pss.createTempFromSORCFM
				+ this.pss.createTempPlanCFMLabDate
				+ this.pss.createTempSPO
				+ this.pss.createTempTAPP
				+ this.pss.createTempInputRR
				+ this.pss.createTempInputPCR
				+ this.pss.createTempInputCOD
				+ this.pss.createTempInputDD
				+ this.pss.createTempInputSR
				+ this.pss.createTempInputSL
				+ this.pss.createTempInputStockRemark
				+ this.createTempOPSWFirst
				+ where
				+ " \r\n"
				+ this.createTempOPSWSecond
				+ this.pss.buildInnerJoinViewUSM_SPE("b", 1, "UserStatus")
				+ " SELECT * \r\n"
				+ "	FROM #tempPrdOPSW \r\n";
		List<Map<String, Object>> datas = this.database.queryList(sql);
		list = new ArrayList<>();
		for (Map<String, Object> map : datas) {
			list.add(this.bcModel._genPCMSSecondTableDetail(map));
		}
		return list;
	}

	@Override
	public PCMSSecondTableDetail upSertRemarkCaseThree(String tableName, String planDate, PCMSSecondTableDetail bean)
	{

		String prdOrder = bean.getProductionOrder();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String caseSave = bean.getCaseSave();
		String sql = " INSERT INTO [PCMS].[dbo]."
				+ tableName
				+ " \r\n"
				+ " ([ProductionOrder] ,"
				+ caseSave
				+ ",[ChangeBy] ,[ChangeDate],[LotNo])"// 55
				+ " values(? , ? , ? , ? , ?   )  "
				+ ";";
		int index = 1;

		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
Connection connection = this.database.getConnection();
PreparedStatement prepared = null;

try {
    prepared = connection.prepareStatement(sql);
			prepared.setString(index ++ , prdOrder);
			this.sshUtl.setSqlDate(prepared, planDate, index ++ );
			prepared.setString(index ++ , bean.getUserId());
			prepared.setTimestamp(index ++ , new Timestamp(time));
			prepared.setString(index ++ , bean.getLotNo());
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			e.printStackTrace();
//			System.err.println("upSertRemarkCaseThree" + e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// 2. ปิดแค่ Statement เท่านั้น!! (ห้ามสั่ง connection.close())
			if (prepared != null) try { prepared.close(); } catch (Exception e) { }
		}
		return bean;
	}

	@Override
	public PCMSSecondTableDetail updateLogRemarkCaseOne(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
	{

		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));

		String sql = " UPDATE [PCMS].[dbo]."
				+ tableName
				+ " 	SET DataStatus = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
				+ " WHERE [ProductionOrder]  = ? "
				+ "		and [SaleOrder] = ?  "
				+ "		and [SaleLine] = ? "
				+ "		and DataStatus = 'O'; ";

		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
Connection connection = this.database.getConnection();
PreparedStatement prepared = null;

try {
    prepared = connection.prepareStatement(sql);
			prepared.setString(1, close_STATUS);
			prepared.setString(2, bean.getUserId());
			prepared.setTimestamp(3, new Timestamp(time));
			prepared.setString(4, prdOrder);
			prepared.setString(5, saleOrder);
			prepared.setString(6, bean.getSaleLine());
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			e.printStackTrace();
//			System.err.println("updateLogRemarkCaseOne" + e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  finally {
			// 2. ปิดแค่ Statement เท่านั้น!! (ห้ามสั่ง connection.close())
			if (prepared != null) try { prepared.close(); } catch (Exception e) { }
		}
		return bean;
	}

	@Override
	public PCMSSecondTableDetail updateLogRemarkCaseFix(String tableName, String valueChange, PCMSSecondTableDetail bean)
	{

		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String caseSave = bean.getCaseSave();

		String sql = "UPDATE [PCMS].[dbo]."
				+ tableName
				+ " SET "
				+ caseSave
				+ " = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
				+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ? and DataStatus = 'O' ";

		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
Connection connection = this.database.getConnection();
PreparedStatement prepared = null;

try {
    prepared = connection.prepareStatement(sql);
			prepared.setString(1, valueChange);
			prepared.setString(2, bean.getUserId());
			prepared.setTimestamp(3, new Timestamp(time));
			prepared.setString(4, prdOrder);
			prepared.setString(5, saleOrder);
			prepared.setString(6, bean.getSaleLine());
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
//			System.err.println("updateLogRemarkCaseOne" + e.getMessage());
			e.printStackTrace();
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  finally {
			// 2. ปิดแค่ Statement เท่านั้น!! (ห้ามสั่ง connection.close())
			if (prepared != null) try { prepared.close(); } catch (Exception e) { }
		}
		return bean;
	}

	@Override
	public PCMSSecondTableDetail updateLogRemarkWithGrade(String tableName, PCMSSecondTableDetail bean, String Status)
	{

		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
//		String caseSave = bean.getCaseSave();
		String grade = bean.getGrade();

		String sql = "UPDATE [PCMS].[dbo]."
				+ tableName
				+ " SET DataStatus = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
				+ " WHERE [ProductionOrder]  = ? and [SaleOrder] = ?  and [SaleLine] = ? and [Grade] = ? and DataStatus = 'O' ";

		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
Connection connection = this.database.getConnection();
PreparedStatement prepared = null;

try {
    prepared = connection.prepareStatement(sql);
			prepared.setString(1, Status);
			prepared.setString(2, bean.getUserId());
			prepared.setTimestamp(3, new Timestamp(time));
			prepared.setString(4, prdOrder);
			prepared.setString(5, saleOrder);
			prepared.setString(6, bean.getSaleLine());
			prepared.setString(7, grade);
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			e.printStackTrace();
//			System.err.println("updateLogRemarkWithGrade" + e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// 2. ปิดแค่ Statement เท่านั้น!! (ห้ามสั่ง connection.close())
			if (prepared != null) try { prepared.close(); } catch (Exception e) { }
		}
		return bean;
	}

	@Override
	public PCMSSecondTableDetail updateLogRemarkCaseThree(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
	{

		String prdOrder = bean.getProductionOrder();
//		String saleOrder = bean.getSaleOrder();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		currentTime.getTime();

		String sql = "UPDATE [PCMS].[dbo]."
				+ tableName
				+ " SET DataStatus = ?  "
				+ " WHERE [ProductionOrder]  = ?  and DataStatus = 'O' ; ";

		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
Connection connection = this.database.getConnection();
PreparedStatement prepared = null;

try {
    prepared = connection.prepareStatement(sql);
			prepared.setString(1, close_STATUS);
			prepared.setString(2, prdOrder);
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			e.printStackTrace();
//			System.err.println("updateLogRemarkCaseOne" + e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  finally {
			// 2. ปิดแค่ Statement เท่านั้น!! (ห้ามสั่ง connection.close())
			if (prepared != null) try { prepared.close(); } catch (Exception e) { }
		}
		return bean;
	}

	@Override
	public PCMSSecondTableDetail upSertRemarkCaseOne(String tableName, String valueChange, PCMSSecondTableDetail bean)
	{

		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String caseSave = bean.getCaseSave();

		String sql = " INSERT INTO [PCMS].[dbo]."
				+ tableName
				+ " ([ProductionOrder],[SaleOrder] ,[SaleLine],"
				+ caseSave
				+ ",[ChangeBy] ,[ChangeDate])"// 55
				+ " values \r\n"
				+ "	(? , ? , ? , ? , ? "
				+ ", ? )  "
				+ ";";

		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
Connection connection = this.database.getConnection();
PreparedStatement prepared = null;

try {
    prepared = connection.prepareStatement(sql);
			prepared.setString(1, prdOrder);
			prepared.setString(2, saleOrder);
			prepared.setString(3, bean.getSaleLine());
			prepared.setString(4, valueChange);
			prepared.setString(5, bean.getUserId());
			prepared.setTimestamp(6, new Timestamp(time));
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
			e.printStackTrace();
//			System.err.println("upSertRemarkCaseOne" + e.getMessage());
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// 2. ปิดแค่ Statement เท่านั้น!! (ห้ามสั่ง connection.close())
			if (prepared != null) try { prepared.close(); } catch (Exception e) { }
		}
		return bean;
	}

	@Override
	public PCMSSecondTableDetail updateLogRemarkCaseTwo(String tableName, PCMSSecondTableDetail bean, String close_STATUS)
	{

		String prdOrder = bean.getProductionOrder();
//		String saleOrder = bean.getSaleOrder();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();

		String sql = " UPDATE [PCMS].[dbo]."
				+ tableName
				+ " SET DataStatus = ? ,[ChangeBy]  = ?,[ChangeDate]  = ? "
				+ " WHERE [ProductionOrder]  = ?  and DataStatus = 'O' ; ";

		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
Connection connection = this.database.getConnection();
PreparedStatement prepared = null;

try {
    prepared = connection.prepareStatement(sql);
			prepared.setString(1, close_STATUS);
			prepared.setString(2, bean.getUserId());
			prepared.setTimestamp(3, new Timestamp(time));
			prepared.setString(4, prdOrder);
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
//			System.err.println("updateLogRemarkCaseOne" + e.getMessage());
			e.printStackTrace();
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// 2. ปิดแค่ Statement เท่านั้น!! (ห้ามสั่ง connection.close())
			if (prepared != null) try { prepared.close(); } catch (Exception e) { }
		}
		return bean;
	}

	@Override
	public PCMSSecondTableDetail upSertRemarkCaseTwo(String tableName, String valueChange, PCMSSecondTableDetail bean)
	{

		String prdOrder = bean.getProductionOrder();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
		String caseSave = bean.getCaseSave();

		String sql = " INSERT INTO [PCMS].[dbo]."
				+ tableName
				+ " \r\n"
				+ " ([ProductionOrder] ,"
				+ caseSave
				+ ",[ChangeBy] ,[ChangeDate])"// 55
				+ " values(? , ? , ? , ?   )  "
				+ ";";

		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
Connection connection = this.database.getConnection();
PreparedStatement prepared = null;

try {
    prepared = connection.prepareStatement(sql);
			prepared.setString(1, prdOrder);
			prepared.setString(2, valueChange);
			prepared.setString(3, bean.getUserId());
			prepared.setTimestamp(4, new Timestamp(time));
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
//			System.err.println("upSertRemarkCaseTwo" + e.getMessage());
			e.printStackTrace();
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		}  finally {
			// 2. ปิดแค่ Statement เท่านั้น!! (ห้ามสั่ง connection.close())
			if (prepared != null) try { prepared.close(); } catch (Exception e) { }
		}
		return bean;
	}

	@Override
	public PCMSSecondTableDetail upSertRemarkCaseWithGrade(String tableName, String valueChange, PCMSSecondTableDetail bean)
	{

		String prdOrder = bean.getProductionOrder();
		String saleOrder = bean.getSaleOrder();
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentTime = calendar.getTime();
		long time = currentTime.getTime();
//		String saleLine = String.format("%06d", Integer.parseInt(bean.getSaleLine()));
		String caseSave = bean.getCaseSave();
		String grade = bean.getGrade();

		String sql = " INSERT INTO [PCMS].[dbo]."
				+ tableName
				+ " 	([ProductionOrder],[SaleOrder] ,[SaleLine],"
				+ caseSave
				+ ",[ChangeBy] "
				+ " 	,[ChangeDate],[Grade])"// 55
				+ " values \r\n"
				+ "		(? , ? , ? , ? , ? "
				+ "    , ? , ? )  ;";

		// 1. ดึง Connection มาถือไว้เฉยๆ (ห้ามใส่ในวงเล็บ try)
Connection connection = this.database.getConnection();
PreparedStatement prepared = null;

try {
    prepared = connection.prepareStatement(sql);
			prepared.setString(1, prdOrder);
			prepared.setString(2, saleOrder);
			prepared.setString(3, bean.getSaleLine());
			prepared.setString(4, valueChange);
			prepared.setString(5, bean.getUserId());
			prepared.setTimestamp(6, new Timestamp(time));
			prepared.setString(7, grade);
			prepared.executeUpdate();
			prepared.close();
			bean.setIconStatus("I");
			bean.setSystemStatus("Update Success.");
		} catch (SQLException e) {
//			System.err.println("upSertRemarkCaseWithGrade" + e.getMessage());
			e.printStackTrace();
			bean.setIconStatus("E");
			bean.setSystemStatus("Something happen.Please contact IT.");
		} finally {
			// 2. ปิดแค่ Statement เท่านั้น!! (ห้ามสั่ง connection.close())
			if (prepared != null) try { prepared.close(); } catch (Exception e) { }
		}
		return bean;
	}

}
