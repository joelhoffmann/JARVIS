package util;

import SpotifyAPI.ClientCredentialsExample;
import com.wrapper.spotify.SpotifyApi;

public class STATIC {
    public static String prefix = ".";

    public static String NameofMusicControlChannel = "music";
    public static String NameofMusicControlRole = "Plattenpräsident";

    public static String NameofWelcomeChannel = "welcome";
    public static String NameofWelcomeRole = "welcome";

    public static String NameOfCategorie = "Jarvis";
    public static String NameofControlChannel = "jarvis";
    public static String NameofJarvisControlRole = "alphatierchen";

    public static String EmoteforSkip = "⏭";
    public static String EmoteforPause = "⏯";
    public static String EmoteforStop = "⏹";
    public static String EmoteforShuffle = "🔀";

    public static String EmoteforHoffi = "🦒";
    public static String EmoteforNXZAS8CA = "🐻";
    public static String EmoteforMalte = "🐺";
    public static String Emoteforready = "✅";
    public static String Emotefordelete = "❌";

    static ClientCredentialsExample TokenGenerator = new ClientCredentialsExample();
    public static String accessToken = TokenGenerator.Token();

    /*
    //Needs for controling the bot
    Channels:
    1. Botcontrol channel      => ID to -IDofControlChannel-
    2. Musiccontrol channel    => ID to -IDofMusicControlChannel-
    3. Welcome channel          => ID to -IDofMusicControlChannel-

    Roles:
    1. J.A.R.V.I.S              => will be autocreated if Jarvis
    2. welcome                  => rights to see the welcome channel and nothing else
    3. botcontrol               => rights to see and write into the Botcontrol channel


    //
     */




}