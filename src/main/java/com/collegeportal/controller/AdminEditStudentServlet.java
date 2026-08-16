package com.collegeportal.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.collegeportal.dao.StudentDAO;
import com.collegeportal.model.Student;
import com.collegeportal.util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/editStudent")
public class AdminEditStudentServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) return false;
        return "ADMIN".equals(session.getAttribute("role"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admins only.");
            return;
        }

        try {
            int studentId = Integer.parseInt(req.getParameter("id"));
            Student student = studentDAO.findById(studentId);

            if (student == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found.");
                return;
            }

            req.setAttribute("student", student);
            req.setAttribute("departments", Constants.DEPARTMENTS);
            req.getRequestDispatcher("/adminEditStudent.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid student ID.");
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "DB error: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admins only.");
            return;
        }

        try {
            int studentId = Integer.parseInt(req.getParameter("studentId"));

            Student s = new Student();
            s.setStudentId(studentId);
            s.setRollNumber(emptyToNull(req.getParameter("rollNumber")));
            s.setDepartment(req.getParameter("department"));
            s.setCourse(req.getParameter("course"));

            String semesterStr = req.getParameter("semester");
            s.setSemester(semesterStr == null || semesterStr.isBlank() ? null : Integer.parseInt(semesterStr));

            s.setPhone(req.getParameter("phone"));
            s.setAddress(req.getParameter("address"));

            String dobStr = req.getParameter("dob");
            s.setDob(dobStr == null || dobStr.isBlank() ? null : java.sql.Date.valueOf(dobStr));

            studentDAO.adminUpdateStudent(s);

            resp.sendRedirect(req.getContextPath() + "/admin/students");

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "DB error: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}