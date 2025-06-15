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
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection 
{
    private static final String URL = "jdbc:derby:db/ticketdb;create=true";
    private static Connection conn;

    public static Connection get() throws SQLException 
    {
        if (conn == null || conn.isClosed()) 
        {
            
            conn = DriverManager.getConnection(URL);
        }
        return conn;
    }
}