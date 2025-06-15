/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ticketbookingsystem;

/**
 *
 * @author kylal
 */

public class event {
    private String name;
    private String location;
    private String date;
    private int seatsAvailable;
    
    public event(String name, String location, String date, int seatsAvailable) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.seatsAvailable = seatsAvailable;
    }
    
    public String getName() {
        return name;
    }
    
    public String getLocation() {
        return location;
    }
    
    public String getDate() {
        return date;
    }
    
    public int seatsAvailable() {
        return seatsAvailable;
    }
    
    public void bookSeat(int count) {
    if (count <= seatsAvailable) {
        seatsAvailable -= count;
    } else {
        throw new IllegalArgumentException("Not enough seats available.");
    }
}

    public void cancelSeat() {
        seatsAvailable++;
    }
    
}
