package edu.rice.comp504.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp504.model.message.Message;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.List;

import static j2html.TagCreator.p;

/**
 * Send messages to the client.
 */
public class MsgToClientSender {

    /**
     * Broadcast message to all users.
     *
     * @param sender  The message sender.
     * @param message The message.
     */
    public static void broadcastMessage(String sender, String message) {
        UserDB.getSessions().forEach(session -> {
            try {
                JsonObject jo = new JsonObject();
                // TODO: use .addProperty(key, value) add a JSON object property that has a key "userMessage"
                //  and a j2html paragraph value
                jo.addProperty("userMessage", p(sender + "says: " + message).render());
                session.getRemote().sendString(String.valueOf(jo));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * registerEventRes.
     */
    public static void registerEventRes() {

    }

    /**
     * loginEventRes.
     * @param session s
     * @param newUser n
     */
    public static void loginEventRes(Session session, User newUser) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("user", String.valueOf(new Gson().toJson(newUser)));
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * createRoomEventRes.
     * @param eventName e
     * @param session s
     * @param lobby l
     * @param room r
     */
    public static void createRoomEventRes(String eventName, Session session, Lobby lobby, RoomAbs room) {
        try {
            JsonObject jo = new JsonObject();
            User curUser = lobby.getSessionUserMap().get(session);
            jo.addProperty("room", new Gson().toJson(room));
            jo.addProperty("user", new Gson().toJson(curUser));
            jo.addProperty("notification", "User: " + curUser.getName() + " Joins the Room");
            jo.addProperty("resType", eventName);
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }

    /**
     * joinRoomEventRes.
     * @param eventName e
     * @param room r
     * @param curUser c
     */
    public static void joinRoomEventRes(String eventName, RoomAbs room, User curUser) {
        room.getMembers().forEach(eachUser -> {
            try {
                JsonObject jo = new JsonObject();
                jo.addProperty("notification", "User: " + curUser.getName() + " Joins the Room");
                jo.addProperty("user", new Gson().toJson(eachUser));
                jo.addProperty("userList", new Gson().toJson(room.getMembers()));
                jo.addProperty("room", new Gson().toJson(room));
                jo.addProperty("resType", eventName);
                if (eachUser.getCurRoom() == room.getId()) {
                    Lobby.makeLobby().getUserIdSessionMap().get(eachUser.getUserId()).getRemote().sendString(String.valueOf(jo));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * joinRoomUserListEventRes.
     * @param eventName e
     * @param session s
     * @param lobby l
     * @param roomList r
     */
    public static void joinRoomUserListEventRes(String eventName, Session session, Lobby lobby, List<RoomAbs> roomList) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty(eventName, new Gson().toJson(roomList));
            jo.addProperty("resType", eventName);
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }

    /**
     * switchRoomUserListEventRes.
     * @param eventName e
     * @param session s
     * @param lobby l
     * @param roomList r
     */
    public static void switchRoomUserListEventRes(String eventName, Session session, Lobby lobby, List<RoomAbs> roomList) {
        try {
            System.out.println("TEST");
            System.out.println(roomList);
            JsonObject jo = new JsonObject();
            jo.addProperty(eventName, new Gson().toJson(roomList));
            jo.addProperty("resType", eventName);
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }

    /**
     * switchRoomEventRes.
     * @param eventName e
     * @param room r
     * @param curUser c
     */
    public static void switchRoomEventRes(String eventName, RoomAbs room, User curUser) {
        room.getMembers().forEach(eachUser -> {
            try {
                JsonObject jo = new JsonObject();
                jo.addProperty("notification", "User: " + curUser.getName() + " Comes Back");
                jo.addProperty("user", new Gson().toJson(eachUser));
                jo.addProperty("userList", new Gson().toJson(room.getMembers()));
                jo.addProperty("room", new Gson().toJson(room));
                jo.addProperty("resType", eventName);
                if (eachUser.getCurRoom() == room.getId()) {
                    Lobby.makeLobby().getUserIdSessionMap().get(eachUser.getUserId()).getRemote().sendString(String.valueOf(jo));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * inviteEventRes.
     * @param eventName e
     * @param room r
     * @param curUser c
     * @param invitedUser i
     * @param lobby l
     */
    public static void inviteEventRes(String eventName, RoomAbs room, User curUser, User invitedUser, Lobby lobby) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("invitation", curUser.getName() + " invited you to join：room-");
            jo.addProperty("invitedUser", new Gson().toJson(invitedUser));
            jo.addProperty("room", new Gson().toJson(room));
            jo.addProperty("userList", new Gson().toJson(room.getMembers()));
            jo.addProperty("resType", eventName);
            Session invitedUserSession = lobby.getUserIdSessionMap().get(invitedUser.getUserId());
            invitedUserSession.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
            Session invitedUserSession = lobby.getUserIdSessionMap().get(invitedUser.getUserId());
            MsgToClientSender.errorMessageRes(invitedUserSession, "Invitation Failed");
        }
    }

    /**
     * inviteUserListEventRes.
     * @param eventName e
     * @param session s
     * @param lobby l
     * @param userList u
     */
    public static void inviteUserListEventRes(String eventName, Session session, Lobby lobby, List<User> userList) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty(eventName, new Gson().toJson(userList));
            jo.addProperty("resType", eventName);
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }

    /**
     * quiteRoomEventRes.
     * @param session s
     * @param eventName e
     * @param room r
     * @param curUser c
     */
    public static void quiteRoomEventRes(Session session, String eventName, RoomAbs room, User curUser) {
        room.getMembers().forEach(eachUser -> {
            try {
                JsonObject jo = new JsonObject();
                jo.addProperty("notification", "User: " + curUser.getName() + " Quit Room");
                jo.addProperty("userList", new Gson().toJson(room.getMembers()));
                jo.addProperty("leftUser", new Gson().toJson(curUser));
                jo.addProperty("room", new Gson().toJson(room));
                jo.addProperty("resType", eventName);
                if (eachUser.getCurRoom() == room.getId()) {
                    Lobby.makeLobby().getUserIdSessionMap().get(eachUser.getUserId()).getRemote().sendString(String.valueOf(jo));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("notification", "You Quit Room-" + room.getId());
            jo.addProperty("user", new Gson().toJson(curUser));
            jo.addProperty("resType", "quiter");
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * banUserEventRes.
     * @param eventName e
     * @param room r
     * @param curUser c
     * @param banUser b
     */
    public static void banUserEventRes(String eventName, RoomAbs room, User curUser, User banUser) {
        room.getMembers().forEach(eachUser -> {
            try {
                JsonObject jo = new JsonObject();
                jo.addProperty("notification", banUser.getName() + " is banned from Room-" + room.getId() + " by admin: " + curUser.getName());
                jo.addProperty("bannedUser", new Gson().toJson(banUser));
                jo.addProperty("user", new Gson().toJson(eachUser));
                jo.addProperty("room", new Gson().toJson(room));
                jo.addProperty("resType", eventName);
                if (eachUser.getCurRoom() == room.getId()) {
                    Lobby.makeLobby().getUserIdSessionMap().get(eachUser.getUserId()).getRemote().sendString(String.valueOf(jo));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("notification", "You are banned from Room-" + room.getId() + " by admin: " + curUser.getName());
            jo.addProperty("user", new Gson().toJson(banUser));
            jo.addProperty("resType", "banned");
            Lobby.makeLobby().getUserIdSessionMap().get(banUser.getUserId()).getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void banHateSpeechRes(String eventName, RoomAbs room, User curUser, User banUser) {
        room.getMembers().forEach(eachUser -> {
            if (eachUser.getUserId() != banUser.getUserId()) {
                try {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("notification", banUser.getName() + " is banned from Room-" + room.getId() + " by admin: " + curUser.getName()+ ", because of hate speech.");
                    jo.addProperty("bannedUser", new Gson().toJson(banUser));
                    jo.addProperty("user", new Gson().toJson(eachUser));
                    jo.addProperty("room", new Gson().toJson(room));
                    jo.addProperty("resType", eventName);
                    if (eachUser.getCurRoom() == room.getId())
                        Lobby.makeLobby().getUserIdSessionMap().get(eachUser.getUserId()).getRemote().sendString(String.valueOf(jo));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("notification", "You are banned from Room-" + room.getId() + " by admin: " + curUser.getName() + ", because of hate speech.");
            jo.addProperty("user", new Gson().toJson(banUser));
            jo.addProperty("resType", "banned");
            Lobby.makeLobby().getUserIdSessionMap().get(banUser.getUserId()).getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void warnHateSpeechRes(String eventName, RoomAbs room, User curUser, User banUser) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("notification", "You are warned from Room-" + room.getId() + " by admin: " + curUser.getName() + ", because of hate speech.");
            jo.addProperty("user", new Gson().toJson(banUser));
            jo.addProperty("resType", "banned");
            Lobby.makeLobby().getUserIdSessionMap().get(banUser.getUserId()).getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void banedUserRes(String eventName, RoomAbs room, User curUser, User banUser) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("notification", "You are banned from Room-" + room.getId() + " by admin: " + curUser.getName() + ", because of hate speech.");
            jo.addProperty("user", new Gson().toJson(banUser));
            jo.addProperty("resType", "banned");
            Lobby.makeLobby().getUserIdSessionMap().get(banUser.getUserId()).getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * banUserListEventRes.
     * @param eventName e
     * @param session s
     * @param lobby l
     * @param userList u
     */
    public static void banUserListEventRes(String eventName, Session session, Lobby lobby, List<User> userList) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty(eventName, new Gson().toJson(userList));
            jo.addProperty("resType", eventName);
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }

    /**
     * privateUserListEventRes.
     * @param eventName e
     * @param session s
     * @param lobby l
     * @param userList u
     */
    public static void privateUserListEventRes(String eventName, Session session, Lobby lobby, List<User> userList) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty(eventName, new Gson().toJson(userList));
            jo.addProperty("resType", eventName);
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }

    /**
     * reportUserEventRes.
     * @param session s
     * @param eventName e
     * @param lobby l
     * @param room r
     * @param curUser c
     * @param admin a
     * @param reportedUser r
     */
    public static void reportUserEventRes(Session session, String eventName, Lobby lobby, RoomAbs room, User curUser, User admin, User reportedUser) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("report", curUser.getName() + " reported User: " + reportedUser.getName());
            jo.addProperty("reportedUser", new Gson().toJson(reportedUser));
            jo.addProperty("room", new Gson().toJson(room));
            //jo.addProperty("userList", new Gson().toJson(room.getMembers()));
            jo.addProperty("resType", eventName);
            //Session reportedUserSession = lobby.getUserIdSessionMap().get(reportedUser.getUserId());
            Session adminSession = lobby.getUserIdSessionMap().get(admin.getUserId());
            adminSession.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Report Failed");
        }
    }

    /**
     *  reportUserListEventRes.
     * @param eventName e
     * @param session s
     * @param lobby l
     * @param userList u
     */
    public static void reportUserListEventRes(String eventName, Session session, Lobby lobby, List<User> userList) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty(eventName, new Gson().toJson(userList));
            jo.addProperty("resType", eventName);
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }

    /**
     * recallMsgEventRes.
     * @param eventName e
     * @param room r
     * @param message m
     */
    public static void sendPublicMsgEventRes(String eventName, Session session, User curUser, RoomAbs room, Message message) {
        room.getMembers().forEach(eachUser -> {
            try {
                JsonObject jo = new JsonObject();
                jo.addProperty("curUser", new Gson().toJson(curUser));
                jo.addProperty("curMsg", new Gson().toJson(message));
                jo.addProperty("curRoom", new Gson().toJson(room));
                jo.addProperty("MsgList", new Gson().toJson(room.getMsgList()));
                jo.addProperty("LOR", "LEFT");
                jo.addProperty("resType", eventName);
                if (eachUser.getCurRoom() == room.getId() && curUser.getUserId() != eachUser.getUserId()) {
                    Lobby.makeLobby().getUserIdSessionMap().get(eachUser.getUserId()).getRemote().sendString(String.valueOf(jo));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("curUser", new Gson().toJson(curUser));
            jo.addProperty("curMsg", new Gson().toJson(message));
            jo.addProperty("curRoom", new Gson().toJson(room));
            jo.addProperty("MsgList", new Gson().toJson(room.getMsgList()));
            jo.addProperty("LOR", "RIGHT");
            jo.addProperty("resType", eventName);
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * editMsgEventRes.
     * @param eventName e
     * @param room r
     * @param message m
     */
    public static void sendPrivateMsgEventRes(String eventName, Session session, User curUser, User privateReceiver, RoomAbs room, Message message) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("curUser", new Gson().toJson(curUser));
            jo.addProperty("curMsg", new Gson().toJson(message));
            jo.addProperty("curRoom", new Gson().toJson(room));
            jo.addProperty("privateReceiver", new Gson().toJson(privateReceiver));
            jo.addProperty("MsgList", new Gson().toJson(room.getMsgList()));
            jo.addProperty("LOR", "LEFT");
            jo.addProperty("resType", eventName);
            if (privateReceiver.getCurRoom() == room.getId() && curUser.getUserId() != privateReceiver.getUserId()) {
                Lobby.makeLobby().getUserIdSessionMap().get(privateReceiver.getUserId()).getRemote().sendString(String.valueOf(jo));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("curUser", new Gson().toJson(curUser));
            jo.addProperty("curMsg", new Gson().toJson(message));
            jo.addProperty("curRoom", new Gson().toJson(room));
            jo.addProperty("MsgList", new Gson().toJson(room.getMsgList()));
            jo.addProperty("LOR", "RIGHT");
            jo.addProperty("privateReceiver", new Gson().toJson(privateReceiver));
            jo.addProperty("resType", eventName);
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * recallMsgEventRes.
     * @param eventName e
     * @param room r
     * @param message m
     * @param  curUser c
     */
    public static void recallMsgEventRes(String eventName, RoomAbs room, Message message, User curUser) {
        room.getMembers().forEach(eachUser -> {
            try {
                JsonObject jo = new JsonObject();
                jo.addProperty("notification", curUser.getName() + " recalled message");
                jo.addProperty("recallMsgId", message.getId());
                jo.addProperty("resType", eventName);
                if (eachUser.getCurRoom() == room.getId() && eachUser.getUserId() != curUser.getUserId()) {
                    Lobby.makeLobby().getUserIdSessionMap().get(eachUser.getUserId()).getRemote().sendString(String.valueOf(jo));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("notification", "You recalled message");
            jo.addProperty("recallMsgId", message.getId());
            jo.addProperty("resType", eventName);
            Lobby.makeLobby().getUserIdSessionMap().get(curUser.getUserId()).getRemote().sendString(String.valueOf(jo));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * getProfileRes.
     * @param eventname e
     * @param curUser c
     * @param session s
     */
    public static void getProfileRes(String eventname, User curUser, Session session) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("user", new Gson().toJson(curUser));
            jo.addProperty("resType", eventname);
            System.out.println(jo);
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }


    }

    /**
     * deleteMsgEventRes.
     * @param eventName e
     * @param session s
     * @param room r
     * @param curUser c
     * @param message m
     * @param msgId m
     */
    public static void deleteMsgEventRes(String eventName, Session session, RoomAbs room, User curUser, Message message, int msgId) {
        room.getMembers().forEach(eachUser -> {
            try {
                JsonObject jo = new JsonObject();
                jo.addProperty("notification", Lobby.makeLobby().getSessionUserMap().get(session).getName() + " delete message");
                jo.addProperty("curUser", new Gson().toJson(curUser));
                jo.addProperty("deletedMsgId", msgId);
                jo.addProperty("curRoom", new Gson().toJson(room));
                jo.addProperty("MsgList", new Gson().toJson(room.getMsgList()));
                jo.addProperty("resType", eventName);
                if (eachUser.getCurRoom() == room.getId()) {
                    Lobby.makeLobby().getUserIdSessionMap().get(eachUser.getUserId()).getRemote().sendString(String.valueOf(jo));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * EditMsgPreEventRes.
     * @param eventName e
     * @param session s
     * @param room r
     * @param curUser c
     * @param message m
     * @param msgId m
     */
    public static void editMsgPreEventRes(String eventName, Session session, RoomAbs room, User curUser, Message message, int msgId) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("curUser", new Gson().toJson(curUser));
            jo.addProperty("msgId", msgId);
            jo.addProperty("message", new Gson().toJson(message));
            jo.addProperty("curRoom", new Gson().toJson(room));
            jo.addProperty("MsgList", new Gson().toJson(room.getMsgList()));
            jo.addProperty("resType", eventName);
            if (curUser.getCurRoom() == room.getId()) {
                session.getRemote().sendString(String.valueOf(jo));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * EditMsgEventRes.
     * @param eventName e
     * @param session s
     * @param room r
     * @param curUser c
     * @param message m
     * @param msgId m
     * @param content c
     */
    public static void editMsgEventRes(String eventName, Session session, RoomAbs room, User curUser, Message message, int msgId, String content) {
        room.getMembers().forEach(eachUser -> {
            try {
                JsonObject jo = new JsonObject();
                jo.addProperty("notification", curUser.getName() + " edit message");
                jo.addProperty("curUser", new Gson().toJson(curUser));
                jo.addProperty("user", new Gson().toJson(eachUser));
                jo.addProperty("editMsgId", msgId);
                jo.addProperty("message", new Gson().toJson(message));
                jo.addProperty("content", content);
                jo.addProperty("curRoom", new Gson().toJson(room));
                jo.addProperty("MsgList", new Gson().toJson(room.getMsgList()));
                jo.addProperty("resType", eventName);
                jo.addProperty("LOR", "LEFT");
                if (eachUser.getCurRoom() == room.getId() && eachUser.getUserId() != curUser.getUserId()) {
                    Lobby.makeLobby().getUserIdSessionMap().get(eachUser.getUserId()).getRemote().sendString(String.valueOf(jo));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("notification",  "You edit the message");
            jo.addProperty("curUser", new Gson().toJson(curUser));
            jo.addProperty("user", new Gson().toJson(curUser));
            jo.addProperty("editMsgId", msgId);
            jo.addProperty("message", new Gson().toJson(message));
            jo.addProperty("content", content);
            jo.addProperty("curRoom", new Gson().toJson(room));
            jo.addProperty("MsgList", new Gson().toJson(room.getMsgList()));
            jo.addProperty("resType", eventName);
            jo.addProperty("LOR", "RIGHT");
            session.getRemote().sendString(String.valueOf(jo));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * error message dealt.
     * @param session s
     * @param errorMessage e
     */
    public static void errorMessageRes(Session session, String errorMessage) {
        try {
            JsonObject jo = new JsonObject();
            jo.addProperty("notification", errorMessage);
            jo.addProperty("resType", "failed");
            session.getRemote().sendString(String.valueOf(jo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
