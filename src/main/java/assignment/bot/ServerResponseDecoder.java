package assignment.bot;

import assignment.web.responses.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * Decoder for the Server Response object
 */
public class ServerResponseDecoder implements Decoder.Text<ServerResponse>
{
    //GSON parser
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().enableComplexMapKeySerialization().create();
    
    /**
     * Decode the JSON response into Java object
     *
     * @param serverResponse - String - Response from the server
     *
     * @return - ServerResponse Java object
     */
    @Override
    public ServerResponse decode(String serverResponse)
    {
        return GSON.fromJson(serverResponse, ServerResponse.class);
    }
    
    /**
     * Verify that the JSON is valid before decoding
     *
     * @param serverResponse - String - Response from the server
     *
     * @return - Boolean - Response whether or not the server response is decodable
     */
    @Override
    public boolean willDecode(String serverResponse)
    {
        ServerResponse response = GSON.fromJson(serverResponse, ServerResponse.class);
        
        return response.getSession() != null;
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
