package th.co.wacoal.atech.pcms2.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import th.co.wacoal.atech.pcms2.entities.PermitDetail;
import th.co.wacoal.atech.pcms2.entities.UserDetail;
import th.co.wacoal.atech.pcms2.service.master.PermitsService;

@Controller
@RequestMapping("Setting/PermitManagement")
public class PermitManagementController {

    private final PermitsService permitsService;
    private final Gson gson = new Gson();

    @Autowired
    public PermitManagementController(PermitsService permitsService) {
        this.permitsService = permitsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPage(HttpSession session) {
        ModelAndView mv = new ModelAndView();
        String user = (String) session.getAttribute("user");
        if (user == null) {
            mv.setViewName("login");
            return mv;
        }
        PermitDetail permit = (PermitDetail) session.getAttribute("permit");
        if (permit == null || !isAdminOrIT(permit.getPermitId())) {
            mv.setViewName("error/AccessDenied");
            mv.addObject("errorMsg", "คุณไม่มีสิทธิ์เข้าถึงหน้านี้");
            return mv;
        }
        mv.setViewName("Setting/PermitManagement");
        return mv;
    }

    @RequestMapping(value = "/api/permits", method = RequestMethod.GET)
    public void getPermits(HttpSession session, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (!checkAccess(session)) { out.print(fail("Unauthorized")); return; }
        ArrayList<PermitDetail> list = permitsService.getPermitsDetail();
        out.print(gson.toJson(list));
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public void getUsers(HttpSession session, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (!checkAccess(session)) { out.print(fail("Unauthorized")); return; }
        ArrayList<UserDetail> list = permitsService.getUsers();
        out.print(gson.toJson(list));
    }

    @RequestMapping(value = "/api/permit/update", method = RequestMethod.POST)
    public void updatePermit(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String user = (String) session.getAttribute("user");
        PermitDetail permit = (PermitDetail) session.getAttribute("permit");
        if (user == null || permit == null || !"ADMIN".equals(permit.getPermitId())) {
            out.print(fail("เฉพาะ ADMIN เท่านั้นที่สามารถแก้ไขสิทธิ์ได้"));
            return;
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) sb.append(line);
            JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
            String permitId = json.get("permitId").getAsString();
            if ("ADMIN".equals(permitId)) {
                out.print(fail("ไม่สามารถแก้ไขสิทธิ์ ADMIN ได้"));
                return;
            }
            PermitDetail update = new PermitDetail(
                0, permitId,
                json.has("description") ? json.get("description").getAsString() : "",
                json.get("isPCMSMain").getAsBoolean(),
                json.get("isPCMSDetail").getAsBoolean(),
                json.get("isPCMSMainToProd").getAsBoolean(),
                json.get("isPCMSMainToLBMS").getAsBoolean(),
                json.get("isPCMSMainToQCMS").getAsBoolean(),
                json.get("isPCMSMainToInspect").getAsBoolean(),
                json.get("isPCMSMainToSFC").getAsBoolean(),
                json.get("isReport").getAsBoolean(),
                json.get("isUserManagement").getAsBoolean()
            );
            int rows = permitsService.updatePermit(update, user);
            if (rows > 0) out.print("{\"status\":\"SUCCESS\",\"message\":\"บันทึกสำเร็จ\"}");
            else out.print(fail("ไม่พบ permitId ที่ระบุ"));
        } catch (Exception e) {
            out.print(fail("เกิดข้อผิดพลาด: " + e.getMessage()));
        }
    }

    @RequestMapping(value = "/api/user/permit", method = RequestMethod.POST)
    public void updateUserPermit(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String user = (String) session.getAttribute("user");
        PermitDetail permit = (PermitDetail) session.getAttribute("permit");
        if (user == null || permit == null || !isAdminOrIT(permit.getPermitId())) {
            out.print(fail("ไม่มีสิทธิ์แก้ไขสิทธิ์ user"));
            return;
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) sb.append(line);
            JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
            String userId = json.get("userId").getAsString();
            String permitId = json.get("permitId").getAsString();
            if (userId == null || userId.trim().isEmpty()) {
                out.print(fail("กรุณาระบุ userId"));
                return;
            }
            // ITSUPP ไม่สามารถกำหนดสิทธิ์ระดับ ADMIN ได้
            if ("ITSUPP".equals(permit.getPermitId()) && "ADMIN".equals(permitId)) {
                out.print(fail("ITSUPP ไม่สามารถกำหนดสิทธิ์ ADMIN ได้"));
                return;
            }
            int rows = permitsService.updateUserPermit(userId, permitId, user);
            if (rows > 0) out.print("{\"status\":\"SUCCESS\",\"message\":\"บันทึกสำเร็จ\"}");
            else out.print(fail("ไม่สามารถบันทึกได้"));
        } catch (Exception e) {
            out.print(fail("เกิดข้อผิดพลาด: " + e.getMessage()));
        }
    }

    private boolean isAdminOrIT(String permitId) {
        return "ADMIN".equals(permitId) || "ITSUPP".equals(permitId);
    }

    private boolean checkAccess(HttpSession session) {
        String user = (String) session.getAttribute("user");
        PermitDetail permit = (PermitDetail) session.getAttribute("permit");
        return user != null && permit != null && isAdminOrIT(permit.getPermitId());
    }

    private String fail(String message) {
        return "{\"status\":\"FAIL\",\"message\":\"" + message.replace("\"", "'") + "\"}";
    }
}
