package utility;

import java.sql.*;
import java.util.ArrayList;
import model.User;

/**
 * Handles all JDBC connectivity and the CRUD operations against the
 * MySQL "customerdb" database (table: student).
 *
 * Connector/J auto-registers its driver through the JAR on the classpath
 * (lib/mysql-connector-j-9.1.0.jar), so no Class.forName(...) call is needed.
 *
 * The student photo is stored as the raw image bytes in a LONGBLOB column, so
 * the picture lives inside the database row itself.
 */
public class DatabaseConnector {

    // DB_URL = "jdbc:<database>://ip_address:port/database_name";
    // useSSL=false & allowPublicKeyRetrieval=true avoid the common MySQL 8/9
    // handshake errors; serverTimezone keeps DATE values consistent.
    static final String DB_URL =
            "jdbc:mysql://localhost:3306/customerdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String DB_USERNAME = "root";
    static final String DB_PASSWORD = "my-secret-pw";

    /** Converts a java.util.Date (from the calendar) to a java.sql.Date, or null. */
    private static java.sql.Date toSqlDate(java.util.Date d) {
        return (d == null) ? null : new java.sql.Date(d.getTime());
    }

    // ----- C : Create -------------------------------------------------------
    public static void addUser(User u1) throws SQLException {
        String query = "INSERT INTO student"
                + "(first_name, last_name, gender, dob, age, phone, email, college, experience, hobbies, photo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, u1.getFirstName());
            stmt.setString(2, u1.getLastName());
            stmt.setString(3, u1.getGender());
            stmt.setDate(4, toSqlDate(u1.getDob()));
            stmt.setInt(5, u1.getAge());
            stmt.setString(6, u1.getPhone());
            stmt.setString(7, u1.getEmail());
            stmt.setString(8, u1.getCollege());
            stmt.setString(9, u1.getExperience());
            stmt.setString(10, u1.getHobby());
            if (u1.getPhoto() != null) {
                stmt.setBytes(11, u1.getPhoto());
            } else {
                stmt.setNull(11, Types.LONGVARBINARY);
            }

            int rows = stmt.executeUpdate();
            System.out.println("Rows inserted: " + rows);
        } catch (SQLException sqle) {
            System.out.println("SQL Exception Occured.");
            System.out.println(sqle);
            throw sqle;
        }
    }

    // ----- R : Read ---------------------------------------------------------
    public static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM student ORDER BY student_id";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User u1 = new User();
                u1.setUserId(rs.getInt("student_id"));
                u1.setFirstName(rs.getString("first_name"));
                u1.setLastName(rs.getString("last_name"));
                u1.setGender(rs.getString("gender"));
                u1.setDob(rs.getDate("dob"));
                u1.setAge(rs.getInt("age"));
                u1.setPhone(rs.getString("phone"));
                u1.setEmail(rs.getString("email"));
                u1.setCollege(rs.getString("college"));
                u1.setExperience(rs.getString("experience"));
                u1.setHobby(rs.getString("hobbies"));
                u1.setPhoto(rs.getBytes("photo"));
                users.add(u1);
            }
        } catch (SQLException sqle) {
            System.out.println("SQL Exception Occured.");
            System.out.println(sqle);
        }
        return users;
    }

    // ----- U : Update -------------------------------------------------------
    public static void updateUser(User oldUser, User updatedUser) {
        String query = "UPDATE student SET "
                + "first_name = ?, last_name = ?, gender = ?, dob = ?, age = ?, "
                + "phone = ?, email = ?, college = ?, experience = ?, hobbies = ?, photo = ? "
                + "WHERE student_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, updatedUser.getFirstName());
            stmt.setString(2, updatedUser.getLastName());
            stmt.setString(3, updatedUser.getGender());
            stmt.setDate(4, toSqlDate(updatedUser.getDob()));
            stmt.setInt(5, updatedUser.getAge());
            stmt.setString(6, updatedUser.getPhone());
            stmt.setString(7, updatedUser.getEmail());
            stmt.setString(8, updatedUser.getCollege());
            stmt.setString(9, updatedUser.getExperience());
            stmt.setString(10, updatedUser.getHobby());
            if (updatedUser.getPhoto() != null) {
                stmt.setBytes(11, updatedUser.getPhoto());
            } else {
                stmt.setNull(11, Types.LONGVARBINARY);
            }
            stmt.setInt(12, oldUser.getUserId());

            int rows = stmt.executeUpdate();
            System.out.println("Rows updated: " + rows);
        } catch (SQLException sqle) {
            System.out.println("SQL Exception Occured.");
            System.out.println(sqle);
        }
    }

    // ----- D : Delete -------------------------------------------------------
    public static void deleteUser(User u1) {
        String query = "DELETE FROM student WHERE student_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, u1.getUserId());
            int rows = stmt.executeUpdate();
            System.out.println("Rows deleted: " + rows);
        } catch (SQLException sqle) {
            System.out.println("SQL Exception Occured.");
            System.out.println(sqle);
        }
    }
}
