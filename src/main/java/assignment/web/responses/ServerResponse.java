package assignment.web.responses;

import assignment.game.GameRoomSession;

/**
 * Wrapper object representing the server response
 */
public class ServerResponse
{
    private GameRoomSession session;
    private Integer you;
    private String errorMessage;
    
    public ServerResponse(GameRoomSession session)
    {
        this.session = session;
    }
    
    public ServerResponse(GameRoomSession session, Integer you)
    {
        this.session = session;
        this.you = you;
    }
    
    public ServerResponse(GameRoomSession session, Integer you, String errorMessage)
    {
        this.session = session;
        this.you = you;
        this.errorMessage = errorMessage;
    }
    
    public GameRoomSession getSession()
    {
        return session;
    }
    
    public Integer getYou()
    {
        return you;
    }
}
