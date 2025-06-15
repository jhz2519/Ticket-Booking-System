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
import java.awt.event.*;
import java.io.*;

public class AdminGUI extends JFrame {

    private admin Admin;
    private DefaultListModel<String> eventListModel;
    private JList<String> eventList;

    public AdminGUI(admin adminUser) {
        this.Admin = adminUser;
        setTitle("Admin Page :)");

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = screenWidth / 2;
        int frameHeight = screenHeight / 2;

        setSize(frameWidth, frameHeight);
        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;
        setLocation(x, y);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        eventListModel = new DefaultListModel<>();
        eventList = new JList<>(eventListModel);
        showEvents();

        JButton createEvent = new JButton("Create Event");
        JButton deleteEvent = new JButton("Delete Event");
        JButton editEvent = new JButton("Edit Event");
        JButton refreshEvents = new JButton("Refresh Events");
        JButton logout = new JButton("Logout");

        createEvent.addActionListener(new CreateEventListener());
        deleteEvent.addActionListener(new DeleteEventListener());
        editEvent.addActionListener(new EditEventListener());
        refreshEvents.addActionListener(new RefreshEventListener());
        logout.addActionListener(new LogoutListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createEvent);
        buttonPanel.add(editEvent);
        buttonPanel.add(deleteEvent);
        buttonPanel.add(refreshEvents);
        buttonPanel.add(logout);

        add(new JScrollPane(eventList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }


    // actionPerformed: runs when specific button is clicked
    private class CreateEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            createEvent();
        }
    }

    private class DeleteEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            deleteEvent();
        }
    }

    private class EditEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            editEvent();
        }
    }

    private class RefreshEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            showEvents();
        }
    }

    private class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            JOptionPane.showMessageDialog(null, "Logging out... Bye!");
        }
    }

    private void showEvents() {
        eventListModel.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("events.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                eventListModel.addElement(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "There was an error when reading the events file.");
        }
    }

    private void createEvent() {
        JTextField nameField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField seatsField = new JTextField();

        //combines labels with input fields above
        Object[] message = {
            "Event Name:", nameField,
            "Location:", locationField,
            "Date (YYYY-MM-DD):", dateField,
            "Seats available:", seatsField
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Create Event", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String location = locationField.getText();
            String date = dateField.getText();
            String seats = seatsField.getText();

            if (name.isEmpty() || location.isEmpty() || date.isEmpty() || seats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please answer all fields.");
                return;
            }

            //won't overwrite existing code
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("events.txt", true))) {
                writer.write(name + "," + location + "," + date + "," + seats);
                writer.newLine(); //line break
                JOptionPane.showMessageDialog(this, "This event has been created.");
                showEvents();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "There was an error when writing to the file.");
            }
        }
    }

    private void deleteEvent() {
        int selectedIndex = eventList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to delete.");
            return;
        }

        String selectedEvent = eventListModel.getElementAt(selectedIndex);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this event?\n" + selectedEvent, "To confirm, press delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            File inputFile = new File("events.txt");
            File tempFile = new File("temp.txt");

            try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.equals(selectedEvent)) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }

            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                JOptionPane.showMessageDialog(this, "This event has been deleted.");
                showEvents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete this event.");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "There was an error while deleting this event.");
        }
    }

    private void editEvent() {
        int selectedIndex = eventList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to edit");
            return;
        }

        String selectedEvent = eventListModel.getElementAt(selectedIndex);
        String[] parts = selectedEvent.split(",");

        JTextField nameField = new JTextField(parts[0]);
        JTextField locationField = new JTextField(parts[1]);
        JTextField dateField = new JTextField(parts[2]);
        JTextField seatsField = new JTextField(parts[3]);

        Object[] message = {
            "Event Name:", nameField,
            "Location:", locationField,
            "Date (YYYY-MM-DD):", dateField,
            "Seats available:", seatsField
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Edit Event", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String newLine = String.join(",", nameField.getText(), locationField.getText(),
                    dateField.getText(), seatsField.getText());

            try {
                File inputFile = new File("events.txt");
                File tempFile = new File("temp.txt");

                try (
                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
                ) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.equals(selectedEvent)) {
                            writer.write(newLine);
                        } else {
                            writer.write(line);
                        }
                        writer.newLine();
                    }
                }

                if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                    JOptionPane.showMessageDialog(this, "This event has been updated.");
                    showEvents();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update this event.");
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "There was an error when editing this event.");
            }
        }
    }
}

