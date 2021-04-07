package SpotifyAPI;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.apache.hc.core5.http.ParseException;
import util.SECRETS;
import util.STATIC;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ClientCredentials {
    static SECRETS secrets = new SECRETS();
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(secrets.getClientId())
            .setClientSecret(secrets.getClientSecret())
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    public ClientCredentials(){

    }
    public String Token (){
        clientCredentials_Sync();
        clientCredentials_Async();
        return STATIC.accessToken;
    }
    public static void clientCredentials_Sync() {
        try {
            final com.wrapper.spotify.model_objects.credentials.ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            STATIC.accessToken = clientCredentials.getAccessToken();
            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void clientCredentials_Async() {
        try {
            final CompletableFuture<com.wrapper.spotify.model_objects.credentials.ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();
            final com.wrapper.spotify.model_objects.credentials.ClientCredentials clientCredentials = clientCredentialsFuture.join();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            STATIC.accessToken = clientCredentials.getAccessToken();
            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
}