package com.labattendance.dao;

import com.labattendance.model.AttendanceRecord;
import com.labattendance.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    public boolean isAlreadyMarked(int sessionId, int studentId) {
        String sql = "SELECT id FROM attendance WHERE session_id = ? AND student_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sessionId);
            stmt.setInt(2, studentId);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return false;
    }

    public boolean markAttendance(int sessionId, int studentId, Timestamp markedAt) {
        String sql = "INSERT INTO attendance (session_id, student_id, marked_at) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sessionId);
            stmt.setInt(2, studentId);
            stmt.setTimestamp(3, markedAt);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt);
        }
        return false;
    }

    public List<AttendanceRecord> getAttendanceBySession(int sessionId) {
        List<AttendanceRecord> list = new ArrayList<>();
        String sql = "SELECT a.id, a.session_id, a.student_id, a.marked_at, " +
                     "u.name AS student_name, u.username AS student_username " +
                     "FROM attendance a " +
                     "JOIN users u ON a.student_id = u.id " +
                     "WHERE a.session_id = ? ORDER BY a.marked_at ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sessionId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                AttendanceRecord record = new AttendanceRecord(
                    rs.getInt("id"),
                    rs.getInt("session_id"),
                    rs.getInt("student_id"),
                    rs.getTimestamp("marked_at")
                );
                record.setStudentName(rs.getString("student_name"));
                record.setStudentUsername(rs.getString("student_username"));
                list.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return list;
    }

    public List<AttendanceRecord> getAttendanceByStudent(int studentId) {
        List<AttendanceRecord> list = new ArrayList<>();
        String sql = "SELECT a.id, a.session_id, a.student_id, a.marked_at, " +
                     "s.subject, s.code, tu.name AS teacher_name " +
                     "FROM attendance a " +
                     "JOIN sessions_lab s ON a.session_id = s.id " +
                     "JOIN users tu ON s.teacher_id = tu.id " +
                     "WHERE a.student_id = ? ORDER BY a.marked_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                AttendanceRecord record = new AttendanceRecord(
                    rs.getInt("id"),
                    rs.getInt("session_id"),
                    rs.getInt("student_id"),
                    rs.getTimestamp("marked_at")
                );
                record.setSubject(rs.getString("subject"));
                record.setSessionCode(rs.getString("code"));
                record.setTeacherName(rs.getString("teacher_name"));
                list.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return list;
    }

    public int countAttendanceBySession(int sessionId) {
        String sql = "SELECT COUNT(*) FROM attendance WHERE session_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sessionId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return 0;
    }
}
