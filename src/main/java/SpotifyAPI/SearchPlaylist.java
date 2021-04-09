package SpotifyAPI;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.requests.data.search.simplified.SearchPlaylistsRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static util.STATIC.accessToken;

public class SearchPlaylist{
    private static final String q = "https://open.spotify.com/playlist/37i9dQZF1DX4jP4eebSWR9?si=oWnmL2M6SxKl40s81M0ekw";

    public static void main(String[] args) {
        System.out.println(accessToken);
        searchPlaylists_Sync();
        searchPlaylists_Async();
    }
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(accessToken)
            .build();
    private static final SearchPlaylistsRequest searchPlaylistsRequest = spotifyApi.searchPlaylists(q)
            .build();

    public static void searchPlaylists_Sync() {
        try {
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = searchPlaylistsRequest.execute();
            System.out.println("Total: " + playlistSimplifiedPaging.getTotal());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void searchPlaylists_Async() {
        try {
            final CompletableFuture<Paging<PlaylistSimplified>> pagingFuture = searchPlaylistsRequest.executeAsync();
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = pagingFuture.join();
            System.out.println("Total: " + playlistSimplifiedPaging.getTotal());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
}
