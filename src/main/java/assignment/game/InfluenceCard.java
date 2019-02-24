package assignment.game;

/**
 * Statuses for the game
 * <p>
 * NONE - The default card used for regular moves
 * DOUBLE - Special card allowing for two consecutive moves
 * REPLACEMENT - Special card allowing the player to replace a tile set by any of the opponents
 * FREEDOM - Special card allowing the player to place a tile on any empty space of the board
 */
public enum InfluenceCard
{
    NONE, DOUBLE, REPLACEMENT, FREEDOM
}
