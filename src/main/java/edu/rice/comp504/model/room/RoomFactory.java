package edu.rice.comp504.model.room;

import edu.rice.comp504.model.user.User;

/**
 * This is the factory class of rooms that help us handle the implementation details of creating a room.
 */
public class RoomFactory implements IRoomFac{
    private static RoomFactory INSTANCE;
    public static int ROOMID = 0;

    /**
     * Constructor.
     */
    private RoomFactory() {

    }

    /**
     * Get the singleton RoomFactory.
     * @return RoomFactory
     */
    public static RoomFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RoomFactory();
        }
        return INSTANCE;
    }

    @Override
    public RoomAbs make(int id, String type, int capacity, String interest, User admin) {
        RoomAbs room = null;
        interest = interest.equals("") ? "No special interest" : interest;
        switch (type) {
            case "public":
                room = new PublicRoom(id, RoomType.PUBLIC, capacity, interest, admin);
                break;
            case "private":
                room = new PrivateRoom(id, RoomType.PRIVATE, capacity, interest, admin);
                break;
            default:
                break;
        }
        return room;
    }
}
