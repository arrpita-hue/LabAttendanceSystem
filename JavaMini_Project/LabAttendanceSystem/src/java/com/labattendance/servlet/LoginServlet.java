package com.labattendance.servlet;

import com.labattendance.dao.UserDAO;
import com.labattendance.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If already logged in, redirect to respective dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.isTeacher()) {
                response.sendRedirect(request.getContextPath() + "/teacher/dashboard.jsp");
                return;
            } else if (user.isStudent()) {
                response.sendRedirect(request.getContextPath() + "/student/dashboard.jsp");
                return;
            }
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=empty_fields");
            return;
        }

        User user = userDAO.authenticate(username.trim(), password);

        if (user != null) {
            // Valid login -> create session
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("user_id", user.getId());
            session.setAttribute("user_name", user.getName());
            session.setAttribute("user_role", user.getRole());

            if (user.isTeacher()) {
                response.sendRedirect(request.getContextPath() + "/teacher/dashboard.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/student/dashboard.jsp");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=invalid_credentials");
        }
    }
}
