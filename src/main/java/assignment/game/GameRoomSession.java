package assignment.game;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Object representing the game room session for a single game
 */
public class GameRoomSession
{
    //Game room settings
    private static final Integer ROWS = 6;
    private static final Integer COLUMNS = 10;
    private static final Integer MAX_PLAYERS = 5;
    
    //Unique ids given to the players
    private Integer nextUniqueId = 0;
    
    //Id of the room
    private Integer id;
    private List<Player> players;
    private GameBoard board;
    private GameStatus status;
    private Player winner;
    private Player playerOnTurn;
    
    public GameRoomSession(Integer id)
    {
        this.id = id;
        this.board = new GameBoard(ROWS, COLUMNS);
        this.players = new LinkedList<>();
        this.status = GameStatus.WAITING_FOR_PLAYERS;
    }
    
    public Integer getId()
    {
        return id;
    }
    
    public Player getPlayerOnTurn()
    {
        return playerOnTurn;
    }
    
    public Integer getPlayersCount()
    {
        return players.size();
    }
    
    public GameStatus getStatus()
    {
        return status;
    }
    
    public GameBoard getBoard()
    {
        return board;
    }
    
    public static Integer getMaxPlayers()
    {
        return MAX_PLAYERS;
    }
    
    public Player getWinner()
    {
        return winner;
    }
    
    /**
     * Find the winner of the game.
     * The player with max score, if multiple the one with the highest id.
     * Note: The id shows the connection order, the highest id is of the player who joined last
     *
     * @return - Player - The winner of the game
     */
    public Player findWinner()
    {
        return players.stream().max(Comparator.comparingInt(Player::getPoints).thenComparing(Player::getId)).get();
    }
    
    /**
     * Start the game if there are at least two players and all players are ready
     *
     * @param seed - Seed for the random assignment of the starting tiles, leave to null for random
     */
    public synchronized void startGame(Integer seed)
    {
        if (areAllPlayersReady() && getPlayersCount() >= 2 && status == GameStatus.WAITING_FOR_PLAYERS)
        {
            assignStartingTiles(seed);
            this.status = GameStatus.PLAYING;
            playerOnTurn = players.get(0);
            Game.addGameRoom();
        }
    }
    
    /**
     * Create a player and add it to the game room if there is room
     *
     * @param name  - Name of the player
     * @param color - Color of the player
     *
     * @return - The newly created player
     *
     * @throws Exception - Exception showing that the room is full
     */
    public synchronized Player addPlayer(String name, String color) throws Exception
    {
        synchronized (this)
        {
            if (players.size() < MAX_PLAYERS)
            {
                Player player = new Player(getNextUniqueId(), name, color);
                players.add(player);
                
                return player;
            }
            else
            {
                throw new Exception("Game is already full!");
            }
        }
    }
    
    /**
     * Remove a player from the game room.
     * If the game has started make sure to update
     * the player on turn to the next one if it was his turn
     *
     * @param player - Player to be removed from the game
     */
    public synchronized void removePlayer(Player player)
    {
        synchronized (this)
        {
            if (status == GameStatus.PLAYING)
            {
                if (playerOnTurn.equals(player))
                {
                    playerOnTurn = getNextPlayer();
                }
                
                if (getPlayersCount() == 2)
                {
                    status = GameStatus.FINISHED;
                    players.remove(player);
                    winner = findWinner();
                }
                else
                {
                    players.remove(player);
                }
            }
            else
            {
                players.remove(player);
            }
        }
    }
    
    /**
     * Allow for a move to be made by the players
     *
     * @param move - Move to be made
     *
     * @throws InvalidMoveException - Exception thrown if the move is invalid
     */
    public void playTurn(Move move) throws InvalidMoveException
    {
        synchronized (this)
        {
            if (getStatus() == GameStatus.PLAYING)
            {
                
                if (move.getPlayer() == playerOnTurn)
                {
                    
                    //Make the move
                    recordMove(move);
                    
                    List<Player> notBlockedPlayers = players.stream().filter(p -> !p.isBlocked(board)).collect(Collectors.toList());
                    
                    if (notBlockedPlayers.size() == 1)
                    {
                        //Update the game to finished and assign the winner
                        fillBoard(notBlockedPlayers.get(0));
                        
                        if (notBlockedPlayers.get(0).isBlocked(board))
                        {
                            notBlockedPlayers.remove(notBlockedPlayers.get(0));
                        }
                    }
                    
                    if (notBlockedPlayers.size() == 0)
                    {
                        this.status = GameStatus.FINISHED;
                        winner = findWinner();
                    }
                    else
                    {
                        do
                        {
                            //Move to next player
                            playerOnTurn = getNextPlayer();
                            
                            //Move to the next player if the current one is blocked
                        } while (playerOnTurn.isBlocked(board));
                    }
                }
                else
                {
                    throw new InvalidMoveException("It's not your turn!");
                }
            }
        }
    }
    
