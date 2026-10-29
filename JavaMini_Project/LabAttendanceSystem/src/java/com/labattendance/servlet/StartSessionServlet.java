package com.labattendance.servlet;

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
import java.security.SecureRandom;
import java.sql.Timestamp;

@WebServlet(name = "StartSessionServlet", urlPatterns = {"/start-session"})
public class StartSessionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SessionDAO sessionDAO;
    private SecureRandom random;

    @Override
    public void init() throws ServletException {
        sessionDAO = new SessionDAO();
        random = new SecureRandom();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/teacher/dashboard.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User teacher = (User) session.getAttribute("user");
        String subject = request.getParameter("subject");

        if (subject == null || subject.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/teacher/dashboard.jsp?error=empty_subject");
            return;
        }

        // Generate 6-digit random numeric code (e.g., "048291")
        int number = random.nextInt(1000000);
        String code = String.format("%06d", number);

        long now = System.currentTimeMillis();
        Timestamp createdAt = new Timestamp(now);
        // Valid for exactly two minutes as specified in report section 1 and 8.2
        Timestamp expiresAt = new Timestamp(now + (2 * 60 * 1000));

        int sessionId = sessionDAO.createSession(subject.trim(), teacher.getId(), code, createdAt, expiresAt);

        if (sessionId > 0) {
            LabSession labSession = sessionDAO.findById(sessionId);
            session.setAttribute("currentActiveSession", labSession);
            response.sendRedirect(request.getContextPath() + "/teacher/dashboard.jsp?session_created=true");
        } else {
            response.sendRedirect(request.getContextPath() + "/teacher/dashboard.jsp?error=creation_failed");
        }
    }
}
