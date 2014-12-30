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

/**
 * This is the Main class of the server. Among its most important responsibilities we have:
 * - Maintain the notes sent by the desktop app
 * - Parse LaTeX equations, generate the images and hold them in memory
 * - Maintain the current slide index of the pptx
 */
public class Main {
    //The presentation notes.
    public static String notes = "";
    //The current slide index.
    public static int index = 0;
    //The preloaded images.
    public static Hashtable<String, BufferedImage> _filesTable;

    /**
     * This method creates the image from the equation given.
     * @param equation - The equation to parse
     * @return
     */
    public static BufferedImage createImage(String equation) {
        TeXFormula fomule = new TeXFormula(equation);
        TeXIcon ti = fomule.createTeXIcon(
                TeXConstants.STYLE_DISPLAY, 40);
        BufferedImage b = new BufferedImage(ti.getIconWidth(), ti
                .getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        ti.paintIcon(new JLabel(), b.getGraphics(), 0, 0);
        return b;
    }

    /**
     * This method process the notes in order to preload the necesary equations and store them
     * in a Hashtable.
     * @param notes
     */
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

    /**
     * The mainline does some initialization and set up the servlets and handlers.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //This allowes me to bind it to the dynamic heroku port.
        Server server = new Server(Integer.parseInt(System.getenv("PORT")));
        //We create the Hashtable
        _filesTable = new Hashtable<String, BufferedImage>();

        //This connector already defaults to an HTTPConnection
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(8888);
        server.addConnector(serverConnector);
        HandlerList handlers;
        handlers = new HandlerList();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        handlers.setHandlers(new Handler[]{new HelloHandler(), context});
        server.setHandler(handlers);
        // Add a websocket to a specific path spec
        ServletHolder holderEvents = new ServletHolder("ws-incoming", IncomingServlet.class);
        context.addServlet(holderEvents, "/incoming/*");
        server.start();
        server.join();
    }
}
