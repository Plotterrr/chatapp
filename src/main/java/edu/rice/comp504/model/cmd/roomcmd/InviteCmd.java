package edu.rice.comp504.model.cmd.roomcmd;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.room.PrivateRoom;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.room.RoomType;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;

import static j2html.TagCreator.p;
import static j2html.TagCreator.s;

public class InviteCmd implements ICmd {
    private final Lobby lobby;

    public InviteCmd() {
        lobby = Lobby.makeLobby();
    }

    @Override
    public void execute(Session session, Map<String, String> body) {
        User curUser = lobby.getSessionUserMap().get(session);
        RoomAbs room = lobby.getRoomMap().get(curUser.getCurRoom());//lobby.getRoomMap().get(Integer.parseInt(body.get("roomId")));
        User invitedUser = lobby.getUserMap().get(Integer.parseInt(body.get("inviteUserId")));
        try {
            System.out.println("Executing " + body.get("eventName") + "CMD" + "; userId: " + curUser.getUserId() + ", roomId: " + room.getId() + ", inviteUser: " + invitedUser.getUserId());
            if (room.getType() == RoomType.PRIVATE && curUser.getUserId() == room.getAdmin().getUserId()) {
                PrivateRoom privateR = (PrivateRoom) room;
                if (privateR.inviteUser(invitedUser)) {
                    MsgToClientSender.inviteEventRes(body.get("eventName"), room, curUser, invitedUser, lobby);
                } else {
                    MsgToClientSender.errorMessageRes(session, "Room is Full");
                }
            } else {
                MsgToClientSender.errorMessageRes(session, "Room is not private or user is not admin");
            }
        } catch (Exception e) {
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }
}