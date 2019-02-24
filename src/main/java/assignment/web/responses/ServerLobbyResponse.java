package assignment.web.responses;

import assignment.game.GameRoomSession;

import java.util.List;

/**
 * Wrapper object representing the server response for the lobby endpoint
 */
public class ServerLobbyResponse
{
    //List of all game rooms
    private List<GameRoomSession> gameRoomSessions;
    
    public ServerLobbyResponse(List<GameRoomSession> gameRoomSessions)
    {
        this.gameRoomSessions = gameRoomSessions;
    }
}
