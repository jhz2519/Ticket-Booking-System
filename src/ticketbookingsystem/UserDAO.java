/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ticketbookingsystem;

/**
 *
 * @author love
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * data acccess objectr for user auth and registration
 */
public class UserDAO 
{
    //queries derby db and returns admin or user object 
    
    /*
    username = login name
    password = login password
    */
    public person getUser(String username, String password) throws SQLException 
    {
        String sql = "SELECT username, role FROM users WHERE username = ? AND password = ?";
        
        try (Connection c = DBConnection.get();
             PreparedStatement st = c.prepareStatement(sql)) 
        {
            st.setString(1, username);
            st.setString(2, password);
            try (ResultSet rs = st.executeQuery()) 
            {
                if (!rs.next()) 
                {
                    return null;
                }
                
                String role = rs.getString("role");
                
                if ("admin".equalsIgnoreCase(role)) 
                {
                    return new admin("Admin", username);
                } else {
                    return new user("User", username);
                }
            }
        }
    }


    // creates a new user in the db with role "user"
   
    public void createUser(String username, String password) throws SQLException 
    {
        String sql = "INSERT INTO users(username, password, role) VALUES (?, ?, 'user')";
        
        try (Connection c = DBConnection.get();
             PreparedStatement st = c.prepareStatement(sql)) 
        {
            st.setString(1, username);
            st.setString(2, password);
            st.executeUpdate();
        }
    }
}
