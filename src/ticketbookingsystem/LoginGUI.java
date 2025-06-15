/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ticketbookingsystem;

/**
 *
 * @author kylagallegos + love
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import ticketbookingsystem.UserDAO;

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

            // validate credentials against the database
            Object loggedInUser;
            try {
                loggedInUser = new UserDAO().getUser(email, password);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(LoginGUI.this,
                        "Database error. See console.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

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
        setLayout(new GridLayout(6, 1, 5, 5)); // adjusted rows to add Register button

        add(new JLabel("Please enter your email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Please enter your password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener()); 
        add(loginButton);

        // *Love added ++ register button for creating an accountfor new users
        JButton registerButton = new JButton("Register");
        
        registerButton.addActionListener(e -> 
        {
            String emailReg = JOptionPane.showInputDialog(LoginGUI.this,
                "Enter new email:");
            if (emailReg == null || emailReg.isBlank()) return;

            String passwordReg = JOptionPane.showInputDialog(LoginGUI.this,
                "Enter new password:");
            if (passwordReg == null || passwordReg.isBlank()) return;

            try 
            {
                new UserDAO().createUser(emailReg, passwordReg);
                JOptionPane.showMessageDialog(LoginGUI.this,
                    "User account is registerd! :)  You can now log in.",
                    "Success!!", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) 
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(LoginGUI.this,
                    "There was an error registering user account :(: " + ex.getMessage(),
                    "Error...", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(registerButton);

        setVisible(true);
        
    }

    public static void main(String[] args) 
    {
    
    //initialise and autocreate db tables before gui 
    DBInit.init();
    
    SwingUtilities.invokeLater(() -> new LoginGUI());
    }

}
