/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ticketbookingsystem;

/**
 *
 * @author kylagallegos
 */

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserGUI extends JFrame {

    private user user;
    private booking bookingSystem;
    private JTextArea displayArea;

    public UserGUI(user user, booking booking) {
        this.user = user;
        this.bookingSystem = booking;

        setTitle("Booking System :)");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 2, screenSize.height / 2);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 7, 5, 5));
        
        JButton viewEvents = new JButton("View Events");
        JButton bookTicket = new JButton("Book Ticket");
        JButton viewBookings = new JButton("View My BookingS");
        JButton updateBooking = new JButton("Update My Booking");
        JButton cancelBooking = new JButton("Cancel My Booking");
        JButton refreshEvents = new JButton("Refresh Events");
        JButton logout = new JButton("Logout");

        buttonsPanel.add(viewEvents);
        buttonsPanel.add(bookTicket);
        buttonsPanel.add(viewBookings);
        buttonsPanel.add(updateBooking);
        buttonsPanel.add(cancelBooking);
        buttonsPanel.add(refreshEvents);
        buttonsPanel.add(logout);

        panel.add(buttonsPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        panel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        add(panel);

        viewEvents.addActionListener(e -> showEvents());
        bookTicket.addActionListener(e -> bookTicket());
        viewBookings.addActionListener(e -> viewMyBookings());
        updateBooking.addActionListener(e -> updateBooking());
        cancelBooking.addActionListener(e -> cancelBooking());
        refreshEvents.addActionListener(e -> showEvents());
        logout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logging out... Bye!");
            System.exit(0);
        });
    }

    private void showEvents() {
        displayArea.setText("Available Events: \n\n");
        File eventFile = new File("events.txt");

        if (!eventFile.exists()) {
            displayArea.append("There was no events file found.\n");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(eventFile))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    count++;
                    displayArea.append(String.format("%d) %s in %s on %s (Seats Available: %s)\n",
                            count, parts[0], parts[1], parts[2], parts[3]));
                }
            }
            if (count == 0) {
                displayArea.append("There are no events available.\n");
            }
        } catch (IOException e) {
            displayArea.append("There was an error loading the events. " + e.getMessage() + "\n");
        }
    }

    
    private void bookTicket() {
    JTextField eventNameField = new JTextField();
    JTextField eventLocationField = new JTextField();
    JTextField userEmailField = new JTextField();
    JTextField ticketCountField = new JTextField();

    Object[] message = {
        "Event Name:", eventNameField,
        "Event Location:", eventLocationField,
        "Your Email:", userEmailField,
        "Number of Tickets:", ticketCountField
    };

    int option = JOptionPane.showConfirmDialog(null, message, "Book Ticket", JOptionPane.OK_CANCEL_OPTION);
    if (option != JOptionPane.OK_OPTION) return;

    String eventName = eventNameField.getText();
    String eventLocation = eventLocationField.getText();
    String userEmail = userEmailField.getText();
    int ticketsToBook;

    //makes sure input for tickets is an integer
    try {
        ticketsToBook = Integer.parseInt(ticketCountField.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Please enter a valid number of tickets.");
        return;
    }

    int eventIndex = -1;
    List<event> events = bookingSystem.getevents();
    for (int i = 0; i < events.size(); i++) {
        event ev = events.get(i);
        if (ev.getName().equalsIgnoreCase(eventName) && //ignores capitilisation
            ev.getLocation().equalsIgnoreCase(eventLocation)) {
            eventIndex = i;
            break;
        }
    }

    if (eventIndex == -1) {
        JOptionPane.showMessageDialog(null, "Event not found.");
        return;
    }

    event chosenEvent = events.get(eventIndex);
    int seatsAvailable = chosenEvent.seatsAvailable();
    if (ticketsToBook > seatsAvailable) {
        JOptionPane.showMessageDialog(null, "There is currently not enough seats available.");
        return;
    }
    
    bookingSystem.bookTicket(new user(user.name, userEmail), eventIndex, ticketsToBook);

    JOptionPane.showMessageDialog(null, "Your tickets have been booked :) ");
}



    private void viewMyBookings() {
        displayArea.setText("Your Bookings: \n\n");
        File ticketFile = new File("tickets.txt");

        if (!ticketFile.exists()) {
            displayArea.append("No booking file found.\n");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ticketFile))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].equalsIgnoreCase(user.email)) {
                    count++;
                    displayArea.append(String.format("%d)%s on %s (%s tickets)\n",
                            count,parts[1], parts[2], parts[3]));
                }
            }
            if (count == 0) {
                displayArea.append("You currently have no bookings.\n");
            }
        } catch (IOException e) {
            displayArea.append("There was an error when reading your bookings: " + e.getMessage() + "\n");
        }
    }

    
    private void updateBooking() {
        File ticketFile = new File("tickets.txt");
        if (!ticketFile.exists()) {
            JOptionPane.showMessageDialog(this, "There are currently no bookings to update.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ticketFile))) {
            
            //creates empty list to stores lines read from file
            List<String> allLines = new ArrayList<>();
            DefaultListModel<String> bookingList = new DefaultListModel<>();
            String line;

            while ((line = reader.readLine()) != null) {
                allLines.add(line);
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].equalsIgnoreCase(user.email)) {
                    bookingList.addElement(String.format("%s on %s (%s tickets)",
                            parts[1], parts[2], parts[3]));
                }
            }

            if (bookingList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "There are currently no bookings to update.");
                return;
            }

            //shows user their bookings; allows them to select one to update 
            JList<String> bookingJList = new JList<>(bookingList);
            bookingJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            int result = JOptionPane.showConfirmDialog(this, new JScrollPane(bookingJList), "Select a booking to update:", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION || bookingJList.getSelectedIndex() == -1) return;

            int selectedIndex = bookingJList.getSelectedIndex();
            String newTicketCountStr = JOptionPane.showInputDialog(this, "Please enter new ticket count:");
            if (newTicketCountStr == null) return;

            //converts to integer
            int newTicketCount;
            try {
                newTicketCount = Integer.parseInt(newTicketCountStr);
                if (newTicketCount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "This number is invalid.");
                return;
            }

            //opens tickets.txt - will overwrite it 
            try (PrintWriter writer = new PrintWriter("tickets.txt")) {
                int userBookingIndex = -1;
                for (String originalLine : allLines) {
                    String[] parts = originalLine.split(",");
                    if (parts.length < 5) {
                        writer.println(originalLine);
                        continue;
                    }

                    //checking if the email matches a user
                    if (parts[4].equalsIgnoreCase(user.email)) {
                        userBookingIndex++;
                        //replace ticket count with updated number
                        if (userBookingIndex == selectedIndex) {
                            parts[3] = String.valueOf(newTicketCount);
                            writer.println(String.join(",", parts));
                        } else {
                            writer.println(originalLine);
                        }
                    } else {
                        writer.println(originalLine);
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Your booking has been updated :) ");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "There was an error when updating your booking: " + e.getMessage());
        }
    }

    
    private void cancelBooking() {
        File ticketFile = new File("tickets.txt");
        if (!ticketFile.exists()) {
            JOptionPane.showMessageDialog(this, "There are currently no bookings to cancel.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ticketFile))) {
            List<String> allLines = new ArrayList<>();
            DefaultListModel<String> bookingList = new DefaultListModel<>();
            String line;

            while ((line = reader.readLine()) != null) {
                allLines.add(line);
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].equalsIgnoreCase(user.email)) {
                    bookingList.addElement(String.format("%s in %s on %s (%s tickets)",
                            parts[0], parts[1], parts[2], parts[3]));
                }
            }

            if (bookingList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "There are currently no bookings to cancel.");
                return;
            }

            //lets user select one booking from the list
            JList<String> bookingJList = new JList<>(bookingList);
            bookingJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            int result = JOptionPane.showConfirmDialog(this, new JScrollPane(bookingJList), "Select Booking to Cancel", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION || bookingJList.getSelectedIndex() == -1) return;

            //stores selected booking
            int selectedIndex = bookingJList.getSelectedIndex();
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure that you want to cancel this booking?", "To confirm, press cancel", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            //opens tickets.txt file - will overwrite it
            try (PrintWriter writer = new PrintWriter("tickets.txt")) {
                int userBookingIndex = -1;
                for (String originalLine : allLines) {
                    String[] parts = originalLine.split(",");
                    if (parts.length < 5) {
                        writer.println(originalLine);
                        continue;
                    }

                    if (parts[4].equalsIgnoreCase(user.email)) {
                        userBookingIndex++;
                        if (userBookingIndex != selectedIndex) {
                            writer.println(originalLine);
                        }
                    } else {
                        writer.println(originalLine);
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Your booking has been cancelled. ");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "There was an error when processing your cancellation: " + e.getMessage());
        }
    }
}