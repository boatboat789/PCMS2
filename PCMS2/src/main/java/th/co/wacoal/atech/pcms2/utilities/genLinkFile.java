///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package Test.utilities;
//
//import th.co.wacoal.pom.dao.impl.PomDocHDaoImpl;
//import th.co.wacoal.pom.db.Database;
//import th.co.wacoal.pom.entity.PomDocH;
//import th.co.wacoal.pom.entity.PomDocNote;
//import th.co.wacoal.pom.dao.PomDocHDao;
//
///**
// *
// * @author 92705
// */
//public class genLinkFile {
//
//    //*** 2019-04-05 ***//
//    public String generateLink(
//            String docId,
//            String poNumber,
//            String purOrg,
//            String purGroup,
//            String vendor,
//            String docDate,
//            String toDocDate,
//            Integer countFile
//    ) {
//
//        String modalActionFile = "";
//
//        if (countFile > 0) {
//            modalActionFile = "<a data-toggle=\"modal\" class=\"noteModal text-success\"data-href=\"../modal/modal_info_po_note_and_file.htm?docId=" + docId + "&poNumber=" + poNumber + "&purchasingOrganization=" + purOrg + "&purchasingGroup=" + purGroup + "&vendor=" + vendor + "&sapCreateDate=" + docDate + "&toSAPCreateDate=" + toDocDate + "\" data-toggle=\"modal\" data-target=\"#noteModal\" title=\"Note\" style=\"cursor: pointer;\">"
//                    + "<span class=\"fas fa-paste fa-2x\" aria-hidden=\"true\" ></span>"
//                    + "</a>"
//                    + "&nbsp;";
//        } else {
//            modalActionFile = "<a data-toggle=\"modal\" class=\"noteModal text-secondary\"data-href=\"../modal/modal_info_po_note_and_file.htm?docId=" + docId + "&poNumber=" + poNumber + "&purchasingOrganization=" + purOrg + "&purchasingGroup=" + purGroup + "&vendor=" + vendor + "&sapCreateDate=" + docDate + "&toSAPCreateDate=" + toDocDate + "\" data-toggle=\"modal\" data-target=\"#noteModal\" title=\"Note\" style=\"cursor: pointer;\">"
//                    + "<span class=\"fas fa-paste fa-2x\" aria-hidden=\"true\" ></span>"
//                    + "</a>"
//                    + "&nbsp;";
//        }
//
//        return modalActionFile;
//
//    }
//
//    public static String mainGenLinkFile(
//            String docId,
//            String poNumber,
//            String purOrg,
//            String purGroup,
//            String vendor,
//            String docDate,
//            String toDocDate,
//            Integer countFile
//    ) {
//        return new genLinkFile().generateLink(
//                docId,
//                poNumber,
//                purOrg,
//                purGroup,
//                vendor,
//                docDate,
//                toDocDate,
//                countFile
//        );
//    }
//
//}
package th.co.wacoal.atech.pcms2.utilities;


