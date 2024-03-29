package SpotifyAPI;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import util.SECRETS;
import util.STATIC;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ClientCredentials {
    private static final Logger LOGGER =  LoggerFactory.getLogger(ClientCredentials.class);
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(SECRETS.clientId)
            .setClientSecret(SECRETS.clientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    public ClientCredentials(){

    }
    public  String Token (){
        clientCredentials_Sync();
        clientCredentials_Async();
        return STATIC.accessToken;
    }
    public static void clientCredentials_Sync() {
        try {
            final com.wrapper.spotify.model_objects.credentials.ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            STATIC.accessToken = clientCredentials.getAccessToken();
            LOGGER.info("Token Expires in: " + clientCredentials.getExpiresIn() + "s");
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            LOGGER.error("Error: " + e.getMessage());
            clientCredentials_Sync();
        }
    }

    public static void clientCredentials_Async() {
        try {
            final CompletableFuture<com.wrapper.spotify.model_objects.credentials.ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();
            final com.wrapper.spotify.model_objects.credentials.ClientCredentials clientCredentials = clientCredentialsFuture.join();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            STATIC.accessToken = clientCredentials.getAccessToken();
            LOGGER.info("Token Expires in: " + clientCredentials.getExpiresIn() + "s");
        } catch (CompletionException e) {
            LOGGER.error("Error: " + e.getCause().getMessage());
            clientCredentials_Async();
        } catch (CancellationException e) {
            LOGGER.info("Async operation cancelled.");
        }
    }
}
