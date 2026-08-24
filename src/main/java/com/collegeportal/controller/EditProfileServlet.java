package com.collegeportal.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.collegeportal.dao.StudentDAO;
import com.collegeportal.model.Student;
import com.collegeportal.util.Constants;
import com.collegeportal.util.FileStorageUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/editProfile")
@MultipartConfig(
    maxFileSize = 2 * 1024 * 1024,
    maxRequestSize = 3 * 1024 * 1024
)
public class EditProfileServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    // ---------------------------------------------------------
    // DISPLAY EDIT PROFILE PAGE
    // ---------------------------------------------------------

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

            req.getRequestDispatcher("/editProfile.jsp")
               .forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();

            req.setAttribute("error", "DB error: " + e.getMessage());

            req.getRequestDispatcher("/error.jsp")
               .forward(req, resp);
        }
    }

    // ---------------------------------------------------------
    // UPDATE PROFILE
    // ---------------------------------------------------------

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

        // -----------------------------------------------------
        // VALIDATION
        // -----------------------------------------------------

        String error = validate(
            department,
            course,
            semesterStr
        );

        if (error != null) {

            req.setAttribute("error", error);
            req.setAttribute("departments", Constants.DEPARTMENTS);

            try {
                Student student = studentDAO.findByUserId(userId);
                req.setAttribute("student", student);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            req.getRequestDispatcher("/editProfile.jsp")
               .forward(req, resp);

            return;
        }

        try {

            // -------------------------------------------------
            // GET EXISTING STUDENT
            // -------------------------------------------------

            Student existing = studentDAO.findByUserId(userId);

            // -------------------------------------------------
            // PROFILE PHOTO HANDLING
            // -------------------------------------------------

            boolean removePhoto =
                    "true".equals(req.getParameter("removePhoto"));

            Part filePart = req.getPart("photo");

            if (removePhoto) {

                FileStorageUtil.deletePhoto(
                    existing.getProfilePhoto()
                );

                studentDAO.clearProfilePhoto(userId);

            } else if (filePart != null && filePart.getSize() > 0) {

                String newFilename =
                        FileStorageUtil.savePhoto(filePart);

                FileStorageUtil.deletePhoto(
                    existing.getProfilePhoto()
                );

                studentDAO.updateProfilePhoto(
                    userId,
                    newFilename
                );
            }

            // -------------------------------------------------
            // UPDATE OTHER PROFILE INFORMATION
            // -------------------------------------------------

            Student s = new Student();

            s.setUserId(userId);
            s.setDepartment(department);
            s.setCourse(course);

            if (semesterStr == null || semesterStr.isBlank()) {
                s.setSemester(null);
            } else {
                s.setSemester(
                    Integer.parseInt(semesterStr)
                );
            }

            s.setPhone(phone);
            s.setAddress(address);

            if (dobStr == null || dobStr.isBlank()) {
                s.setDob(null);
            } else {
                s.setDob(
                    java.sql.Date.valueOf(dobStr)
                );
            }

            studentDAO.updateStudent(s);

            // -------------------------------------------------
            // SUCCESS
            // -------------------------------------------------

            resp.sendRedirect(
                req.getContextPath() + "/dashboard.jsp"
            );

        } catch (IllegalArgumentException e) {

            req.setAttribute("error", e.getMessage());
            req.setAttribute("departments", Constants.DEPARTMENTS);

            try {
                Student student = studentDAO.findByUserId(userId);
                req.setAttribute("student", student);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

            req.getRequestDispatcher("/editProfile.jsp")
               .forward(req, resp);

        } catch (SQLException e) {

            e.printStackTrace();

            req.setAttribute(
                "error",
                "DB error: " + e.getMessage()
            );

            req.getRequestDispatcher("/error.jsp")
               .forward(req, resp);
        }
    }

    // ---------------------------------------------------------
    // VALIDATION
    // ---------------------------------------------------------

    private String validate(
            String department,
            String course,
            String semesterStr) {

        if (department == null || department.isBlank()) {
            return "Department is required.";
        }

        if (course == null || course.isBlank()) {
            return "Course is required.";
        }

        if (semesterStr != null && !semesterStr.isBlank()) {

            try {

                int sem = Integer.parseInt(semesterStr);

                if (sem < 1 || sem > 12) {
                    return "Semester must be between 1 and 12.";
                }

            } catch (NumberFormatException e) {
                return "Semester must be a number.";
            }
        }

        return null;
    }
}