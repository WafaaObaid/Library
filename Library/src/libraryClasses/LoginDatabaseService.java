
package mysmartlibrary;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class LoginDatabaseService {

    public List<User> getAllUsers() throws SQLException{
        List<User> userList = new ArrayList();
        String sql = "SELECT id, first_name, last_name, email,password_hash From Users";
        try(Connection conn = DataBase.getConnection();
                PreparedStatement stat = conn.prepareStatement(sql);
                ResultSet rs = stat.executeQuery()){
            while(rs.next()){
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password_hash")
                        
                );
                userList.add(user);
                
            }
            
        }
        return userList;
        
    }
    
    public boolean isEmailExist(String email) throws SQLException{
        String query = "SELECT count(*) From Users Where email = ?";
        try(Connection conn = DataBase.getConnection();
              PreparedStatement stat = conn.prepareStatement(query)){
            stat.setString(1, email);
            try(ResultSet rs = stat.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1) > 0;                
                }
                
            }
            
        }
        return false;
    }
    
    public void saveUser(User newUser) throws SQLException{
        String query = "INSERT INTO Users (first_name, last_name, email, password_hash) VALUES (?, ?, ?, ?)";
      try (Connection conn = DataBase.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, newUser.getFirstName());
        stmt.setString(2, newUser.getLastName());
        stmt.setString(3, newUser.getEmail());
        stmt.setString(4, newUser.getPasswordHash());
        stmt.executeUpdate(); 
    }
        
    }
    
    
    
}

