package edu.rice.comp504.model.room;

import edu.rice.comp504.model.message.Message;
import edu.rice.comp504.model.message.MessageStatus;
import edu.rice.comp504.model.message.MessageType;
import edu.rice.comp504.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the abstract class of Rooms, it includes functions shared with public and private rooms.
 */
public abstract class RoomAbs {
    private int id;
    private RoomType type;
    private final int capacity;
    private final String interest;
    private final User admin;
    private List<User> members;
    private List<Message> msgList; // when message is recalled, don't delete it change it to "... is recalled", change the message status to recalled
    private Map<Integer, Integer> msgMapping; // <msgId, msgListIndex>
    private List<User> bannedUser;
    private Map<User, Integer> userMessageMap;

    /**
     * Constructor.
     *
     * @param id       int roomId
     * @param type     RoomType
     * @param capacity int room capacity
     * @param interest string room interest
     * @param admin    admin user
     */
    public RoomAbs(int id, RoomType type, int capacity, String interest, User admin) {
        this.id = id;
        this.type = type; // this.type = "public";
        this.capacity = capacity;
        this.admin = admin;
        this.interest = interest;
        this.members = new ArrayList<>();
        this.msgList = new ArrayList<>();
        this.msgMapping = new HashMap<>();
        this.bannedUser = new ArrayList<>();
        this.userMessageMap = new HashMap<>();
    }

    /**
     * Get the room id.
     *
     * @return int roomId
     */
    public int getId() {
        return id;
    }

    /**
     * Set room id.
     *
     * @param id int id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get room type.
     *
     * @return RoomType
     */
    public RoomType getType() {
        return this.type;
    }

    /**
     * Set Room Type.
     *
     * @param type RoomType
     */
    public void setType(RoomType type) {
        this.type = type;
    }

    /**
     * Get room capacity.
     *
     * @return int room capacity
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Get Admin.
     *
     * @return User admin
     */
    public User getAdmin() {
        return this.admin;
    }

    /**
     * Get the interest of the current room.
     *
     * @return the interest
     */
    public String getInterest() {
        return this.interest;
    }

    /**
     * Get members in the room.
     *
     * @return User of user
     */
    public List<User> getMembers() {
        return this.members;
    }

    /**
     * Add user to room.
     *
     * @param user User
     */
    public boolean addMembers(User user) {
        if (members.size() < capacity) {
            this.members.add(user);
            return true;
        }
        return false;

    }

    /**
     * Get a list of messages.
     *
     * @return a list of message
     */
    public List<Message> getMsgList() {
        return this.msgList;
    }

    /**
     * Add a message to list.
     *
     * @param msg a message
     */
    public void addMsgToList(Message msg) {
        this.msgList.add(msg);
        int curIndex = this.msgList.size() - 1;
        msgMapping.put(msg.getId(), curIndex);
        if (msg.getSentFrom() != null) {
            userMessageMap.put(msg.getSentFrom(), msg.getId());
        }
        if (msg.getSentTo() != null) {
            userMessageMap.put(msg.getSentTo(), msg.getId());
        }
    }

    /**
     * Get the MsgMap.
     *
     * @return The MsgMap
     */
    public Map<Integer, Integer> getMsgMapping() {
        return this.msgMapping;
    }

    /**
     * Set the MsgMap.
     *
     * @param msgMapping Map of message id and its index in msgList
     */
    public void setMsgMapping(Map<Integer, Integer> msgMapping) {
        this.msgMapping = msgMapping;
    }

    /**
     * Get BannedUser.
     *
     * @return List of User User
     */
    public List<User> getBannedUser() {
        return this.bannedUser;
    }

    /**
     * Add user to Ban list.
     *
     * @param user User
     */
    public void addBannedUser(User user) {
        this.bannedUser.add(user);
        this.members.remove(user);
    }

    /**
     * Get the user message map.
     *
     * @return The user message map.
     */
    public Map<User, Integer> getUserMessageMap() {
        return this.userMessageMap;
    }

    /**
     * Set the user message map.
     *
     * @param map The new user message map
     */
    public void setUserMessageMap(Map<User, Integer> map) {
        this.userMessageMap = map;
    }

    /**
     * Remove from Banned User.
     *
     * @param user The user
     * @return The removed user
     */
    public User removeFromBannedUser(User user) {
        bannedUser.remove(user);
        members.add(user);
        return user;
    }

    /**
     * Check if the user is in room.
     *
     * @param user User
     * @return Boolean True if user is in room
     */
    public boolean isInRoom(User user) {
        return members.contains(user);
    }

    /**
     * Get the message from message list.
     *
     * @param msgId Int message id
     * @return A Message
     */
    public Message getMessageFromList(int msgId) {
        return msgList.get(msgMapping.get(msgId));
    }


    /**
     * recall a message by id.
     *
     * @param msgId id of message
     */
    public void recallMessage(int msgId) {
        //TODO: Find the index of msgId in msgMapping, msgList[index].setStatus(recalled)
    }

    /**
     * recall a message by Message.
     * @param m the message
     */
    public void recallMessage(Message m) {
        //this.msgList.removeIf(i -> i.getId() == m.getId());
        //this.msgMapping.remove(m.getId());
        //this.userMessageMap.remove(m.getSentFrom());
        m.setStatus(MessageStatus.RECALLED);

    }

    /**
     * Delete the message m if user is the admin.
     * @param m the message
     */
    public void deleteMessage(Message m) {
        this.msgList.removeIf(i -> i.getId() == m.getId());
        this.msgMapping.remove(m.getId());
        this.userMessageMap.remove(m.getSentFrom());
    }

    /**
     * Edit the Message by msgId and new String s.
     *
     * @param msgId The editing message id
     * @param s     the new string s
     * @return the edited message
     */
    public Message editMessage(int msgId, String s) {
        int messageIndex = this.msgMapping.get(msgId);

        this.msgList.get(messageIndex).setContent(s);

        return this.msgList.get(messageIndex);
    }

    /**
     * Override the equals method for Room classes.
     *
     * @param obj The comparing obj
     * @return True if the compared room equals to this room
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RoomAbs) {

            return ((RoomAbs) obj).getId() == this.id
                    && ((RoomAbs) obj).getAdmin().getName().equals(this.admin.getName())
                    && ((RoomAbs) obj).getType().equals(this.type)
                    && ((RoomAbs) obj).getCapacity() == this.capacity;
        }

        return false;
    }

}
