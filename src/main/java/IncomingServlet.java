import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * Created by David on 10/4/14.
 */
public class IncomingServlet extends WebSocketServlet {
    @Override
    //This servlet creates a Powerpoint socket every time a socket connects
    //to this particular context (/incoming/*)
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.register(PowerPointSocket.class);
    }
}
