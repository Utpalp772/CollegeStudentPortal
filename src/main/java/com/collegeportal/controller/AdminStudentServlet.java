package com.collegeportal.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.collegeportal.dao.StudentDAO;
import com.collegeportal.model.Student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/students")
public class AdminStudentServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get existing session
        HttpSession session = req.getSession(false);

        // Authentication check
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Admin role check
        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            resp.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Admins only."
            );
            return;
        }

        // Read search parameter
        String keyword = req.getParameter("q");

        // Read sorting parameters
        String sortBy = req.getParameter("sortBy");
        String sortDir = req.getParameter("sortDir");

        // Read page parameter
        int page = 1;

        try {
            String pageParam = req.getParameter("page");

            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }

        } catch (NumberFormatException e) {
            // If page is invalid, use page 1
            page = 1;
        }

        // Prevent page from being less than 1
        if (page < 1) {
            page = 1;
        }

        try {

            List<Student> students;

            int totalCount;

            // Check whether user is searching
            boolean searching =
                    keyword != null && !keyword.isBlank();

            if (searching) {

                // Remove extra spaces from search keyword
                String searchKeyword = keyword.trim();

                // Get students for current page
                students = studentDAO.search(
                    searchKeyword,
                    sortBy,
                    sortDir,
                    page
                );

                // Get total matching students
                totalCount = studentDAO.countSearch(
                    searchKeyword
                );

            } else {

                // Get students for current page
                students = studentDAO.findAll(
                    sortBy,
                    sortDir,
                    page
                );

                // Get total number of students
                totalCount = studentDAO.countAll();
            }

            // StudentDAO uses 10 students per page.
            int pageSize = 10;

            // Calculate total number of pages
            int totalPages = (int) Math.ceil(
                totalCount / (double) pageSize
            );

            // Always keep at least one page
            if (totalPages < 1) {
                totalPages = 1;
            }

            // If requested page is greater than total pages,
            // use the last available page.
            if (page > totalPages) {
                page = totalPages;

                // Reload students for the corrected page
                if (searching) {

                    students = studentDAO.search(
                        keyword.trim(),
                        sortBy,
                        sortDir,
                        page
                    );

                } else {

                    students = studentDAO.findAll(
                        sortBy,
                        sortDir,
                        page
                    );
                }
            }

            // Send students to JSP
            req.setAttribute(
                "students",
                students
            );

            // Preserve search keyword
            req.setAttribute(
                "keyword",
                keyword
            );

            // Preserve sorting
            req.setAttribute(
                "sortBy",
                sortBy
            );

            req.setAttribute(
                "sortDir",
                sortDir
            );

            // Pagination information
            req.setAttribute(
                "currentPage",
                page
            );

            req.setAttribute(
                "totalPages",
                totalPages
            );

            req.setAttribute(
                "totalCount",
                totalCount
            );

            // Open student list page
            req.getRequestDispatcher(
                "/adminStudents.jsp"
            ).forward(req, resp);

        } catch (SQLException e) {

            e.printStackTrace();

            req.setAttribute(
                "error",
                "DB error: " + e.getMessage()
            );

            req.getRequestDispatcher(
                "/error.jsp"
            ).forward(req, resp);
        }
    }
}