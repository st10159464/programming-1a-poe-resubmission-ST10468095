package io.github.st10159464;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Login {
    private String username;
    private String password;
    private String cellNumber;

    private static final List<Login> users = new ArrayList<>();

    public String getCellNumber() {
        return cellNumber;
    }

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$");
    }

    public boolean checkCellPhoneNumber(String cell) {
        // Accepts +27 followed by 9 digits (South African format)
        return cell != null && cell.matches("^\\+27\\d{9}$");
    }

    public String registerUser(String username, String password, String cell) {
        if (!checkUserName(username)) return "Username is not correctly formatted...";
        if (!checkPasswordComplexity(password)) return "Password is not correctly formatted...";
        if (!checkCellPhoneNumber(cell)) return "Cell phone number incorrectly formatted...";
        this.username = username;
        this.password = password;
        this.cellNumber = cell;
        users.add(this);
        return "User registered successfully.";
    }

    public boolean loginUser(String username, String password) {
        for (Login user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return true;
            }
        }
        return false;
    }

    public String returnLoginStatus(boolean loginStatus) {
        return loginStatus ? "Login successful." : "Login failed.";
    }

    public boolean checkMessageID() {
        // Dummy implementation
        return true;
    }

    public String createMessageHash() {
        // Dummy implementation
        return "HASH:ABCD";
    }

    public static void showMenu() {
        String menu = "QuickChat Menu:\n" +
                      "1. Send Message\n" +
                      "2. View Messages\n" +
                      "3. Logout";
        JOptionPane.showMessageDialog(null, menu);
    }

    public static void main(String[] args) {
    Login login = new Login();

    // Show username requirements
    JOptionPane.showMessageDialog(null, "Username requirements:\n- Must contain an underscore (_)\n- Must be 5 characters or fewer");

    String registrationMessage;
    String username, password, cell;

    // Loop until registration is successful
    do {
        username = JOptionPane.showInputDialog("Enter username:");
        password = JOptionPane.showInputDialog("Enter password (min 8 chars, cap, num, special):");
        cell = JOptionPane.showInputDialog("Enter cell phone number (9 digits, e.g. 812345678):");

        // Automatically add +27 if not present
        if (cell != null && !cell.startsWith("+27")) {
            cell = "+27" + cell.replaceAll("^0+", ""); // Remove leading zeros if present
        }

        registrationMessage = login.registerUser(username, password, cell);
        JOptionPane.showMessageDialog(null, registrationMessage);
    } while (!registrationMessage.equals("User registered successfully."));

    // Proceed to login after successful registration
    String loginUser = username;
    String loginPass = password;
    boolean status = login.loginUser(loginUser, loginPass);

    if (status) {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat");
        Message.showMenu(); // Show the menu from Message.java
    } else {
        JOptionPane.showMessageDialog(null, "Login failed.");
    }
}
}
