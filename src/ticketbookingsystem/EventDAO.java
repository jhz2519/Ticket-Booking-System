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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventDAO 
{
    public static class Event 
    {
        public final int id;
        public final String name;
        public final LocalDate date;
        public final String location;
        
        public Event(int id, String name, LocalDate date, String location) 
        {
            this.id = id; this.name = name; this.date = date; this.location = location;
        }
        @Override public String toString() 
        {
            return id + ") " + name + " at " + location + " on " + date;
        }
    }

    public List<Event> getAll() throws SQLException 
    {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM events";
        
        try (Connection c = DBConnection.get();
             PreparedStatement st = c.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) 
        {
            while (rs.next()) {
                list.add(new Event(
                  rs.getInt("id"),
                  rs.getString("name"),
                  rs.getDate("date").toLocalDate(),
                  rs.getString("location")
                ));
            }
        }
        return list;
    }

    public void add(String name, LocalDate date, String location) throws SQLException 
    {
        String sql = "INSERT INTO events(name,date,location) VALUES (?,?,?)";
        
        try (Connection c = DBConnection.get();
             PreparedStatement st = c.prepareStatement(sql)) 
        {
            st.setString(1, name);
            st.setDate(2, Date.valueOf(date));
            st.setString(3, location);
            st.executeUpdate();
        }
    }
}
