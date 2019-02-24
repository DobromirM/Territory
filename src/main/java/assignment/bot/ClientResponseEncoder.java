package assignment.bot;

import assignment.web.responses.ClientResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Encoder for the client response object
 */
public class ClientResponseEncoder implements Encoder.Text<ClientResponse>
{
    //GSON parser
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().enableComplexMapKeySerialization().create();
    
    /**
     * Encode the client response into a JSON string
     *
     * @param clientResponse - Response from the client to be encoded
     *
     * @return - String - JSON representation of the response
     */
    @Override
    public String encode(ClientResponse clientResponse) throws EncodeException
    {
        return GSON.toJson(clientResponse);
    }
    
    @Override
    public void init(EndpointConfig endpointConfig)
    {
    
    }
    
    @Override
    public void destroy()
    {
    
    }
}
