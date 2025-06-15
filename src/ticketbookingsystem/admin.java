/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ticketbookingsystem;

/**
 *
 * @author love
 */

import java.util.*;
import java.io.*;

public class admin extends person 
{
    public admin(String name, String email) 
    {
        super(name, email);
    }

    @Override
    public void showMenu() 
    {
        Scanner scanner = new Scanner(System.in);
        while (true) 
        {
            System.out.println("\nAdmin Menu:");
            System.out.println("1) Create Event");
            System.out.println("2) View All Events");
            System.out.println("3) Delete Event");
            //new function: edit event details - kyla
            System.out.println("4) Edit Event Details");
            System.out.println("5) Logout");
           
            

            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            switch (input) 
            {
                case "1":
                    createEvent(scanner);
                    break;
                case "2":
                    viewEvents();
                    break;
                case "3":
                    deleteEvent(scanner);
                    break;
                case "4":
                    editEvent(scanner); //new function 
                    break;
                case "5":
                    System.out.println("Logging out.");
                    return;
                default:
                    System.out.println("Incorrect choice :( Try again please");
            }
        }
    }

    private void createEvent(Scanner scanner) {
        
        System.out.print("Enter event name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter event location: ");
        String location = scanner.nextLine();
        
        System.out.print("Enter event date (e.g., 2024-01-11): ");
        String date = scanner.nextLine();
        
        System.out.print("Enter capacity: ");
        String capacity = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("events.txt", true))) {
            writer.write(name + "," + location + "," + date + "," + capacity);
            writer.newLine();
            System.out.println("Event has been created successfully!!");
        } catch (IOException e) {
            System.out.println("Error writing to file"); //events.txt
        }
    }

    private void viewEvents() 
    {
        try (BufferedReader reader = new BufferedReader(new FileReader("events.txt"))) 
        {
            String line;
            System.out.println("\n----={ Events List }=---");
            while ((line = reader.readLine()) != null) 
            {
                String[] splitting = line.split(",");
                if (splitting.length == 4) 
                {
                    System.out.println("Name: " + splitting[0] + " | Location: " + splitting[1] + "| Date: " + splitting[2] + "| Capacity: " + splitting[3]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading events file.");
        }
    }

    private void deleteEvent(Scanner scanner) 
    {
        System.out.print("Please Enter the name of event you wish to delete: ");
        String eventName = scanner.nextLine();

        File inputFile = new File("events.txt");
        File tempFile = new File("temp.txt"); //to avoid data loss

        try (
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) 
            {
                if (!line.startsWith(eventName + ",")) 
                {
                    writer.write(line);
                    writer.newLine();
                } else {
                    found = true;
                }
            }

            if (found)
            {
                if (inputFile.delete() && tempFile.renameTo(inputFile)) 
                {
                    System.out.println("\nThe Selected Event has been deleted successfully!");
               } else {
                    System.out.println("failed to modift event file :(");
               }
            } else 
            {
                System.out.println("Event not found");
                tempFile.delete();
            }
        } catch (IOException e) 
        {
            System.out.println("Error in even deletion.");
             //error catching
        }
    }
    
    //new function: edit event
    private void editEvent(Scanner scanner) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("events.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("There was an error when loading the events.");
            return;
        }

        if (lines.isEmpty()) {
            System.out.println("There were no events found.");
            return;
        }

        System.out.println("\n---={ Events }=---");
        for (int i = 0; i < lines.size(); i++) {
            System.out.println((i + 1) + ") " + lines.get(i));
        }

        System.out.print("Enter event number to edit: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index < 0 || index >= lines.size()) {
            System.out.println("This event number is invalid.");
            return;
        }

        System.out.print("New name (press enter if the event name remains unchanged): ");
        String newName = scanner.nextLine();

        System.out.print("New date (press enter if the event date remains unchanged): ");
        String newDate = scanner.nextLine();

        System.out.print("New seat count (press enter if the seat count remains unchanged): ");
        String newSeats = scanner.nextLine();

        String[] parts = lines.get(index).split(",");
        if (!newName.isEmpty()) parts[0] = newName;
        if (!newDate.isEmpty()) parts[2] = newDate;
        if (!newSeats.isEmpty()) parts[3] = newSeats;

        lines.set(index, String.join(",", parts));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("events.txt"))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            
            System.out.println("The event has been updated.");
            
        } catch (IOException e) {
            
            System.out.println("The event failed to update.");
        }
    }
}
