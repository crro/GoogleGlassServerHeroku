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
                //Tell the computer to start the presentation
                SessionHQ.getInstance().sendAction("tkraska", "Action Start");
            } else if (action.equals("GET IMAGE")) {
                //TODO: Change this to make it fetch from the preloaded images.
                response.setContentType("image/png");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                //Creating the image
                String equation = request.getParameter("Equation");
                TeXFormula fomule = new TeXFormula(equation);
                TeXIcon ti = fomule.createTeXIcon(
                        TeXConstants.STYLE_DISPLAY, 40);
                BufferedImage b = new BufferedImage(ti.getIconWidth(), ti
                        .getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
                ti.paintIcon(new JLabel(), b.getGraphics(), 0, 0);
                OutputStream outputStream = response.getOutputStream();
                ImageIO.write(b, "png", outputStream);
                outputStream.close();
            } else if (action.equals("Update Index")) {
                Main.index = Integer.parseInt(request.getParameter("Current Index"));
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                System.out.println("The current index is:" + Main.index);
            } else {
                //Since we are not returning the notes, we just send the action to the client
                SessionHQ.getInstance().sendAction("tkraska", action);
            }
        } else if (requestMethod.equals("GET")){
            if (action == null) {
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
                System.out.print("IMAGE");
                String equation = request.getParameter("Equation");
                response.setContentType("image/png");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                OutputStream outputStream = response.getOutputStream();
                //We return the image from the table.
                ImageIO.write(Main._filesTable.get(equation), "png", outputStream);
                outputStream.close();
            } else if (action.equals("INDEX")) {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);
                System.err.println("We are returning the index" + Main.index);
                //Sending the index to the glass application.
                response.getWriter().println(Integer.toString(Main.index));
            }
        } else {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>Welcome to Glass App. Developed by David Correa, Mentored by Tim Kraska</h1>");
        }
    }




}
