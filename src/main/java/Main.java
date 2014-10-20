import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;

public class Main {

    public static String notes = "";
    public static void main(String[] args) throws Exception {
        Server server = new Server(Integer.parseInt(System.getenv("PORT")));

        //This connector already defaults to an HTTPConnection
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(8888);
        server.addConnector(serverConnector);
        // We need to create a ServletHandler to parse HTTP requests and those are the ones that the Servlet will
        //Context is for the paths /something.

//        WebSocketServerFactory webSocketServerFactory = new WebSocketServerFactory();
//        try {
//            webSocketServerFactory.init();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        HandlerList handlers;
        handlers = new HandlerList();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        handlers.setHandlers(new Handler[]{new HelloHandler(), context});
        server.setHandler(handlers);
        //server.setHandler(context);
        // Add a websocket to a specific path spec
        ServletHolder holderEvents = new ServletHolder("ws-incoming", IncomingServlet.class);
        context.addServlet(holderEvents, "/incoming/*");




        server.start();
        server.join();
    }
}
