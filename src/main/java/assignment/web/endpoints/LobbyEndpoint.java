package assignment.web.endpoints;

import assignment.game.Game;
import assignment.web.responses.ServerLobbyResponse;
import assignment.web.responses.ServerLobbyResponseEncoder;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Endpoint for the game lobby
 */
@ServerEndpoint (value = "/lobby", encoders = ServerLobbyResponseEncoder.class)
public class LobbyEndpoint
{
    //List of sessions subscribed to this endpoint
    private static List<Session> sessions = new ArrayList<>();
    
    /**
     * Add the new session to the session pool and send the current game state back
     *
     * @param session - The new session that is being opened
     */
    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException
    {
        sessions.add(session);
        session.getBasicRemote().sendObject(new ServerLobbyResponse(Game.getGameRoomSessions()));
    }
    
    /**
     * Remove the closing session from the pool
     *
     * @param session - Session to be closed
     */
    @OnClose
    public void onClose(Session session)
    {
        sessions.remove(session);
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
    public void onError(Throwable t)
    {
    }
    
    /**
     * Broadcast the the Game state to all subscribed sessions
     */
    static void broadcast() throws IOException, EncodeException
    {
        for (Session s : sessions)
        {
            if (s.isOpen())
            {
                s.getBasicRemote().sendObject(new ServerLobbyResponse(Game.getGameRoomSessions()));
            }
        }
    }
}
