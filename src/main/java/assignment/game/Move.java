package assignment.game;

/**
 * Object representing a player's move
 */
public class Move
{
    private InfluenceCard card;
    private final Coordinates firstCoord;
    private final Coordinates secondCoord;
    private Player player = null;
    
    public Move(InfluenceCard card, Coordinates firstCoord, Coordinates secondCoord)
    {
        this.card = card;
        this.firstCoord = firstCoord;
        this.secondCoord = secondCoord;
    }
    
    public Move(InfluenceCard card, Coordinates firstCoord, Coordinates secondCoord, Player player)
    {
        this.card = card;
        this.firstCoord = firstCoord;
        this.secondCoord = secondCoord;
        this.player = player;
    }
    
    public Coordinates getFirstCoord()
    {
        return firstCoord;
    }
    
    public InfluenceCard getCard()
    {
        return card;
    }
    
    public Coordinates getSecondCoord()
    {
        return secondCoord;
    }
    
    public Player getPlayer()
    {
        return player;
    }
    
    public void setPlayer(Player player)
    {
        this.player = player;
    }
    
    /**
     * Check if the move is valid or not
     *
     * @return - Boolean - Whether the move is valid or not
     */
    public boolean isValid()
    {
        if (card == null)
        {
            return false;
        }
        
        if (player == null)
        {
            return false;
        }
        
        if (firstCoord == null)
        {
            return false;
        }
        
        if (card != InfluenceCard.NONE && !player.getCards().contains(card))
        {
            return false;
        }
        
        if (card == InfluenceCard.DOUBLE)
        {
            if (secondCoord == null)
            {
                return false;
            }
            
            if (firstCoord.equals(secondCoord))
            {
                return false;
            }
        }
        
        return true;
    }
}
