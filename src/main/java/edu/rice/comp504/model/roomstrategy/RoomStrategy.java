package edu.rice.comp504.model.roomstrategy;


import edu.rice.comp504.model.room.RoomAbs;

/**
 * An interface for public and private room behaviors.
 */
public interface RoomStrategy {

    /**
     * Get the strategy name of the room.
     * @return the strategy name of the room.
     */
    String getName();

    /**
     * Update the message in the current room.
     * @param room The current room.
     */
    void updateMessage(RoomAbs room);
}
