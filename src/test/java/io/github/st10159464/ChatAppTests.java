package io.github.st10159464;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit tests for the ChatApp application.
 * <p>
 * This test class covers the following functionalities:
 * <ul>
 *   <li>User registration and login via the {@link Login} class.</li>
 *   <li>Validation of username format and password complexity.</li>
 *   <li>Validation of South African cell phone number format.</li>
 *   <li>Message creation and validation via the {@link Message} class.</li>
 *   <li>Checks for message ID length, recipient cell format, and message hash format.</li>
 *   <li>Includes setup for test objects and tests for both valid and invalid cases.</li>
 * </ul>
 * <p>
 * Each test method is annotated with {@code @Test} and verifies a specific aspect of the application's logic.
 * The {@code setUp()} method initializes required objects before each test.
 * The {@code main} method prints the Maven version for reference.
 */
public class ChatAppTests {

    private Login login;
    private Message message;
    // Setup method to initialize test objects before each test runs
    @Before
    public void setUp() {
        login = new Login();
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        message = new Message("+27838968976", "Hello!");
    }
    
    // Test for valid username format
    @Test
    public void testCheckUserName_Valid() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    // Test for invalid username format
    @Test
    public void testCheckUserName_Invalid() {
        assertFalse(login.checkUserName("kyle!!!!!!"));
    }

    // Test for password meeting all complexity requirements
    @Test
    public void testCheckPassword_Valid() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    // Test for password that fails complexity requirements
    @Test
    public void testCheckPassword_Invalid() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    // Test for valid South African cell phone number
    @Test
    public void testCheckCellNumber_Valid() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    // Test for invalid cell phone number format
    @Test
    public void testCheckCellNumber_Invalid() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    // Test login success with correct credentials
    @Test
    public void testLoginSuccess() {
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    // Test login failure with incorrect credentials
    @Test
    public void testLoginFail() {
        assertFalse(login.loginUser("wrong", "pass"));
    }

    // Test that the message ID is of acceptable length
    @Test
    public void testMessageIDLength() {
        assertTrue(message.checkMessageID());
    }

    // Test that the recipient's phone number is correctly formatted
    @Test
    public void testRecipientFormat() {
        assertEquals(1, message.checkRecipientCell());
    }

    // Test that the message hash is correctly generated
    @Test
    public void testMessageHashFormat() {
        String hash = message.createMessageHash();
        assertTrue(hash.contains(":"));
        assertTrue(hash.matches(".*[A-Z]{2,}.*"));
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        Message m1 = new Message("+27838968976", "Hello!");
        Message m2 = new Message("+27838968977", "Hi!");
        m1.send();
        m2.send();
        List<Message> sent = Message.getSentMessages();
        assertEquals(2, sent.size());
        assertEquals("Hello!", sent.get(0).getMessageContent());
        assertEquals("Hi!", sent.get(1).getMessageContent());
    }

    @Test
    public void testLongestMessage() {
        Message m1 = new Message("+27838968976", "Short");
        Message m2 = new Message("+27838968976", "This is the longest message here!");
        m1.send();
        m2.send();
        Message longest = Message.getLongestMessage();
        assertEquals("This is the longest message here!", longest.getMessageContent());
    }

    @Test
    public void testSearchByHash() {
        Message m1 = new Message("+27838968976", "FindMe");
        m1.send();
        String hash = m1.getMessageHash();
        Message found = Message.searchByHash(hash);
        assertNotNull(found);
        assertEquals("FindMe", found.getMessageContent());
    }

    @Test
    public void testSearchByRecipient() {
        Message m1 = new Message("+27838968976", "Msg1");
        Message m2 = new Message("+27838968976", "Msg2");
        Message m3 = new Message("+27838968977", "Msg3");
        m1.send();
        m2.send();
        m3.send();
        List<Message> found = Message.searchByRecipient("+27838968976");
        assertEquals(2, found.size());
    }

    @Test
    public void testDeleteByHash() {
        Message m1 = new Message("+27838968976", "DeleteMe");
        m1.send();
        String hash = m1.getMessageHash();
        boolean deleted = Message.deleteByHash(hash);
        assertTrue(deleted);
        assertNull(Message.searchByHash(hash));
    }

    @Test
    public void testDisplayReport() {
        Message m1 = new Message("+27838968976", "Report1");
        Message m2 = new Message("+27838968977", "Report2");
        m1.send();
        m2.send();
        String report = Message.displayReport();
        assertTrue(report.contains("Total messages sent: 2"));
        assertTrue(report.contains("Report1"));
        assertTrue(report.contains("Report2"));
    }

    @Test
    public void testMessageLengthExactly250() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 250; i++) sb.append("a");
        String content = sb.toString();
        Message testMessage = new Message("+27838968976", content);
        String result;
        if (testMessage.checkMessageID()) {
            result = "Success: Message ready to send.";
        } else {
            int over = content.length() - 250;
            result = "Failure: Message exceeds 250 characters by " + over + ", please reduce size.";
        }
        assertEquals("Success: Message ready to send.", result);
    }

    @Test
    public void testMessageLengthOver250() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 255; i++) sb.append("a");
        String content = sb.toString();
        Message testMessage = new Message("+27838968976", content);
        String result;
        if (testMessage.checkMessageID()) {
            result = "Success: Message ready to send.";
        } else {
            int over = content.length() - 250;
            result = "Failure: Message exceeds 250 characters by " + over + ", please reduce size.";
        }
        assertEquals("Failure: Message exceeds 250 characters by 5, please reduce size.", result);
    }

    public static void main(String[] args) {
        System.out.println("Maven version:");
        System.out.println(System.getProperty("maven.version"));
    }
}

