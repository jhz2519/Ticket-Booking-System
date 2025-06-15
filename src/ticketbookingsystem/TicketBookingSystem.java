/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ticketbookingsystem;

/**
 *
 * @author love
 */

import java.util.Scanner;

public class TicketBookingSystem 
{

    public static void main(String[] args) 
    {
        System.out.println("--={ Welcome to Ticket Booking System }=--\n\n");
        
        //+ login functionality - lovelyn
        person loggedInUser = Login.login("users.txt");
        
        if (loggedInUser != null) 
        {
            System.out.println("\nLogin successful!! Welcome <3 " + loggedInUser.name);
            loggedInUser.showMenu();
        } else 
        {
            System.out.println("Now exiting Ticket Booking System..."); 
        }
    }
}
