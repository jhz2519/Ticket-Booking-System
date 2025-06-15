/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ticketbookingsystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author love
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DBInit 
{
    public static void init() 
    
    {
        try (Connection c = DBConnection.get();
             Statement s = c.createStatement()) 
        {

            // creating the user table
            try 
            {
                s.executeUpdate("""
                  CREATE TABLE users (
                    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    username VARCHAR(50) UNIQUE,
                    password VARCHAR(50),
                    role     VARCHAR(20)
                  )""");
            } catch (SQLException e) 
            {
                if (!"X0Y32".equals(e.getSQLState())) 
                {
                    e.printStackTrace();
                }
            }

            // always have a default admin login
            try {
                s.executeUpdate("""
                  INSERT INTO users(username,password,role)
                    VALUES ('admin','adminpass','admin')
                  """);
            } catch (SQLException e) 
            {
                
                if (!"23505".equals(e.getSQLState())) 
                {
                    e.printStackTrace();
                }
            }

            // creating the events table in db
            try 
            {
                s.executeUpdate("""
                  CREATE TABLE events (
                    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    name VARCHAR(100),
                    date DATE,
                    location VARCHAR(100)
                  )""");
            } catch (SQLException e) 
            {
                if (!"X0Y32".equals(e.getSQLState())) {
                    e.printStackTrace();
                }
            }

            // creating ticket table
            try {
                s.executeUpdate("""
                  CREATE TABLE tickets (
                    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    user_id INT,
                    event_id INT,
                    FOREIGN KEY (user_id)  REFERENCES users(id),
                    FOREIGN KEY (event_id) REFERENCES events(id)
                  )""");
            } catch (SQLException e) 
            {
                if (!"X0Y32".equals(e.getSQLState())) 
                {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}
