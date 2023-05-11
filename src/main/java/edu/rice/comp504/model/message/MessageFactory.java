package edu.rice.comp504.model.message;

import edu.rice.comp504.model.user.User;

public class MessageFactory implements IMessageFac{
    private static MessageFactory INSTANCE;
    private static int msgId = 0;

    /**
     * Constructor.
     */
    private MessageFactory() {

    }

    /**
     * Get the singleton MessageFactory.
     * @return MessageFactory
     */
    public static MessageFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageFactory();
        }
        return INSTANCE;
    }

    @Override
    public Message make(User sentFrom, User sentTo, String content, String type) {
        Message m = null;

        switch (type) {
            case "public":
                m = new Message(msgId++, sentFrom, sentTo, content, MessageType.PUBLIC);
                break;
            case "private":
                m = new Message(msgId++, sentFrom, sentTo, content, MessageType.PRIVATE);
                break;
            default:
                break;
        }
        return m;
    }
}
