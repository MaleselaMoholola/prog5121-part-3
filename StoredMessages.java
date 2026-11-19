package pitsimoholola_part1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class StoredMessages {
    
    private final String jsonFile;
    
    private String[] sentMessage = new String[0];
    private String[] disregardedMessage = new String[0];
    private String[] storedMessages = new String[0];
    private String[] messageHash = new String[0];
    private String[] messageID = new String[0];
    
    //Parallel arrays for searching ad displaying details
    
    private String[] recipients = new String[0];
    private String[] senders = new String[0];
    private String[] statuses = new String[0];
    
    //Internal records retain the complete message details
    private final List<MessageRecord> messages = new ArrayList<>();
    
    public StoredMessages() {
        this("message.json");
 }
  
    public StoredMessages (String jsonFile) {
    
        this.jsonFile =jsonFile;
        loadStoredMessagesFromJSON();
        populateArrays();
    }
     
    private static class MessageRecord {
        
        String id;
        String hash;
        String recipient;
        String text;
        String status;
        String sender;
        
        MessageRecord( String id, String hash, String recipient,
                String text, String status, String sender) {
            this.id = id;
            this.hash = hash;
            this.recipient = recipient;
            this.text = text;
            this.status = status;
            this.sender = sender;
        }
    }
    
    public void addMessages(String id, String hash, String recipient,
                String text, String status) {
        
        messages.add(new MessageRecord(
        id, hash, recipient, text, status, "Developer"));
        
        populateArrays();
        
        if ("Stored".equalsIgnoreCase(status)) {
            saveStoredMessagesToJSON();
        }
    }
    public final void populateArrays() {
        List<String> sent = new ArrayList<>();
        List<String> disregarded = new ArrayList<>();
        List<String> stored = new ArrayList<>();
        List<String> hashes = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        List<String> recipientList = new ArrayList<>();
        List<String> senderList = new ArrayList<>();
        List<String> statusList = new ArrayList<>();
        
        for (MessageRecord message : messages) {
            hashes.add(message.hash);
            ids.add(message.id);
            recipientList.add(message.recipient);
            senderList.add(message.sender);
            statusList.add(message.status);
            
            if ("Sent".equalsIgnoreCase(message.status)){
                sent.add(message.text);
            } else if ("Disregarded".equalsIgnoreCase(message.status)
                    || "Disregarded".equalsIgnoreCase(message.status)) {
                disregarded.add(message.text);
            } else if ("Stored".equalsIgnoreCase(message.status)) {
                stored.add(message.text);
            }
    } 
        this.sentMessage = sent.toArray(new String[0]);
        this.disregardedMessage = disregarded.toArray(new String[0]);
        this.storedMessages = stored.toArray(new String[0]);
        this.messageHash = hashes.toArray(new String[0]);
        this.messageID = ids.toArray(new String[0]);
        this.recipients = recipientList.toArray(new String[0]);
        this.senders = senderList.toArray(new String[0]);
        this.statuses = statusList.toArray(new String[0]);
    }
    // Display sender and recipient of all stored messages
public String displaySendersAndRecipientsOfStored()
{
    String output = "=== Stored Messages: Senders & Recipients ===\n";
    boolean found = false;

    for (int i = 0; i < messages.size(); i++)
    {
        MessageRecord m = messages.get(i);
        if ("Stored".equalsIgnoreCase(m.status))
        {
            output = output + "Sender: " + m.sender
                   + " | Recipient: " + m.recipient + "\n";
            found = true;
        }
    }

    if (!found)
    {
        return "No stored messages found.\n";
    }
    return output;
}

// Display the longest stored message
public String displayLongestStoredMessage()
{
    MessageRecord longest = null;

    for (int i = 0; i < messages.size(); i++)
    {
        MessageRecord m = messages.get(i);
        if ("Stored".equalsIgnoreCase(m.status) && m.text != null)
        {
            if (longest == null || m.text.length() > longest.text.length())
            {
                longest = m;
            }
        }
    }

    if (longest == null)
    {
        return "No stored messages found.\n";
    }

    return "Longest Stored Message (" + longest.text.length() + " chars):\n"
         + "ID: " + longest.id + " | Recipient: " + longest.recipient + "\n"
         + "Message: " + longest.text + "\n";
}

// Search for a message ID and display corresponding recipient and message
public String searchByMessageId(String targetId)
{
    if (targetId == null)
    {
        return "Invalid Message ID.\n";
    }

    for (int i = 0; i < messages.size(); i++)
    {
        MessageRecord m = messages.get(i);
        if (targetId.equalsIgnoreCase(m.id))
        {
            return "Message ID: " + m.id
                 + "\nRecipient: " + m.recipient
                 + "\nMessage: " + m.text + "\n";
        }
    }

    return "No message found with ID: " + targetId + "\n";
}

// Search for all messages stored for a particular recipient
public String searchStoredByRecipient(String targetRecipient)
{
    if (targetRecipient == null)
    {
        return "Invalid recipient.\n";
    }

    String output = "=== Stored Messages for Recipient: " + targetRecipient + " ===\n";
    boolean found = false;

    for (int i = 0; i < messages.size(); i++)
    {
        MessageRecord m = messages.get(i);
        if ("Stored".equalsIgnoreCase(m.status)
                && targetRecipient.equalsIgnoreCase(m.recipient))
        {
            output = output + "- [ID: " + m.id + "] " + m.text + "\n";
            found = true;
        }
    }

    if (!found)
    {
        return "No stored messages found for recipient: " + targetRecipient + "\n";
    }
    return output;
}

// Delete a message using the message hash
public boolean deleteMessageByHash(String targetHash)
{
    if (targetHash == null)
    {
        return false;
    }

    boolean removed = false;
    for (int i = 0; i < messages.size(); i++)
    {
        MessageRecord m = messages.get(i);
        if (targetHash.equals(m.hash))
        {
            messages.remove(i);
            removed = true;
            break;
        }
    }

    if (removed)
    {
        populateArrays();
        saveStoredMessagesToJSON();
    }
    return removed;
}

// Display a report listing full details of all stored messages
public String displayStoredMessagesReport()
{
    String output = "=== Full Stored Messages Report ===\n";
    int count = 0;

    for (int i = 0; i < messages.size(); i++)
    {
        MessageRecord m = messages.get(i);
        if ("Stored".equalsIgnoreCase(m.status))
        {
            count++;
            output = output + count + ". "
                   + "ID: " + m.id
                   + " | Hash: " + m.hash
                   + " | Sender: " + m.sender
                   + " | Recipient: " + m.recipient
                   + " | Status: " + m.status
                   + " | Message: " + m.text + "\n";
        }
    }

    if (count == 0)
    {
        return "No stored messages to display.\n";
    }

    output = output + "Total Stored Messages: " + count + "\n";
    return output;
}

// Simple JSON loader/saver without regex or lambda expressions
public final void loadStoredMessagesFromJSON()
{
    Path path = Paths.get(jsonFile);
    if (!Files.exists(path))
    {
        return;
    }

    try
    {
        String content = Files.readString(path, StandardCharsets.UTF_8);
        int start = content.indexOf("{");
        while (start != -1)
        {
            int end = content.indexOf("}", start);
            if (end == -1)
            {
                break;
            }
            String obj = content.substring(start + 1, end);

            String id = getSimpleValue(obj, "id");
            String hash = getSimpleValue(obj, "hash");
            String recipient = getSimpleValue(obj, "recipient");
            String text = getSimpleValue(obj, "message");
            String status = getSimpleValue(obj, "status");
            String sender = getSimpleValue(obj, "sender");

            if (status == null || status.equals(""))
            {
                status = "Stored";
            }
            if (sender == null || sender.equals(""))
            {
                sender = "Developer";
            }
            if (text != null)
            {
                messages.add(new MessageRecord(
                        id != null ? id : "",
                        hash != null ? hash : "",
                        recipient != null ? recipient : "",
                        unescapeJson(text),
                        status,
                        sender
                ));
            }
            start = content.indexOf("{", end + 1);
        }
        populateArrays();
    }
    catch (IOException e)
    {
        System.err.println("Error reading JSON file '" + jsonFile + "': " + e.getMessage());
    }
}

public final void saveStoredMessagesToJSON()
{
    String json = "[\n";
    List<MessageRecord> storedOnly = new ArrayList<>();

    for (int i = 0; i < messages.size(); i++)
    {
        MessageRecord m = messages.get(i);
        if ("Stored".equalsIgnoreCase(m.status))
        {
            storedOnly.add(m);
        }
    }

    for (int i = 0; i < storedOnly.size(); i++)
    {
        MessageRecord m = storedOnly.get(i);
        json = json + "  {\n"
             + "    \"id\": \"" + escapeJson(m.id) + "\",\n"
             + "    \"hash\": \"" + escapeJson(m.hash) + "\",\n"
             + "    \"recipient\": \"" + escapeJson(m.recipient) + "\",\n"
             + "    \"message\": \"" + escapeJson(m.text) + "\",\n"
             + "    \"status\": \"" + escapeJson(m.status) + "\",\n"
             + "    \"sender\": \"" + escapeJson(m.sender) + "\"\n"
             + "  }";
        if (i < storedOnly.size() - 1)
        {
            json = json + ",";
        }
        json = json + "\n";
    }
    json = json + "]\n";

    try
    {
        Files.writeString(Paths.get(jsonFile), json, StandardCharsets.UTF_8);
    }
    catch (IOException e)
    {
        System.err.println("Error writing JSON file '" + jsonFile + "': " + e.getMessage());
    }
}

private static String getSimpleValue(String jsonObject, String key)
{
    String searchKey = "\"" + key + "\"";
    int keyPos = jsonObject.indexOf(searchKey);
    if (keyPos == -1)
    {
        return null;
    }
    int colonPos = jsonObject.indexOf(":", keyPos + searchKey.length());
    if (colonPos == -1)
    {
        return null;
    }
    int firstQuote = jsonObject.indexOf("\"", colonPos + 1);
    if (firstQuote == -1)
    {
        return null;
    }
    int secondQuote = firstQuote + 1;
    while (secondQuote < jsonObject.length())
    {
        char c = jsonObject.charAt(secondQuote);
        if (c == '\\')
        {
            secondQuote += 2;
            continue;
        }
        if (c == '"')
        {
            return jsonObject.substring(firstQuote + 1, secondQuote);
        }
        secondQuote++;
    }
    return null;
}

private static String escapeJson(String value)
{
    if (value == null)
    {
        return "";
    }
    return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
}

// Fixed backslash order so trailing "\\" is unescaped properly
private static String unescapeJson(String value)
{
    if (value == null)
    {
        return "";
    }
    String result = "";
    for (int i = 0; i < value.length(); i++)
    {
        char current = value.charAt(i);
        if (current == '\\' && i + 1 < value.length())
        {
            char next = value.charAt(i + 1);
            if (next == '\\')
            {
                result = result + "\\";
                i++;
            }
            else if (next == '"')
            {
                result = result + "\"";
                i++;
            }
            else if (next == 'n')
            {
                result = result + "\n";
                i++;
            }
            else if (next == 'r')
            {
                result = result + "\r";
                i++;
            }
            else
            {
                result = result + current;
            }
        }
        else
        {
            result = result + current;
        }
    }
    return result;
}

// Getters for arrays / unit testing
public String[] getSentMessage() { return sentMessage.clone(); }
public String[] getDisregardedMessage() { return disregardedMessage.clone(); }
public String[] getStoredMessages() { return storedMessages.clone(); }
public String[] getMessageHash() { return messageHash.clone(); }
public String[] getMessageID() { return messageID.clone(); }
public String[] getRecipients() { return recipients.clone(); }
public String[] getSenders() { return senders.clone(); }
public String[] getStatuses() { return statuses.clone(); }
    } 