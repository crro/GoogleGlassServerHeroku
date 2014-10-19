import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.io.IOException;

/**
 * Created by David on 10/4/14.
 */
public class PowerPointSocket extends WebSocketAdapter {
    //This was based on a simple EchoSocket
    public Session session;

    @Override
    public void onWebSocketConnect(Session sess)
    {
        super.onWebSocketConnect(sess);
        session = sess;

        System.out.println("Socket Connected: " + sess);
    }

    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        System.out.println("Received TEXT message: " + message);
        String[] words = message.split(" ");
        if (words[0].equals("Hello")) {
            try {
                getSession().getRemote().sendString("Connected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (words[0].equals("Join")) {
            SessionHQ.getInstance().beginSession(words[1], this);
        } else {

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
