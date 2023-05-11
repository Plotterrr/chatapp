package edu.rice.comp504.model.message;

import edu.rice.comp504.model.user.User;

/**
 * A factory that makes Message.
 */
public interface IMessageFac {
    /**
     * Makes a Message.
     * @return Message
     */
    Message make(User sentFrom, User sentTo, String content, String type);
}
