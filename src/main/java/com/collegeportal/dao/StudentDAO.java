package com.collegeportal.dao;

import com.collegeportal.model.Student;
import com.collegeportal.util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StudentDAO {

    // Number of students displayed per page.
    private static final int PAGE_SIZE = 10;

    // Allowed columns for sorting.
    // This prevents SQL injection through the sortBy parameter.
    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "first_name",
        "roll_number",
        "department",
        "semester"
    );

    // ---------------------------------------------------------
    // CREATE STUDENT PROFILE
    // ---------------------------------------------------------

    // Called right after a new user registers.
    // Creates a basic student profile with user_id, first_name, and last_name.
    public void createStudentProfile(int userId, String firstName, String lastName)
            throws SQLException {

        String sql = "INSERT INTO students (user_id, first_name, last_name) "
                   + "VALUES (?, ?, ?)";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, firstName);
            ps.setString(3, lastName);

            ps.executeUpdate();
        }
    }

    // ---------------------------------------------------------
    // FIND STUDENT BY USER ID
    // ---------------------------------------------------------

    // Finds a student profile using the user's ID.
    public Student findByUserId(int userId) throws SQLException {

        String sql = "SELECT * FROM students WHERE user_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapRow(rs);
                }

                return null;
            }
        }
    }

    // ---------------------------------------------------------
    // FIND STUDENT BY STUDENT ID
    // ---------------------------------------------------------

    // Finds a student using the student_id.
    // Used by the admin edit/view/delete functionality.
    public Student findById(int studentId) throws SQLException {

        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapRow(rs);
                }

                return null;
            }
        }
    }

    // ---------------------------------------------------------
    // COUNT ALL STUDENTS
    // ---------------------------------------------------------

    // Counts all students.
    // Used to calculate the total number of pages.
    public int countAll() throws SQLException {

        String sql = "SELECT COUNT(*) FROM students";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();

            return rs.getInt(1);
        }
    }

    // ---------------------------------------------------------
    // COUNT SEARCH RESULTS
    // ---------------------------------------------------------

    // Counts students matching the search keyword.
    // Used to calculate the total number of search-result pages.
    public int countSearch(String keyword) throws SQLException {

        String sql = "SELECT COUNT(*) FROM students WHERE "
                   + "first_name LIKE ? OR last_name LIKE ? OR "
                   + "roll_number LIKE ? OR department LIKE ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";

            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);

            try (ResultSet rs = ps.executeQuery()) {

                rs.next();

                return rs.getInt(1);
            }
        }
    }

    // ---------------------------------------------------------
    // FIND ALL STUDENTS WITH SORTING + PAGINATION
    // ---------------------------------------------------------

    // Finds students for a specific page with sorting.
    public List<Student> findAll(String sortBy, String sortDir, int page)
            throws SQLException {

        List<Student> students = new ArrayList<>();

        // Prevent invalid or null page numbers.
        if (page < 1) {
            page = 1;
        }

        // Check for null before calling contains().
        String column = (sortBy != null && ALLOWED_SORT_COLUMNS.contains(sortBy))
                ? sortBy
                : "student_id";

        String direction = "desc".equalsIgnoreCase(sortDir)
                ? "DESC"
                : "ASC";

        int offset = (page - 1) * PAGE_SIZE;

        String sql = "SELECT * FROM students ORDER BY "
                   + column + " " + direction
                   + " LIMIT ? OFFSET ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, PAGE_SIZE);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }
        }

        return students;
    }

    // ---------------------------------------------------------
    // SEARCH STUDENTS WITH SORTING + PAGINATION
    // ---------------------------------------------------------

    // Searches students by first name, last name, roll number,
    // or department, with sorting and pagination.
    public List<Student> search(
            String keyword,
            String sortBy,
            String sortDir,
            int page) throws SQLException {

        List<Student> students = new ArrayList<>();

        // Prevent invalid or null page numbers.
        if (page < 1) {
            page = 1;
        }

        // Check for null before calling contains().
        String column = (sortBy != null && ALLOWED_SORT_COLUMNS.contains(sortBy))
                ? sortBy
                : "student_id";

        String direction = "desc".equalsIgnoreCase(sortDir)
                ? "DESC"
                : "ASC";

        int offset = (page - 1) * PAGE_SIZE;

        String sql = "SELECT * FROM students WHERE "
                   + "first_name LIKE ? OR last_name LIKE ? OR "
                   + "roll_number LIKE ? OR department LIKE ? "
                   + "ORDER BY " + column + " " + direction
                   + " LIMIT ? OFFSET ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";

            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);

            ps.setInt(5, PAGE_SIZE);
            ps.setInt(6, offset);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    students.add(mapRow(rs));
                }
            }
        }

        return students;
    }

    // ---------------------------------------------------------
    // STUDENT PROFILE UPDATE
    // ---------------------------------------------------------

    // Updates the student's profile information.
    // Roll number is not updated here.
    public void updateStudent(Student s) throws SQLException {

        String sql = "UPDATE students SET department = ?, course = ?, "
                   + "semester = ?, phone = ?, address = ?, dob = ? "
                   + "WHERE user_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getDepartment());
            ps.setString(2, s.getCourse());

            // Semester can be NULL.
            if (s.getSemester() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, s.getSemester());
            }

            ps.setString(4, s.getPhone());
            ps.setString(5, s.getAddress());
            ps.setDate(6, s.getDob());
            ps.setInt(7, s.getUserId());

            ps.executeUpdate();
        }
    }

    // ---------------------------------------------------------
    // ADMIN UPDATE STUDENT
    // ---------------------------------------------------------

    // Admin version of updateStudent().
    // Admin can also update the roll number.
    public void adminUpdateStudent(Student s) throws SQLException {

        String sql = "UPDATE students SET roll_number = ?, department = ?, "
                   + "course = ?, semester = ?, phone = ?, address = ?, dob = ? "
                   + "WHERE student_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getRollNumber());
            ps.setString(2, s.getDepartment());
            ps.setString(3, s.getCourse());

            // Semester can be NULL.
            if (s.getSemester() == null) {
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, s.getSemester());
            }

            ps.setString(5, s.getPhone());
            ps.setString(6, s.getAddress());
            ps.setDate(7, s.getDob());

            ps.setInt(8, s.getStudentId());

            ps.executeUpdate();
        }
    }

    // ---------------------------------------------------------
    // DELETE STUDENT
    // ---------------------------------------------------------

    // Deletes a student using student_id.
    public void deleteStudent(int studentId) throws SQLException {

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            ps.executeUpdate();
        }
    }

    // ---------------------------------------------------------
    // MAP DATABASE ROW TO STUDENT OBJECT
    // ---------------------------------------------------------

    // Converts a database row into a Student object.
    private Student mapRow(ResultSet rs) throws SQLException {

        Student s = new Student();

        s.setStudentId(rs.getInt("student_id"));
        s.setUserId(rs.getInt("user_id"));
        s.setRollNumber(rs.getString("roll_number"));
        s.setFirstName(rs.getString("first_name"));
        s.setLastName(rs.getString("last_name"));
        s.setDob(rs.getDate("dob"));
        s.setPhone(rs.getString("phone"));
        s.setAddress(rs.getString("address"));
        s.setCourse(rs.getString("course"));
        s.setDepartment(rs.getString("department"));

        // Handle nullable semester.
        int semester = rs.getInt("semester");

        if (rs.wasNull()) {
            s.setSemester(null);
        } else {
            s.setSemester(semester);
        }

        s.setProfilePhoto(rs.getString("profile_photo"));
        s.setUpdatedAt(rs.getTimestamp("updated_at"));

        return s;
    }
}