///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package Test.utilities;
//
//import java.util.List;
//import th.co.wacoal.pom.dao.PomDocNoteDao;
//import th.co.wacoal.pom.dao.impl.PomDocNoteDaoImpl;
//import th.co.wacoal.pom.dao.impl.master.PomMasDocStepDaoImpl;
//import th.co.wacoal.pom.db.Database;
//import th.co.wacoal.pom.entity.PomDocNote;
//import th.co.wacoal.pom.entity.master.PomMasDocStep;
//import th.co.wacoal.pom.dao.master.PomMasDocStepDao;
//
///**
// *
// * @author 92705
// */
//public class getDocStep {
//
//    public String getDocStepByStepId(
//            String docStepId,
//            String stepStatusSign,
//            String stepStatusColor
//    ) {
//
//        String sign = "";
//        Database db = null;
//        try {
//            db = new Database("sqlServer");
//            PomMasDocStepDao masterDocStepDao = new PomMasDocStepDaoImpl(db);
//
//            PomMasDocStep docStepByID = masterDocStepDao.getDocStepByID(docStepId);
//            sign = "<i class=\"" + stepStatusSign + " fa-2x\" style=\"color:" + stepStatusColor + ";cursor: pointer;\" data-toggle=\"tooltip\" aria-hidden=\"true\" data-placement=\"bottom\" title=\"" + docStepByID.getDocstepName() + "\"></i>";
//            return sign;
//        } catch (Exception ex) {
//            return "/index/error.htm?error=" + ex.toString();
//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }
//    }
//
//    public static String mainGetDocStep(
//            String docStepId,
//            String stepStatusSign,
//            String stepStatusColor
//    ) {
//        return new getDocStep().getDocStepByStepId(
//                docStepId,
//                stepStatusSign,
//                stepStatusColor
//        );
//    }
//
//}
package th.co.wacoal.atech.pcms2.utilities;


