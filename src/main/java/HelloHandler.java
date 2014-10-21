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
        String requestMethod = request.getMethod();
        String action = request.getParameter("Action");
        if (requestMethod.equals("POST")) {
            System.out.print(requestMethod);

            if (action.equals("Action Notes")) {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                System.err.println("hola hola hola "+Main.notes);
                response.getWriter().println(Main.notes);
                SessionHQ.getInstance().sendAction("tkraska", "Action Start");
            } else {
                SessionHQ.getInstance().sendAction("tkraska", action);
            }
        } else {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            response.getWriter().println("<h1>Welcome to Glass App. Developed by David Correa, Mentored by Tim Kraska</h1>");
        }
//            Thread.sleep(3000);
//            SessionHQ.getInstance().sendMessage("tkraska", "Message Start");
//            Thread.sleep(3000);
//            SessionHQ.getInstance().sendMessage("tkraska", "Message Next");
//            Thread.sleep(3000);
//            SessionHQ.getInstance().sendMessage("tkraska", "Message Previous");
//            Thread.sleep(3000);
//            SessionHQ.getInstance().sendMessage("tkraska", "Message End");


    }
        //WebSocketServerConnection wbSConnection = (WebSocketServerConnection) request.getAttribute(HttpConnection.UPGRADE_CONNECTION_ATTRIBUTE);
//        response.setContentType("text/html;charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//        baseRequest.setHandled(true);
//        response.getWriter().println("<h1>Hello World</h1>");




}
