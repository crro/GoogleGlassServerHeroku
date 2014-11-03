import org.eclipse.jetty.server.HttpConnection;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.websocket.server.WebSocketServerConnection;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

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
                //Sending the notes to the glass application.
                response.getWriter().println(Main.notes);
                SessionHQ.getInstance().sendAction("tkraska", "Action Start");
            } else if (action.equals("Post Image")) {
                response.setContentType("image/png");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                BufferedInputStream inputStream = new BufferedInputStream(request.getInputStream());
                BufferedImage image = ImageIO.read(inputStream);//Now I got the image
                //I'm going to send it back just to make sure that I'm doing this properly
                System.err.println(response.getContentType());
                OutputStream outputStream = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len =inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
            } else {
                //Since we are not requesting the notes, we just send the action to the client
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
