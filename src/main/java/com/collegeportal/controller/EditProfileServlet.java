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

@WebServlet("/editProfile")
public class EditProfileServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try {
            Student student = studentDAO.findByUserId(userId);
            req.setAttribute("student", student);
            req.setAttribute("departments", Constants.DEPARTMENTS);
            req.getRequestDispatcher("/editProfile.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "DB error: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        String department = req.getParameter("department");
        String course = req.getParameter("course");
        String semesterStr = req.getParameter("semester");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String dobStr = req.getParameter("dob");

        String error = validate(department, course, semesterStr);
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("departments", Constants.DEPARTMENTS);
            req.getRequestDispatcher("/editProfile.jsp").forward(req, resp);
            return;
        }

        try {
            Student s = new Student();
            s.setUserId(userId);
            s.setDepartment(department);
            s.setCourse(course);
            s.setSemester(semesterStr.isBlank() ? null : Integer.parseInt(semesterStr));
            s.setPhone(phone);
            s.setAddress(address);
            s.setDob(dobStr.isBlank() ? null : java.sql.Date.valueOf(dobStr));

            studentDAO.updateStudent(s);

            resp.sendRedirect(req.getContextPath() + "/profile");

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "DB error: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    private String validate(String department, String course, String semesterStr) {
        if (department == null || department.isBlank()) return "Department is required.";
        if (course == null || course.isBlank()) return "Course is required.";
        if (semesterStr != null && !semesterStr.isBlank()) {
            try {
                int sem = Integer.parseInt(semesterStr);
                if (sem < 1 || sem > 12) return "Semester must be between 1 and 12.";
            } catch (NumberFormatException e) {
                return "Semester must be a number.";
            }
        }
        return null;
    }
}