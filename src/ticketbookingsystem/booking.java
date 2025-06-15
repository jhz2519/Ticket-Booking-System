/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ticketbookingsystem;

/**
 *
 * @author kylal
 */

import java.io.*;
import java.util.*;

public class booking {

    private List<event> events = new ArrayList<>();

    public List<event> getevents() {
        return events;
    }

    public booking() {
        loadEventsFromFile();
    }

    public void addEvent(event e) {
        events.add(e);
        saveEventToFile(e);
    }

    public void saveEventToFile(event event) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("events.txt", true))) {
            bw.write(event.getName() + "," + event.getLocation() + "," + event.getDate() + "," + event.seatsAvailable());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("There was an error saving the event.");
        }
    }

    private void loadEventsFromFile() {
        File file = new File("events.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0].trim();
                    String location = parts[1].trim();
                    String date = parts[2].trim();
                    int seats = Integer.parseInt(parts[3].trim());

                    events.add(new event(name, location, date, seats));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("There was an error while loading events.");
        }
    }

    public void listEvents() {
        if (events.isEmpty()) {
            System.out.println("There are no events available.");
            return;
        }
        for (int i = 0; i < events.size(); i++) {
            event e = events.get(i);
            System.out.printf("%d. %s - %s at %s [%d seats]\n", i + 1, e.getName(), e.getDate(), e.getLocation(), e.seatsAvailable());
        }
    }

    
    
    
   public void bookTicket(user user, int eventIndex, int ticketCount) {
    if (eventIndex < 0 || eventIndex >= events.size()) {
        System.out.println("This event number is invalid.");
        return;
    }

    event e = events.get(eventIndex);
    if (e.seatsAvailable() >= ticketCount) {
       e.bookSeat(ticketCount);  
        ticket t = new ticket(user.name, e.getName(), e.getDate(), ticketCount, user.email);
        saveTicketToFile(t);
        saveAllEvents(); // Update the seat count
        System.out.println("The ticket has been booked.");
    } else {
        System.out.println("There are no enough seats available.");
    }
}

    public void saveTicketToFile(ticket ticket) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("tickets.txt", true))) {
            bw.write(ticket.toString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("There was an error while saving the ticket.");
        }
    }

    public void viewAllBookings() {
        File file = new File("tickets.txt");
        if (!file.exists()) {
            System.out.println("No bookings have been made yet.");
            return;
        }

        System.out.println("Booked tickets:");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 1;
            while ((line = br.readLine()) != null) {
                System.out.println(count++ + ". " + line);
            }
        } catch (IOException e) {
            System.out.println("There was an error while reading the tickets file.");
        }
    }

    public void deleteEvent(int index) {
        if (index >= 0 && index < events.size()) {
            events.remove(index);
            saveAllEvents();
            System.out.println("The event has been deleted.");
        } else {
            System.out.println("This event number is invalid.");
        }
    }

    private void saveAllEvents() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("events.txt"))) {
            for (event e : events) {
                bw.write(e.getName() + "," + e.getLocation() + "," + e.getDate() + "," + e.seatsAvailable());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving updated events.");
        }
    }

    public int getEventCount() {
        return events.size();
    }
}
