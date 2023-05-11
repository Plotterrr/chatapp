package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.Lobby;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;

/**
 * Interface used to pass command.
 */
public interface ICmd {
    /**
     * Execute the command on a line.
     * @param body The line.
     */
    public void execute(Session session, Map<String, String> body);
}
