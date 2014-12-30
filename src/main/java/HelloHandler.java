import org.eclipse.jetty.server.HttpConnection;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.websocket.server.WebSocketServerConnection;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;

/**
 * This class is the main hanlder of HTTP requests. It received the PING requests to keep the connection open.
 * It receives the requests from the Desktop app to update the slide index.
 * It also receives the requests from the Glass device to forward commands, request the notes, and the current index.
 * Finally, it handles any request not meant to go the the /incoming/ context, destined for websockets.
 * Created by David on 10/1/14.
 */
public class HelloHandler extends AbstractHandler {
    WebSocketServerFactory _webSocketFactory;

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //If it goes to /incoming/ we let it pass since it must go to the servlet to establish a WebSocket connection.
        if (target.equals("/incoming/")) {
            return;
        }
        String requestMethod = request.getMethod();
        String action = request.getParameter("Action");
        //Handling POST requests
        if (requestMethod.equals("POST")) {
            System.out.print(requestMethod);
            //This request is sent from glass asking for the notes
            if (action.equals("Action Notes")) {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                System.err.println("hola hola hola "+Main.notes);
                //Sending the notes to the glass application.
                response.getWriter().println(Main.notes);
                //Tell the computer to start the presentation
                SessionHQ.getInstance().sendAction("tkraska", "Action Start");
            } else if (action.equals("Update Index")) {
                //This request is sent from the Desktop app to update the current slide index.
                Main.index = Integer.parseInt(request.getParameter("Current Index"));
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                System.out.println("The current index is:" + Main.index);
            } else {
                //This case is for the regular command sent to glass.
                SessionHQ.getInstance().sendAction("tkraska", action);
            }
        } else if (requestMethod.equals("GET")){
            // Handling GET requests
            if (action == null) {
                //When they visit the group's webpage
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                response.getWriter().println("<h1>Welcome to Glass App. Developed by David Correa Orozco, Mentored by Tim Kraska</h1>" +
                        "<h3>This Google Glass application complements the use of Microsoft Powerpoint on a Macintosh machine in a teaching setting.\n" +
                        "The original objective for this application was to have the following basic requirements:</h3>\n" +
                                "<ul>\n" +
                                "  <h3><li>Display the presentation notes for each slide in Microsoft PowerPoint.</li></h3>\n" +
                                "  <h3><li>Parse LaTeX equations and present them as images to the user as part of the notes.</li></h3>\n" +
                                "  <h3><li>Prevent the device from going to sleep while there is an ongoing presentation.</li></h3>\n" +
                                "  <h3><li>Allow the user to control the flow of the presentation from the Glass device.</li></h3>\n" +
                                "  <h3><li>Maintain a full synchronization between the Glass device and the PowerPoint presentation. Any change in made in the Glass device must be reflected on the presentation and any change in the presentation itself must trigger an update in the Glass device.</li></h3>\n" +
                                "</ul>" +
                        "<h3>This application built during this semester is the first part of a two semester project, to develop a more complex application for an honors undergraduate thesis.</h3>");
            } else if (action.equals("IMAGE")) {
                //When the glass device requests a particular image
                String equation = request.getParameter("Equation");
                response.setContentType("image/png");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                OutputStream outputStream = response.getOutputStream();
                //We return the image from the table.
                ImageIO.write(Main._filesTable.get(equation), "png", outputStream);
                outputStream.close();
            } else if (action.equals("INDEX")) {
                //When the glass device requests the current index.
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                System.err.println("We are returning the index" + Main.index);
                //Sending the index to the glass application.
                response.getWriter().println(Integer.toString(Main.index));
            }
        } else {
            //Other default scenario
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            response.getWriter().println("<h1>Welcome to Glass App. Developed by David Correa, Mentored by Tim Kraska</h1>");
        }
    }




}
