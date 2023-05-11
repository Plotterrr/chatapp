package edu.rice.comp504.model.room;

import edu.rice.comp504.model.user.User;

/**
 * A factory that makes Room.
 */
public interface IRoomFac {
    /**
     * Makes a Room.
     * @return A Room
     */
    RoomAbs make(int id, String type, int capacity, String interest, User admin);
}
