package mysmartlibrary;

import mysmartlibrary.Member_1;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import mysmartlibrary.DataBase;

public class MemberDAO {

    // استرجاع جميع الأعضاء
    public List<Member_1> getAllMembers() throws SQLException {
        List<Member_1> list = new ArrayList<>();
        String sql = "SELECT id, name, contact FROM Members";
        try (Connection conn = DataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Member_1(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact")
                ));
            }
        }
        return list;
    }

    // إضافة عضو جديد
    public void insertMember(String name, String contact) throws SQLException {
        if (isNameExists(name)) throw new SQLException("Member name already exists.");
        String sql = "INSERT INTO Members (name, contact) VALUES (?, ?)";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, contact);
            stmt.executeUpdate();
        }
    }

    // تعديل عضو
    public void updateMember(int id, String name, String contact) throws SQLException {
        if (isNameExistsForOtherId(name, id)) throw new SQLException("Another member with same name exists.");
        String sql = "UPDATE Members SET name = ?, contact = ? WHERE id = ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, contact);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    // حذف عضو
    public void deleteMember(int id) throws SQLException {
        String sql = "DELETE FROM Members WHERE id = ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // التحقق من الاسم المكرر عند الإضافة
    public boolean isNameExists(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Members WHERE name = ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public boolean isNameExistsForOtherId(String name, int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Members WHERE name = ? AND id <> ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, id);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}
