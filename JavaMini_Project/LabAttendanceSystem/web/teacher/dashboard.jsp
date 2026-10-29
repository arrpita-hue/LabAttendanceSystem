<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.labattendance.model.User"%>
<%@ page import="com.labattendance.model.LabSession"%>
<%@ page import="com.labattendance.dao.SessionDAO"%>
<%@ page import="java.util.List"%>
<%
    User teacher = (User) session.getAttribute("user");
    if (teacher == null || !teacher.isTeacher()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=login_required");
        return;
    }

    SessionDAO sessionDAO = new SessionDAO();
    List<LabSession> pastSessions = sessionDAO.getSessionsByTeacher(teacher.getId());

    LabSession activeSession = (LabSession) session.getAttribute("currentActiveSession");
    if (activeSession != null) {
        // Refresh session state from database
        activeSession = sessionDAO.findById(activeSession.getId());
        session.setAttribute("currentActiveSession", activeSession);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher Dashboard - Lab Attendance System</title>
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
                Welcome, <strong><%= teacher.getName() %></strong>
                <span class="badge badge-role">Teacher</span>
            </span>
            <a href="<%= request.getContextPath() %>/logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <div class="container">
        
        <% 
            String error = request.getParameter("error");
            String sessionCreated = request.getParameter("session_created");
            if ("empty_subject".equals(error)) { 
        %>
            <div class="alert alert-danger">&#9888; Please enter a valid subject name.</div>
        <% } else if ("access_denied".equals(error)) { %>
            <div class="alert alert-danger">&#9888; You are logged in as a Teacher. Only Students can submit attendance codes.</div>
        <% } else if ("true".equals(sessionCreated)) { %>
            <div class="alert alert-success">&#10004; Lab session started! Display the code below to your students.</div>
        <% } %>

        <% if (activeSession != null && !activeSession.isExpired()) { %>
            <!-- Active Code Display Box -->
            <div class="active-code-box">
                <h3 style="font-weight: 500; opacity: 0.9;">CURRENT ACTIVE SESSION: <%= activeSession.getSubject().toUpperCase() %></h3>
                <div class="active-code-number" id="codeDisplay"><%= activeSession.getCode() %></div>
                <div class="timer-box">
                    <span>&#9201; Code expires in:</span>
                    <span id="countdown"><%= activeSession.getRemainingSeconds() %>s</span>
                </div>
                <div style="margin-top: 1rem;">
                    <a href="<%= request.getContextPath() %>/view-attendance?sessionId=<%= activeSession.getId() %>" class="btn btn-secondary btn-sm" style="background:#ffffff; color:#1e293b; font-weight:600;">
                        View Live Student Attendance
                    </a>
                </div>
            </div>

            <script>
                // Live countdown timer for teacher display
                let remaining = <%= activeSession.getRemainingSeconds() %>;
                const countdownEl = document.getElementById("countdown");
                const codeDisplayEl = document.getElementById("codeDisplay");

                const interval = setInterval(function() {
                    remaining--;
                    if (remaining <= 0) {
                        clearInterval(interval);
                        countdownEl.innerHTML = "<span style='color:#f87171;'>EXPIRED</span>";
                        codeDisplayEl.style.opacity = "0.5";
                        codeDisplayEl.style.textDecoration = "line-through";
                    } else {
                        let mins = Math.floor(remaining / 60);
                        let secs = remaining % 60;
                        countdownEl.innerText = mins + "m " + (secs < 10 ? "0" : "") + secs + "s";
                    }
                }, 1000);
            </script>
        <% } %>

        <div class="grid-2">
            <!-- Start New Session Card -->
            <div class="card">
                <h2 class="card-title">Start New Lab Session</h2>
                <p style="font-size: 0.88rem; color: #64748b; margin-bottom: 1.25rem;">
                    Starting a new session generates a random 6-digit code valid for <strong>2 minutes</strong>.
                </p>

                <form action="<%= request.getContextPath() %>/start-session" method="POST">
                    <div class="form-group">
                        <label for="subject">Subject / Lab Name</label>
                        <input type="text" id="subject" name="subject" placeholder="e.g. Advanced Java Lab - Batch A" required>
                    </div>

                    <button type="submit" class="btn btn-primary btn-block">&#9654; Start Session &amp; Generate Code</button>
                </form>
            </div>

            <!-- Quick Instructions Card -->
            <div class="card" style="background-color: #f8fafc;">
                <h2 class="card-title">Teacher Instructions</h2>
                <ol style="margin-left: 1.2rem; font-size: 0.88rem; line-height: 1.8; color: #334155;">
                    <li>Enter the Subject or Batch name and click <strong>Start Session</strong>.</li>
                    <li>Display the generated <strong>6-digit code</strong> on the lab projector or board.</li>
                    <li>Students must enter the code within <strong>2 minutes</strong> from their own devices.</li>
                    <li>Click <strong>View Live Attendance</strong> to monitor which students have marked their presence.</li>
                </ol>
            </div>
        </div>

        <!-- Past Sessions Table -->
        <div class="card">
            <h2 class="card-title">Lab Sessions History</h2>
            <% if (pastSessions.isEmpty()) { %>
                <p style="font-size: 0.9rem; color: #64748b; padding: 1rem 0;">No lab sessions created yet. Start your first session above!</p>
            <% } else { %>
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Subject</th>
                            <th>Code</th>
                            <th>Created At</th>
                            <th>Expires At</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            int idx = 1;
                            for (LabSession s : pastSessions) { 
                        %>
                            <tr>
                                <td><%= idx++ %></td>
                                <td><strong><%= s.getSubject() %></strong></td>
                                <td><code style="font-size:1.05rem; font-weight:700;"><%= s.getCode() %></code></td>
                                <td><%= s.getCreatedAt() %></td>
                                <td><%= s.getExpiresAt() %></td>
                                <td>
                                    <% if (!s.isExpired()) { %>
                                        <span class="badge badge-active">Active</span>
                                    <% } else { %>
                                        <span class="badge badge-expired">Expired</span>
                                    <% } %>
                                </td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/view-attendance?sessionId=<%= s.getId() %>" class="btn btn-secondary btn-sm">
                                        View Report
                                    </a>
                                </td>
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
