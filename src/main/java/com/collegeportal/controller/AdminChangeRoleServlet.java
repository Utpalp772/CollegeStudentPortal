package com.collegeportal.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.collegeportal.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/changeRole")
public class AdminChangeRoleServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (!"ADMIN".equals(session.getAttribute("role"))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admins only.");
            return;
        }

        try {
            int targetUserId = Integer.parseInt(req.getParameter("userId"));
            String newRole = req.getParameter("newRole");

            if (!"ADMIN".equals(newRole) && !"STUDENT".equals(newRole)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid role.");
                return;
            }

            // Prevent an admin from accidentally demoting their own
            // currently logged-in account and locking themselves out
            // of the admin panel mid-session.
            int currentUserId = (int) session.getAttribute("userId");
            if (targetUserId == currentUserId) {
                resp.sendRedirect(req.getContextPath() + "/admin/students?error=selfRoleChange");
                return;
            }

            userDAO.updateRole(targetUserId, newRole);
            resp.sendRedirect(req.getContextPath() + "/admin/students");

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID.");
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "DB error: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}