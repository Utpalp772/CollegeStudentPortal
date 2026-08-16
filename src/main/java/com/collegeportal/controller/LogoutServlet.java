package com.collegeportal.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.collegeportal.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null) {
            Object userIdObj = session.getAttribute("userId");
            if (userIdObj != null) {
                try {
                    userDAO.clearRememberToken((int) userIdObj);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            session.invalidate();
        }

        Cookie cookie = new Cookie("rememberToken", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);

        resp.sendRedirect("login.jsp");
    }
}