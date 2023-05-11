package edu.rice.comp504.model;

import edu.rice.comp504.model.message.Message;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.room.RoomFactory;
import edu.rice.comp504.model.user.User;
import edu.rice.comp504.model.user.UserFactory;
import org.eclipse.jetty.websocket.api.Session;

import java.util.*;

/**
 * Lobby of room list, user list and message list.
 */
public class Lobby {
    private static Lobby singleton = null;
    private int numRoomCreated = 0;
    private int numUserCreated = 0;

    /**
     * Lobby make.
     *
     * @return lobby
     */
    public static Lobby makeLobby() {
        if (singleton == null) {
            singleton = new Lobby();
        }
        return singleton;
    }

    /**
     * user list.
     */
    private ArrayList<User> users;

    /**
     * room list.
     */
    private ArrayList<RoomAbs> rooms;

    /**
     * message list.
     */
    private ArrayList<Message> msgs;

    /**
     * map for the user and its id.
     */
    private Map<Integer, User> userMap;

    /**
     * map for the room and its id.
     */
    private Map<Integer, RoomAbs> roomMap;

    /**
     * Session-User map.
     */
    private Map<Session, User> sessionUserMap;

    private Map<Integer, Session> userIdSessionMap;

    private Map<Integer, List<RoomAbs>> userJoinedRoomsMap;

    /**
     * Constructor.
     */
    public Lobby() {
        this.users = new ArrayList<>();
        this.userMap = new HashMap<>();
        this.rooms = new ArrayList<>();
        this.roomMap = new HashMap<>();
        this.sessionUserMap = new HashMap<>();
        this.userIdSessionMap = new HashMap<>();
        this.userJoinedRoomsMap = new HashMap<>();
    }

    public Map<Integer, List<RoomAbs>> getUserJoinedRoomsMap() {
        return this.userJoinedRoomsMap;
    }

    public List<RoomAbs> getUserJoinedRoomList(int userId) {
        return this.userJoinedRoomsMap.get(userId);
    }

    /**
     * get all rooms in the list.
     */
    public ArrayList<RoomAbs> getRooms() {
        return this.rooms;
    }

    /**
     * add a room in the list.
     */
    public ArrayList<RoomAbs> addRoom(RoomAbs r) {
        this.rooms.add(r);
        this.roomMap.put(r.getId(), r);
        return this.rooms;
    }

    /**
     * remove a room in the list.
     */
    public ArrayList<RoomAbs> removeRoom(RoomAbs r) {
        this.rooms.removeIf(i -> i.getId() == r.getId());
        this.roomMap.remove(r.getId());
        return this.rooms;
    }

    /**
     * get all messages in the list.
     */
    public ArrayList<Message> getMsgs() {
        return msgs;
    }

    /**
     * add a message in the list.
     */
    public ArrayList<Message> addMsgs(Message msg) {
        this.msgs.add(msg);
        return this.msgs;
    }

    /**
     * remove a message in the list.
     */
    public ArrayList<Message> removeMsgs(Message msg) {
        this.msgs.removeIf(v -> v.getId() == msg.getId());
        return this.msgs;
    }

    /**
     * get all users in the list.
     */
    public ArrayList<User> getUsers() {
        return this.users;
    }

    /**
     * add a user in the list.
     */
    public ArrayList<User> addUser(User u) {
        this.users.add(u);
        this.userMap.put(u.getUserId(), u);
        this.userJoinedRoomsMap.put(u.getUserId(), new ArrayList<>());
        return this.users;
    }

    /**
     * findUser.
     * @param userName userName
     * @param pwd pwd
     * @return User
     */
    public User findUser(String userName, String pwd) {
        boolean userInList = false;
        for (User u : this.userMap.values()) {
            if (Objects.equals(u.getName(), userName) && Objects.equals(u.getPwd(), pwd)) {
                userInList = true;
                return u;
            }
        }
        return null;
    }

    /**
     * remove a user in the list.
     */
    public ArrayList<User> removeUser(User u) {
        this.users.removeIf(i -> i.getUserId() == u.getUserId());
        this.userMap.remove(u.getUserId());
        return this.users;
    }

    /**
     * generateRoom.
     * @param privateOrPublic privateOrPublic
     * @param capacity capacity
     * @return List of RoomAbs
     */
    public ArrayList<RoomAbs> generateRoom(String privateOrPublic, String capacity) {
        int id = RoomFactory.ROOMID++;
        User user = UserFactory.getOnly().make(id, "test", "test", 21, "Rice", Arrays.asList("a", "b"));
        int currCapacity = -1;
        try {
            currCapacity = Integer.parseInt(capacity);
        } catch (Exception e) {
            System.out.println("capacity is not a number");
            return null;
        }
        createRoom(privateOrPublic, currCapacity, "a", user);

        return this.rooms;
    }

