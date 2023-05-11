package edu.rice.comp504.controller;

import edu.rice.comp504.adapter.WebSocketAdapter;
import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.room.*;
import com.google.gson.Gson;
import edu.rice.comp504.model.user.User;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
public class ChatAppController {

    /**
     * Chat App entry point.
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/public");
        Lobby lobby = Lobby.makeLobby();
        Gson gson = new Gson();
        webSocket("/chatapp", WebSocketAdapter.class);
        init();

        //POST /register body: userID, interests, password
        post("/register", (request, response) -> {
            String userName = request.queryMap().value("userName");
            String interest = request.queryMap().value("interests");
            List<String> interests = new ArrayList<String>() {
            };
            interests.add(interest);
            interests.forEach(i -> System.out.println(i));
            String password = request.queryMap().value("password");
            String school = request.queryMap().value("school");
            int age = Integer.parseInt(request.queryMap().value("age"));

            User user = lobby.createUser(userName, password, age, school, interests);
            if (user != null) {
                //System.out.println("here");
                System.out.println(userName + interests + password + age + school);
            } else {
                System.out.println("userName " + userName + " already exists; please create a new one");
            }
            return gson.toJson(user);
        });
        //POST /login/:userID&password body: userID, password
        post("/login", (request, response) -> {
            String userName = request.queryMap().value("userName");
            String password = request.queryMap().value("password");
            System.out.println(userName + password);
            User user = lobby.findUser(userName, password);
            if (user != null) {
                System.out.println(user.getName() + user.getPwd());
            } else {
                System.out.println("user does not exist");
            }
            return gson.toJson(user);
        });
        //POST /createRoom/:roomID body: userID, roomID, interests
        post("/createRoom", (request, response) -> {
            String capacity = request.queryMap().value("capacity");
            String privateOrPublic = request.queryMap().value("publicOrPrivate");
            lobby.generateRoom(privateOrPublic, capacity);
            TestAbs a = new TestClass(1);
            return gson.toJson(a);
        });

        post("/roomId", (request, response) -> {
            return null;
        });

        post("/getRoomUserList", (request, response) -> {
            String roomIDStr = request.queryMap().value("roomID");
            int roomID = Integer.parseInt(roomIDStr);
            RoomAbs room = lobby.getRoomMap().get(roomID);
            return gson.toJson(room);
        });

//        POST /roomID/sendMessage body: userID, message(from, to)
        post("/roomId/sendMessage", (request, response) -> {

            return null;
        });

    }


    /**
     * Get the heroku assigned port number.
     *
     * @return The heroku assigned port number
     */
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; // return default port if heroku-port isn't set.
    }
}
