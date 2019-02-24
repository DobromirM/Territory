package gameTests;

import assignment.game.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author DMarinov
 * Created on: 02/Nov/2018
 */
public class PlayerTest
{
    
    @Test
    public void playerCardsTest()
    {
        Player player = new Player(1, "Test", "testColor");
        List<InfluenceCard> actualCards = player.getCards();
        
        List<InfluenceCard> expectedCards = new ArrayList<>();
        expectedCards.add(InfluenceCard.DOUBLE);
        expectedCards.add(InfluenceCard.FREEDOM);
        expectedCards.add(InfluenceCard.REPLACEMENT);
        
        Assert.assertEquals(3, actualCards.size());
        Assert.assertEquals(expectedCards, actualCards);
    }
    
    @Test
    public void playerRemoveCardTest()
    {
        Player player = new Player(1, "Test", "testColor");
        List<InfluenceCard> actualCards = player.getCards();
        player.removeCard(InfluenceCard.DOUBLE);
        
        List<InfluenceCard> expectedCards = new ArrayList<>();
        expectedCards.add(InfluenceCard.FREEDOM);
        expectedCards.add(InfluenceCard.REPLACEMENT);
        
        Assert.assertEquals(2, actualCards.size());
        Assert.assertEquals(expectedCards, actualCards);
        
        expectedCards.remove(InfluenceCard.REPLACEMENT);
        player.removeCard(InfluenceCard.REPLACEMENT);
        
        Assert.assertEquals(1, actualCards.size());
        Assert.assertEquals(expectedCards, actualCards);
        
        player.removeCard(InfluenceCard.FREEDOM);
        Assert.assertEquals(0, actualCards.size());
    }
    
    @Test
    public void playerAddPointsTest()
    {
        Player player = new Player(1, "Test", "testColor");
        Assert.assertEquals(new Integer(0), player.getPoints());
        
        player.addPoint();
        player.addPoint();
        player.addPoint();
        
        Assert.assertEquals(new Integer(3), player.getPoints());
    }
    
    @Test
    public void playerRemovePointsTest()
    {
        Player player = new Player(1, "Test", "testColor");
        Assert.assertEquals(new Integer(0), player.getPoints());
        
        player.removePoint();
        Assert.assertEquals(new Integer(0), player.getPoints());
        
        player.addPoint();
        player.addPoint();
        player.addPoint();
        player.removePoint();
        player.removePoint();
        
        Assert.assertEquals(new Integer(1), player.getPoints());
    }
    
    @Test
    public void playerWithReachableTilesTest()
    {
        Player player = new Player(1, "Test", "testColor");
        GameBoard board = new GameBoard(1, 1);
        
        Set<GameTile> mockReachableTiles = new HashSet<>();
        mockReachableTiles.add(board.getTile(new Coordinates(0, 0)));
        player.setReachableTiles(mockReachableTiles);
        
        Assert.assertFalse(player.isBlocked(board));
    }
    
    @Test
    public void playerWithoutReachableTilesTest()
    {
        Player player = new Player(1, "Test", "testColor");
        GameBoard board = new GameBoard(1, 1);
        player.removeCard(InfluenceCard.FREEDOM);
        
        Assert.assertTrue(player.isBlocked(board));
    }
    
    @Test
    public void playerWithTakenReachableTilesTest()
    {
        Player player = new Player(1, "Test", "testColor");
        GameBoard board = new GameBoard(1, 1);
        board.setTile(new Coordinates(0, 0), 1);
        
        Set<GameTile> mockReachableTiles = new HashSet<>();
        mockReachableTiles.add(board.getTile(new Coordinates(0, 0)));
        player.setReachableTiles(mockReachableTiles);
        
        Assert.assertTrue(player.isBlocked(board));
    }
    
    @Test
    public void playerWithReplaceableReachableTilesTest()
    {
        Player player = new Player(1, "Test", "testColor");
        GameBoard board = new GameBoard(1, 1);
        board.setTile(new Coordinates(0, 0), 2);
        
        Set<GameTile> mockReachableTiles = new HashSet<>();
        mockReachableTiles.add(board.getTile(new Coordinates(0, 0)));
        player.setReachableTiles(mockReachableTiles);
        
        Assert.assertFalse(player.isBlocked(board));
    }
    
    @Test
    public void playerWithReplaceableReachableTilesNoCardTest()
    {
        Player player = new Player(1, "Test", "testColor");
        GameBoard board = new GameBoard(1, 1);
        board.setTile(new Coordinates(0, 0), 2);
        player.removeCard(InfluenceCard.REPLACEMENT);
        
        Set<GameTile> mockReachableTiles = new HashSet<>();
        mockReachableTiles.add(board.getTile(new Coordinates(0, 0)));
        player.setReachableTiles(mockReachableTiles);
        
        Assert.assertTrue(player.isBlocked(board));
    }
    
    @Test
    public void playerWithFreedomTilesTest()
    {
        Player player = new Player(1, "Test", "testColor");
        GameBoard board = new GameBoard(1, 1);
        
        Assert.assertFalse(player.isBlocked(board));
    }
    
    @Test
    public void playerWithFreedomTilesTestNoCard()
    {
        Player player = new Player(1, "Test", "testColor");
        GameBoard board = new GameBoard(1, 1);
        player.removeCard(InfluenceCard.FREEDOM);
        
        Assert.assertTrue(player.isBlocked(board));
    }
    
    @Test
    public void playerRemoveReachableTiles()
    {
        Player player = new Player(1, "Test", "testColor");
        GameBoard board = new GameBoard(1, 1);
        
        Set<GameTile> mockReachableTiles = new HashSet<>();
        mockReachableTiles.add(board.getTile(new Coordinates(0, 0)));
        mockReachableTiles.add(board.getTile(new Coordinates(0, 1)));
        player.setReachableTiles(mockReachableTiles);
        
        Assert.assertEquals(2, player.getReachableTiles().size());
        
        List<GameTile> removeList = new ArrayList<>();
        removeList.add(board.getTile(new Coordinates(0, 1)));
        player.removeReachableTiles(removeList);
        
        Assert.assertEquals(1, player.getReachableTiles().size());
        Assert.assertTrue(player.getReachableTiles().contains(board.getTile(new Coordinates(0, 0))));
    }
}
