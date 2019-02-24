package assignment.game;

import java.util.Objects;

/**
 * Object representing a single game tile for the board
 */
public class GameTile
{
    private Integer value;
    private Coordinates coordinates;
    
    GameTile(Coordinates coordinates)
    {
        this.coordinates = coordinates;
    }
    
    public Integer getValue()
    {
        return value;
    }
    
    void setValue(Integer value)
    {
        this.value = value;
    }
    
    public Coordinates getCoordinates()
    {
        return coordinates;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        GameTile tile = (GameTile) o;
        return Objects.equals(value, tile.value) && Objects.equals(coordinates, tile.coordinates);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(value, coordinates);
    }
}
