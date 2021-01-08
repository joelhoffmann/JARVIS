package SpotifyAPI;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static util.STATIC.accessToken;

public class getTracksOfPlaylist {
    private static String playlistId = "";

    private static SpotifyApi spotifyApi = null;
    private static GetPlaylistRequest getPlaylistRequest = null;

    public getTracksOfPlaylist(String ID) {
        playlistId = ID;
        spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        getPlaylistRequest = spotifyApi.getPlaylist(playlistId).build();
    }

    public ArrayList<String> getTrackNames() {
        ArrayList<String> Tracks_Raw = new ArrayList<>();
        try {
            final Playlist playlist = getPlaylistRequest.execute();
            PlaylistTrack[] PlaylistItems = playlist.getTracks().getItems();

            for(int i = 0; i < PlaylistItems.length; i++){
                getTrackInfo info = new getTrackInfo(PlaylistItems[i].getTrack().getId());
                Tracks_Raw.add("ytsearch: " + PlaylistItems[i].getTrack().getName() + " " + info.getTrackAuthor());
            }

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return Tracks_Raw;
    }
}