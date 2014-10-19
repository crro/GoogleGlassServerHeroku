import org.eclipse.jetty.server.HttpConnection;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.websocket.server.WebSocketServerConnection;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by David on 10/1/14.
 */
public class HelloHandler extends AbstractHandler {
    WebSocketServerFactory _webSocketFactory;

    public HelloHandler() {
        //_webSocketFactory = factory;
    }
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {

        if (target.equals("/incoming/")) {
            return;
        }
//        try {
//            String method = baseRequest.getMethod();
//            Thread.sleep(3000);
//            SessionHQ.getInstance().sendMessage("tkraska", "Message Start");
//            Thread.sleep(3000);
//            SessionHQ.getInstance().sendMessage("tkraska", "Message Next");
//            Thread.sleep(3000);
//            SessionHQ.getInstance().sendMessage("tkraska", "Message Previous");
//            Thread.sleep(3000);
//            SessionHQ.getInstance().sendMessage("tkraska", "Message End");
//        } catch (InterruptedException t) {
//            t.printStackTrace();
//        }
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>Hello World</h1>");

    }
        //WebSocketServerConnection wbSConnection = (WebSocketServerConnection) request.getAttribute(HttpConnection.UPGRADE_CONNECTION_ATTRIBUTE);
//        response.setContentType("text/html;charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//        baseRequest.setHandled(true);
//        response.getWriter().println("<h1>Hello World</h1>");




}