    /**
     * CreateRoom.
     * @param type type
     * @param capacity capacity
     * @param interest interest
     * @param admin admin
     * @return RoomAbs room
     */
    public RoomAbs createRoom(String type, int capacity, String interest, User admin) {
        RoomAbs newRoom = RoomFactory.getInstance().make(this.numRoomCreated, type, capacity, interest, admin);
        this.rooms.add(newRoom);
        this.roomMap.put(this.numRoomCreated, newRoom);
        this.numRoomCreated += 1;
        System.out.println("Create room: " + newRoom.getId());
        return newRoom;
    }

    /**
     * Create User.
     * @param name name
     * @param pwd pwd
     * @param age age
     * @param school school
     * @param interest interest
     * @return User
     */
    public User createUser(String name, String pwd, int age, String school, List<String> interest) {
        for (User u : this.userMap.values()) {
            if (Objects.equals(u.getName(), name)) {
                return null;
            }
        }
        User newUser = UserFactory.getOnly().make(numUserCreated, name, pwd, age, school, interest);
        this.users.add(newUser);
        this.userMap.put(newUser.getUserId(), newUser);
        this.userJoinedRoomsMap.put(newUser.getUserId(), new ArrayList<>());
        this.numUserCreated += 1;
        System.out.println("Create user: " + newUser.getUserId() + " name: " + newUser.getName());
        return newUser;
    }

    public void blockUser(int userId, int roomId) {
        this.userMap.get(userId).setIsBlocked(this.roomMap.get(roomId), Boolean.TRUE);
    }

    public void removeBlockUser(int userId, int roomId) {
        this.userMap.get(userId).removeIsBlocked(this.roomMap.get(roomId));

    }

    /**
     * a function to let user with userId joins the room with roomId.
     */
    public boolean joinRoom(int userId, int roomId) {
        System.out.println("User: " + userId + " join room: " + roomId);
        RoomAbs room = roomMap.get(roomId);
        User user = userMap.get(userId);
        if (room.getMembers().contains(user) || room.getBannedUser().contains(user)) {
            return false;
        }
        if (room.addMembers(user)) {
            user.setCurRoom(room.getId());
            this.userJoinedRoomsMap.get(user.getUserId()).add(room);
            return true;
        }

        return false;
    }

    /**
     * a function to let user with userId switches the room with roomId.
     */
    public boolean switchRoom(int userId, int roomId) {
        System.out.println("User: " + userId + " switch room: " + roomId);
        RoomAbs room = roomMap.get(roomId);
        User user = userMap.get(userId);
        if (room.getBannedUser().contains(user) || user.getCurRoom() == roomId) {
            return false;
        } else {
            user.setCurRoom(room.getId());
            return true;
        }
    }

    /**
     * a function to let user with userId quits the room with roomId.
     */
    public void quitRoom(int userId, int roomId) {
        RoomAbs curRoom = roomMap.get(roomId);
        User curUser = userMap.get(userId);
        curRoom.getMembers().removeIf(i -> i.getUserId() == curUser.getUserId());
        curUser.setCurRoom(-1);
        curUser.getJoinRooms().removeIf(i -> i.getId() == roomId);
        roomMap.get(roomId).getUserMessageMap().remove(curUser);
        this.userJoinedRoomsMap.get(curUser.getUserId()).removeIf(i -> i.getId() == roomId);
        // need to remove message when user quit?
    }

    /**
     * a function to let user with userId sends a message msg in the room with roomId.
     */
    public void sendMessage(int userId, int roomId, Message msg) {

    }

    /**
     * get the available users that can join room r.
     */
    public ArrayList<User> getAvailableUsers(RoomAbs r) {
        ArrayList<User> avail = new ArrayList<>();
        if (r.getMembers().size() < r.getCapacity()) {
            for (User u : this.users) {
                if (u.getInterest().equals(r.getInterest())) {
                    Boolean flag = false;
                    for (RoomAbs ro : u.getJoinRooms()) {
                        if (ro.getId() == r.getId()) {
                            flag = true;
                        }
                    }
                    if (flag == false && (u.getIsBlocked().get(r) != true || u.getIsBlocked().size() == 0)) {
                        avail.add(u);
                    }
                }
            }
            return null;
        }
        return avail;
    }

    /**
     * get the available rooms that user u can join.
     */
    public ArrayList<RoomAbs> getAvailableRooms(User u) {
        return new ArrayList<RoomAbs>();
    }

    /**
     * Get the Session-User map.
     *
     * @return map of Session-User
     */
    public Map<Session, User> getSessionUserMap() {
        return sessionUserMap;
    }

    /**
     * Add Session-User to map.
     *
     * @param session Session
     * @param user    User
     */
    public void addSessionUser(Session session, User user) {
        sessionUserMap.put(session, user);
        userIdSessionMap.put(user.getUserId(), session);
    }

    /**
     * Remove a Session from Session-User map.
     *
     * @param session Session
     */
    public void removeSession(Session session) {
        userIdSessionMap.remove(sessionUserMap.get(session).getUserId());
        sessionUserMap.remove(session);
    }

    public Map<Integer, Session> getUserIdSessionMap() {
        return userIdSessionMap;
    }


    public Map<Integer, RoomAbs> getRoomMap() {
        return roomMap;
    }

    public Map<Integer, User> getUserMap() {
        return userMap;
    }
}
