package assignment.web.endpoints;

import assignment.game.*;
import assignment.game.Player;
import assignment.web.responses.*;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

/**
 * Endpoint for playing a game
 */
@ServerEndpoint (value = "/play/{id}/{name}/{color}", encoders = ServerResponseEncoder.class, decoders = ClientResponseDecoder.class)
public class PlayEndpoint
{
    //Map for holding all sessions corresponding to the game rooms
    static Map<Integer, List<Session>> sessions = new HashMap<>();
    
    //The game room for the current player
    private GameRoomSession gameRoomSession;
    private Player currentPlayer;
    private Session currentSession;
    
    /**
     * Add the new session to the session pool, create the player and send the current game state back
     * only if the game is not full or hasn't started yet
     *
     * @param roomId  - Id of the room to join
     * @param name    - Name of the player
     * @param color   - Color of the player
     * @param session - The new session that is being opened
     */
    @OnOpen
    public void onOpen(@PathParam ("id") Integer roomId, @PathParam ("name") String name, @PathParam ("color") String color, Session session) throws Exception
    {
        gameRoomSession = Game.getGameRoom(roomId);
        
        synchronized (gameRoomSession)
        {
            if (gameRoomSession.getPlayersCount() < GameRoomSession.getMaxPlayers() && gameRoomSession.getStatus() == GameStatus.WAITING_FOR_PLAYERS)
            {
                currentSession = session;
                addToSessions(roomId, currentSession);
                
                currentPlayer = gameRoomSession.addPlayer(name, color);
                session.getBasicRemote().sendObject(new ServerResponse(gameRoomSession, currentPlayer.getId()));
                broadcast();
            }
            else
            {
                throw new Exception("Cannot join the room");
            }
        }
    }
    
    /**
     * Remove the player and session from the pool on close
     */
    @OnClose
    public void onClose()
    {
        try
        {
            synchronized (gameRoomSession)
            {
                sessions.get(gameRoomSession.getId()).remove(currentSession);
                gameRoomSession.removePlayer(currentPlayer);
                gameRoomSession.startGame(null);
                broadcast();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
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
     * Receive response from the client
     *
     * @param clientResponse - Client response object decoded by the ClientResponseDecoder
     */
    @OnMessage
    public void onMessage(ClientResponse clientResponse) throws IOException, EncodeException
    {
        try
        {
            if (clientResponse.getType() == ClientResponseType.READY && gameRoomSession.getStatus() == GameStatus.WAITING_FOR_PLAYERS)
            {
                currentPlayer.setReady(clientResponse.getReady());
                gameRoomSession.startGame(null);
            }
            else
            {
                Move move = clientResponse.getMove();
                move.setPlayer(currentPlayer);
                gameRoomSession.playTurn(move);
            }
            
            broadcast();
        }
        catch (Exception e)
        {
            currentSession.getBasicRemote().sendObject(new ServerResponse(gameRoomSession, currentPlayer.getId(), e.getMessage()));
        }
    }
    
    /**
     * Broadcast the game room state to all subscribers of the room
     */
    private void broadcast() throws IOException, EncodeException
    {
        for (Session s : sessions.get(gameRoomSession.getId()))
        {
            if (s.isOpen())
            {
                s.getBasicRemote().sendObject(new ServerResponse(gameRoomSession));
            }
        }
        
        LobbyEndpoint.broadcast();
    }
    
    /**
     * Add a new subscriber to the room
     */
    static synchronized void addToSessions(Integer roomId, Session session)
    {
        if (sessions.containsKey(roomId))
        {
            sessions.get(roomId).add(session);
        }
        else
        {
            sessions.put(roomId, new ArrayList<>());
            sessions.get(roomId).add(session);
        }
    }
}
