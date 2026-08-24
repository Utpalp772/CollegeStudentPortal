package com.collegeportal.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.collegeportal.dao.StudentDAO;
import com.collegeportal.model.Student;
import com.collegeportal.util.FileStorageUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/uploadPhoto")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024, maxRequestSize = 3 * 1024 * 1024)
public class UploadPhotoServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try {
            Student student = studentDAO.findByUserId(userId);
            Part filePart = req.getPart("photo");

            String newFilename = FileStorageUtil.savePhoto(filePart);

            FileStorageUtil.deletePhoto(student.getProfilePhoto());

            studentDAO.updateProfilePhoto(userId, newFilename);

            resp.sendRedirect(req.getContextPath() + "/profile");

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("student", studentDAO_safeFind(userId));
            req.getRequestDispatcher("/editProfile.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "DB error: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    private Student studentDAO_safeFind(int userId) {
        try {
            return studentDAO.findByUserId(userId);
        } catch (SQLException e) {
            return null;
        }
    }
}