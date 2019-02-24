package assignment.web.responses;

import assignment.game.Move;

/**
 * Wrapper object representing the client response
 */
public class ClientResponse
{
    private Move move;
    private Boolean ready;
    private ClientResponseType type;
    
    public ClientResponse(Move move)
    {
        this.move = move;
    }
    
    public ClientResponse(Boolean ready)
    
    {
        this.ready = ready;
    }
    
    public Boolean getReady()
    {
        return ready;
    }
    
    public Move getMove()
    {
        return move;
    }
    
    public ClientResponseType getType()
    {
        return type;
    }
    
    public void setType(ClientResponseType type)
    {
        this.type = type;
    }
}
