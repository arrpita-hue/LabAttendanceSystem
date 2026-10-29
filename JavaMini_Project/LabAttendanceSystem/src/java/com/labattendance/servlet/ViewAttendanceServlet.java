package com.labattendance.servlet;

import com.labattendance.dao.AttendanceDAO;
import com.labattendance.dao.SessionDAO;
import com.labattendance.model.AttendanceRecord;
import com.labattendance.model.LabSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewAttendanceServlet", urlPatterns = {"/view-attendance"})
public class ViewAttendanceServlet extends HttpServlet {
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
        String sessionIdStr = request.getParameter("sessionId");
        if (sessionIdStr == null || sessionIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/teacher/dashboard.jsp");
            return;
        }

        try {
            int sessionId = Integer.parseInt(sessionIdStr.trim());
            LabSession labSession = sessionDAO.findById(sessionId);
            if (labSession == null) {
                response.sendRedirect(request.getContextPath() + "/teacher/dashboard.jsp?error=session_not_found");
                return;
            }

            List<AttendanceRecord> records = attendanceDAO.getAttendanceBySession(sessionId);
            request.setAttribute("sessionDetails", labSession);
            request.setAttribute("attendanceList", records);
            request.getRequestDispatcher("/teacher/report.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/teacher/dashboard.jsp?error=invalid_session_id");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
