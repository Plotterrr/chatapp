package edu.rice.comp504.model.cmd;

import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;

public class NullCmd implements ICmd{
    @Override
    public void execute(Session session, Map<String, String> body) {
        System.out.println("This is NULL cmd");
    }
}
