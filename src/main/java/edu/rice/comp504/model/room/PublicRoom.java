package edu.rice.comp504.model.room;

import edu.rice.comp504.model.user.User;

/**
 * This is the public room class. In this kind of room all users who have same interest of the room can join it.
 */
public class PublicRoom extends RoomAbs {

    public PublicRoom(int id, RoomType type, int capacity, String interest, User admin) {
        super(id, type, capacity, interest, admin);
    }

}
