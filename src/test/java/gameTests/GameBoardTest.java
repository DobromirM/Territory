package gameTests;

import assignment.game.Coordinates;
import assignment.game.GameBoard;
import assignment.game.GameTile;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author DMarinov
 * Created on: 02/Nov/2018
 */
public class GameBoardTest
{
    
    @Test
    public void oneEmptyTileTest()
    {
        GameBoard board = new GameBoard(1, 2);
        board.setTile(new Coordinates(0, 0), 1);
        
        int emptyTilesCount = board.getEmptyTiles().size();
        
        Assert.assertEquals(1, emptyTilesCount);
    }
    
    @Test
    public void manyEmptyTiles()
    {
        GameBoard board = new GameBoard(3, 3);
        board.setTile(new Coordinates(0, 0), 1);
        
        int emptyTilesCount = board.getEmptyTiles().size();
        
        Assert.assertEquals(8, emptyTilesCount);
    }
    
    @Test
    public void zeroEmptyTiles()
    {
        GameBoard board = new GameBoard(2, 2);
        board.setTile(new Coordinates(0, 0), 1);
        board.setTile(new Coordinates(0, 1), 1);
        board.setTile(new Coordinates(1, 0), 1);
        board.setTile(new Coordinates(1, 1), 1);
        
        int emptyTilesCount = board.getEmptyTiles().size();
        
        Assert.assertEquals(0, emptyTilesCount);
    }
    
    @Test
    public void validCoordinatesTest()
    {
        GameBoard board = new GameBoard(2, 2);
        Coordinates coords = new Coordinates(1, 1);
        
        Assert.assertTrue(board.areValidCoordinates(coords));
    }
    
    @Test
    public void invalidCoordinatesTest()
    {
        GameBoard board = new GameBoard(2, 2);
        Coordinates coords = new Coordinates(1, 3);
        
        Assert.assertFalse(board.areValidCoordinates(coords));
        
        coords = new Coordinates(3, 1);
        Assert.assertFalse(board.areValidCoordinates(coords));
        
        coords = new Coordinates(3, 3);
        Assert.assertFalse(board.areValidCoordinates(coords));
    }
    
    @Test
    public void getTileCoordsTest()
    {
        GameBoard board = new GameBoard(3, 3);
        Coordinates coords = new Coordinates(1, 2);
        GameTile tile = board.getTile(coords);
        
        Assert.assertEquals(coords, tile.getCoordinates());
        
        coords = new Coordinates(0, 0);
        tile = board.getTile(coords);
        
        Assert.assertEquals(coords, tile.getCoordinates());
        
        coords = new Coordinates(2, 0);
        tile = board.getTile(coords);
        
        Assert.assertEquals(coords, tile.getCoordinates());
    }
    
