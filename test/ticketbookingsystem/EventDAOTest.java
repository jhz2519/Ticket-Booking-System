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
public class EventDAOTest {
    
    private EventDAO dao;

    @BeforeAll
    public void setUpClass() {
        // initialise schema and DAO
        DBInit.init();
        dao = new EventDAO();
    }

    
     // testing tthat getAll() returns an empty list immediately after a fresh init.

    @Test
    public void testGetAllEmptyInitially() throws SQLException {
        // Assuming a fresh DBInit, there should be no events yet
        List<EventDAO.Event> all = dao.getAll();
        assertNotNull(all, "getAll() should never return null");
        assertTrue(all.isEmpty(), "No events should exist after fresh init");
    }

    
     // testing that adding an event and then retrieving it via getAll() works.
     
    @Test
    public void testAddAndRetrieveEvent() throws SQLException {
        String name = "JUnit Conference";
        LocalDate date = LocalDate.of(2025, 7, 1);
        String location = "Testville";

        // adding the event
        dao.add(name, date, location);

        // retrieve all events and verify ours is present
        List<EventDAO.Event> all = dao.getAll();
        assertTrue(
            all.stream().anyMatch(e ->
                e.id == e.id && 
                e.name.equals(name) &&
                e.date.equals(date) &&
                e.location.equals(location)
            ),
            "Added event should be returned by getAll()"
        );
    }
}
