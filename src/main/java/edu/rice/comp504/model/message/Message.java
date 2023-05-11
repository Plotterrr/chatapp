package edu.rice.comp504.model.message;


import com.google.gson.JsonObject;
import edu.rice.comp504.model.user.User;

import java.util.Locale;

public class Message {
    private int id;
    private User sentFrom;
    private User sentTo;
    private String content;
    private MessageType type;
    private MessageStatus status;


    /**
     * Constructor.
     *
     * @param id       Id of message
     * @param sentFrom User, the message sender
     * @param sentTo   User, the message receiver
     * @param content  String
     * @param type     type of message public or private
     */
    public Message(int id, User sentFrom, User sentTo, String content, MessageType type) {
        this.id = id;
        this.sentFrom = sentFrom;
        this.sentTo = sentTo;
        this.content = content;
        this.type = type;
        this.status = MessageStatus.NORMAL;
    }

    /**
     * Get the id of message.
     *
     * @return int id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the message sender.
     *
     * @return User Sender
     */
    public User getSentFrom() {
        return sentFrom;
    }

    /**
     * Get the message receiver.
     *
     * @return User receiver
     */
    public User getSentTo() {
        return sentTo;
    }

    /**
     * Get message content.
     *
     * @return String content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content of message.
     *
     * @param content String content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the type of message.
     *
     * @return type of message
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Get the status of the message.
     *
     * @return MessageStatus
     */
    public MessageStatus getStatus() {
        return status;
    }

    /**
     * Set the status of the message.
     *
     * @param status MessageStatus
     */
    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    /**
     * Determine message hatespeech or not.
     *
     */
    public void checkMessage() {
        String co = content;
        String[] prepare = co.replaceAll("\\pP", "").toLowerCase().split(" ");
        String prepare2 = co.replaceAll("\\pP", "").toLowerCase();
        if(prepare2.contains("hate speech")){
            sentFrom.addHateSpeechtimes();
        }
        for (String p : prepare) {
            if (!HateSpeech.getEnum(p).name().equals("good")) {
                sentFrom.addHateSpeechtimes();
            }
        }
    }


    /**
     * Override the equals function.
     *
     * @param obj the comparing object o
     * @return True if two message are the same one
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            Message messageO = (Message) obj;

            return messageO.getId() == this.id && messageO.getContent().equals(this.content);
        }

        return false;
    }
}
