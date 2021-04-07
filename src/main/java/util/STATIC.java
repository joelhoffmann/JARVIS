package util;

import SpotifyAPI.ClientCredentials;

public class STATIC {
    public static String prefix = ".";

    public static String NameofMusicControlChannel = "music";
    public static String NameofMusicControlRole = "Plattenpräsident";
    public static String ColorofMusicControlRole = "#00ffff";

    public static String NameOfCategorie = "Jarvis";
    public static String NameofControlChannel = "jarvis";
    public static String NameofJarvisControlRole = "alphatierchen";

    public static String EmoteforSkip = "⏭";
    public static String EmoteforPause = "⏯";
    public static String EmoteforStop = "⏹";
    public static String EmoteforShuffle = "🔀";
    public static String Emoteforlower = "🔉";
    public static String Emoteforhigher = "🔊";

    public static String EmoteforHoffi = "🦒";
    public static String EmoteforNXZAS8CA = "🐻";
    public static String EmoteforMalte = "🐺";
    public static String Emoteforready = "✅";
    public static String Emotefordelete = "❌";

    static ClientCredentials TokenGenerator = new ClientCredentials();
    public static String accessToken = TokenGenerator.Token();


}