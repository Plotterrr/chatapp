package edu.rice.comp504.model.user;


import org.eclipse.jetty.websocket.api.Session;

import java.util.List;

/**
 * Class for creating normal users.
 */
public class Normal extends User {
    public Normal(int userId, String name, String pwd, int age, String school, List<String> interest) {
        super(userId, name, pwd, age, school, interest);
    }
}
