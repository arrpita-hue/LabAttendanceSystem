<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.labattendance.model.User"%>
<%@ page import="com.labattendance.model.LabSession"%>
<%@ page import="com.labattendance.model.AttendanceRecord"%>
<%@ page import="java.util.List"%>
<%
    User teacher = (User) session.getAttribute("user");
    if (teacher == null || !teacher.isTeacher()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=login_required");
        return;
    }

    LabSession labSession = (LabSession) request.getAttribute("sessionDetails");
    List<AttendanceRecord> records = (List<AttendanceRecord>) request.getAttribute("attendanceList");

    if (labSession == null) {
        response.sendRedirect(request.getContextPath() + "/teacher/dashboard.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Session Attendance Report - <%= labSession.getSubject() %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <header class="navbar">
        <div class="navbar-brand">
            Lab Attendance <span>System</span>
        </div>
        <div class="navbar-right">
            <span class="user-info">
                Welcome, <strong><%= teacher.getName() %></strong>
            </span>
            <a href="<%= request.getContextPath() %>/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <div class="container">

        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
            <div>
                <a href="<%= request.getContextPath() %>/teacher/dashboard.jsp" class="btn btn-secondary btn-sm">&larr; Back to Dashboard</a>
            </div>
            <div>
                <a href="<%= request.getContextPath() %>/export-csv?sessionId=<%= labSession.getId() %>" class="btn btn-primary btn-sm">&#128190; Export to CSV</a>
            </div>
        </div>

        <div class="card">
            <h2 class="card-title">Session Details</h2>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem; margin-bottom: 1rem;">
                <div>
                    <label>Subject</label>
                    <div style="font-size: 1.1rem; font-weight: 700;"><%= labSession.getSubject() %></div>
                </div>
                <div>
                    <label>Session Code</label>
                    <div style="font-size: 1.1rem; font-weight: 700; font-family: monospace;"><%= labSession.getCode() %></div>
                </div>
                <div>
                    <label>Created Time</label>
                    <div style="font-size: 0.95rem;"><%= labSession.getCreatedAt() %></div>
                </div>
                <div>
                    <label>Total Present</label>
                    <div><span class="badge badge-active" style="font-size: 1rem; padding: 0.3rem 0.8rem;"><%= (records != null) ? records.size() : 0 %> Students</span></div>
                </div>
            </div>
        </div>

        <div class="card">
            <h2 class="card-title">Attending Students List</h2>
            <% if (records == null || records.isEmpty()) { %>
                <p style="font-size: 0.9rem; color: #64748b; padding: 1rem 0;">No attendance has been recorded for this session yet.</p>
            <% } else { %>
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Student Name</th>
                            <th>Username</th>
                            <th>Time Marked</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            int index = 1;
                            for (AttendanceRecord r : records) { 
                        %>
                            <tr>
                                <td><%= index++ %></td>
                                <td><strong><%= r.getStudentName() %></strong></td>
                                <td><code><%= r.getStudentUsername() %></code></td>
                                <td><%= r.getMarkedAt() %></td>
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
