package com.labattendance.filter;

import com.labattendance.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"/teacher/*", "/student/*", "/start-session", "/mark-attendance", "/view-attendance", "/export-csv"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Prevent caching of protected pages
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();

        // 1. If not logged in, redirect to login page
        if (user == null) {
            res.sendRedirect(contextPath + "/login.jsp?error=login_required");
            return;
        }

        // 2. Role-based URL authorization checks
        boolean isTeacherPath = uri.contains("/teacher/") || uri.endsWith("/start-session") || uri.endsWith("/view-attendance") || uri.endsWith("/export-csv");
        boolean isStudentPath = uri.contains("/student/") || uri.endsWith("/mark-attendance");

        if (isTeacherPath && !user.isTeacher()) {
            // Student attempting to access Teacher resources
            res.sendRedirect(contextPath + "/student/dashboard.jsp?error=access_denied");
            return;
        }

        if (isStudentPath && !user.isStudent()) {
            // Teacher attempting to access Student resources
            res.sendRedirect(contextPath + "/teacher/dashboard.jsp?error=access_denied");
            return;
        }

        // User authenticated and authorized for this request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
