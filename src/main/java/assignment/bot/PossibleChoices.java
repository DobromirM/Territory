package assignment.bot;

import assignment.game.GameTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Object for possible choices for a move
 */
public class PossibleChoices
{
    //List of tiles for the possible choices
    private List<GameTile> tiles;
    private Integer maxReachable;
    private Integer minGuardable;
    
    public PossibleChoices()
    {
        maxReachable = 0;
        minGuardable = Integer.MAX_VALUE;
        tiles = new ArrayList<>();
    }
    
    public List<GameTile> getTiles()
    {
        return tiles;
    }
    
    public void setTiles(List<GameTile> tiles)
    {
        this.tiles = tiles;
    }
    
    /**
     * Get the first tile from the list or null if it is empty
     *
     * @return - GameTile - First tile from the list
     */
    public GameTile getFirstTile()
    {
        if (tiles.size() > 0)
        {
            return tiles.get(0);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Get the maximum tiles that the player cannot reach which will become reachable if
     * any of the game tiles from the list is selected
     *
     * @return - Tiles count
     */
    public Integer getMaxReachable()
    {
        return maxReachable;
    }
    
    public void setMaxReachable(Integer maxReachable)
    {
        this.maxReachable = maxReachable;
    }
    
    /**
     * Get the minimum empty tiles that the player has to set in order to fully guard any of the
     * tiles from the list
     *
     * @return - Tiles count
     */
    public Integer getMinGuardable()
    {
        return minGuardable;
    }
    
    public void setMinGuardable(Integer minGuardable)
    {
        this.minGuardable = minGuardable;
    }
}
