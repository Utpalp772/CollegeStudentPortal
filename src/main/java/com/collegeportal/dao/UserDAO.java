package com.collegeportal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.collegeportal.model.User;
import com.collegeportal.util.DBConnectionUtil;

public class UserDAO {

    // Check whether username or email already exists
    public boolean usernameOrEmailExists(String username, String email) throws SQLException {

        String sql = "SELECT user_id FROM users WHERE username = ? OR email = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Register a new user
    public void registerUser(User user) throws SQLException {

        String sql = "INSERT INTO users (username, email, password_hash, role) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole());

            ps.executeUpdate();
        }
    }

    // Find user by username
    public User findByUsername(String username) throws SQLException {

        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User user = new User();

                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setRole(rs.getString("role"));

                    return user;
                }

                return null;
            }
        }
    }

    // Update remember-me token
    public void updateRememberToken(int userId, String token) throws SQLException {

        String sql = "UPDATE users SET remember_token = ? WHERE user_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, token);
            ps.setInt(2, userId);

            ps.executeUpdate();
        }
    }

    // Find user by remember-me token
    public User findByRememberToken(String token) throws SQLException {

        String sql = "SELECT * FROM users WHERE remember_token = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, token);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User user = new User();

                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setRole(rs.getString("role"));

                    return user;
                }

                return null;
            }
        }
    }

    // Clear remember-me token
    public void clearRememberToken(int userId) throws SQLException {

        String sql = "UPDATE users SET remember_token = NULL WHERE user_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ps.executeUpdate();
        }
    }
}