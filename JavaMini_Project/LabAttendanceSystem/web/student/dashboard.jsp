<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.labattendance.model.User"%>
<%@ page import="com.labattendance.model.AttendanceRecord"%>
<%@ page import="com.labattendance.dao.AttendanceDAO"%>
<%@ page import="java.util.List"%>
<%
    User student = (User) session.getAttribute("user");
    if (student == null || !student.isStudent()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=login_required");
        return;
    }

    AttendanceDAO attendanceDAO = new AttendanceDAO();
    List<AttendanceRecord> history = attendanceDAO.getAttendanceByStudent(student.getId());
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard - Lab Attendance System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <header class="navbar">
        <div class="navbar-brand">
            Lab Attendance <span>System</span>
        </div>
        <div class="navbar-right">
            <span class="badge badge-active-users">
                Users Online: <%= (application.getAttribute("activeUsersCount") != null) ? application.getAttribute("activeUsersCount") : 0 %>
            </span>
            <span class="user-info">
                Welcome, <strong><%= student.getName() %></strong>
                <span class="badge badge-role">Student</span>
            </span>
            <a href="<%= request.getContextPath() %>/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <div class="container">

        <% 
            String error = request.getParameter("error");
            String success = request.getParameter("success");
            String subject = request.getParameter("subject");

            if ("access_denied".equals(error)) { %>
            <div class="alert alert-danger">
                <span>&#9888; <strong>Access Denied:</strong> Only Teachers can access teacher pages.</span>
            </div>
        <% } else if ("marked".equals(success)) { 
        %>
            <div class="alert alert-success">
                <span>&#10004; <strong>Success!</strong> Attendance recorded successfully <%= (subject != null) ? "for " + subject : "" %>.</span>
            </div>
        <% } else if ("code_expired".equals(error)) { %>
            <div class="alert alert-danger">
                <span>&#9200; <strong>Code Expired:</strong> This attendance code has expired. Codes are only valid for 2 minutes from creation.</span>
            </div>
        <% } else if ("already_marked".equals(error)) { %>
            <div class="alert alert-warning">
                <span>&#9888; <strong>Duplicate Attempt:</strong> You have already marked attendance for this session.</span>
            </div>
        <% } else if ("code_not_found".equals(error)) { %>
            <div class="alert alert-danger">
                <span>&#10060; <strong>Invalid Code:</strong> No active lab session found matching that 6-digit code.</span>
            </div>
        <% } else if ("invalid_code_format".equals(error)) { %>
            <div class="alert alert-danger">
                <span>&#9888; Please enter a complete 6-digit attendance code.</span>
            </div>
        <% } else if ("mark_failed".equals(error)) { %>
            <div class="alert alert-danger">
                <span>&#9888; An error occurred while recording attendance. Please try again.</span>
            </div>
        <% } %>

        <div class="grid-2">
            <!-- Mark Attendance Form -->
            <div class="card">
                <h2 class="card-title">Mark Lab Attendance</h2>
                <p style="font-size: 0.88rem; color: #64748b; margin-bottom: 1.25rem;">
                    Look at the projector/board in the lab and enter the 6-digit rotating code displayed by your teacher.
                </p>

                <form action="<%= request.getContextPath() %>/mark-attendance" method="POST">
                    <div class="form-group">
                        <label for="code" style="text-align: center;">Enter 6-Digit Code</label>
                        <input type="text" id="code" name="code" class="code-input" maxlength="6" pattern="[0-9]{6}" placeholder="000000" required autofocus autocomplete="off">
                    </div>

                    <button type="submit" class="btn btn-primary btn-block">&#9989; Submit Attendance</button>
                </form>
            </div>

            <!-- Student Instructions -->
            <div class="card" style="background-color: #f8fafc;">
                <h2 class="card-title">How It Works</h2>
                <ol style="margin-left: 1.2rem; font-size: 0.88rem; line-height: 1.8; color: #334155;">
                    <li>The teacher creates a lab session with a rotating 6-digit code.</li>
                    <li>The code is only valid for <strong>2 minutes</strong> to prevent proxy attendance.</li>
                    <li>Enter the code and click <strong>Submit Attendance</strong>.</li>
                    <li>You can only submit once per session. Your successful entries appear in your history below.</li>
                </ol>
            </div>
        </div>

        <!-- Student Attendance History -->
        <div class="card">
            <h2 class="card-title">My Attendance History</h2>
            <% if (history == null || history.isEmpty()) { %>
                <p style="font-size: 0.9rem; color: #64748b; padding: 1rem 0;">No attendance records found yet. Enter your session code above when lab starts.</p>
            <% } else { %>
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Subject</th>
                            <th>Teacher</th>
                            <th>Session Code</th>
                            <th>Time Marked</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            int i = 1;
                            for (AttendanceRecord rec : history) { 
                        %>
                            <tr>
                                <td><%= i++ %></td>
                                <td><strong><%= rec.getSubject() %></strong></td>
                                <td><%= rec.getTeacherName() %></td>
                                <td><code><%= rec.getSessionCode() %></code></td>
                                <td><%= rec.getMarkedAt() %></td>
                                <td><span class="badge badge-active">Present</span></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
        </div>

    </div>

    <footer>
        Lab Attendance System with Rotating Code &bull; Vidyalankar Institute of Technology &bull; Advanced Java
    </footer>

</body>
</html>
