package com.collegeportal.filter;

import java.io.IOException;
import java.sql.SQLException;

import com.collegeportal.dao.UserDAO;
import com.collegeportal.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final String[] PUBLIC_PATHS = {
        "/login", "/login.jsp", "/register", "/register.jsp", "/resources"
    };

    private final UserDAO userDAO = new UserDAO();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        boolean isPublic = false;
        for (String p : PUBLIC_PATHS) {
            if (path.startsWith(p)) { isPublic = true; break; }
        }

        HttpSession session = req.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("userId") != null);

        if (!loggedIn && !isPublic) {
            loggedIn = tryAutoLoginFromCookie(req, resp);
        }

        if (isPublic || loggedIn) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
    }

    private boolean tryAutoLoginFromCookie(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return false;

        String token = null;
        for (Cookie c : cookies) {
            if ("rememberToken".equals(c.getName())) {
                token = c.getValue();
                break;
            }
        }
        if (token == null) return false;

        try {
            User user = userDAO.findByRememberToken(token);
            if (user == null) {
                Cookie expired = new Cookie("rememberToken", "");
                expired.setPath("/");
                expired.setMaxAge(0);
                resp.addCookie(expired); // stale token — stop the browser resending it
                return false;
            }

            HttpSession newSession = req.getSession(true);
            newSession.setAttribute("userId", user.getUserId());
            newSession.setAttribute("username", user.getUsername());
            newSession.setAttribute("role", user.getRole());
            return true;

        } catch (SQLException e) {
            return false;
        }
    }
}