/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.progassignmentpart3;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author lab_services_student
 */
public class ProgAssignmentPart3 {

 private static final String STORED_MESSAGES_FILE = "storedMessages.json";
    private static final Gson gson = new Gson();

    public static void saveStoredMessagesToFile(List<Message> storedMessages) {
        try (Writer writer = new FileWriter(STORED_MESSAGES_FILE)) {
            gson.toJson(storedMessages, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving stored messages: " + e.getMessage());
        }
    }

    public static List<Message> loadStoredMessagesFromFile() {
        try (Reader reader = new FileReader(STORED_MESSAGES_FILE)) {
            Type listType = new TypeToken<List<Message>>(){}.getType();
            List<Message> messages = gson.fromJson(reader, listType);
            return messages != null ? messages : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        String correctUsername = "nate_";
        String correctPassword = "Nathan1!";

        String loginUsername = JOptionPane.showInputDialog("Enter username:");
        String loginPassword = JOptionPane.showInputDialog("Enter password:");

        if (!LoginClass.verifyUsernameandPassword(loginUsername, loginPassword, correctUsername, correctPassword)) {
            JOptionPane.showMessageDialog(null, "Login failed. Exiting.");
            System.exit(0);
        }

        String cellphoneNumber;
        do {
            cellphoneNumber = JOptionPane.showInputDialog("Enter your cellphone number:");
        } while (cellphoneNumber == null || !LoginClass.checkCellphoneNumber(cellphoneNumber));

        int maxMessages = 0;
        while (maxMessages <= 0) {
            try {
                String input = JOptionPane.showInputDialog("Enter maximum number of messages you want to send:");
                if (input == null) System.exit(0);
                maxMessages = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid number. Please enter an integer.");
            }
        }

        List<Message> sentMessages = new ArrayList<>();
        List<Message> disregardedMessages = new ArrayList<>();
        List<Message> storedMessages = loadStoredMessagesFromFile();

        Random random = new Random();

        boolean exit = false;
        while (!exit) {
            String[] options = {
                "Send Message", "Show Sent Messages", "Show Stored Messages",
                "Display Sender/Recipient", "Display Longest Message",
                "Search by Message ID", "Search by Recipient",
                "Delete by Hash", "Show Report", "Quit"
            };
            int choice = JOptionPane.showOptionDialog(null, "Choose an option:", "Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0: //Send message
                    if (sentMessages.size() < maxMessages) {
                        String newMessage = JOptionPane.showInputDialog("Enter message (max 250 chars):");
                        if (newMessage == null || newMessage.length() > 250 || newMessage.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Invalid message.");
                            break;
                        }

                        String[] actionOptions = {"Send", "Disregard", "Store"};
                        int action = JOptionPane.showOptionDialog(null, "Choose action:", "Message Action",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, actionOptions, actionOptions[0]);

                        //id for messages
                        String id = String.format("%010d", random.nextInt(1_000_000_000));

                        if (action == 0) { //Send
                            Message msg = new Message(id, newMessage, cellphoneNumber);
                            sentMessages.add(msg);
                            JOptionPane.showMessageDialog(null, "Message sent!\nID: " + id + "\nHash: " + msg.createMessageHash());

                        } else if (action == 1) { // Disregard
                            Message msg = new Message(id, newMessage, cellphoneNumber);
                            disregardedMessages.add(msg);
                            JOptionPane.showMessageDialog(null, "Message disregarded.");

                        } else if (action == 2) { // Store
                            Message msg = new Message(id, newMessage, cellphoneNumber);
                            storedMessages.add(msg);
                            saveStoredMessagesToFile(storedMessages);
                            JOptionPane.showMessageDialog(null, "Message stored.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Message limit reached.");
                    }
                    break;

                case 1: 
                    if (sentMessages.isEmpty()) JOptionPane.showMessageDialog(null, "No sent messages.");
                    else JOptionPane.showMessageDialog(null, Message.printMessages(sentMessages));
                    break;

                case 2: //Show wstored
                    if (storedMessages.isEmpty()) JOptionPane.showMessageDialog(null, "No stored messages.");
                    else {
                        StringBuilder sb = new StringBuilder();
                        for (Message m : storedMessages) {
                            sb.append(m.getContent()).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, sb.toString());
                    }
                    break;

                case 3: 
                    if (sentMessages.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No sent messages.");
                    } else {
                        StringBuilder info = new StringBuilder();
                        for (Message m : sentMessages) {
                            info.append("Sender: You\nRecipient: ").append(m.getRecipient()).append("\n\n");
                        }
                        JOptionPane.showMessageDialog(null, info.toString());
                    }
                    break;

                case 4: //longest
                    if (sentMessages.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No messages.");
                    } else {
                        Message longest = sentMessages.stream()
                                .max(Comparator.comparingInt(m -> m.getContent().length()))
                                .orElse(null);
                        JOptionPane.showMessageDialog(null, longest != null ? longest.getContent() : "No messages.");
                    }
                    break;

                case 5: //Search 
                    String searchId = JOptionPane.showInputDialog("Enter message ID:");
                    if (searchId == null || searchId.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Invalid ID.");
                        break;
                    }
                    Message foundId = sentMessages.stream().filter(m -> m.getId().equals(searchId)).findFirst().orElse(null);
                    if (foundId != null)
                        JOptionPane.showMessageDialog(null, "Recipient: " + foundId.getRecipient() + "\nMessage: " + foundId.getContent());
                    else JOptionPane.showMessageDialog(null, "Not found.");
                    break;

                case 6: //recipients
                    String searchRec = JOptionPane.showInputDialog("Enter recipient:");
                    if (searchRec == null || searchRec.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Invalid recipient.");
                        break;
                    }
                    StringBuilder found = new StringBuilder();
                    for (Message m : sentMessages) {
                        if (m.getRecipient().equals(searchRec)) {
                            found.append(m.getContent()).append("\n");
                        }
                    }
                    JOptionPane.showMessageDialog(null, found.length() > 0 ? found.toString() : "No messages.");
                    break;

                case 7: //delete
                    String delHash = JOptionPane.showInputDialog("Enter hash:");
                    if (delHash == null || delHash.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Invalid hash.");
                        break;
                    }
                    Iterator<Message> iterator = sentMessages.iterator();
                    boolean deleted = false;
                    while (iterator.hasNext()) {
                        Message m = iterator.next();
                        if (m.createMessageHash().equals(delHash)) {
                            iterator.remove();
                            deleted = true;
                            JOptionPane.showMessageDialog(null, "Deleted.");
                            break;
                        }
                    }
                    if (!deleted) JOptionPane.showMessageDialog(null, "Hash not found.");
                    break;

                case 8: //Show report
                    if (sentMessages.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No messages.");
                    } else {
                        StringBuilder report = new StringBuilder("Report of Sent Messages:\n\n");
                        for (Message m : sentMessages) {
                            report.append("ID: ").append(m.getId())
                                    .append("\nRecipient: ").append(m.getRecipient())
                                    .append("\nContent: ").append(m.getContent())
                                    .append("\nHash: ").append(m.createMessageHash())
                                    .append("\n\n");
                        }
                        JOptionPane.showMessageDialog(null, report.toString());
                    }
                    break;

                case 9: //Quit
                default:
                    exit = true;
                    JOptionPane.showMessageDialog(null, "Goodbye!");
                    break;
            }
        }
    }
}
