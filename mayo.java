package finals;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class mayo {

    private static final String URL = "jdbc:mysql://localhost:3306/connection";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void loadStudentsIntoTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        String sql = "SELECT * FROM love";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("year_level"),
                    rs.getString("course"),
                    rs.getString("scholarship")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addStudent(String id, String name, String password, String year, String course, String scholarship) {
        String sql = "INSERT INTO love (student_id, name, password, year_level, course, scholarship) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.setString(4, year);
            pstmt.setString(5, course);
            pstmt.setString(6, scholarship);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /* Fixed updateStudent method */
    public void updateStudentIdAware(String originalId, String newId, String name, String password, String year, String course, String scholarship) {
        String sql = "UPDATE love SET student_id = ?, name = ?, password = ?, year_level = ?, course = ?, scholarship = ? WHERE student_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newId);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.setString(4, year);
            pstmt.setString(5, course);
            pstmt.setString(6, scholarship);
            pstmt.setString(7, originalId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                System.err.println("No records updated, student_id might not exist: " + originalId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public void deleteStudent(String id) {
        String sql = "DELETE FROM love WHERE student_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void searchStudent(String id, JTable table, JTextField tfName, JTextField tfPassword, JTextField tfYear, JTextField tfCourse, JTextField tfScholarship) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        String sql = "SELECT * FROM love WHERE student_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String password = rs.getString("password");
                String year = rs.getString("year_level");
                String course = rs.getString("course");
                String scholarship = rs.getString("scholarship");
                tfName.setText(name);
                tfPassword.setText(password);
                tfYear.setText(year);
                tfCourse.setText(course);
                tfScholarship.setText(scholarship);
                model.addRow(new Object[]{id, name, password, year, course, scholarship});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<String> getAllScholarships() {
        List<String> scholarships = new ArrayList<>();
        String sql = "SELECT DISTINCT scholarship FROM love";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                scholarships.add(rs.getString("scholarship"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return scholarships;
    }

    public Object[][] getStudentsByScholarship(String scholarship) {
        List<Object[]> students = new ArrayList<>();
        String sql = "SELECT * FROM love WHERE scholarship = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, scholarship);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(new Object[]{
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("year_level"),
                    rs.getString("course"),
                    rs.getString("scholarship")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return students.toArray(new Object[students.size()][]);
    }

    public void updateStudent(String id, String name, String password, String year, String course, String scholarship) {
        String sql = "UPDATE love SET name = ?, password = ?, year_level = ?, course = ?, scholarship = ? WHERE student_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.setString(3, year);
            pstmt.setString(4, course);
            pstmt.setString(5, scholarship);
            pstmt.setString(6, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
