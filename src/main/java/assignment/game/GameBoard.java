package assignment.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Object representing the game board for a single game room
 */
public class GameBoard
{
    private List<GameTile> tiles;
    private Integer rows;
    private Integer columns;
    
    /**
     * Initialize the game board with rows x columns empty tiles
     *
     * @param rows    - Number of rows for the board
     * @param columns - Number of columns for the board
     */
    public GameBoard(Integer rows, Integer columns)
    {
        this.rows = rows;
        this.columns = columns;
        this.tiles = new ArrayList<>();
        
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                this.tiles.add(new GameTile(new Coordinates(i, j)));
            }
        }
    }
    
    /**
     * Return a list of all board tiles which are empty
     *
     * @return - List of tiles
     */
    public List<GameTile> getEmptyTiles()
    {
        return tiles.stream().filter(t -> t.getValue() == null).collect(Collectors.toList());
    }
    
    /**
     * Get a tile by given coordinates
     *
     * @param coord - Coordinates of the tile to be returned
     *
     * @return - Tile at the given coordinates
     */
    public GameTile getTile(Coordinates coord)
    {
        return tiles.stream().filter(t -> t.getCoordinates().equals(coord)).findFirst().orElse(null);
    }
    
    /**
     * Set the value of the tile at the given coordinates
     *
     * @param coord    - Coordinates of the tile to be set
     * @param playerId - Id of the player used as the value of the tile
     */
    public void setTile(Coordinates coord, Integer playerId)
    {
        getTile(coord).setValue(playerId);
    }
    
    /**
     * Check if coordinates are valid for the given board
     *
     * @param coord - Coordinates to validate
     *
     * @return - Bool - response whether or not the coordinates are valid
     */
    public boolean areValidCoordinates(Coordinates coord)
    {
        return coord.getX() >= 0 && coord.getY() >= 0 && coord.getX() < this.rows && coord.getY() < this.columns;
    }
    
    /**
     * Get all adjacent tiles to the tile with given coordinates
     *
     * @param coord - Coordinates of the tile for which the adjacent tiles should be returned
     *
     * @return - List of adjacent tiles
     */
    public List<GameTile> getAdjacentTiles(Coordinates coord)
    {
        int x = coord.getX();
        int y = coord.getY();
        
        List<GameTile> adjacent = new ArrayList<>();
        
        for (int i = x - 1; i <= x + 1; i++)
        {
            for (int j = y - 1; j <= y + 1; j++)
            {
                Coordinates currentCoord = new Coordinates(i, j);
                if (areValidCoordinates(currentCoord))
                {
                    adjacent.add(getTile(currentCoord));
                }
            }
        }
        
        adjacent.remove(getTile(coord));
        
        return adjacent;
    }
    
    /**
     * Find all tiles that are no longer reachable by the player after the tile
     * at the specified coordinates was replaced
     *
     * @param coords   - Coordinates of the replaced tile
     * @param playerId - Id of the player which had the tile before it was replaced
     *
     * @return - List of tiles which are no longer reachable by that player
     */
    public List<GameTile> findUnreachableTiles(Coordinates coords, Integer playerId)
    {
        List<GameTile> unreachableTiles = new ArrayList<>();
        
        //Get a list of all tiles that might not be reachable anymore by the replaced player
        List<GameTile> disputedTiles = getAdjacentTiles(coords);
        
        for (GameTile disputedTile : disputedTiles)
        {
            List<GameTile> adjacentForCurrent = getAdjacentTiles(disputedTile.getCoordinates());
            boolean isUnreachable = adjacentForCurrent.stream().filter(t -> t.getValue() != null).noneMatch(tile -> tile.getValue().equals(playerId));
            
            if (isUnreachable)
            {
                unreachableTiles.add(disputedTile);
            }
        }
        
        return unreachableTiles;
    }
}
