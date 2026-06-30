package th.co.wacoal.atech.pcms2.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.service.TaskService;

@Controller
@RequestMapping("Setting/JobManagement")
public class JobManagementController {

    private final TaskService taskService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public JobManagementController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPage(HttpSession session) {
        ModelAndView mv = new ModelAndView();
        String user = (String) session.getAttribute("user");
        if (user == null) {
            mv.setViewName("login");
            mv.addObject("alertmsg", "Please log in to access this page.");
            return mv;
        }
        PermitDetail permit = (PermitDetail) session.getAttribute("permit");
        if (permit == null || !isAdminOrIT(permit.getPermitId())) {
            mv.setViewName("error/AccessDenied");
            mv.addObject("errorMsg", "You do not have permission to access this page.");
            return mv;
        }
        mv.setViewName("Setting/JobManagement");
        return mv;
    }

    @RequestMapping(value = "/api/status", method = RequestMethod.GET)
    public void getStatus(HttpSession session, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String user = (String) session.getAttribute("user");
        PermitDetail permit = (PermitDetail) session.getAttribute("permit");
        if (user == null || permit == null || !isAdminOrIT(permit.getPermitId())) {
            out.print("{\"status\":\"FAIL\",\"message\":\"Unauthorized\"}");
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("isRunning", taskService.isRunning());
        map.put("scheduleEnabled", taskService.isScheduleEnabled());
        out.print(new Gson().toJson(map));
    }

    @RequestMapping(value = "/api/run", method = RequestMethod.POST)
    public void runJob(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String user = (String) session.getAttribute("user");
        PermitDetail permit = (PermitDetail) session.getAttribute("permit");
        if (user == null || permit == null || !isAdminOrIT(permit.getPermitId())) {
            out.print("{\"status\":\"FAIL\",\"message\":\"Unauthorized\"}");
            return;
        }
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        if (!isValidDate(fromDate) || !isValidDate(toDate)) {
            out.print("{\"status\":\"FAIL\",\"message\":\"รูปแบบวันที่ไม่ถูกต้อง (yyyy-MM-dd)\"}");
            return;
        }
        String rangeErr = validateRange(fromDate, toDate);
        if (rangeErr != null) {
            HashMap<String, Object> err = new HashMap<>();
            err.put("status", "FAIL");
            err.put("message", rangeErr);
            out.print(new Gson().toJson(err));
            return;
        }
        if (taskService.isRunning()) {
            out.print("{\"status\":\"FAIL\",\"message\":\"job กำลังทำงานอยู่ กรุณารอให้เสร็จก่อน\"}");
            return;
        }
        final String from = fromDate;
        final String to = toDate;
        new Thread(() -> {
            try {
                taskService.runManual(from, to);
            } catch (Exception e) {
                log.error("[JobManagement] runManual failed", e);
            }
        }).start();
        out.print("{\"status\":\"STARTED\",\"message\":\"เริ่มการซ่อมข้อมูลแล้ว\"}");
    }

    @RequestMapping(value = "/api/schedule/toggle", method = RequestMethod.POST)
    public void toggleSchedule(HttpSession session, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String user = (String) session.getAttribute("user");
        PermitDetail permit = (PermitDetail) session.getAttribute("permit");
        if (user == null || permit == null || !isAdminOrIT(permit.getPermitId())) {
            out.print("{\"status\":\"FAIL\",\"message\":\"Unauthorized\"}");
            return;
        }
        boolean newState = !taskService.isScheduleEnabled();
        taskService.setScheduleEnabled(newState);
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "SUCCESS");
        map.put("scheduleEnabled", newState);
        map.put("message", newState ? "เปิดใช้งาน Schedule แล้ว" : "ปิดใช้งาน Schedule แล้ว");
        out.print(new Gson().toJson(map));
    }

    private boolean isAdminOrIT(String permitId) {
        return "ADMIN".equals(permitId) || "ITSUPP".equals(permitId);
    }

    private boolean isValidDate(String date) {
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    /** คืน error message ถ้าช่วงวันที่ไม่ถูกต้อง, คืน null ถ้าผ่าน. กติกา: ซ่อมได้ไม่เกิน 1 อาทิตย์ถึงปัจจุบัน */
    private String validateRange(String fromDate, String toDate) {
        try {
            java.time.LocalDate from = java.time.LocalDate.parse(fromDate);
            java.time.LocalDate to = java.time.LocalDate.parse(toDate);
            java.time.LocalDate today = java.time.LocalDate.now();
            if (from.isAfter(to)) return "วันที่เริ่มต้นต้องไม่หลังวันที่สิ้นสุด";
            if (to.isAfter(today)) return "วันที่สิ้นสุดต้องไม่เกินวันนี้";
            if (from.isBefore(today.minusDays(7))) return "ซ่อมข้อมูลได้ไม่เกิน 1 อาทิตย์ย้อนหลัง";
            return null;
        } catch (Exception e) {
            return "รูปแบบวันที่ไม่ถูกต้อง";
        }
    }
}
