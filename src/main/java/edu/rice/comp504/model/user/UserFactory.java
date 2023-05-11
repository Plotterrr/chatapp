package edu.rice.comp504.model.user;

import edu.rice.comp504.model.room.RoomAbs;
import org.eclipse.jetty.websocket.api.Session;


import java.util.List;
import java.util.Map;

/**
 * User factory.
 */
public class UserFactory implements IUserFac {
    private static UserFactory ONLY;

    /**
     * Constructor.
     */
    private UserFactory() {

    }

    /**
     * Get singleton UserFactory.
     *
     * @return UserFactory
     */
    public static UserFactory getOnly() {
        if (ONLY == null) {
            ONLY = new UserFactory();
        }
        return ONLY;
    }

    @Override
    public User make(int userId, String name, String pwd, int age, String school, List<String> interest) {
        return new Normal(userId, name, pwd, age,  school, interest);
    }

}
