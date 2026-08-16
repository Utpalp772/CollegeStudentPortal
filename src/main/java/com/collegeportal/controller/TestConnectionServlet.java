package com.collegeportal.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import com.collegeportal.util.DBConnectionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/test-db")
public class TestConnectionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        try (Connection conn = DBConnectionUtil.getConnection()) {
            out.println("Connected to: " + conn.getCatalog());
        } catch (Exception e) {
            out.println("Connection failed: " + e.getMessage());
        }
    }
}