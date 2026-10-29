package com.labattendance.servlet;

import com.labattendance.dao.AttendanceDAO;
import com.labattendance.dao.SessionDAO;
import com.labattendance.model.LabSession;
import com.labattendance.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

@WebServlet(name = "MarkAttendanceServlet", urlPatterns = {"/mark-attendance"})
public class MarkAttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SessionDAO sessionDAO;
    private AttendanceDAO attendanceDAO;

    @Override
    public void init() throws ServletException {
        sessionDAO = new SessionDAO();
        attendanceDAO = new AttendanceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/student/dashboard.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User student = (User) session.getAttribute("user");
        String code = request.getParameter("code");

        if (code == null || code.trim().length() != 6) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard.jsp?error=invalid_code_format");
            return;
        }

        code = code.trim();

        // 1. Look up session by code
        LabSession labSession = sessionDAO.findByCode(code);
        if (labSession == null) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard.jsp?error=code_not_found");
            return;
        }

        // 2. Check if code has expired (2 minutes limit)
        if (labSession.isExpired()) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard.jsp?error=code_expired");
            return;
        }

        // 3. Prevent duplicate attendance for the same session
        if (attendanceDAO.isAlreadyMarked(labSession.getId(), student.getId())) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard.jsp?error=already_marked");
            return;
        }

        // 4. Mark attendance
        boolean success = attendanceDAO.markAttendance(
            labSession.getId(),
            student.getId(),
            new Timestamp(System.currentTimeMillis())
        );

        if (success) {
            String encodedSubject = URLEncoder.encode(labSession.getSubject(), StandardCharsets.UTF_8.toString());
            response.sendRedirect(request.getContextPath() + "/student/dashboard.jsp?success=marked&subject=" + encodedSubject);
        } else {
            response.sendRedirect(request.getContextPath() + "/student/dashboard.jsp?error=mark_failed");
        }
    }
}
