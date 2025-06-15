/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ticketbookingsystem;

/**
 *
 * @author love
 */

import java.io.*;
import java.util.*;

public class user extends person 
{

    public user(String name, String email) 
    {
        super(name, email);
    }

    @Override
    public void showMenu() 
    {
        Scanner scanner = new Scanner(System.in);
        
        while (true) 
        {
            System.out.println("\nUser Menu:");
            System.out.println("1) View Events");
            System.out.println("2) Create a Booking");
            System.out.println("3) View My Bookings");
            System.out.println("4) Update Booking"); //new function
            System.out.println("5) Cancel Booking");
            System.out.println("6) Exit");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) 
            {
                case "1":
                    viewEvents();
                    break;
                case "2":
                    bookTicket(scanner);
                    break;
                case "3":
                    viewMyBookings();
                    break;
                case "4":
                    updateBooking(scanner);
                    break;
                case "5":
                    cancelBooking(scanner);
                    break;
                case "6":
                    System.out.println("Logging out..");
                    return;
                default:
                    System.out.println("Invalid option. Please try again :)");
            }
        }
    }

    
    private void viewEvents() 
    {
        try (BufferedReader reader = new BufferedReader(new FileReader("events.txt"))) 
        {
            String line;
            int count = 1;
            
            System.out.println("\n---={ Available Events }=---");
            
            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split(",");
                if (parts.length == 4) 
                {
                    System.out.println(count + ") Name: " + parts[0] + " | Location: " + parts[1] + "| Date: " + parts[2] + "| Capacity: " + parts[3]);
                    count++;
                }
            }
        } catch (IOException e) 
        {
            System.out.println("error accesing the file :(");
        }
    }

    
    private void bookTicket(Scanner scanner) 
    {
        List<String> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("events.txt"))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                events.add(line);
            }
        } catch (IOException e) 
        {
            System.out.println("error acessing file.");
            return;
        }

        if (events.isEmpty()) 
        {
            System.out.println("No events available.");
            return;
        }

        System.out.println("\n---={ Choose an Event to Book }=---");
        
        for (int i = 0; i < events.size(); i++) 
        {
            String[] splitting = events.get(i).split(",");
            System.out.println((i + 1) + ") " + splitting[0] + " at " + splitting[1] + " on " + splitting[2]);
        }

        System.out.print("Please Enter event number: ");
        try 
        {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
           
            if (choice >= 0 && choice < events.size()) 
            {
                String eventLine = events.get(choice);
                saveTicket(eventLine);
                
                System.out.println("\n\nTicket booked successfully!");
            } else 
            {
                System.out.println("Invalid event number");
            }
        } catch (NumberFormatException e) 
        {
            System.out.println("Invalid input.");
        }
    }

    private void saveTicket(String eventData) 
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tickets.txt", true))) {
            
            writer.write(eventData + "," + this.email);
            writer.newLine();
        } catch (IOException e) 
        {
            System.out.println("Error writing the ticket");
        }
    }

    private void viewMyBookings() 
    {
        try (BufferedReader reader = new BufferedReader(new FileReader("tickets.txt"))) 
        {
            String line;
            int count = 1;
            
            System.out.println("\n---={ Your Bookings }=---");
            
            while ((line = reader.readLine()) != null) 
            {
                if (line.endsWith("," + this.email)) 
                {
                    String[] parts = line.split(",");
                    System.out.println(count + ") " + parts[0] + " at " + parts[1] + " on " + parts[2]);
                    count++;
                }
            }
                if (count == 1) {
                           System.out.println("You have no bookings. Would you like to make a booking?");
                       }
                   } catch (IOException e) {
                       System.out.println("Error reading your bookings.");
                   }
               }
    
    //new function: update booking
    private void updateBooking(Scanner scanner) {
    List<String> allTickets = new ArrayList<>();
    List<String> userTickets = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader("tickets.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            allTickets.add(line);
            if (line.endsWith("," + this.email)) {
                userTickets.add(line);
            }
        }
    } catch (IOException e) {
        System.out.println("There was an error when reading the ticket data.");
        return;
    }

    if (userTickets.isEmpty()) {
        System.out.println("You currently have no bookings to update.");
        return;
    }

    System.out.println("\n---={ Your Current Bookings }=---");
    for (int i = 0; i < userTickets.size(); i++) {
        String[] parts = userTickets.get(i).split(",");
        System.out.println((i + 1) + ") " + parts[0] + " at " + parts[1] + " on " + parts[2]);
    }

    System.out.print("Enter the number of the booking you'd like to update: ");
    int choice;
    try {
        choice = Integer.parseInt(scanner.nextLine()) - 1;
    } catch (NumberFormatException e) {
        System.out.println("This input is invalid.");
        return;
    }

    if (choice < 0 || choice >= userTickets.size()) {
        System.out.println("This booking number is invalid.");
        return;
    }

    String oldBooking = userTickets.get(choice);
    allTickets.remove(oldBooking);

    List<String> events = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader("events.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            events.add(line);
        }
    } catch (IOException e) {
        System.out.println("There was an error when reading the events.");
        return;
    }

    if (events.isEmpty()) {
        System.out.println("There are no events available.");
        return;
    }

    System.out.println("\n---={ Choose New Event }=---");
    for (int i = 0; i < events.size(); i++) {
        String[] parts = events.get(i).split(",");
        System.out.println((i + 1) + ") " + parts[0] + " at " + parts[1] + " on " + parts[2]);
    }

    System.out.print("Enter new event number: ");
    int newChoice;
    try {
        newChoice = Integer.parseInt(scanner.nextLine()) - 1;
    } catch (NumberFormatException e) {
        System.out.println("This input is invalid.");
        return;
    }

    if (newChoice < 0 || newChoice >= events.size()) {
        System.out.println("This event number is invalid.");
        return;
    }

    String newEvent = events.get(newChoice);
    String[] parts = newEvent.split(",");
    if (parts.length < 3) {
        System.out.println("This event data is corrupt.");
        return;
    }

    String newBooking = parts[0] + "," + parts[1] + "," + parts[2] + "," + this.email;
    allTickets.add(newBooking);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("tickets.txt"))) {
        for (String line : allTickets) {
            writer.write(line);
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("There was an error when writing the updated ticket.");
        return;
    }

    System.out.println("Your booking has been updated successfully.");
}

    
    private void cancelBooking(Scanner scanner) 
    {
        List<String> lines = new ArrayList<>();
        List<String> userBookings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("tickets.txt"))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                lines.add(line);
                if (line.endsWith("," + this.email)) 
                {
                    userBookings.add(line);
                }
            }
        } catch (IOException e) 
        {
            System.out.println("error: cant read ticket.");
            return;
        }

        if (userBookings.isEmpty()) 
        {
            System.out.println("You have no bookings to cancel. \nWould you like to make a booking?");
            return;
        }

        System.out.println("\n---={ Your Bookings }=---");
        for (int i = 0; i < userBookings.size(); i++) 
        {
            String[] cut = userBookings.get(i).split(",");
            System.out.println((i + 1) + ") " + cut[0] + " at " + cut[1] + " on " + cut[2]);
        }

        System.out.print("Enter booking number to cancel: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            
            if (choice >= 0 && choice < userBookings.size()) 
            {
                String bookingToRemove = userBookings.get(choice);
                lines.remove(bookingToRemove);

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("tickets.txt"))) 
                {
                    for (String line : lines) 
                    {
                        writer.write(line);
                        writer.newLine();
                    }
                }

                System.out.println("\n\nBooking cancelled successfully!");
            } else {
                System.out.println("Invalid booking number.");
            }
        } catch (NumberFormatException e) 
        {
            System.out.println("Input is not valid :(");
        } catch (IOException e) 
        {
            System.out.println("Error ocured while updating tickets.");
        }
    }
    
}