package com.collegeportal.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.collegeportal.dao.UserDAO;
import com.collegeportal.model.User;
import com.collegeportal.util.PasswordUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        String error = validate(username, email, password, confirmPassword);
        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        try {
            if (userDAO.usernameOrEmailExists(username, email)) {
                req.setAttribute("error", "Username or email is already taken.");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPasswordHash(PasswordUtil.hash(password));
            user.setRole("STUDENT");
            userDAO.registerUser(user);

            resp.sendRedirect("login.jsp?registered=true");
      

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "DB error: " + e.getMessage());
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }

    private String validate(String username, String email, String password, String confirmPassword) {
        if (username == null || username.isBlank()) return "Username is required.";
        if (email == null || !email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) return "Enter a valid email.";
        if (password == null || password.length() < 8) return "Password must be at least 8 characters.";
        if (!password.equals(confirmPassword)) return "Passwords do not match.";
        return null;
    }
}