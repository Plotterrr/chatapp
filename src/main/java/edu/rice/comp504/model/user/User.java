package edu.rice.comp504.model.user;

import edu.rice.comp504.model.room.RoomAbs;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class of a User.
 */
public abstract class User {
    private int userId; // userId
    private String name; // userName
    private String pwd; //user Password
    private int age;  // user age
    private String school;  // user school
    //private Session session;  // Websocket session
    private List<String> interest = new ArrayList<>();  // user interests
    private List<RoomAbs> adminRooms = new ArrayList<>();  // the room list the user can manage
    private Map<RoomAbs, Boolean> isBlocked; // whether the user is blocked in some rooms
    private List<RoomAbs> joinRooms = new ArrayList<>(); // the room list the user has already joined
    private int curRoom;
    private int hateSpeechtimes;

    /**
     * Constructor.
     *
     * @param userId user Id
     * @param name   user name
     * @param pwd    user password
     * @param age    user age
     * @param school user school
     */
    public User(int userId, String name, String pwd, int age, String school, List<String> interest) {
        this.userId = userId;
        this.name = name;
        this.pwd = pwd;
        this.age = age;
        this.school = school;
        this.interest = interest;
        this.isBlocked = new HashMap<>();
        this.curRoom = -1;
        this.hateSpeechtimes = 0;
    }

    /**
     * Get userId.
     *
     * @return userId
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * Get username.
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Set username.
     *
     * @param name String name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get user password.
     *
     * @return String password
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Set user password.
     *
     * @param pwd String password
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * Get user age.
     *
     * @return int age
     */
    public int getAge() {
        return age;
    }

    /**
     * Set user age.
     *
     * @param age int age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Get user school.
     *
     * @return String school
     */
    public String getSchool() {
        return school;
    }

    /**
     * Set user school.
     *
     * @param school String school
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * Get user interests.
     *
     * @return List of String interests
     */
    public List<String> getInterest() {
        return interest;
    }

    /**
     * Add user interests.
     *
     * @param interest interest
     */
    public void addInterest(String interest) {
        this.interest.add(interest);
    }

    /**
     * Remove user interests.
     *
     * @param interest interest
     */
    public void removeInterest(String interest) {
        for (String obj : this.interest) {
            if (obj.equals(interest)) {
                this.interest.remove(interest);
            }
        }
    }

    /**
     * Get user admin room list.
     *
     * @return adminRooms
     */
    public List<RoomAbs> getAdminRooms() {
        return adminRooms;
    }

    /**
     * Add user admin room list.
     *
     * @param adminRoom adminRoom
     */
    public void addAdminRooms(RoomAbs adminRoom) {
        this.adminRooms.add(adminRoom);
    }

    /**
     * Remove user admin room list.
     *
     * @param adminRoom adminRoom
     */
    public void removeAdminRooms(RoomAbs adminRoom) {
        for (RoomAbs obj : this.adminRooms) {
            if (obj.getId() == adminRoom.getId()) {
                this.adminRooms.remove(adminRoom);
            }
        }
    }

    /**
     * Get user block map.
     *
     * @return map isBlocked, Room, Boolean
     */
    public Map<RoomAbs, Boolean> getIsBlocked() {
        return isBlocked;
    }

    /**
     * Add user block map.
     *
     * @param room  Room room
     * @param block boolean block
     */
    public void addIsBlocked(RoomAbs room, Boolean block) {
        this.isBlocked.put(room, block);
    }

    /**
     * Set user block map.
     *
     * @param room  Room room
     * @param block boolean block
     */
    public void setIsBlocked(RoomAbs room, Boolean block) {
        for (RoomAbs obj : this.isBlocked.keySet()) {
            if (obj.getId() == room.getId()) {
                this.isBlocked.replace(obj, block);
            }
        }
    }

    /**
     * Remove user block map.
     *
     * @param room Room room
     */
    public void removeIsBlocked(RoomAbs room) {
        this.isBlocked.keySet().removeIf(key -> key.getId() == room.getId());
    }

    /**
     * Get user join room list.
     *
     * @return joinRooms
     */
    public List<RoomAbs> getJoinRooms() {
        return joinRooms;
    }

    /**
     * Add user join room list.
     *
     * @param joinRoom joinRoom
     */
    public void addJoinRooms(RoomAbs joinRoom) {
        this.joinRooms.add(joinRoom);
    }

    /**
     * Remove user join room list.
     *
     * @param joinRoom joinRoom
     */
    public void removeJoinRooms(RoomAbs joinRoom) {
        for (RoomAbs obj : this.joinRooms) {
            if (obj.getId() == joinRoom.getId()) {
                this.joinRooms.remove(joinRoom);
            }
        }
    }

    /**
     * Get the Room that the user currently in.
     *
     * @return int
     */
    public int getCurRoom() {
        return curRoom;
    }

    /**
     * Set user currently in room.
     *
     * @param room int
     */
    public void setCurRoom(int room) {
        curRoom = room;
    }

    /**
     * Get User's hate speech time.
     *
     * @return int
     */
    public int getHateSpeechtimes() {
        return hateSpeechtimes;
    }

    /**
     * Add User's hate speech time by 1.
     */
    public void addHateSpeechtimes() {
        this.hateSpeechtimes++;
    }
}
