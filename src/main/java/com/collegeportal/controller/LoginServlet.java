package com.collegeportal.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.collegeportal.dao.UserDAO;
import com.collegeportal.model.User;
import com.collegeportal.util.PasswordUtil;
import com.collegeportal.util.TokenUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean rememberMe = "true".equals(req.getParameter("rememberMe"));

        try {
            User user = userDAO.findByUsername(username);

            if (user == null || !PasswordUtil.verify(password, user.getPasswordHash())) {
                req.setAttribute("error", "Invalid username or password.");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            req.changeSessionId();

            if (rememberMe) {
                String token = TokenUtil.generateToken();
                userDAO.updateRememberToken(user.getUserId(), token);

                Cookie rememberCookie = new Cookie("rememberToken", token);
                rememberCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
                rememberCookie.setPath("/");
                rememberCookie.setHttpOnly(true); // JS can't read it
                // rememberCookie.setSecure(true); // turn on once served over HTTPS
                resp.addCookie(rememberCookie);
            }

            resp.sendRedirect("dashboard.jsp");

        } catch (SQLException e) {
            req.setAttribute("error", "Something went wrong. Please try again.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}