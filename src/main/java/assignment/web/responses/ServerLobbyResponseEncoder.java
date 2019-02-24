package assignment.web.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Encoder for the server lobby response object
 */
public class ServerLobbyResponseEncoder implements Encoder.Text<ServerLobbyResponse>
{
    //GSON parser
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().enableComplexMapKeySerialization().create();
    
    /**
     * Encode the server lobby response into a JSON string
     *
     * @param serverLobbyResponse - Response from the server to be encoded
     *
     * @return - String - JSON representation of the response
     */
    @Override
    public String encode(ServerLobbyResponse serverLobbyResponse)
    {
        return GSON.toJson(serverLobbyResponse);
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
