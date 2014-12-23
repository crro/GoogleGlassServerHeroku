import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

public class Main {

    public static String notes = "";
    public static int index = 0;
    public static Hashtable<String, BufferedImage> _filesTable;

    public static BufferedImage createImage(String equation) {
        TeXFormula fomule = new TeXFormula(equation);
        TeXIcon ti = fomule.createTeXIcon(
                TeXConstants.STYLE_DISPLAY, 40);
        BufferedImage b = new BufferedImage(ti.getIconWidth(), ti
                .getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        ti.paintIcon(new JLabel(), b.getGraphics(), 0, 0);
        return b;
    }

    public static void processNotes(String notes) {
        System.out.println("Processing: " + notes);
        String[] notesWords = notes.split("\n");
        for (String note : notesWords) {
            BufferedImage bImage = null;
            String equation = null;
            if (note.contains("PROCSLIDE")) {
                //then we change the slide
                if (note.contains("<<")) {
                    String[] notesDivided = note.split("<<");
                    //we generate the image here and add it to the HashTable
                    equation = notesDivided[1].substring(0, notesDivided[1].length() - 2);
                    bImage = createImage(equation);
                }
            } else if (note.contains("<<") && note.contains(">>")){
                //we post and get an image to post
                equation = note.substring(2, note.length() - 2);
                bImage = createImage(equation);
            } else {/*Ignore it*/}
            if (bImage != null && equation != null) {
                //Store it in the Hashtable
                _filesTable.put(equation, bImage);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //This allowes me to bind it to the dynamic heroku port.
        Server server = new Server(Integer.parseInt(System.getenv("PORT")));
        //We create the Hashtable
        _filesTable = new Hashtable<String, BufferedImage>();

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
