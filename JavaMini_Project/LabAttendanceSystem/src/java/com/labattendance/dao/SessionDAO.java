package com.labattendance.dao;

import com.labattendance.model.LabSession;
import com.labattendance.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO {

    public int createSession(String subject, int teacherId, String code, Timestamp createdAt, Timestamp expiresAt) {
        String sql = "INSERT INTO sessions_lab (subject, teacher_id, code, created_at, expires_at) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, subject);
            stmt.setInt(2, teacherId);
            stmt.setString(3, code);
            stmt.setTimestamp(4, createdAt);
            stmt.setTimestamp(5, expiresAt);

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return -1;
    }

    public LabSession findByCode(String code) {
        // Fetch the most recent session created with this code
        String sql = "SELECT s.id, s.subject, s.teacher_id, s.code, s.created_at, s.expires_at, u.name AS teacher_name " +
                     "FROM sessions_lab s " +
                     "JOIN users u ON s.teacher_id = u.id " +
                     "WHERE s.code = ? ORDER BY s.id DESC LIMIT 1";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, code);
            rs = stmt.executeQuery();

            if (rs.next()) {
                LabSession session = new LabSession(
                    rs.getInt("id"),
                    rs.getString("subject"),
                    rs.getInt("teacher_id"),
                    rs.getString("code"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("expires_at")
                );
                session.setTeacherName(rs.getString("teacher_name"));
                return session;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return null;
    }

    public LabSession findById(int id) {
        String sql = "SELECT s.id, s.subject, s.teacher_id, s.code, s.created_at, s.expires_at, u.name AS teacher_name " +
                     "FROM sessions_lab s " +
                     "JOIN users u ON s.teacher_id = u.id " +
                     "WHERE s.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                LabSession session = new LabSession(
                    rs.getInt("id"),
                    rs.getString("subject"),
                    rs.getInt("teacher_id"),
                    rs.getString("code"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("expires_at")
                );
                session.setTeacherName(rs.getString("teacher_name"));
                return session;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return null;
    }

    public List<LabSession> getSessionsByTeacher(int teacherId) {
        List<LabSession> list = new ArrayList<>();
        String sql = "SELECT s.id, s.subject, s.teacher_id, s.code, s.created_at, s.expires_at, u.name AS teacher_name " +
                     "FROM sessions_lab s " +
                     "JOIN users u ON s.teacher_id = u.id " +
                     "WHERE s.teacher_id = ? ORDER BY s.id DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, teacherId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                LabSession session = new LabSession(
                    rs.getInt("id"),
                    rs.getString("subject"),
                    rs.getInt("teacher_id"),
                    rs.getString("code"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("expires_at")
                );
                session.setTeacherName(rs.getString("teacher_name"));
                list.add(session);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn, stmt, rs);
        }
        return list;
    }
}
