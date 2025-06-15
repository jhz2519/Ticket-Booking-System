/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ticketbookingsystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author love
 */


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TicketDAOTest {
    
    private EventDAO eventDao;
    private TicketDAO ticketDao;
    private final int userId = 1;  // the seeded admin user
    private int eventId;

    @BeforeAll
    public void setUpClass() throws SQLException {
        // initialise the schema and DAOs
        DBInit.init();
        eventDao  = new EventDAO();
        ticketDao = new TicketDAO();

        // creating a test event
        eventDao.add("Test Event", LocalDate.of(2025, 1, 1), "Testville");
        List<EventDAO.Event> events = eventDao.getAll();
        EventDAO.Event e = events.get(0);
        eventId = e.id;
    }

    
     // test booking a ticket and then  retrieving it for the user
     
    @Test
    public void testBookAndForUser() throws SQLException {
        ticketDao.book(userId, eventId);
        List<TicketDAO.Ticket> tickets = ticketDao.forUser(userId);
        assertFalse(tickets.isEmpty(), "User should have at least one ticket");
        assertEquals("Test Event", tickets.get(0).eventName);
    }

    
    // test updating a ticket that exists to a new event
     
    @Test
    public void testUpdate() throws SQLException {
        List<TicketDAO.Ticket> tickets = ticketDao.forUser(userId);
        int ticketId = tickets.get(0).id;

        // creating a second event
        eventDao.add("Second Event", LocalDate.of(2025, 2, 2), "Hall");
        List<EventDAO.Event> events = eventDao.getAll();
        EventDAO.Event newEvent = events.stream()
            .filter(ev -> ev.name.equals("Second Event"))
            .findFirst()
            .orElseThrow();
        
        ticketDao.update(ticketId, newEvent.id);
        
        List<TicketDAO.Ticket> updated = ticketDao.forUser(userId);
        assertEquals("Second Event", updated.get(0).eventName,
            "ticket should reference the new updated event :)");
    }

    
    // testing cancelling a ticket removes it from the users bookings list
    
    @Test
    public void testCancel() throws SQLException {
        List<TicketDAO.Ticket> tickets = ticketDao.forUser(userId);
        int ticketId = tickets.get(0).id;
        
        ticketDao.cancel(ticketId);
        
        List<TicketDAO.Ticket> afterCancel = ticketDao.forUser(userId);
        assertTrue(afterCancel.isEmpty(),
            "After cancellation, user should have no tickets!");
    }
}
