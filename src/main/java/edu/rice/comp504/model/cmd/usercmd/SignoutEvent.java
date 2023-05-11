package edu.rice.comp504.model.cmd.usercmd;

import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;

public class SignoutEvent implements ICmd {
    private final Lobby lobby;

    public SignoutEvent() {
        lobby = Lobby.makeLobby();
    }

    @Override
    public void execute(Session session, Map<String, String> body) {
        try {
            User curUser = lobby.getSessionUserMap().get(session);
            lobby.getRooms().forEach(room -> {
                if (room.getMembers().contains(curUser)) {
                    lobby.quitRoom(curUser.getUserId(), room.getId());

                    MsgToClientSender.quiteRoomEventRes(session, "quitRoom", room, curUser);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }
}
