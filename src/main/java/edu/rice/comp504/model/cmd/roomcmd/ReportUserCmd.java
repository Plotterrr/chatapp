package edu.rice.comp504.model.cmd.roomcmd;

import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;

public class ReportUserCmd implements ICmd {
    private final Lobby lobby;

    public ReportUserCmd() {
        lobby = Lobby.makeLobby();
    }

    @Override
    public void execute(Session session, Map<String, String> body) {

        try {
            User curUser = lobby.getSessionUserMap().get(session);
            RoomAbs room = lobby.getRoomMap().get(curUser.getCurRoom());
            User reportedUser = lobby.getUserMap().get(Integer.parseInt(body.get("reportedUserId")));
            User admin = room.getAdmin();
            System.out.println("Executing " + body.get("eventName") + "CMD" + "; userId: " + curUser.getUserId() + ", roomId: " + room.getId() + ", reportedUser: " + reportedUser.getUserId());
            if (curUser.getUserId() != room.getAdmin().getUserId()) {
                MsgToClientSender.reportUserEventRes(session, body.get("eventName"), lobby, room, curUser, admin, reportedUser);
            } else {
                MsgToClientSender.errorMessageRes(session, "Admin cannot report, use ban instead");
            }
        } catch (Exception e) {
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }
}
