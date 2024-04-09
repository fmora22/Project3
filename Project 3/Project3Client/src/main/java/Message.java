import java.io.Serializable;

public class Message implements Serializable {
    static final long serialVersionUID = 42L;
    // Define message types as constants, use numbers to determine type?
    public static final int TYPE_BROADCAST = 1;
    public static final int TYPE_GROUP = 2;
    public static final int TYPE_INDIVIDUAL = 3;
    public static final int TYPE_USERNAME_CHECK = 4;
    public static final int TYPE_USERNAME_TAKEN = 5;
    public static final int TYPE_USERNAME_ACCEPTED = 6;

    // Attributes of the Message class
    private int messageType;
    private String senderId;
    private String recipientId; // Can be used for individual or group ID??
    private String messageContent;

    // Constructor for a broadcast message
    public Message(int messageType, String senderId, String messageContent) {
        this.messageType = messageType;
        this.senderId = senderId;
        this.messageContent = messageContent;
        this.recipientId = null; // Not applicable for broadcast messages
    }

    // Constructor for a group or individual message
    public Message(int messageType, String senderId, String recipientId, String messageContent) {
        this.messageType = messageType;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.messageContent = messageContent;
    }

    // Getters and setters for all attributes
    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    // Override the toString method for easier debugging and logging
    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", senderId='" + senderId + '\'' +
                ", recipientId='" + recipientId + '\'' +
                ", messageContent='" + messageContent + '\'' +
                '}';
    }
}