    /**
     * Find a player in the room by the given id
     *
     * @param id - Id of the player
     *
     * @return - Player object or null if not present
     */
    private Player findPlayerById(Integer id)
    {
        return players.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }
    
    /**
     * Fill all the empty tiles of the board with tiles for the specified player
     * Note: Used when all players except for one are blocked to end the game faster
     *
     * @param player - Player which should take all the empty board tiles
     */
    private void fillBoard(Player player)
    {
        board.getEmptyTiles().stream().forEach(t -> {
            t.setValue(player.getId());
            player.addPoint();
        });
    }
    
    /**
     * Check if all players in the game room are ready
     *
     * @return - Boolean - Response whether or not all players are ready
     */
    private boolean areAllPlayersReady()
    {
        return players.stream().allMatch(Player::isReady);
    }
    
    /**
     * Get the next unique id for a player
     *
     * @return - Unique player id
     */
    private Integer getNextUniqueId()
    {
        nextUniqueId = nextUniqueId + 1;
        return nextUniqueId;
    }
    
    /**
     * Get the next player on turn after the current one
     *
     * @return - Player for the next turn
     */
    private Player getNextPlayer()
    {
        int nextPlayerIndex = players.indexOf(playerOnTurn) + 1;
        
        if (nextPlayerIndex >= players.size())
        {
            nextPlayerIndex = 0;
        }
        
        return players.get(nextPlayerIndex);
    }
    
    /**
     * Randomly assign starting tiles for the players
     *
     * @param seed - Seed for the randomized
     *             Note: Used for testing, leave as null if you want the tiles to be random
     */
    private void assignStartingTiles(Integer seed)
    {
        ArrayList<Integer> x = (ArrayList<Integer>) IntStream.range(0, ROWS).boxed().collect(Collectors.toList());
        ArrayList<Integer> y = (ArrayList<Integer>) IntStream.range(0, COLUMNS).boxed().collect(Collectors.toList());
        
        if (seed != null)
        {
            Random random = new Random(seed);
            Collections.shuffle(x, random);
            Collections.shuffle(y, random);
        }
        else
        {
            Collections.shuffle(x);
            Collections.shuffle(y);
        }
        
        for (Player player : players)
        {
            Coordinates coord = new Coordinates(x.remove(0), y.remove(0));
            board.setTile(coord, player.getId());
            player.addPoint();
            player.addReachableTiles(board.getAdjacentTiles(coord));
        }
    }
    
    /**
     * Record the player move if it is valid
     *
     * @param move - Move to be recorded
     *
     * @throws InvalidMoveException - Exception if the move is invalid with the specific reason
     */
    private void recordMove(Move move) throws InvalidMoveException
    {
        if (move.isValid())
        {
            if (move.getCard() == InfluenceCard.NONE)
            {
                recordNormalMove(move.getPlayer(), move.getFirstCoord());
            }
            else
            {
                recordSpecialMove(move);
            }
        }
        else
        {
            throw new InvalidMoveException("Invalid move!");
        }
    }
    
    /**
     * Record normal (non-card) move
     *
     * @param player        - Player making the move
     * @param selectedCoord - Selected coordinates for the tile
     *
     * @throws InvalidMoveException - Exception if the move is invalid with the specific reason
     */
    private void recordNormalMove(Player player, Coordinates selectedCoord) throws InvalidMoveException
    {
        if (board.getTile(selectedCoord).getValue() != null)
        {
            throw new InvalidMoveException("Tile is already taken!");
        }
        else
        {
            boolean reachable = player.getReachableTiles().stream().anyMatch(t -> t.equals(board.getTile(selectedCoord)));
            
            if (reachable)
            {
                board.setTile(selectedCoord, player.getId());
                player.addPoint();
                player.addReachableTiles(board.getAdjacentTiles(selectedCoord));
            }
            else
            {
                throw new InvalidMoveException("You cannot reach this tile!");
            }
        }
    }
    