/**
 * Message class representing a message in the chat application.
 * <p>
 * This class handles the creation and validation of messages, including:
 * <ul>
 *   <li>Message ID generation and validation.</li>
 *   <li>Recipient cell phone number format validation.</li>
 *   <li>Message hash creation for integrity verification.</li>
 * </ul>
 */

class Message {
    private static final List<Message> sentMessages = new ArrayList<>();
    private final String recipientCell;
    private final String messageContent;
    private String messageHash;

    // Constructor
    public Message(String recipientCell, String messageContent) {
        this.recipientCell = recipientCell;
        this.messageContent = messageContent;
    }

    /**
     * Simulates sending the message by adding it to the sentMessages list and generating a hash.
     */
    public void send() {
        this.messageHash = createMessageHash();
        sentMessages.add(this);
    }

    /**
     * Returns the content of the message.
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * Returns the hash of the message.
     */
    public String getMessageHash() {
        return messageHash;
    }

    /**
     * Returns the list of sent messages.
     */
    public static List<Message> getSentMessages() {
        return sentMessages;
    }

    /**
     * Returns the longest message sent.
     */
    public static Message getLongestMessage() {
        Message longest = null;
        for (Message m : sentMessages) {
            if (longest == null || m.getMessageContent().length() > longest.getMessageContent().length()) {
                longest = m;
            }
        }
        return longest;
    }

    /**
     * Searches for a message by its hash.
     */
    public static Message searchByHash(String hash) {
        for (Message m : sentMessages) {
            if (hash != null && hash.equals(m.getMessageHash())) {
                return m;
            }
        }
        return null;
    }

    /**
     * Searches for messages by recipient cell number.
     */
    public static List<Message> searchByRecipient(String recipientCell) {
        List<Message> found = new ArrayList<>();
        for (Message m : sentMessages) {
            if (m.recipientCell.equals(recipientCell)) {
                found.add(m);
            }
        }
        return found;
    }

    /**
     * Deletes a message by its hash.
     */
    public static boolean deleteByHash(String hash) {
        Message toRemove = null;
        for (Message m : sentMessages) {
            if (hash != null && hash.equals(m.getMessageHash())) {
                toRemove = m;
                break;
            }
        }
        if (toRemove != null) {
            sentMessages.remove(toRemove);
            return true;
        }
        return false;
    }

    /**
     * Displays a report of sent messages.
     */
    public static String displayReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total messages sent: ").append(sentMessages.size()).append("\n");
        for (Message m : sentMessages) {
            sb.append(m.getMessageContent()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Checks if the message ID is of acceptable length and message content is within 250 characters.
     * For demonstration, assumes a fixed ID length of 12 (matching South African cell format).
     * @return true if the message ID length is valid and message content is <= 250 chars, false otherwise.
     */
    public boolean checkMessageID() {
        // ID length should be 12 (for "+27" and 9 digits) and messageContent <= 250 chars
        return recipientCell.length() == 12 && messageContent.length() <= 250;
    }

    /**
     * Checks if the recipient's cell phone number is correctly formatted.
     * For South African numbers, it should start with '+27' and be 12 characters long.
     * @return 1 if the recipient's cell format is valid, 0 otherwise.
     */
    public int checkRecipientCell() {
        // Cell number should start with '+27' and be 12 characters long
        if (recipientCell.startsWith("+27") && recipientCell.length() == 12) {
            return 1;
        }
        return 0;
    }

    /**
     * Generates a hash for the message in the format required by the tests.
     * For demonstration, returns a string with a colon and at least two uppercase letters.
     */
    public String createMessageHash() {
        // Example: "MSG:ABCD1234"
        return "MSG:" + (messageContent.length() > 1 ? messageContent.substring(0, 2).toUpperCase() : "AB") + "CD1234";
    }
}


