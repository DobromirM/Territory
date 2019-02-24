package assignment.bot;

import assignment.game.GameStatus;
import assignment.game.Move;
import assignment.web.responses.ClientResponse;
import assignment.web.responses.ClientResponseType;
import assignment.web.responses.ServerResponse;

import javax.websocket.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Client endpoint for the bot
 */
@ClientEndpoint (decoders = ServerResponseDecoder.class, encoders = ClientResponseEncoder.class)
public class BotClientEndpoint
{
    static CountDownLatch latch;
    private Integer id;
    private boolean ready;
    private Session session;
    
    /**
     * Save reference to the session and display that the bot has connected to the server
     *
     * @param session - The new session
     */
    @OnOpen
    public void onOpen(Session session)
    {
        System.out.println("Connected");
        this.session = session;
    }
    
    /**
     * Receive response from the server and send a response back
     *
     * @param serverResponse - Response from the server
     */
    @OnMessage
    public void onMessage(ServerResponse serverResponse) throws IOException, EncodeException, InterruptedException
    {
        GameStatus gameStatus = serverResponse.getSession().getStatus();
        
        if (id == null)
        {
            id = serverResponse.getYou();
        }
        
        if (gameStatus == GameStatus.WAITING_FOR_PLAYERS && !ready)
        {
            TimeUnit.SECONDS.sleep(30);
            ready = true;
            session.getBasicRemote().sendObject(new ClientResponse(true));
        }
        
        if (gameStatus == GameStatus.PLAYING && serverResponse.getSession().getPlayerOnTurn().getId().equals(id))
        {
            TimeUnit.SECONDS.sleep(1);
            Move move = Bot.selectMove(serverResponse.getSession());
            ClientResponse response = new ClientResponse(move);
            response.setType(ClientResponseType.MOVE);
            
            session.getBasicRemote().sendObject(response);
        }
        
        if (gameStatus == GameStatus.FINISHED)
        {
            TimeUnit.MINUTES.sleep(1);
            session.close();
        }
    }
    
    /**
     * Set the latch to stop the bot and display that the bot has disconnected
     */
    @OnClose
    public void onClose()
    {
        System.out.println("Disconnected");
        latch.countDown();
    }
    
    /**
     * Ignore errors
     *
     * @param t - Throwable
     */
    @OnError
    public void onError(Throwable t)
    {
    }
}
