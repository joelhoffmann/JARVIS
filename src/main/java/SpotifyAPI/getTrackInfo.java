package SpotifyAPI;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static util.STATIC.accessToken;

public class getTrackInfo {
    private static String id = "";
    private static SpotifyApi spotifyApi = null;
    private static GetTrackRequest getTrackRequest = null;

    public getTrackInfo(String ID){
        id = ID;
        spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        getTrackRequest = spotifyApi.getTrack(id)
                .build();
    }

    public String getTrackAuthor (){
        try {
            final Track track = getTrackRequest.execute();
            ArtistSimplified[] Artists = track.getArtists();
            return Artists[0].getName();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public String getTrackName (){
        try {
            final Track track = getTrackRequest.execute();
            return track.getName();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    public static void getTrack_Sync() {
        try {
            final Track track = getTrackRequest.execute();

            System.out.println("Name: " + track.getName());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void getTrack_Async() {
        try {
            final CompletableFuture<Track> trackFuture = getTrackRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final Track track = trackFuture.join();

            System.out.println("Name: " + track.getName());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }

    public static void main(String[] args) {
        getTrack_Sync();
        getTrack_Async();
    }
}