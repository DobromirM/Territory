package assignment.game;

/**
 * Statuses for the game
 * <p>
 * WAITING_FOR_PLAYERS - The game has not started and is waiting for players to join or to set their status to 'ready'
 * PLAYING - The game is currently in progress
 * FINISHED - The game has finished
 */
public enum GameStatus
{
    WAITING_FOR_PLAYERS, PLAYING, FINISHED
}
