package edu.rice.comp504.model.cmd.messagecmd;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.message.Message;
import edu.rice.comp504.model.message.MessageFactory;
import edu.rice.comp504.model.message.MessageType;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.p;

public class SendMsgCmd implements ICmd {
    private final Lobby lobby;

    public SendMsgCmd() {
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
        boolean skipPrivate = false;
        if (body.get("privateReceiver").isEmpty() || body.get("privateReceiver").equals("")) {
            skipPrivate = true;
        }

        try {
            if (room.isInRoom(user)) {

                Message message = MessageFactory.getInstance().make(user, null, body.get("content"), body.get("messageType")); // sendTo?
                room.addMsgToList(message);

                if (message.getType() == MessageType.PUBLIC) {
                    MsgToClientSender.sendPublicMsgEventRes(body.get("eventName"), session, user, room, message);
                } else if (!skipPrivate) {
                    User privateReceiver = lobby.getUsers().get(Integer.parseInt(body.get("privateReceiver")));
                    MsgToClientSender.sendPrivateMsgEventRes(body.get("eventName"), session, user, privateReceiver, room, message);
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
