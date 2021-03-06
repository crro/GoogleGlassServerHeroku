import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * This servlet creates a PowerPointSocket every time a socket connects
 * to the particular context (/incoming/*).
 * Created by David on 10/4/14.
 */
public class IncomingServlet extends WebSocketServlet {
    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.register(PowerPointSocket.class);
    }
}
