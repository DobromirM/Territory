package assignment.web.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Encoder for the server response object
 */
public class ServerResponseEncoder implements Encoder.Text<ServerResponse>
{
    //GSON parser
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().enableComplexMapKeySerialization().create();
    
    /**
     * Encode the server response into a JSON string
     *
     * @param serverResponse - Response from the server to be encoded
     *
     * @return - String - JSON representation of the response
     */
    @Override
    public String encode(ServerResponse serverResponse)
    {
        return GSON.toJson(serverResponse);
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
