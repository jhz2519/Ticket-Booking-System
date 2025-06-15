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

public class Login 
{
    public static person login(String filename) 
    {
        Scanner scan = new Scanner(System.in);
        
        System.out.print("Please enter email: ");
        String email = scan.nextLine();
        
        System.out.print("Please enter password: ");
        String password = scan.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) //users.txt contains login data
        {
            String line;
            
            //reading file one line at a time until end 
            while ((line = reader.readLine()) != null) 
            {
                String[] data = line.split(",");
                //splitting it by commas for credentials, email,pass,role.
                if (data.length == 3) 
                {
                    String storedEmail = data[0];
                    String storedPassword = data[1];
                    String role = data[2];
                    
                    //format = email,pass,role for user data

                    if (storedEmail.equals(email) && storedPassword.equals(password)) 
                    {
                        if (role.equalsIgnoreCase("admin")) 
                        {
                            return new admin("Admin", email);
                        } else 
                        {
                            return new user("User", email); 
                        }
                    }
                }
            }
            System.out.println("Oops. Password or Email is invalid :(");
        } catch (IOException e) 
        {
            System.out.println("Error reading the file.");
        }

        return null;
    }
}

