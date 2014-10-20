import java.util.Hashtable;

/**
 * Created by David on 10/7/14.
 */
public class SessionHQ {
    private static final SessionHQ INSTANCE = new SessionHQ();

    public static SessionHQ getInstance() {
        return INSTANCE;
    }
    //For now, the String will be the userID
    private Hashtable<String, PowerPointSocket> openSessions
            = new Hashtable<String, PowerPointSocket>();

    public void beginSession(String userId, PowerPointSocket pSocket) {
        openSessions.put(userId, pSocket);
    }
    public void removeSession(String userId) {
        openSessions.remove(userId);
    }

    public void sendAction(String userId, String action) {
        PowerPointSocket pSocket = openSessions.get(userId);
        pSocket.session.getRemote().sendStringByFuture(action);
    }

}
