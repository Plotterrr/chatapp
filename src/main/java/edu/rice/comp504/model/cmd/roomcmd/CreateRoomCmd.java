package edu.rice.comp504.model.cmd.roomcmd;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.room.RoomFactory;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;

public class CreateRoomCmd implements ICmd {
    private final Lobby lobby;

    public CreateRoomCmd() {
        lobby = Lobby.makeLobby();
    }

    @Override
    public void execute(Session session, Map<String, String> body) {
        try {
            User user = lobby.getSessionUserMap().get(session);
            int capacity = Integer.parseInt(body.get("capacity"));
            String publicOrPrivate = body.get("publicOrPrivate");
            String interest = body.get("interest");
            RoomAbs newRoom = lobby.createRoom(publicOrPrivate, capacity, interest, user);
            User curUser = lobby.getSessionUserMap().get(session);
            if (lobby.joinRoom(curUser.getUserId(), newRoom.getId())) {
                MsgToClientSender.createRoomEventRes(body.get("eventName"), session, lobby, newRoom);
            } else {
                MsgToClientSender.errorMessageRes(session, "unable to join");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event failed");
        }
    }
}