    @Test
    public void getAdjacentTilesCenterTest()
    {
        GameBoard board = new GameBoard(4, 4);
        Coordinates coords = new Coordinates(2, 2);
        
        List<GameTile> tiles = board.getAdjacentTiles(coords);
        
        Assert.assertEquals(8, tiles.size());
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(2, 1))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 1))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 2))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(2, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 2))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 1))));
    }
    
    @Test
    public void getAdjacentTilesEdgeTest()
    {
        GameBoard board = new GameBoard(4, 4);
        Coordinates coords = new Coordinates(2, 3);
        
        List<GameTile> tiles = board.getAdjacentTiles(coords);
        
        Assert.assertEquals(5, tiles.size());
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 2))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(2, 2))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 2))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 3))));
    }
    
    @Test
    public void getAdjacentTilesCornerTest()
    {
        GameBoard board = new GameBoard(4, 4);
        Coordinates coords = new Coordinates(0, 0);
        
        List<GameTile> tiles = board.getAdjacentTiles(coords);
        
        Assert.assertEquals(3, tiles.size());
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(0, 1))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 1))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 0))));
    }
    
    @Test
    public void findUnreachableTilesCenterAllTest()
    {
        GameBoard board = new GameBoard(4, 4);
        Coordinates coords = new Coordinates(2, 2);
        
        List<GameTile> tiles = board.findUnreachableTiles(coords, 1);
        
        Assert.assertEquals(8, tiles.size());
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(2, 1))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 1))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 2))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(2, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 2))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 1))));
    }
    
    @Test
    public void findUnreachableTilesCenterSomeTest()
    {
        GameBoard board = new GameBoard(4, 4);
        Coordinates coords = new Coordinates(2, 2);
        board.setTile(new Coordinates(0, 3), 1);
        
        List<GameTile> tiles = board.findUnreachableTiles(coords, 1);
        
        Assert.assertEquals(6, tiles.size());
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(2, 1))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 1))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 2))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(2, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 1))));
    }
    
    @Test
    public void findUnreachableTilesCenterNoneTest()
    {
        GameBoard board = new GameBoard(4, 4);
        Coordinates coords = new Coordinates(2, 2);
        board.setTile(new Coordinates(0, 2), 1);
        board.setTile(new Coordinates(2, 0), 1);
        board.setTile(new Coordinates(2, 2), 1);
        
        List<GameTile> tiles = board.findUnreachableTiles(coords, 1);
        
        Assert.assertEquals(0, tiles.size());
    }
    
    @Test
    public void findUnreachableTilesEdgeAllTest()
    {
        GameBoard board = new GameBoard(4, 4);
        Coordinates coords = new Coordinates(2, 3);
        
        List<GameTile> tiles = board.findUnreachableTiles(coords, 1);
        
        Assert.assertEquals(5, tiles.size());
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 2))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(2, 2))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 2))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 3))));
    }
    
    @Test
    public void findUnreachableTilesEdgeSomeTest()
    {
        GameBoard board = new GameBoard(4, 4);
        Coordinates coords = new Coordinates(2, 3);
        board.setTile(new Coordinates(0, 1), 1);
        board.setTile(new Coordinates(3, 1), 1);
        
        List<GameTile> tiles = board.findUnreachableTiles(coords, 1);
        
        Assert.assertEquals(2, tiles.size());
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(1, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 3))));
    }
    
    @Test
    public void findUnreachableTilesEdgeNoneTest()
    {
        GameBoard board = new GameBoard(4, 4);
        Coordinates coords = new Coordinates(2, 3);
        board.setTile(new Coordinates(2, 1), 1);
        board.setTile(new Coordinates(0, 2), 1);
        board.setTile(new Coordinates(3, 2), 1);
        
        List<GameTile> tiles = board.findUnreachableTiles(coords, 1);
        
        Assert.assertEquals(0, tiles.size());
    }
    
    @Test
    public void findUnreachableTilesCornerAllTest()
    {
        GameBoard board = new GameBoard(4, 4);
        Coordinates coords = new Coordinates(3, 3);
        
        List<GameTile> tiles = board.findUnreachableTiles(coords, 1);
        
        Assert.assertEquals(3, tiles.size());
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(2, 2))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(2, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 2))));
    }
    
    @Test
    public void findUnreachableTilesCornerSomeTest()
    {
        GameBoard board = new GameBoard(4, 4);
        Coordinates coords = new Coordinates(3, 3);
        board.setTile(new Coordinates(1, 1), 1);
        
        List<GameTile> tiles = board.findUnreachableTiles(coords, 1);
        
        Assert.assertEquals(2, tiles.size());
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(2, 3))));
        Assert.assertTrue(tiles.contains(board.getTile(new Coordinates(3, 2))));
    }
    
    @Test
    public void findUnreachableTilesCornerNoneTest()
    {
        GameBoard board = new GameBoard(4, 4);
        Coordinates coords = new Coordinates(3, 3);
        board.setTile(new Coordinates(1, 1), 1);
        board.setTile(new Coordinates(3, 1), 1);
        board.setTile(new Coordinates(1, 3), 1);
        
        List<GameTile> tiles = board.findUnreachableTiles(coords, 1);
        
        Assert.assertEquals(0, tiles.size());
    }
}
