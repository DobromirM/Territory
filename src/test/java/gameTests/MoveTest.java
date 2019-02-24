package gameTests;

import assignment.game.Coordinates;
import assignment.game.InfluenceCard;
import assignment.game.Move;
import assignment.game.Player;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author DMarinov
 * Created on: 02/Nov/2018
 */
public class MoveTest
{
    @Test
    public void validRegularMoveTest()
    {
        Move move = new Move(InfluenceCard.NONE, new Coordinates(1, 2), null);
        Player player = new Player(1, "Test", "testColor");
        move.setPlayer(player);
        
        Assert.assertTrue(move.isValid());
    }
    
    @Test
    public void invalidRegularMoveTest()
    {
        Move firstMove = new Move(null, new Coordinates(1, 2), null);
        Player player = new Player(1, "Test", "testColor");
        firstMove.setPlayer(player);
        
        Assert.assertFalse(firstMove.isValid());
        
        Move secondMove = new Move(InfluenceCard.NONE, null, null);
        secondMove.setPlayer(player);
        Assert.assertFalse(secondMove.isValid());
        
        Move thirdMove = new Move(InfluenceCard.NONE, new Coordinates(1, 2), null);
        Assert.assertFalse(thirdMove.isValid());
    }
    
    @Test
    public void invalidSpecialMoveTest()
    {
        Move move = new Move(InfluenceCard.FREEDOM, new Coordinates(1, 2), null);
        Player player = new Player(1, "Test", "testColor");
        player.removeCard(InfluenceCard.FREEDOM);
        move.setPlayer(player);
        
        Assert.assertFalse(move.isValid());
    }
    
    @Test
    public void validReplacementMoveTest()
    {
        Move move = new Move(InfluenceCard.REPLACEMENT, new Coordinates(1, 2), null);
        Player player = new Player(1, "Test", "testColor");
        move.setPlayer(player);
        
        Assert.assertTrue(move.isValid());
    }
    
    @Test
    public void validFreedomMoveTest()
    {
        Move move = new Move(InfluenceCard.FREEDOM, new Coordinates(1, 2), null);
        Player player = new Player(1, "Test", "testColor");
        move.setPlayer(player);
        
        Assert.assertTrue(move.isValid());
    }
    
    @Test
    public void validDoubleMoveTest()
    {
        Move move = new Move(InfluenceCard.DOUBLE, new Coordinates(1, 2), new Coordinates(5, 5));
        Player player = new Player(1, "Test", "testColor");
        move.setPlayer(player);
        
        Assert.assertTrue(move.isValid());
        Assert.assertNotNull(move.getSecondCoord());
    }
    
    @Test
    public void invalidDoubleMoveTest()
    {
        Move firstMove = new Move(InfluenceCard.DOUBLE, new Coordinates(1, 2), null);
        Player player = new Player(1, "Test", "testColor");
        firstMove.setPlayer(player);
        
        Assert.assertFalse(firstMove.isValid());
    
        Move secondMove = new Move(InfluenceCard.DOUBLE, new Coordinates(1, 2), new Coordinates(1, 2));
        secondMove.setPlayer(player);
    
        Assert.assertFalse(secondMove.isValid());
    }
}
