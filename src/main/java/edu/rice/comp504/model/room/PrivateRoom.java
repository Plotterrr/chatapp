package edu.rice.comp504.model.room;

import edu.rice.comp504.model.user.User;

/**
 * This is the private room class. In this kind of room only admin can invite user to join it.
 */
public class PrivateRoom extends RoomAbs {

    public PrivateRoom(int id, RoomType type, int capacity, String interest, User admin) {
       super(id, type, capacity, interest, admin);
    }

    /**
     * Invite a new user.
     * @param user The user who will be invite
     * @return The invited user.
     */
    public boolean inviteUser(User user) {
        return this.getCapacity() > this.getMembers().size();
    }
}
