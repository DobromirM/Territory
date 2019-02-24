package gameTests;

import assignment.game.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author DMarinov
 * Created on: 02/Nov/2018
 */
public class GameRoomSessionTest
{
    
    @Test
    public void startGameTwoPlayersTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        
        Assert.assertEquals(GameStatus.WAITING_FOR_PLAYERS, session.getStatus());
        Assert.assertEquals(new Integer(0), playerFoo.getPoints());
        Assert.assertEquals(new Integer(0), playerBar.getPoints());
        
        session.startGame(null);
        
        Assert.assertEquals(GameStatus.PLAYING, session.getStatus());
        Assert.assertEquals(playerFoo, session.getPlayerOnTurn());
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        Assert.assertTrue(playerFoo.getReachableTiles().size() > 0);
        Assert.assertTrue(playerBar.getReachableTiles().size() > 0);
        Assert.assertEquals(new Integer(1), session.getId());
        Assert.assertEquals(new Integer(5), GameRoomSession.getMaxPlayers());
    }
    
    @Test
    public void startGameFivePlayersTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        Player playerBaz = session.addPlayer("Baz", "baz_color");
        Player playerQux = session.addPlayer("Qux", "qux_color");
        Player playerQuux = session.addPlayer("Quux", "quux_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        playerBaz.setReady(true);
        playerQux.setReady(true);
        playerQuux.setReady(true);
        
        Assert.assertEquals(GameStatus.WAITING_FOR_PLAYERS, session.getStatus());
        Assert.assertEquals(new Integer(0), playerFoo.getPoints());
        Assert.assertEquals(new Integer(0), playerBar.getPoints());
        Assert.assertEquals(new Integer(0), playerBaz.getPoints());
        Assert.assertEquals(new Integer(0), playerQux.getPoints());
        Assert.assertEquals(new Integer(0), playerQuux.getPoints());
        
        session.startGame(null);
        
        Assert.assertEquals(GameStatus.PLAYING, session.getStatus());
        Assert.assertEquals(playerFoo, session.getPlayerOnTurn());
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        Assert.assertEquals(new Integer(1), playerBaz.getPoints());
        Assert.assertEquals(new Integer(1), playerQux.getPoints());
        Assert.assertEquals(new Integer(1), playerQuux.getPoints());
        
        Assert.assertTrue(playerFoo.getReachableTiles().size() > 0);
        Assert.assertTrue(playerBar.getReachableTiles().size() > 0);
        Assert.assertTrue(playerBaz.getReachableTiles().size() > 0);
        Assert.assertTrue(playerQux.getReachableTiles().size() > 0);
        Assert.assertTrue(playerQuux.getReachableTiles().size() > 0);
    }
    
    @Test
    public void startGameOnePlayerTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        
        playerFoo.setReady(true);
        
        Assert.assertEquals(GameStatus.WAITING_FOR_PLAYERS, session.getStatus());
        Assert.assertEquals(new Integer(0), playerFoo.getPoints());
        
        session.startGame(null);
        
        Assert.assertEquals(GameStatus.WAITING_FOR_PLAYERS, session.getStatus());
        Assert.assertNull(session.getPlayerOnTurn());
        Assert.assertEquals(new Integer(0), playerFoo.getPoints());
        
        Assert.assertFalse(playerFoo.getReachableTiles().size() > 0);
    }
    
    @Test
    public void startGameZeroPlayersTest()
    {
        GameRoomSession session = new GameRoomSession(1);
        
        Assert.assertEquals(GameStatus.WAITING_FOR_PLAYERS, session.getStatus());
        
        session.startGame(null);
        
        Assert.assertEquals(GameStatus.WAITING_FOR_PLAYERS, session.getStatus());
        Assert.assertNull(session.getPlayerOnTurn());
    }
    
    @Test
    public void addTooManyPlayersTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        session.addPlayer("Foo", "foo_color");
        session.addPlayer("Bar", "bar_color");
        session.addPlayer("Baz", "baz_color");
        session.addPlayer("Qux", "qux_color");
        session.addPlayer("Quux", "quux_color");
        
        Exception exception = new Exception();
        
        Assert.assertNull(exception.getMessage());
        
        try
        {
            session.addPlayer("Quuz", "quuz_color");
        }
        catch (Exception e)
        {
            exception = e;
        }
        
        Assert.assertEquals("Game is already full!", exception.getMessage());
    }
    
    @Test
    public void startGameThreePlayersWithDisconnectsTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        Player playerBaz = session.addPlayer("Baz", "bar_color");
        
        playerFoo.setReady(true);
        
        session.removePlayer(playerFoo);
        session.removePlayer(playerBaz);
        
        playerBar.setReady(true);
        
        session.startGame(null);
        
        Assert.assertEquals(GameStatus.WAITING_FOR_PLAYERS, session.getStatus());
        Assert.assertEquals(new Integer(0), playerBaz.getPoints());
        
        Player newPlayerBaz = session.addPlayer("Baz", "baz_color");
        Player newPlayerFoo = session.addPlayer("Foo", "foo_color");
        
        newPlayerBaz.setReady(true);
        newPlayerFoo.setReady(true);
        
        session.startGame(null);
        
        Assert.assertEquals(GameStatus.PLAYING, session.getStatus());
        Assert.assertEquals(playerBar, session.getPlayerOnTurn());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        Assert.assertEquals(new Integer(1), newPlayerBaz.getPoints());
        Assert.assertEquals(new Integer(1), newPlayerFoo.getPoints());
        
        Assert.assertTrue(playerBar.getReachableTiles().size() > 0);
        Assert.assertTrue(newPlayerBaz.getReachableTiles().size() > 0);
        Assert.assertTrue(newPlayerFoo.getReachableTiles().size() > 0);
    }
    
    @Test
    public void twoPlayersDisconnectWhilePlayingTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        
        session.startGame(13);
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        session.removePlayer(playerFoo);
        
        Assert.assertEquals(GameStatus.FINISHED, session.getStatus());
        Assert.assertEquals(new Integer(1), session.getPlayersCount());
        Assert.assertEquals(new Integer(2), session.getWinner().getId());
    }
    
    @Test
    public void threePlayersDisconnectWhilePlayingTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        Player playerBaz = session.addPlayer("Baz", "baz_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        playerBaz.setReady(true);
        
        session.startGame(13);
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        Assert.assertEquals(new Integer(1), playerBaz.getPoints());
        
        session.removePlayer(playerFoo);
        
        Assert.assertEquals(GameStatus.PLAYING, session.getStatus());
        Assert.assertEquals(new Integer(2), session.getPlayersCount());
        Assert.assertEquals(playerBar, session.getPlayerOnTurn());
    }
    
    @Test
    public void makeNormalMovesTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        
        session.startGame(13);
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        Move moveFoo = new Move(InfluenceCard.NONE, new Coordinates(2, 3), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(2), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        Move moveBar = new Move(InfluenceCard.NONE, new Coordinates(5, 4), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(2), playerFoo.getPoints());
        Assert.assertEquals(new Integer(2), playerBar.getPoints());
    }
    
    @Test
    public void makeNormalInvalidMovesTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        
        session.startGame(13);
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        try
        {
            
            Move moveFoo = new Move(InfluenceCard.NONE, new Coordinates(2, 2), null);
            moveFoo.setPlayer(playerFoo);
            
            session.playTurn(moveFoo);
        }
        catch (Exception e)
        {
            Assert.assertEquals("You cannot reach this tile!", e.getMessage());
        }
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        try
        {
            Move moveBar = new Move(InfluenceCard.NONE, new Coordinates(5, 4), null);
            moveBar.setPlayer(playerBar);
            session.playTurn(moveBar);
        }
        catch (Exception e)
        {
            Assert.assertEquals("It's not your turn!", e.getMessage());
        }
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        try
        {
            
            Move moveFoo = new Move(InfluenceCard.NONE, new Coordinates(5, 5), null);
            moveFoo.setPlayer(playerFoo);
            
            session.playTurn(moveFoo);
        }
        catch (Exception e)
        {
            Assert.assertEquals("Tile is already taken!", e.getMessage());
        }
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        try
        {
            
            Move moveFoo = new Move(InfluenceCard.NONE, null, null);
            moveFoo.setPlayer(playerFoo);
            
            session.playTurn(moveFoo);
        }
        catch (Exception e)
        {
            Assert.assertEquals("Invalid move!", e.getMessage());
        }
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
    }
    
    @Test
    public void makeFreedomMoveTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        
        session.startGame(13);
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        Move moveFoo = new Move(InfluenceCard.FREEDOM, new Coordinates(1, 1), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(2), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        Assert.assertEquals(new Integer(1), session.getBoard().getTile(new Coordinates(1, 1)).getValue());
    }
    
    @Test
    public void makeFreedomInvalidMoveTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        
        session.startGame(13);
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        try
        {
            Move moveFoo = new Move(InfluenceCard.FREEDOM, new Coordinates(5, 5), null);
            moveFoo.setPlayer(playerFoo);
            
            session.playTurn(moveFoo);
        }
        catch (Exception e)
        {
            Assert.assertEquals("Tile is already taken!", e.getMessage());
        }
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
    }
    
    @Test
    public void makeReplacementMoveTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        
        session.startGame(13);
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        Move moveFoo = new Move(InfluenceCard.NONE, new Coordinates(3, 4), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Move moveBar = new Move(InfluenceCard.NONE, new Coordinates(4, 5), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(2), playerFoo.getPoints());
        Assert.assertEquals(new Integer(2), playerBar.getPoints());
        
        moveFoo = new Move(InfluenceCard.REPLACEMENT, new Coordinates(4, 5), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(3), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        Assert.assertEquals(new Integer(1), session.getBoard().getTile(new Coordinates(4, 5)).getValue());
    }
    
    @Test
    public void makeReplacementInvalidMoveTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        
        session.startGame(13);
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        Move moveFoo = new Move(InfluenceCard.NONE, new Coordinates(3, 4), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Move moveBar = new Move(InfluenceCard.NONE, new Coordinates(4, 5), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(2), playerFoo.getPoints());
        Assert.assertEquals(new Integer(2), playerBar.getPoints());
        
        try
        {
            moveFoo = new Move(InfluenceCard.REPLACEMENT, new Coordinates(3, 4), null);
            moveFoo.setPlayer(playerFoo);
            
            session.playTurn(moveFoo);
        }
        catch (Exception e)
        {
            Assert.assertEquals("Cannot replace your own tile!", e.getMessage());
        }
        
        try
        {
            moveFoo = new Move(InfluenceCard.REPLACEMENT, new Coordinates(5, 5), null);
            moveFoo.setPlayer(playerFoo);
            
            session.playTurn(moveFoo);
        }
        catch (Exception e)
        {
            Assert.assertEquals("You cannot reach this tile!", e.getMessage());
        }
        
        try
        {
            moveFoo = new Move(InfluenceCard.REPLACEMENT, new Coordinates(3, 5), null);
            moveFoo.setPlayer(playerFoo);
            
            session.playTurn(moveFoo);
        }
        catch (Exception e)
        {
            Assert.assertEquals("Cannot replace empty tiles!", e.getMessage());
        }
        
        Assert.assertEquals(new Integer(2), playerFoo.getPoints());
        Assert.assertEquals(new Integer(2), playerBar.getPoints());
    }
    
    @Test
    public void makeDoubleMoveTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        
        session.startGame(13);
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        Move moveFoo = new Move(InfluenceCard.DOUBLE, new Coordinates(2, 5), new Coordinates(2, 6));
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(3), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        Assert.assertEquals(new Integer(1), session.getBoard().getTile(new Coordinates(2, 5)).getValue());
        Assert.assertEquals(new Integer(1), session.getBoard().getTile(new Coordinates(2, 6)).getValue());
        
        Move moveBar = new Move(InfluenceCard.DOUBLE, new Coordinates(5, 4), new Coordinates(5, 6));
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(3), playerFoo.getPoints());
        Assert.assertEquals(new Integer(3), playerBar.getPoints());
        Assert.assertEquals(new Integer(2), session.getBoard().getTile(new Coordinates(5, 4)).getValue());
        Assert.assertEquals(new Integer(2), session.getBoard().getTile(new Coordinates(5, 6)).getValue());
    }
    
    @Test
    public void makeDoubleInvalidTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        
        session.startGame(13);
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        try
        {
            Move moveFoo = new Move(InfluenceCard.DOUBLE, new Coordinates(1, 1), new Coordinates(2, 6));
            moveFoo.setPlayer(playerFoo);
            
            session.playTurn(moveFoo);
        }
        catch (Exception e)
        {
            Assert.assertEquals("You cannot reach this tile!", e.getMessage());
        }
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        Assert.assertEquals(8, playerFoo.getReachableTiles().size());
        
        try
        {
            Move moveFoo = new Move(InfluenceCard.DOUBLE, new Coordinates(2, 5), new Coordinates(1, 1));
            moveFoo.setPlayer(playerFoo);
            
            session.playTurn(moveFoo);
        }
        catch (Exception e)
        {
            Assert.assertEquals("You cannot reach this tile!", e.getMessage());
        }
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        Assert.assertEquals(8, playerFoo.getReachableTiles().size());
    }
    
    @Test
    public void getWinnerTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        
        session.startGame(13);
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        Move moveFoo = new Move(InfluenceCard.NONE, new Coordinates(2, 3), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(2), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        Assert.assertEquals(playerFoo, session.findWinner());
        
        Move moveBar = new Move(InfluenceCard.NONE, new Coordinates(5, 4), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(2), playerFoo.getPoints());
        Assert.assertEquals(new Integer(2), playerBar.getPoints());
        
        Assert.assertEquals(playerBar, session.findWinner());
    }
    
    @Test
    public void fillBoardTest() throws Exception
    {
        GameRoomSession session = new GameRoomSession(1);
        Player playerFoo = session.addPlayer("Foo", "foo_color");
        Player playerBar = session.addPlayer("Bar", "bar_color");
        
        playerFoo.setReady(true);
        playerBar.setReady(true);
        
        session.startGame(13);
        
        Assert.assertEquals(new Integer(1), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        Move moveFoo = new Move(InfluenceCard.NONE, new Coordinates(3, 4), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(2), playerFoo.getPoints());
        Assert.assertEquals(new Integer(1), playerBar.getPoints());
        
        Move moveBar = new Move(InfluenceCard.NONE, new Coordinates(4, 5), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(2), playerFoo.getPoints());
        Assert.assertEquals(new Integer(2), playerBar.getPoints());
        
        moveFoo = new Move(InfluenceCard.DOUBLE, new Coordinates(4, 4), new Coordinates(5, 4));
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(4), playerFoo.getPoints());
        Assert.assertEquals(new Integer(2), playerBar.getPoints());
        
        moveBar = new Move(InfluenceCard.FREEDOM, new Coordinates(3, 5), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(4), playerFoo.getPoints());
        Assert.assertEquals(new Integer(3), playerBar.getPoints());
        
        moveFoo = new Move(InfluenceCard.NONE, new Coordinates(2, 5), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(5), playerFoo.getPoints());
        Assert.assertEquals(new Integer(3), playerBar.getPoints());
        
        moveBar = new Move(InfluenceCard.NONE, new Coordinates(4, 6), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(5), playerFoo.getPoints());
        Assert.assertEquals(new Integer(4), playerBar.getPoints());
        
        moveFoo = new Move(InfluenceCard.NONE, new Coordinates(3, 6), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(6), playerFoo.getPoints());
        Assert.assertEquals(new Integer(4), playerBar.getPoints());
        
        moveBar = new Move(InfluenceCard.REPLACEMENT, new Coordinates(3, 6), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(5), playerFoo.getPoints());
        Assert.assertEquals(new Integer(5), playerBar.getPoints());
        
        moveFoo = new Move(InfluenceCard.NONE, new Coordinates(2, 6), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(6), playerFoo.getPoints());
        Assert.assertEquals(new Integer(5), playerBar.getPoints());
        
        moveBar = new Move(InfluenceCard.NONE, new Coordinates(5, 6), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(6), playerFoo.getPoints());
        Assert.assertEquals(new Integer(6), playerBar.getPoints());
        
        moveFoo = new Move(InfluenceCard.NONE, new Coordinates(2, 7), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(7), playerFoo.getPoints());
        Assert.assertEquals(new Integer(6), playerBar.getPoints());
        
        moveBar = new Move(InfluenceCard.NONE, new Coordinates(5, 7), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(7), playerFoo.getPoints());
        Assert.assertEquals(new Integer(7), playerBar.getPoints());
        
        moveFoo = new Move(InfluenceCard.NONE, new Coordinates(2, 8), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(8), playerFoo.getPoints());
        Assert.assertEquals(new Integer(7), playerBar.getPoints());
        
        moveBar = new Move(InfluenceCard.NONE, new Coordinates(4, 7), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(8), playerFoo.getPoints());
        Assert.assertEquals(new Integer(8), playerBar.getPoints());
        
        moveFoo = new Move(InfluenceCard.NONE, new Coordinates(3, 8), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(9), playerFoo.getPoints());
        Assert.assertEquals(new Integer(8), playerBar.getPoints());
        
        moveBar = new Move(InfluenceCard.NONE, new Coordinates(3, 7), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(9), playerFoo.getPoints());
        Assert.assertEquals(new Integer(9), playerBar.getPoints());
        
        moveFoo = new Move(InfluenceCard.NONE, new Coordinates(4, 8), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(10), playerFoo.getPoints());
        Assert.assertEquals(new Integer(9), playerBar.getPoints());
        
        moveBar = new Move(InfluenceCard.NONE, new Coordinates(5, 8), null);
        moveBar.setPlayer(playerBar);
        
        session.playTurn(moveBar);
        
        Assert.assertEquals(new Integer(10), playerFoo.getPoints());
        Assert.assertEquals(new Integer(10), playerBar.getPoints());
        
        moveFoo = new Move(InfluenceCard.REPLACEMENT, new Coordinates(5, 8), null);
        moveFoo.setPlayer(playerFoo);
        
        session.playTurn(moveFoo);
        
        Assert.assertEquals(new Integer(51), playerFoo.getPoints());
        Assert.assertEquals(new Integer(9), playerBar.getPoints());
        Assert.assertEquals(GameStatus.FINISHED, session.getStatus());
        Assert.assertEquals(playerFoo, session.getWinner());
    }
}
