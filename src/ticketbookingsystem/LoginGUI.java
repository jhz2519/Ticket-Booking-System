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

//Contains Main Method - run this class

public class LoginGUI extends JFrame {
    
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    //runs when user clicks login button
    private class LoginButtonListener implements ActionListener {
        
        public void actionPerformed(ActionEvent event) {
            
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                
                JOptionPane.showMessageDialog(LoginGUI.this,
                        "Please enter both email and password.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                
                return;
                
            }

            //checks if email and login matches a user from users.txt
            Object loggedInUser = getUserFromLogin(email, password);

            if (loggedInUser instanceof admin) {
                
                AdminGUI adminGUI = new AdminGUI((admin) loggedInUser);
                adminGUI.setVisible(true);
                dispose(); //closes login window
            } else if (loggedInUser instanceof user) {
                
                booking bookingSystem = new booking();
                UserGUI userGui = new UserGUI((user) loggedInUser, bookingSystem);
                userGui.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginGUI.this,
                        "Email or password is invalid. Please try again.",
                        "Login Failed.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    public LoginGUI() {
        
        setTitle("Login");

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameWidth = screenWidth / 2;
        int frameHeight = screenHeight / 2;

        this.setSize(frameWidth, frameHeight);
        
        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;
        setLocation(x, y);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 5, 5));

        add(new JLabel("Please enter your email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Please enter your password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener()); 
        add(loginButton);

        setVisible(true);
        
    }

   
    private Object getUserFromLogin(String email, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String fileEmail = parts[0];
                    String filePassword = parts[1];
                    String nameOrRole = parts[2];

                    if (fileEmail.equalsIgnoreCase(email) && filePassword.equals(password)) {
                        if (nameOrRole.equalsIgnoreCase("admin")) {
                            return new admin("Admin", fileEmail);
                        } else {
                            return new user(nameOrRole, fileEmail);
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "There was an error when reading the users file.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            
        }
        return null;
    }

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new LoginGUI());
    }

}
