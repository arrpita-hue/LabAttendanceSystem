package com.labattendance.servlet;

import com.labattendance.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (name == null || name.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/register.jsp?error=empty_fields");
            return;
        }

        name = name.trim();
        username = username.trim().toLowerCase();
        role = role.trim().toUpperCase();

        if (!"STUDENT".equals(role) && !"TEACHER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/register.jsp?error=invalid_role");
            return;
        }

        boolean success = userDAO.registerUser(name, username, password, role);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=registered");
        } else {
            response.sendRedirect(request.getContextPath() + "/register.jsp?error=username_taken");
        }
    }
}
