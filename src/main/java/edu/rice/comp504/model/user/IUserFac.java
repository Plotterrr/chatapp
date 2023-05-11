package edu.rice.comp504.model.user;

import edu.rice.comp504.model.room.RoomAbs;
import org.eclipse.jetty.websocket.api.Session;

import java.util.List;
import java.util.Map;

/**
 * A factory makes User.
 */
public interface IUserFac {
    /**
     * Make a user.
     *
     * @return a User
     */
    User make(int userId, String name, String pwd, int age, String school, List<String> interest);
}
