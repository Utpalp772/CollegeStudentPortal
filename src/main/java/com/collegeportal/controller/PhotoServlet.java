package com.collegeportal.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import com.collegeportal.dao.StudentDAO;
import com.collegeportal.model.Student;
import com.collegeportal.util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/photo")
public class PhotoServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            int studentId = Integer.parseInt(req.getParameter("studentId"));
            Student student = studentDAO.findById(studentId);

            if (student == null || student.getProfilePhoto() == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Path filePath = Path.of(Constants.UPLOAD_DIR, student.getProfilePhoto());
            if (!Files.exists(filePath)) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String mimeType = Files.probeContentType(filePath);
            resp.setContentType(mimeType != null ? mimeType : "application/octet-stream");
            Files.copy(filePath, resp.getOutputStream());

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}