package assignment.bot;

import assignment.game.*;
import org.glassfish.tyrus.client.ClientManager;

import org.apache.commons.cli.*;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * Main class for the bot
 */
public class Bot
{
    //The current phase of the bot
    private static PhaseType phase = PhaseType.REACH;
    
    /**
     * Main method for starting the bot
     *
     * @param args - Arguments for initialisation of the bot player
     */
    public static void main(String[] args)
    {
        Options options = getCmdOptions();
        
        CommandLine commandLine = parseCmd(args, options);
        
        String name = commandLine.getOptionValue("name");
        String color = commandLine.getOptionValue("color");
        String room = commandLine.getOptionValue("room");
        
        BotClientEndpoint.latch = new CountDownLatch(1);
        ClientManager client = ClientManager.createClient();
        try
        {
            client.connectToServer(BotClientEndpoint.class, new URI("ws://localhost:8080/play/" + room + "/" + name + "/" + color));
            BotClientEndpoint.latch.await();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Select the next move of the bot
     *
     * @param gameRoom - State of the game room
     *
     * @return - The move that the bot wants to make
     */
    static Move selectMove(GameRoomSession gameRoom)
    {
        Player you = gameRoom.getPlayerOnTurn();
        GameBoard board = gameRoom.getBoard();
        Move selectedMove = new Move(InfluenceCard.NONE, null, null);
        
        if (phase == PhaseType.REACH)
        {
            selectedMove = getReachMove(you, board, selectedMove);
        }
        
        if (phase == PhaseType.GUARD)
        {
            selectedMove = getGuardMove(you, board, selectedMove);
        }
        
        if (phase == PhaseType.ENDGAME)
        {
            selectedMove = getEndgameMove(you, board, selectedMove);
        }
        
        return selectedMove;
    }
    
    /**
     * Select the next best move for the 'REACH' phase
     *
     * @param you          - Player - You
     * @param board        - The state of the game board
     * @param selectedMove - Reference to the empty selected move
     *
     * @return - The selected move
     */
    private static Move getReachMove(Player you, GameBoard board, Move selectedMove)
    {
        List<GameTile> reachable;
        
        if (you.getCards().contains(InfluenceCard.REPLACEMENT))
        {
            reachable = new ArrayList<>(you.getReachableTiles());
        }
        else
        {
            reachable = you.getReachableTiles().stream().filter(t -> t.getValue() == null).collect(Collectors.toList());
        }
        
        PossibleChoices choices = getTilesWithMostReach(reachable, board);
        
        if (choices.getMaxReachable() == 0)
        {
            phase = PhaseType.GUARD;
        }
        else
        {
            Collections.shuffle(choices.getTiles());
            
            if (choices.getFirstTile().getValue() != null)
            {
                selectedMove = new Move(InfluenceCard.REPLACEMENT, choices.getFirstTile().getCoordinates(), null, you);
            }
            else
            {
                selectedMove = new Move(InfluenceCard.NONE, choices.getFirstTile().getCoordinates(), null, you);
            }
        }
        return selectedMove;
    }
    
    /**
     * Select the next best move for the 'GUARD' phase
     *
     * @param you          - Player - You
     * @param board        - The state of the game board
     * @param selectedMove - Reference to the empty selected move
     *
     * @return - The selected move
     */
    private static Move getGuardMove(Player you, GameBoard board, Move selectedMove)
    {
        List<GameTile> reachable = you.getReachableTiles().stream().filter(t -> t.getValue() == null).collect(Collectors.toList());
        PossibleChoices choices = getGuardableTiles(reachable, board, you);
        
        if (choices.getFirstTile() != null)
        {
            
            List<GameTile> selection = board.getAdjacentTiles(choices.getFirstTile().getCoordinates()).stream().filter(t -> t.getValue() == null).collect(Collectors.toList());
            
            if (selection.size() > 1 && you.getCards().contains(InfluenceCard.DOUBLE))
            {
                selectedMove = new Move(InfluenceCard.DOUBLE, selection.get(0).getCoordinates(), selection.get(1).getCoordinates());
            }
            else
            {
                selectedMove = new Move(InfluenceCard.NONE, selection.get(0).getCoordinates(), null);
            }
        }
        else
        {
            phase = PhaseType.ENDGAME;
        }
        return selectedMove;
    }
    
    /**
     * Select the next best move for the 'ENDGAME' phase
     *
     * @param you          - Player - You
     * @param board        - The state of the game board
     * @param selectedMove - Reference to the empty selected move
     *
     * @return - The selected move
     */
    private static Move getEndgameMove(Player you, GameBoard board, Move selectedMove)
    {
        List<GameTile> guardedTiles = getGuardedTiles(board, you);
        
        List<GameTile> selection = you.getReachableTiles().stream().filter(t -> !guardedTiles.contains(t) && t.getValue() == null).collect(Collectors.toList());
        
        if (selection.size() > 0)
        {
            Collections.shuffle(selection);
            selectedMove = new Move(InfluenceCard.NONE, selection.get(0).getCoordinates(), null);
        }
        else
        {
            if (guardedTiles.size() > 0)
            {
                Coordinates coordinates = guardedTiles.remove(0).getCoordinates();
                selectedMove = new Move(InfluenceCard.NONE, coordinates, null);
            }
            else
            {
                if (you.getCards().contains(InfluenceCard.FREEDOM) && board.getEmptyTiles().size() > 0)
                {
                    selectedMove = new Move(InfluenceCard.FREEDOM, board.getEmptyTiles().get(0).getCoordinates(), null);
                }
                else
                {
                    if (you.getCards().contains(InfluenceCard.REPLACEMENT))
                    {
                        GameTile replacement = you.getReachableTiles().stream().filter(t -> !t.getValue().equals(you.getId())).findFirst().orElse(null);
                        
                        if (replacement != null)
                        {
                            selectedMove = new Move(InfluenceCard.REPLACEMENT, replacement.getCoordinates(), null);
                        }
                    }
                }
            }
        }
        return selectedMove;
    }
    
    /**
     * Get the tiles which will increase the player's reach the most
     *
     * @param tiles - Possible tiles to play
     * @param board - Game board state
     *
     * @return - Object with possible choices containing a list with the tiles with maximum reach
     * and the maximum reach as a number
     */
    private static PossibleChoices getTilesWithMostReach(List<GameTile> tiles, GameBoard board)
    {
        PossibleChoices possibleChoices = new PossibleChoices();
        
        for (GameTile tile : tiles)
        {
            Coordinates coords = tile.getCoordinates();
            List<GameTile> surrounding = board.getAdjacentTiles(coords).stream().filter(t -> t.getValue() == null).collect(Collectors.toList());
            Integer reach = Math.toIntExact(surrounding.stream().filter(t -> !tiles.contains(t)).count());
            
            if (possibleChoices.getMaxReachable() < reach)
            {
                possibleChoices.setMaxReachable(reach);
                possibleChoices.getTiles().clear();
                possibleChoices.getTiles().add(tile);
            }
            
            if (possibleChoices.getMaxReachable().equals(reach))
            {
                possibleChoices.getTiles().add(tile);
            }
        }
        
        return possibleChoices;
    }
    
    /**
     * Get the tiles needed to be played for all tiles which require
     * the least amount of moves to make them fully guarded
     *
     * @param tiles - Possible tiles to guard
     * @param board - Game board state
     * @param you   - Player - You
     *
     * @return - The tiles that need to be played in order to guard an empty tile on the board
     */
    private static PossibleChoices getGuardableTiles(List<GameTile> tiles, GameBoard board, Player you)
    {
        PossibleChoices possibleChoices = new PossibleChoices();
        
        for (GameTile tile : tiles)
        {
            Coordinates coords = tile.getCoordinates();
            boolean isGuardable = board.getAdjacentTiles(coords).stream().noneMatch(t -> t.getValue() != null && !t.getValue().equals(you.getId()));
            
            if (isGuardable)
            {
                int emptySurroundingCount = Math.toIntExact(board.getAdjacentTiles(coords).stream().filter(t -> t.getValue() == null).count());
                
                if (emptySurroundingCount < possibleChoices.getMinGuardable() && emptySurroundingCount > 0)
                {
                    possibleChoices.setMinGuardable(emptySurroundingCount);
                    possibleChoices.getTiles().clear();
                    possibleChoices.getTiles().add(tile);
                }
                
                if (emptySurroundingCount == possibleChoices.getMinGuardable())
                {
                    possibleChoices.getTiles().add(tile);
                }
            }
        }
        
        return possibleChoices;
    }
    
    /**
     * Get all empty tiles that are fully guarded by the player
     *
     * @param board - Game board state
     * @param you   - Player - You
     *
     * @return - All tiles that are fully guarded by the player
     */
    private static List<GameTile> getGuardedTiles(GameBoard board, Player you)
    {
        List<GameTile> guardedTiles = new ArrayList<>();
        
        List<GameTile> emptyTiles = board.getEmptyTiles();
        
        for (GameTile tile : emptyTiles)
        {
            List<GameTile> adjacent = board.getAdjacentTiles(tile.getCoordinates());
            boolean isGuarded = adjacent.stream().allMatch(t -> t.getValue() != null && t.getValue().equals(you.getId()));
            
            if (isGuarded)
            {
                guardedTiles.add(tile);
            }
        }
        
        return guardedTiles;
    }
    
    /**
     * Parse the cmd arguments provided when running bot
     *
     * @param args    - Command line arguments passed to the bot
     * @param options - Options for the cmd
     *
     * @return - Parsed cmd arguments
     */
    private static CommandLine parseCmd(String[] args, Options options)
    {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine commandLine = null;
        
        try
        {
            commandLine = parser.parse(options, args);
        }
        catch (ParseException e)
        {
            System.out.println(e.getMessage());
            formatter.printHelp("bot", options);
            
            System.exit(1);
        }
        return commandLine;
    }
    
    /**
     * Get the cmd options that need to be specified when starting the bot
     *
     * @return - Options for the cmd start up script
     */
    private static Options getCmdOptions()
    {
        Options options = new Options();
        
        Option nameOption = new Option("n", "name", true, "bot name");
        nameOption.setRequired(true);
        options.addOption(nameOption);
        
        Option colorOption = new Option("c", "color", true, "bot color");
        colorOption.setRequired(true);
        options.addOption(colorOption);
        
        Option roomOption = new Option("r", "room", true, "room id");
        roomOption.setRequired(true);
        roomOption.setType(Integer.class);
        options.addOption(roomOption);
        return options;
    }
}
