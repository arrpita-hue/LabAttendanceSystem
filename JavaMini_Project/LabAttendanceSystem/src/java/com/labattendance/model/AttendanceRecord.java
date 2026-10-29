package com.labattendance.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class AttendanceRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int sessionId;
    private int studentId;
    private Timestamp markedAt;

    // Joined fields for display purposes
    private String studentName;
    private String studentUsername;
    private String subject;
    private String teacherName;
    private String sessionCode;

    public AttendanceRecord() {
    }

    public AttendanceRecord(int id, int sessionId, int studentId, Timestamp markedAt) {
        this.id = id;
        this.sessionId = sessionId;
        this.studentId = studentId;
        this.markedAt = markedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Timestamp getMarkedAt() {
        return markedAt;
    }

    public void setMarkedAt(Timestamp markedAt) {
        this.markedAt = markedAt;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }
}
