package assignment.game;

import java.util.*;

/**
 * Object representing a player
 */
public class Player
{
    //Id of the player
    private Integer id;
    private List<InfluenceCard> cards;
    
    //All tiles that the player can reach
    private Set<GameTile> reachableTiles;
    private Integer points = 0;
    private Boolean ready = false;
    private String name;
    private String color;
    
    public Player(Integer id, String name, String color)
    {
        this.id = id;
        this.name = name;
        this.color = color;
        this.cards = new ArrayList<>();
        cards.add(InfluenceCard.DOUBLE);
        cards.add(InfluenceCard.FREEDOM);
        cards.add(InfluenceCard.REPLACEMENT);
        reachableTiles = new HashSet<>();
    }
    
    public Integer getId()
    {
        return id;
    }
    
    public List<InfluenceCard> getCards()
    {
        return cards;
    }
    
    public Set<GameTile> getReachableTiles()
    {
        return reachableTiles;
    }
    
    public Integer getPoints()
    {
        return points;
    }
    
    public void setReady(Boolean ready)
    {
        this.ready = ready;
    }
    
    public Boolean isReady()
    {
        return ready;
    }
    
    public void setReachableTiles(Set<GameTile> reachableTiles)
    {
        this.reachableTiles = reachableTiles;
    }
    
    /**
     * Remove card from the player's hand
     *
     * @param card - Card to be removed
     */
    public void removeCard(InfluenceCard card)
    {
        cards.remove(card);
    }
    
    /**
     * Add reachable tiles to the list of reachable tiles of the player
     *
     * @param tiles - List of new tiles to be added
     */
    public void addReachableTiles(List<GameTile> tiles)
    {
        this.reachableTiles.addAll(tiles);
    }
    
    /**
     * Remove reachable tiles from the list of reachable tiles of the player
     * Note: Used when an opponent replaces your tile
     *
     * @param tiles - List of tiles that the player can no longer reach
     */
    public void removeReachableTiles(List<GameTile> tiles)
    {
        this.reachableTiles.removeAll(tiles);
    }
    
    /**
     * Add a point to the player's score
     */
    public void addPoint()
    {
        this.points = points + 1;
    }
    
    /**
     * Remove a point from the player's score
     */
    public void removePoint()
    {
        if (this.points > 0)
        {
            this.points = points - 1;
        }
    }
    
    /**
     * Check if the player can do any moves
     *
     * @param board - Game board
     *
     * @return - Boolean - Whether or not the player can make any legal moves
     */
    public boolean isBlocked(GameBoard board)
    {
        if (hasEmptyReachableTile())
        {
            return false;
        }
        
        if (this.getCards().contains(InfluenceCard.REPLACEMENT))
        {
            if (hasReplaceableReachableTile())
            {
                return false;
            }
        }
        
        if (this.getCards().contains(InfluenceCard.FREEDOM))
        {
            if (board.getEmptyTiles().size() > 0)
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Check if the player has any reachable tiles that are empty
     *
     * @return - Boolean - Whether or not the player has empty tiles in reach
     */
    private boolean hasEmptyReachableTile()
    {
        return this.getReachableTiles().stream().anyMatch(t -> t.getValue() == null);
    }
    
    /**
     * Check if the player has any reachable tiles that can be replaced (Opponent's tiles)
     *
     * @return - Boolean - Whether or not the player has replaceable tiles in reach
     */
    private boolean hasReplaceableReachableTile()
    {
        return this.getReachableTiles().stream().anyMatch(t -> !t.getValue().equals(this.id));
    }
}
