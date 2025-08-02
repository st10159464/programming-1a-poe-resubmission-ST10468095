package io.github.st10159464;

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
        Message.clearMessages(); // Clear sent messages before each test
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
        String hash = message.getMessageHash();
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


