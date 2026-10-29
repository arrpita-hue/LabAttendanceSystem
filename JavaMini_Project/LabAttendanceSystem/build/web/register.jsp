<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Lab Attendance System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

    <header class="navbar">
        <div class="navbar-brand">
            Lab Attendance <span>System</span>
        </div>
        <div class="navbar-right">
            <a href="login.jsp" class="btn btn-secondary btn-sm">Back to Login</a>
        </div>
    </header>

    <div class="container" style="max-width: 480px; margin-top: 2.5rem;">
        <div class="card">
            <h2 class="card-title" style="text-align: center; border: none; margin-bottom: 1.5rem;">Create New Profile</h2>

            <% 
                String error = request.getParameter("error");
                if ("empty_fields".equals(error)) { 
            %>
                <div class="alert alert-danger">
                    <span>&#9888; Please fill in all required fields.</span>
                </div>
            <% } else if ("username_taken".equals(error)) { %>
                <div class="alert alert-danger">
                    <span>&#9888; That username is already taken. Please choose another.</span>
                </div>
            <% } else if ("invalid_role".equals(error)) { %>
                <div class="alert alert-danger">
                    <span>&#9888; Please select a valid role (Student or Teacher).</span>
                </div>
            <% } %>

            <form action="register" method="POST">
                <div class="form-group">
                    <label for="name">Full Name</label>
                    <input type="text" id="name" name="name" placeholder="e.g. John Doe" required autofocus>
                </div>

                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" placeholder="Choose a unique username" required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="Create a password" required>
                </div>

                <div class="form-group">
                    <label for="role">Register As</label>
                    <select id="role" name="role" required style="width: 100%; padding: 0.7rem 0.9rem; border: 1px solid #e2e8f0; border-radius: 6px; font-size: 0.95rem; outline: none; background-color: #fff;">
                        <option value="STUDENT" selected>Student</option>
                        <option value="TEACHER">Teacher</option>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary btn-block" style="margin-top: 1.25rem;">&#10004; Create Profile</button>
            </form>

            <div style="text-align: center; margin-top: 1.5rem; font-size: 0.9rem;">
                Already have an account? <a href="login.jsp" style="color: #2563eb; font-weight: 600; text-decoration: none;">Sign In here</a>
            </div>
        </div>
    </div>

    <footer>
        Lab Attendance System with Rotating Code &bull; Vidyalankar Institute of Technology &bull; Advanced Java
    </footer>

</body>
</html>