    /**
     * Record a special (card) move
     *
     * @param move - Move to be recorded
     *
     * @throws InvalidMoveException - Exception if the move is invalid with the specific reason
     */
    private void recordSpecialMove(Move move) throws InvalidMoveException
    {
        
        if (move.getCard() == InfluenceCard.FREEDOM)
        {
            recordFreedomMove(move);
        }
        
        if (move.getCard() == InfluenceCard.REPLACEMENT)
        {
            recordReplacementMove(move);
        }
        
        if (move.getCard() == InfluenceCard.DOUBLE)
        {
            recordDoubleMove(move);
        }
    }
    
    /**
     * Record a freedom move
     *
     * @param move - Freedom move to be recorded
     *
     * @throws InvalidMoveException - Exception if the move is invalid with the specific reason
     */
    private void recordFreedomMove(Move move) throws InvalidMoveException
    {
        Integer selectedTileValue = board.getTile(move.getFirstCoord()).getValue();
        
        if (selectedTileValue == null)
        {
            board.setTile(move.getFirstCoord(), move.getPlayer().getId());
            move.getPlayer().addReachableTiles(board.getAdjacentTiles(move.getFirstCoord()));
            move.getPlayer().addPoint();
            move.getPlayer().removeCard(InfluenceCard.FREEDOM);
        }
        else
        {
            throw new InvalidMoveException("Tile is already taken!");
        }
    }
    
    /**
     * Record a replacement move
     *
     * @param move - Move to be recorded
     *
     * @throws InvalidMoveException - Exception if the move is invalid with the specific reason
     */
    private void recordReplacementMove(Move move) throws InvalidMoveException
    {
        Integer selectedTileValue = board.getTile(move.getFirstCoord()).getValue();
        Player replacedPlayer = findPlayerById(selectedTileValue);
        
        if (selectedTileValue == null)
        {
            throw new InvalidMoveException("Cannot replace empty tiles!");
        }
        
        if (move.getPlayer().getId().equals(selectedTileValue))
        {
            throw new InvalidMoveException("Cannot replace your own tile!");
        }
        
        boolean reachable = move.getPlayer().getReachableTiles().stream().anyMatch(t -> t.equals(board.getTile(move.getFirstCoord())));
        
        if (reachable)
        {
            //Replace the tile with the new value
            board.setTile(move.getFirstCoord(), move.getPlayer().getId());
            
            //Find all the tiles that are not reachable anymore by the replaced player
            List<GameTile> unreachableTiles = board.findUnreachableTiles(move.getFirstCoord(), selectedTileValue);
            
            //Remove the unreachable tiles from the player and remove one point from his score
            replacedPlayer.removeReachableTiles(unreachableTiles);
            replacedPlayer.removePoint();
            
            //Update the current player as normal
            move.getPlayer().addReachableTiles(board.getAdjacentTiles(move.getFirstCoord()));
            move.getPlayer().addPoint();
            move.getPlayer().removeCard(InfluenceCard.REPLACEMENT);
        }
        else
        {
            throw new InvalidMoveException("You cannot reach this tile!");
        }
    }
    
    /**
     * Record double move
     *
     * @param move - Move to be recorded
     *
     * @throws InvalidMoveException - Exception if the move is invalid with the specific reason
     */
    private void recordDoubleMove(Move move) throws InvalidMoveException
    {
        
        Set<GameTile> tiles = new HashSet<>(move.getPlayer().getReachableTiles());
        
        //Make first move
        recordNormalMove(move.getPlayer(), move.getFirstCoord());
        
        //Make second move
        try
        {
            recordNormalMove(move.getPlayer(), move.getSecondCoord());
        }
        catch (InvalidMoveException exception)
        {
            // Make sure to revert the first move if the second one fails
            move.getPlayer().setReachableTiles(tiles);
            board.setTile(move.getFirstCoord(), null);
            move.getPlayer().removePoint();
            
            throw exception;
        }
        
        move.getPlayer().removeCard(InfluenceCard.DOUBLE);
    }
}