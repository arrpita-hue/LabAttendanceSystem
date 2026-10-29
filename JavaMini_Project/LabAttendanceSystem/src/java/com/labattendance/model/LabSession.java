package com.labattendance.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class LabSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String subject;
    private int teacherId;
    private String teacherName;
    private String code;
    private Timestamp createdAt;
    private Timestamp expiresAt;

    public LabSession() {
    }

    public LabSession(int id, String subject, int teacherId, String code, Timestamp createdAt, Timestamp expiresAt) {
        this.id = id;
        this.subject = subject;
        this.teacherId = teacherId;
        this.code = code;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        if (expiresAt == null) return true;
        return System.currentTimeMillis() > expiresAt.getTime();
    }

    public long getRemainingSeconds() {
        if (expiresAt == null) return 0;
        long diff = expiresAt.getTime() - System.currentTimeMillis();
        return diff > 0 ? (diff / 1000) : 0;
    }
}
