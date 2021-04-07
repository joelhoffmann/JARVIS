package commands;

import SpotifyAPI.getTrackInfo;
import SpotifyAPI.getTracksOfPlaylist;
import audioCore.AudioInfo;
import audioCore.PlayerSendHandler;
import audioCore.TrackManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class cmdMusic implements command {
    //TODO: needs an urgent rework!

    private final int PLAYLIST_LIMIT = 1000;
    public static Guild guild;
    public static AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();
    public static AudioPlayer player;
    public List<String> trackSublist;
    public ArrayList<String> Tracks;

    public EmbedBuilder eb;


    //Audio Manager als Audio-Stream-Recource deklarieren
    public cmdMusic() {
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    //Erstellt einen Audioplayer und fügt diesen in die PLAYERS-Map ein.
    public AudioPlayer createPlayer(Guild g) {
        player = playerManager.createPlayer();
        TrackManager m = new TrackManager(player);
        player.addListener(m);
        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(player));
        PLAYERS.put(g, new AbstractMap.SimpleEntry<>(player, m));
        return player;
    }

    //Returnt, ob die Guild einen Eintrag in der PLAYERS-Map hat.
    public boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }

    //Returnt den momentanen Player der Guild aus der PLAYERS-Map,
    //oder erstellt einen neuen Player für die Guild.
    public AudioPlayer getPlayer(Guild g) {
        if (hasPlayer(g))
            return PLAYERS.get(g).getKey();
        else
            return createPlayer(g);
    }

    //Returnt den momentanen TrackManager der Guild aus der PLAYERS-Map.

    public TrackManager getManager(Guild g) {
        return PLAYERS.get(g).getValue();
    }

    //Returnt, ob die Guild einen Player hat oder ob der momentane Player
    //gerade einen Track spielt.
    public boolean isIdle(Guild g) {
        return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
    }

    //Läd aus der URL oder dem Search String einen Track oder eine Playlist
    //in die Queue.

    private void loadTrack(String identifier, Member author, Message msg) {
        Guild guild = author.getGuild();
        getPlayer(guild);

        playerManager.setFrameBufferDuration(10000);
        //Todo: Fix this override
        playerManager.loadItem(identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, author);
                player.setVolume(20);
                updateInfoMessage();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                player.setVolume(20);
                if (msg.getContentDisplay().contains("spotify") && msg.getContentDisplay().contains("playlist") && msg.getContentDisplay().contains("http") == true) {
                    getManager(guild).queue(playlist.getTracks().get(0), author);
                } else if (msg.getContentDisplay().contains("spotify") && msg.getContentDisplay().contains("track") && msg.getContentDisplay().contains("http") == true) {
                    getManager(guild).queue(playlist.getTracks().get(0), author);
                    updateInfoMessage();
                } else if (msg.getContentDisplay().contains(".play") && msg.getContentDisplay().contains("http") == false) {
                    getManager(guild).queue(playlist.getTracks().get(0), author);
                    updateInfoMessage();
                } else {
                    for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                        getManager(guild).queue(playlist.getTracks().get(i), author);
                    }
                    updateInfoMessage();
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });

    }


    //Stoppt den momentanen Track, worauf der nächste Track gespielt wird.
    public void skip() {
        player.stopTrack();
    }

    public void stop(){
        getManager(guild).purgeQueue();
        skip();
        guild.getAudioManager().closeAudioConnection();
        System.gc();
        defaultInfoMessage();
    }
    public void shuffle(){
        if (isIdle(guild)) return;
        getManager(guild).shuffleQueue();
    }
    public void pause(){
        if(player.isPaused()){
            player.setPaused(false);
        }else{
            player.setPaused(true);
        }
    }
    public void vol_lower(){
        int volume = player.getVolume();
        volume = volume - 5;
        player.setVolume(volume);
    }
    public void vol_higher(){
        int volume = player.getVolume();
        volume = volume + 5;
        player.setVolume(volume);
    }


    // Erzeugt aus dem Timestamp in Millisekunden ein hh:mm:ss - Zeitformat.
    //TODO: method is not in usage, could be deleted or used to display remaining time of the playing track.
    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }
    //Returnt aus der AudioInfo eines Tracks die Informationen als String.

    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    //Sendet eine Embed-Message in der Farbe Rot mit eingegebenen Content.
    //TODO: method is not used.
    private void sendErrorMsg(MessageReceivedEvent event, String content) {
        event.getChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription(content)
                        .build()
        ).queue();
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (event.getTextChannel().getName().equals(STATIC.NameofMusicControlChannel)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        guild = event.getGuild();
        //Volume Befehl
        if (event.getMessage().getContentDisplay().startsWith(".vol")) {
            if (args.length == 1) {
                player.setVolume(Integer.parseInt(args[0]));
            } else {
                event.getTextChannel().sendMessage("Falsche Eingabe").queue();
                return;
            }
        //Display Queue
        } else if (event.getMessage().getContentDisplay().startsWith(".queue")) {
            if (args.length == 0) {
                if (isIdle(guild)) return;
                int sideNumb = args.length > 1 ? Integer.parseInt(args[1]) : 1;
                List<String> tracks = new ArrayList<>();
                trackSublist = new ArrayList<>();
                getManager(guild).getQueue().forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));
                if (tracks.size() > 20)
                    trackSublist = tracks.subList((sideNumb - 1) * 20, (sideNumb - 1) * 20 + 20);
                else
                    trackSublist = tracks;
                String out = trackSublist.stream().collect(Collectors.joining("\n"));
                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setDescription("**CURRENT QUEUE:**\n" + out).build()
                ).queue();
            } else {
                event.getTextChannel().sendMessage("Falsche Eingabe").queue();
                return;
            }
        //Searching for a song or playlist
        } else if (args[0].toLowerCase().startsWith("http")) {
            if (args.length == 1) {
                String input = args[0];
                if (input.startsWith("http://") || input.startsWith("https://")) {
                    //Youtube
                    if (input.contains("youtube")) {
                        input = "ytsearch: " + input;
                        loadTrack(input, event.getMember(), event.getMessage());
                        //Spotify
                    } else if (input.contains("spotify")) {
                        //Playlist???
                        if (input.contains("playlist")) {
                            String[] test = input.split("/");
                            String ID = test[test.length - 1].substring(0, test[test.length - 1].indexOf("?"));

                            getTracksOfPlaylist TrackName = new getTracksOfPlaylist(ID);
                            Tracks = TrackName.getTrackNames();
                            for (int i = 0; i < Tracks.size(); i++) {
                                loadTrack(Tracks.get(i), event.getMember(), event.getMessage());
                            }
                            int waiting = 0;
                            while (getManager(guild).getQueue().isEmpty()) {
                                if (waiting == 0) {
                                    waiting = 1;
                                }
                            }
                            updateInfoMessage();
                            //Track
                        } else if (input.contains("track")) {
                            String[] test = input.split("/");
                            String ID = test[test.length - 1].substring(0, test[test.length - 1].indexOf("?"));
                            getTrackInfo Track = new getTrackInfo(ID);
                            loadTrack("ytsearch: " + Track.getTrackName(), event.getMember(), event.getMessage());
                        }

                    }
                }

            } else {
                sendErrorMsg(event, "Please enter a valid source!");
                return;
            }
        //direkt play
        } else if (event.getMessage().getContentDisplay().toLowerCase().contains(".play")) {
            String input = "ytsearch: ";
            for (int i = 0; i < args.length; i++) {
                input += (args[i] + " ");
            }
            loadTrack(input, event.getMember(), event.getMessage());
        //Extended commands
        } else {
            switch (args[0].toLowerCase()) {

                case "stop":
                case "delete":
                    stop();
                    break;

                case "now":
                case "info":
                    defaultInfoMessage();
                    break;
                case "skip":
                    skip();
                    break;
            }
        }
    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {
    }

    @Override
    public String help() {
        return null;
    }

    public void updateInfoMessage() {
        eb = new EmbedBuilder();
        eb.setColor(Color.blue)
                .setDescription("-----------Aktueller Track-----------")
                .addField("Title", TrackManager.queue.peek().getTrack().getInfo().title, false)
                .addField("by", TrackManager.queue.peek().getTrack().getInfo().author, false);
        List<Message> messages = guild.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).getHistory().retrievePast(20).complete();
        Message msg = guild.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).editMessageById(messages.get(messages.size() - 1).getId(), eb.build()).complete();
        msg.addReaction(STATIC.EmoteforPause).complete();
        msg.addReaction(STATIC.EmoteforStop).complete();
        msg.addReaction(STATIC.EmoteforSkip).complete();
        msg.addReaction(STATIC.EmoteforShuffle).complete();
        msg.addReaction(STATIC.Emoteforlower).complete();
        msg.addReaction(STATIC.Emoteforhigher).complete();
    }
    public void defaultInfoMessage() {
        EmbedBuilder eb2 = new EmbedBuilder();
        eb2.setColor(Color.blue)
                .setDescription("-----------Aktueller Track-----------")
                .addField("Title", "Auf der Mauer auf der Lauer sitzt ne kleine Wanze (inoffical Video)", false)
                .addField("by", "Weiß ich net. Quelle: Kopf", false);
        List<Message> messages = guild.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).getHistory().retrievePast(20).complete();
        Message msg = guild.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).editMessageById(messages.get(messages.size() - 1).getId(), eb2.build()).complete();
        msg.addReaction(STATIC.EmoteforPause).complete();
        msg.addReaction(STATIC.EmoteforStop).complete();
        msg.addReaction(STATIC.EmoteforSkip).complete();
        msg.addReaction(STATIC.EmoteforShuffle).complete();
        msg.addReaction(STATIC.Emoteforlower).complete();
        msg.addReaction(STATIC.Emoteforhigher).complete();
    }

    public void sendInfoMessage() {
        eb = new EmbedBuilder();
        eb.setColor(Color.blue)
                .setDescription("-----------Aktueller Track-----------")
                .addField("Title", TrackManager.queue.peek().getTrack().getInfo().title, false)
                .addField("by", TrackManager.queue.peek().getTrack().getInfo().author, false);
        List<Message> messages = guild.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).getHistory().retrievePast(20).complete();

        Message msg = guild.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).sendMessage(eb.build()).complete();
        msg.addReaction(STATIC.EmoteforPause).complete();
        msg.addReaction(STATIC.EmoteforStop).complete();
        msg.addReaction(STATIC.EmoteforSkip).complete();
        msg.addReaction(STATIC.EmoteforShuffle).complete();
        msg.addReaction(STATIC.Emoteforlower).complete();
        msg.addReaction(STATIC.Emoteforhigher).complete();
    }
}