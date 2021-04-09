package util;

import SpotifyAPI.ClientCredentials;

public class STATIC {
    public static String prefix = ".";
    public static String NameofMusicControlChannel = "music";
    public static String NameofControlChannel = "jarvis";
    public static String NameofJarvisControlRole = "alphatierchen";
    public static String EmoteforSkip = "⏭";
    public static String EmoteforPause = "⏯";
    public static String EmoteforStop = "⏹";
    public static String EmoteforShuffle = "🔀";
    public static String Emoteforlower = "🔉";
    public static String Emoteforhigher = "🔊";

    static ClientCredentials TokenGenerator = new ClientCredentials();
    public static String accessToken = TokenGenerator.Token();


}