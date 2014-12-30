# Heroku Server For Google Glass Lecturing App

This is the server hosted in Heroku for the Google Glass Lecturing App. The most important features are:
- Maintaining permanent connections with desktop clients through WebSockets
- Handing HTTP requests (more below)
- Storing the notes from the desktop clients
- Preloading the images corresponding to the text equations sent in the notes. This allows for a faster response time to the Glass application.

The address of the server is: http://googleglassserver.herokuapp.com/ and it is always available.
The code for this server will be exported to Google App Engine in the near future for scalability and financial reasons.

## HTTP Requests

The server handles GET and POST requests that serve several purposes:
- Keep the connection open. This is a simple PING sent by the desktop app.
- Update the current slide index of the presentation.
- Receive presentation commands from the Google Glass device that need to be forwarded to the desktop app.
- Receive requests for the current status of the presentation to update what the Glass is displaying.

## Main.java

This is the Main class of the server. Among its most important responsibilities we have:
 - Maintain the notes sent by the desktop app
 - Parse LaTeX equations, generate the images and hold them in memory
 - Maintain the current slide index of the pptx.

## PowerPointSocket.java

This is the class that models the WebSocket that establishes the permanent connection with the
Desktop app. Its main functionality is to receive the notes and to forward the pptx commands from glass.

## SessionHQ.java

This class stores the different WebSocket sessions of the application. At the moment, there is only one session.

## HelloHandler.java

This class is the main hanlder of HTTP requests. It received the PING requests to keep the connection open.
It receives the requests from the Desktop app to update the slide index.
It also receives the requests from the Glass device to forward commands, request the notes, and the current index.
Finally, it handles any request not meant to go the the /incoming/ context, destined for websockets.

## IncomingServlet.java

This servlet creates a PowerPointSocket every time a socket connects to the particular context (/incoming/*).

## ByeHandler.java
Dummy handler used for learning purposes, kept for future reference.




