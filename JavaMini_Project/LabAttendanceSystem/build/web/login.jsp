<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Lab Attendance System</title>
    <link rel="stylesheet" href="css/style.css">
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
        </div>
    </header>

    <div class="container" style="max-width: 460px; margin-top: 3.5rem;">
        <div class="card">
            <h2 class="card-title" style="text-align: center; border: none; margin-bottom: 1.5rem;">User Login</h2>

            <% 
                String error = request.getParameter("error");
                String msg = request.getParameter("msg");
                if ("invalid_credentials".equals(error)) { 
            %>
                <div class="alert alert-danger">
                    <span>&#9888; Invalid username or password. Please try again.</span>
                </div>
            <% } else if ("empty_fields".equals(error)) { %>
                <div class="alert alert-danger">
                    <span>&#9888; Please enter both username and password.</span>
                </div>
            <% } else if ("login_required".equals(error)) { %>
                <div class="alert alert-warning">
                    <span>&#128274; You must log in to access that page.</span>
                </div>
            <% } else if ("logged_out".equals(msg)) { %>
                <div class="alert alert-success">
                    <span>&#10004; You have been logged out successfully.</span>
                </div>
            <% } else if ("registered".equals(msg)) { %>
                <div class="alert alert-success">
                    <span>&#10004; Profile created successfully! You can now log in below.</span>
                </div>
            <% } %>

            <form action="login" method="POST">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" placeholder="e.g. bhanu or arpita" required autofocus>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="Enter your password" required>
                </div>

                <button type="submit" class="btn btn-primary btn-block">Sign In</button>
            </form>

            <div style="text-align: center; margin-top: 1.25rem;">
                <a href="register.jsp" class="btn btn-secondary btn-block" style="background:#f1f5f9; color:#1e293b; border: 1px solid #e2e8f0; font-size:0.9rem;">
                    &#43; Create New Profile (Student / Teacher)
                </a>
            </div>

            <div style="margin-top: 1.5rem; padding-top: 1.25rem; border-top: 1px dashed #e2e8f0; font-size: 0.85rem; color: #64748b;">
                <strong>Sample Test Credentials:</strong>
                <ul style="margin: 0.5rem 0 0 1.2rem; line-height: 1.5;">
                    <li><strong>Teacher:</strong> <code>bhanu</code> / <code>password123</code></li>
                    <li><strong>Teacher:</strong> <code>teacher1</code> / <code>admin123</code></li>
                    <li><strong>Student:</strong> <code>arpita</code> / <code>student123</code></li>
                    <li><strong>Student:</strong> <code>tanishka</code> / <code>student123</code></li>
                </ul>
            </div>
        </div>
    </div>

    <footer>
        Lab Attendance System with Rotating Code &bull; Vidyalankar Institute of Technology &bull; Advanced Java
    </footer>

</body>
</html>
