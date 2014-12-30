import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.io.IOException;

/**
 * This is the class that models the WebSocket that establishes the permanent connection with the
 * Desktop app. Its main functionality is to receive the notes and to forward the pptx commands from glass.
 * Created by David on 10/4/14.
 */
public class PowerPointSocket extends WebSocketAdapter {
    public Session session;

    /**
     * Method called when a connection is established
     * @param sess
     */
    @Override
    public void onWebSocketConnect(Session sess)
    {
        super.onWebSocketConnect(sess);
        session = sess;
        System.out.println("Socket Connected: " + sess);
        //When it is first connected it requests the notes.
        sess.getRemote().sendStringByFuture("Action Notes");
    }

    /**
     * This method is called when a message is received through the socket. Here is where the server
     * receives the first connection and the presentation slides.
     * @param message - message received from the other endpoint.
     */
    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        System.out.println("Received TEXT message: " + message);
        String[] words = message.split(" ");
        if (message.contains("PROCSLIDE")) {
            //Saving the notes
            Main.notes = message;
            Main.processNotes(message);
        } else if (words[0].equals("Hello")) {
            try {
                getSession().getRemote().sendString("Connected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (words[0].equals("Join")) {
            SessionHQ.getInstance().beginSession(words[1], this);
        } else {
            System.err.println("Sorry");
        }

    }

    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode,reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }

    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }

}
