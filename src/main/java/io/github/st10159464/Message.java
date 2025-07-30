package io.github.st10159464;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Message {
    private final String recipientCell;
    private final String messageContent;
    private final String messageHash;

    // Static list to store sent messages
    private static final List<Message> sentMessages = new ArrayList<>();

    public Message(String recipientCell, String messageContent) {
        this.recipientCell = recipientCell;
        this.messageContent = messageContent;
        this.messageHash = createMessageHash();
    }

    // Add message to sentMessages
    public void send() {
        sentMessages.add(this);
    }

    // Ensures message content is not more than 250 characters
    public boolean checkMessageID() {
        return messageContent.length() <= 250;
    }

    // Ensures recipient cell is +27 and 12 characters
    public int checkRecipientCell() {
        return (recipientCell != null && recipientCell.startsWith("+27") && recipientCell.length() == 12) ? 1 : 0;
    }

    // Creates a hash for the message
    private String createMessageHash() {
        // Example: "MSG:" + first 8 chars of hashcode in uppercase hex
        return "MSG:" + Integer.toHexString((recipientCell + messageContent).hashCode()).toUpperCase();
    }

    public String getRecipientCell() {
        return recipientCell;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getMessageHash() {
        return messageHash;
    }

    // Returns all sent messages
    public static List<Message> getSentMessages() {
        return sentMessages;
    }

    // Returns the longest message sent
    public static Message getLongestMessage() {
        Message longest = null;
        for (Message m : sentMessages) {
            if (longest == null || m.messageContent.length() > longest.messageContent.length()) {
                longest = m;
            }
        }
        return longest;
    }

    // Search for a message by hash
    public static Message searchByHash(String hash) {
        for (Message m : sentMessages) {
            if (m.getMessageHash().equals(hash)) {
                return m;
            }
        }
        return null;
    }

    // Search all messages for a recipient
    public static List<Message> searchByRecipient(String recipientCell) {
        List<Message> result = new ArrayList<>();
        for (Message m : sentMessages) {
            if (m.getRecipientCell().equals(recipientCell)) {
                result.add(m);
            }
        }
        return result;
    }

    // Delete a message by hash
    public static boolean deleteByHash(String hash) {
        return sentMessages.removeIf(m -> m.getMessageHash().equals(hash));
    }

    // Display a report of all sent messages
    public static String displayReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total messages sent: ").append(sentMessages.size()).append("\n");
        for (Message m : sentMessages) {
            sb.append("To: ").append(m.getRecipientCell())
              .append(" | Content: ").append(m.getMessageContent())
              .append(" | Hash: ").append(m.getMessageHash()).append("\n");
        }
        return sb.toString();
    }

    // For testing: clear sent messages
    public static void clearMessages() {
        sentMessages.clear();
    }

    public static void showMenu() {
        String[] options = {"Send Message", "Show Recently Sent Messages", "Show Report", "Quit"};
        boolean running = true;
        while (running) {
            int choice = JOptionPane.showOptionDialog(
                null,
                "QuickChat Menu:\nChoose an option:",
                "QuickChat Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );

            switch (choice) {
                case 0: // Send Message
                    String recipient = JOptionPane.showInputDialog("Enter recipient cell (+27#########):");
                    String content = JOptionPane.showInputDialog("Enter your message (max 250 chars):");
                    if (recipient != null && content != null) {
                        Message msg = new Message(recipient, content);
                        msg.send();
                        JOptionPane.showMessageDialog(null, "Message sent!");
                    }
                    break;
                case 1: // Show Recently Sent Messages
                    if (sentMessages.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No messages sent yet.");
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (Message m : sentMessages) {
                            sb.append("To: ").append(m.getRecipientCell())
                              .append(" - ").append(m.getMessageContent()).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, sb.toString(), "Recently Sent Messages", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case 2: // Show Report
                    String report = Message.displayReport();
                    JOptionPane.showMessageDialog(null, report, "Message Report", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 3: // Quit
                case JOptionPane.CLOSED_OPTION:
                    running = false;
                    break;
            }
        }
    }
}