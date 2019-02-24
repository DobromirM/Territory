package gameTests;

import assignment.game.Game;
import assignment.game.GameRoomSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author DMarinov
 * Created on: 02/Nov/2018
 */
public class GameTest
{
    @Test
    public void getGameRoomsTest()
    {
        List<GameRoomSession> gameRoomSessions = Game.getGameRoomSessions();
        GameRoomSession firstGameRoom = Game.getGameRoom(1);
        Assert.assertEquals(firstGameRoom, gameRoomSessions.get(0));
    }
    
    @Test
    public void addGameRoomTest()
    {
        List<GameRoomSession> gameRoomSessions = Game.getGameRoomSessions();
        int size = gameRoomSessions.size();
        
        Game.addGameRoom();
        gameRoomSessions = Game.getGameRoomSessions();
        Assert.assertEquals(size + 1, gameRoomSessions.size());
    }
}
