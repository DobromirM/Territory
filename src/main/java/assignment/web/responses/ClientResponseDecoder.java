package assignment.web.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * Decoder for the Client Response object
 */
public class ClientResponseDecoder implements Decoder.Text<ClientResponse>
{
    //GSON parser
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().enableComplexMapKeySerialization().create();
    
    /**
     * Decode the JSON response into Java object
     *
     * @param clientResponse - String - Response from the client
     *
     * @return - ClientResponse Java object
     */
    @Override
    public ClientResponse decode(String clientResponse)
    {
        ClientResponse response = GSON.fromJson(clientResponse, ClientResponse.class);
        if (response.getMove() == null)
        {
            response.setType(ClientResponseType.READY);
            return response;
        }
        else
        {
            response.setType(ClientResponseType.MOVE);
            return response;
        }
    }
    
    /**
     * Verify that the JSON is valid before decoding
     *
     * @param clientResponse - String - Response from the client
     *
     * @return - Boolean - Response whether or not the client response is decodable
     */
    @Override
    public boolean willDecode(String clientResponse)
    {
        ClientResponse response = GSON.fromJson(clientResponse, ClientResponse.class);
        
        return response.getMove() != null || response.getReady() != null;
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
