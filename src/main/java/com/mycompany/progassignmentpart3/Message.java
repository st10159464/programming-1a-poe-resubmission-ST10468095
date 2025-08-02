/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progassignmentpart3;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.swing.JOptionPane;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Message {

    private static final String STORED_MESSAGES_FILE = "storedMessages.json";
    private static final Gson gson = new Gson();

    private final String id;
    private final String content;
    private final String recipient;

    public Message(String id, String content, String recipient) {
        this.id = id;
        this.content = content;
        this.recipient = recipient;
    }

    
    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getRecipient() {
        return recipient;
    }

    // Check message id length 
    public boolean checkMessageID() {
        return id != null && id.length() <= 10;
    }

    // Check cellphonlength
    public int checkRecipientCell() {
        if (recipient == null) return -1;
        if (recipient.length() <= 10 && recipient.startsWith("07")) {
            return 1;  // valid
        } else {
            return 0;  // invalid
        }
    }

    // Create message hash
    public String createMessageHash() {
        if (content == null || id == null) return "";
        String[] words = content.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : "";
        return id + ":" + (firstWord + lastWord).toUpperCase() + ":" + content.length();
    }

    // Print messages from a list
    public static String printMessages(List<Message> messages) {
        if (messages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message msg : messages) {
            sb.append("ID: ").append(msg.getId())
              .append("\nRecipient: ").append(msg.getRecipient())
              .append("\nContent: ").append(msg.getContent())
              .append("\nHash: ").append(msg.createMessageHash())
              .append("\n\n");
        }
        return sb.toString();
    }

    // Save messages in JSON file
    public static void storeMessagesToJson(List<Message> messages) {
        try (Writer writer = new FileWriter(STORED_MESSAGES_FILE)) {
            gson.toJson(messages, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving messages: " + e.getMessage());
        }
    }

    // Load stored messages from JSON file
    public static List<Message> loadMessagesFromJson() {
        try (Reader reader = new FileReader(STORED_MESSAGES_FILE)) {
            Type listType = new TypeToken<List<Message>>(){}.getType();
            List<Message> messages = gson.fromJson(reader, listType);
            if (messages != null) {
                return messages;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading messages: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
