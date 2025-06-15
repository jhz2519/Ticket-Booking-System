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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO 
{
    public static class Ticket 
    {
        public final int id;
        public final int userId;
        public final String eventName;
        public final LocalDate eventDate;
       
        public Ticket(int id, int userId, String eventName, LocalDate eventDate) 
        {
            this.id = id; this.userId = userId;
            this.eventName = eventName; this.eventDate = eventDate;
        }
        @Override public String toString() 
        {
            return id + ") " + eventName + " on " + eventDate;
        }
    }

    public List<Ticket> forUser(int userId) throws SQLException 
    {
        List<Ticket> out = new ArrayList<>();
        String sql = """
          SELECT t.id, e.name, e.date
            FROM tickets t
            JOIN events e ON t.event_id = e.id
           WHERE t.user_id = ?
        """;
        
        
        try (Connection c = DBConnection.get();
             PreparedStatement st = c.prepareStatement(sql)) 
        {
            st.setInt(1, userId);
            try (ResultSet rs = st.executeQuery()) 
            {
                while (rs.next()) 
                {
                    out.add(new Ticket(
                      rs.getInt("id"),
                      userId,
                      rs.getString("name"),
                      rs.getDate("date").toLocalDate()
                    ));
                }
            }
        }
        return out;
    }

    public void book(int userId, int eventId) throws SQLException 
    {
        String sql = "INSERT INTO tickets(user_id,event_id) VALUES (?,?)";
       
        try (Connection c = DBConnection.get();
             PreparedStatement st = c.prepareStatement(sql)) 
        {
            st.setInt(1, userId);
            st.setInt(2, eventId);
            st.executeUpdate();
        }
    }

    public void update(int ticketId, int newEventId) throws SQLException 
    {
        String sql = "UPDATE tickets SET event_id=? WHERE id=?";
       
        try (Connection c = DBConnection.get();
             PreparedStatement st = c.prepareStatement(sql)) 
        {
            st.setInt(1, newEventId);
            st.setInt(2, ticketId);
            st.executeUpdate();
        }
    }

    public void cancel(int ticketId) throws SQLException 
    {
        
        String sql = "DELETE FROM tickets WHERE id=?";
        
        try (Connection c = DBConnection.get();
             PreparedStatement st = c.prepareStatement(sql)) 
        {
            st.setInt(1, ticketId);
            st.executeUpdate();
        }
    }
}