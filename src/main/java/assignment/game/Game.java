package assignment.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Main game class holding references to all game room sessions
 */
public class Game
{
    //Unique id for the next game room session
    private static Integer uniqueId = 0;
    
    //Thread safe list holding all game room session, initialized with 3 starting rooms
    private static List<GameRoomSession> gameRoomSessions = Collections.synchronizedList(new ArrayList<>(Arrays.asList(createGameRoom(), createGameRoom(), createGameRoom())));
    
    /**
     * Find the game room with the specified id
     *
     * @param id - Id of the game room
     *
     * @return - Game room object
     */
    public static GameRoomSession getGameRoom(Integer id)
    {
        return gameRoomSessions.stream().filter(r -> r.getId().equals(id)).findFirst().get();
    }
    
    /**
     * Return the full list of game rooms
     *
     * @return - List of game rooms
     */
    public static List<GameRoomSession> getGameRoomSessions()
    {
        return gameRoomSessions;
    }
    
    /**
     * Add a new game room to the pool
     */
    public static void addGameRoom()
    {
        gameRoomSessions.add(createGameRoom());
    }
    
    /**
     * Create a new game room with the next unique id
     *
     * @return - New game room
     */
    private static GameRoomSession createGameRoom()
    {
        uniqueId++;
        return new GameRoomSession(uniqueId);
    }
}
