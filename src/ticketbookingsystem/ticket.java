/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ticketbookingsystem;

/**
 *
 * @author kylal
 */

public class ticket {
    private String userName;
    private String eventName;
    private String date;
    private int ticketCount; //new
    private String userEmail; //new

    public ticket(String userName, String eventName, String date, int ticketCount, String userEmail) {
        this.userName = userName;
        this.eventName = eventName;
        this.date = date;
        this.ticketCount = ticketCount; //new
        this.userEmail = userEmail; //new
    }

    @Override
    public String toString() {
        return userName + "," + eventName + "," + date + "," + ticketCount + "," + userEmail;
    }
}

