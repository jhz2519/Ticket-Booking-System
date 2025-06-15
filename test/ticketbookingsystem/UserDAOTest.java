/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ticketbookingsystem;

/**
 *
 * @author love
 */

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;

public class UserDAOTest {
    
    private static UserDAO dao;
    
    @BeforeAll
    public static void setUpClass() {
        // initialise the derbyDB and DAO
        DBInit.init();
        dao = new UserDAO();
    }

    //Test that the default seeded admin can log in.
    @Test
    public void testAdminLoginSeeded() throws SQLException 
    {
        person adminObj = dao.getUser("admin", "adminpass");
        assertNotNull(adminObj, "Seeded admin should log in");
        assertTrue(adminObj instanceof admin, "Should return an admin object");
    }

   
     // Testing that creating a new user and then logging in works
     
    @Test
    public void testCreateUserAndLogin() throws SQLException {
        String username = "junit@example.com";
        String password = "junit123";
        dao.createUser(username, password);

        person p = dao.getUser(username, password);
        
        assertNotNull(p, "New user should log in");
        assertTrue(p instanceof user, "Should return a user object");
    }

    
     //Test that invalid credentials return null
     
    @Test
    public void testInvalidLoginReturnsNull() throws SQLException {
        person nobody = dao.getUser("noone", "nopass");
        assertNull(nobody, "Bad credentials must return null");
    }
}
