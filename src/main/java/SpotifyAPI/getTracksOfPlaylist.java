package SpotifyAPI;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.ArrayList;

import static util.STATIC.accessToken;

public class getTracksOfPlaylist {
    private static GetPlaylistRequest getPlaylistRequest;

    public getTracksOfPlaylist(String ID) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();
        getPlaylistRequest = spotifyApi.getPlaylist(ID).build();
    }

    public ArrayList<String> getTrackNames() {
        ArrayList<String> Tracks_Raw = new ArrayList<>();
        try {
            final Playlist playlist = getPlaylistRequest.execute();
            PlaylistTrack[] PlaylistItems = playlist.getTracks().getItems();
            for (PlaylistTrack playlistItem : PlaylistItems) {
                getTrackInfo info = new getTrackInfo(playlistItem.getTrack().getId());
                Tracks_Raw.add("ytsearch: " + playlistItem.getTrack().getName() + " " + info.getTrackAuthor());
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return Tracks_Raw;
    }
}