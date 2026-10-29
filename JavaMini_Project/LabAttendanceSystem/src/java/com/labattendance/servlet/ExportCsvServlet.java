package com.labattendance.servlet;

import com.labattendance.dao.AttendanceDAO;
import com.labattendance.dao.SessionDAO;
import com.labattendance.model.AttendanceRecord;
import com.labattendance.model.LabSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ExportCsvServlet", urlPatterns = {"/export-csv"})
public class ExportCsvServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SessionDAO sessionDAO;
    private AttendanceDAO attendanceDAO;

    @Override
    public void init() throws ServletException {
        sessionDAO = new SessionDAO();
        attendanceDAO = new AttendanceDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sessionIdStr = request.getParameter("sessionId");
        if (sessionIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sessionId parameter");
            return;
        }

        try {
            int sessionId = Integer.parseInt(sessionIdStr.trim());
            LabSession labSession = sessionDAO.findById(sessionId);
            if (labSession == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Session not found");
                return;
            }

            List<AttendanceRecord> records = attendanceDAO.getAttendanceBySession(sessionId);

            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"attendance_" + labSession.getSubject().replaceAll("\\s+", "_") + "_session_" + sessionId + ".csv\"");

            PrintWriter out = response.getWriter();
            out.println("Lab Subject," + escapeCsv(labSession.getSubject()));
            out.println("Session Code," + labSession.getCode());
            out.println("Created At," + labSession.getCreatedAt());
            out.println("Total Students Present," + records.size());
            out.println();
            out.println("Roll / ID,Student Name,Username,Marked At");

            int index = 1;
            for (AttendanceRecord record : records) {
                out.println(index++ + "," +
                    escapeCsv(record.getStudentName()) + "," +
                    escapeCsv(record.getStudentUsername()) + "," +
                    record.getMarkedAt());
            }
            out.flush();
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid sessionId parameter");
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
