package assignment.bot;

/**
 * Phase of the bot
 * <p>
 * REACH - The bot is trying to set tiles so that it can get the maximum possible reach
 * GUARD - The bot is tyring to guard empty tiles that are not reachable by other players
 * ENDGAME - The bot prioritises all empty tiles that are shared between players and then it fills its guarded tiles
 */
public enum PhaseType
{
    REACH, GUARD, ENDGAME
}
