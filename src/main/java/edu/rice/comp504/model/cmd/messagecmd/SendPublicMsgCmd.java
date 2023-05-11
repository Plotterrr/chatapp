package edu.rice.comp504.model.cmd.messagecmd;

import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.message.Message;
import edu.rice.comp504.model.message.MessageFactory;
import edu.rice.comp504.model.message.MessageType;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;

public class SendPublicMsgCmd implements ICmd {
    private final Lobby lobby;

    public SendPublicMsgCmd() {
        lobby = Lobby.makeLobby();
    }

    /**
     * Execute command to add message to the room list.
     *
     * @param body Map with keys: RoomId, UserId, Content, MessageType
     */
    @Override
    public void execute(Session session, Map<String, String> body) {
        RoomAbs room = lobby.getRoomMap().get(Integer.parseInt(body.get("currentRoomId")));
        User user = lobby.getSessionUserMap().get(session);

        String content = body.get("content");
        try {
            if (room.isInRoom(user)) {

                Message message = MessageFactory.getInstance().make(user, null, body.get("content"), body.get("messageType")); // sendTo?
                message.checkMessage();
                if (1<= user.getHateSpeechtimes() && user.getHateSpeechtimes()< 3) {
                    for (int i = 0; i < lobby.getUserJoinedRoomsMap().get(user.getUserId()).size(); i++) {
                        RoomAbs r = lobby.getUserJoinedRoomsMap().get(user.getUserId()).get(i);
                        User admin = r.getAdmin();
                        MsgToClientSender.warnHateSpeechRes("banUser", r, admin, user);
                    }
                } else if (user.getHateSpeechtimes() == 3) {
                    for (int i = 0; i < lobby.getUserJoinedRoomsMap().get(user.getUserId()).size(); i++) {
                        RoomAbs r = lobby.getUserJoinedRoomsMap().get(user.getUserId()).get(i);
                        User admin = r.getAdmin();
                        MsgToClientSender.banHateSpeechRes("banUser", r, admin, user);
                    }
                } else if (user.getHateSpeechtimes() > 3) {
                    for (int i = 0; i < lobby.getUserJoinedRoomsMap().get(user.getUserId()).size(); i++) {
                        RoomAbs r = lobby.getUserJoinedRoomsMap().get(user.getUserId()).get(i);
                        User admin = r.getAdmin();
                        MsgToClientSender.banedUserRes("banUser", r, admin, user);
                    }
                }else {
                    room.addMsgToList(message);

                    if (message.getType() == MessageType.PUBLIC) {
                        MsgToClientSender.sendPublicMsgEventRes(body.get("eventName"), session, user, room, message);
                    }
                }

            } else {
                MsgToClientSender.errorMessageRes(session, "User is not in the room");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }

    }
}
