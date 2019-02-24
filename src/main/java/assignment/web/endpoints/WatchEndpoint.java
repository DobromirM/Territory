package assignment.web.endpoints;

import assignment.game.Game;
import assignment.web.responses.ServerResponse;
import assignment.web.responses.ServerResponseEncoder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * Endpoint for watching a game
 */
@ServerEndpoint (value = "/watch/{room}", encoders = ServerResponseEncoder.class)
public class WatchEndpoint
{
    private Integer room;
    private Session currentSession;
    
    /**
     * Add the new session to the session pool of that room
     *
     * @param roomId  - Id of the room
     * @param session - The new open session
     */
    @OnOpen
    public void onOpen(@PathParam ("room") Integer roomId, Session session) throws Exception
    {
        currentSession = session;
        room = roomId;
        PlayEndpoint.addToSessions(roomId, session);
        session.getBasicRemote().sendObject(new ServerResponse(Game.getGameRoom(roomId)));
    }
    
    /**
     * Remove the session from the subscribers pool
     */
    @OnClose
    public void onClose()
    {
        PlayEndpoint.sessions.get(room).remove(currentSession);
    }
    
    /**
     * Ignore messages
     */
    @OnMessage
    public void onMessage(String message)
    {
    
    }
    
    /**
     * Ignore errors
     * <p>
     * Note: This ignores errors related to the client just closing his browser
     * if not ignored they can be used for a DDoS attack
     *
     * @param t - Throwable
     */
    @OnError
    public void on(Throwable t)
    {
    }
}
