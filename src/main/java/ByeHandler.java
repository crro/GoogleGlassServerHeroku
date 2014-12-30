import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Dummy handler used for learning purposes, kept for future reference.
 * Created by David on 10/1/14.
 */
public class ByeHandler extends AbstractHandler {
    public void handle(String target, Request baseRequest,HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>No World</h1>");
    }
}
