package mysmartlibrary;


import java.lang.reflect.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import mysmartlibrary.DataBase;
import mysmartlibrary.Member_1;
/**
 *
 * @author HP
 */
public class BorrowingDAO {

    
    public void insertBorrowing(Borrowing_1 b) throws SQLException {
        //write the sql statment
        String sql = "INSERT INTO borrowing (book_id, member_id, borrow_date) VALUES (?, ?, ?)";
        //initialize a connection to databadse
        try (Connection conn = DataBase.getConnection();
         //initializing the sql statment and to read the auto increment key ...id..
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         //applying the updates to data base after getting it from getters..
            stmt.setInt(1, b.getBookId());
            stmt.setInt(2, b.getMemberId());
            stmt.setDate(3, Date.valueOf(b.getBorrowDate()));
            stmt.executeUpdate();
           //to get the val of the key cus its auto increment
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    b.setId(keys.getInt(1));
                }
            }
        }
    }

    // تعديل استعارة
    public void updateBorrowing(Borrowing_1 b) throws SQLException {
         //write the sql statment
        String sql = "UPDATE borrowing SET book_id = ?, member_id = ?, borrow_date = ? WHERE id = ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             //applying the updates to data base after getting it from getters..
            stmt.setInt(1, b.getBookId());
            stmt.setInt(2, b.getMemberId());
            stmt.setDate(3, Date.valueOf(b.getBorrowDate()));
            stmt.setInt(4, b.getId());
            stmt.executeUpdate();
        }
    }

    // حذف استعارة
    public void deleteBorrowing(int id) throws SQLException {
        String sql = "DELETE FROM borrowing WHERE id = ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // جلب جميع الاستعارات
    public List<Borrowing_1> getAllBorrowings() throws SQLException {
        List<Borrowing_1> list = new ArrayList<>();
        String sql = "SELECT b.id, b.book_id, b.member_id, b.borrow_date, " +
                     "bk.title AS bookTitle, m.name AS memberName " +
                     "FROM borrowing b " +
                     "JOIN books bk ON b.book_id = bk.id " +
                     "JOIN members m ON b.member_id = m.id";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
//while there is a next row in the result set make a borrowing obj and store values in it to put it in the list to return the list when the loop is stoped
            while (rs.next()) {
                Borrowing_1 br = new Borrowing_1(
                    rs.getInt("id"),
                    rs.getInt("book_id"),
                    rs.getInt("member_id"),
                    rs.getString("borrow_date"),
                    "", 
                    rs.getString("memberName"),
                    rs.getString("bookTitle")
                );
                list.add(br);
            }
        }
        return list;
    }

    // البحث
    public List<Borrowing_1> searchBorrowings(String keyword) throws SQLException {
        List<Borrowing_1> list = new ArrayList<>();
        String sql = "SELECT b.id, b.book_id, b.member_id, b.borrow_date, " +
                     "bk.title AS bookTitle, m.name AS memberName " +
                     "FROM borrowing b " +
                     "JOIN books bk ON b.book_id = bk.id " +
                     "JOIN members m ON b.member_id = m.id " +
                     "WHERE LOWER(bk.title) LIKE ? OR LOWER(m.name) LIKE ? OR CAST(b.borrow_date AS CHAR) LIKE ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String pattern = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Borrowing_1 br = new Borrowing_1(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getString("borrow_date"),
                        "",
                        rs.getString("memberName"),
                        rs.getString("bookTitle")
                    );
                    list.add(br);
                }
            }
        }
        return list;
    }

}
