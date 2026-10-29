-- =======================================================
-- Database Setup Script for Lab Attendance System
-- Project: Lab Attendance System with Rotating Code
-- Vidyalankar Institute of Technology (Academic Year 2026-27)
-- =======================================================

CREATE DATABASE IF NOT EXISTS lab_attendance_db;
USE lab_attendance_db;

-- 1. Table: users
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('TEACHER', 'STUDENT') NOT NULL
);

-- 2. Table: sessions_lab
CREATE TABLE IF NOT EXISTS sessions_lab (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject VARCHAR(100) NOT NULL,
    teacher_id INT NOT NULL,
    code CHAR(6) NOT NULL,
    created_at DATETIME NOT NULL,
    expires_at DATETIME NOT NULL,
    CONSTRAINT fk_session_teacher FOREIGN KEY (teacher_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. Table: attendance
CREATE TABLE IF NOT EXISTS attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    session_id INT NOT NULL,
    student_id INT NOT NULL,
    marked_at DATETIME NOT NULL,
    CONSTRAINT fk_att_session FOREIGN KEY (session_id) REFERENCES sessions_lab(id) ON DELETE CASCADE,
    CONSTRAINT fk_att_student FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_session_student UNIQUE (session_id, student_id)
);

-- =======================================================
-- Initial Seed Data (Only inserted if username doesn't exist)
-- Password Hashes are generated using SHA-256:
-- 'password123' -> ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f
-- 'admin123'    -> 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
-- 'student123'  -> 703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b
-- =======================================================

INSERT IGNORE INTO users (name, username, password_hash, role) VALUES
('Prof. Bhanu Tekwani', 'bhanu', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'TEACHER'),
('Prof. Lab Incharge', 'teacher1', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'TEACHER'),
('Arpita Yewale', 'arpita', '703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b', 'STUDENT'),
('Tanishka Nandurkar', 'tanishka', '703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b', 'STUDENT'),
('Rahul Sharma', 'rahul', '703b0a3d6ad75b649a28adde7d83c6251da457549263bc7ff45ec709b0a8448b', 'STUDENT');
