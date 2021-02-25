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

    private static final int PLAYLIST_LIMIT = 1000;
    public static Guild guild;
    public static AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private static Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();
    public static EmbedBuilder eb;
    public static AudioPlayer player;
    public static List<String> trackSublist;
    public static ArrayList<String> Tracks;

    /**
     * Audio Manager als Audio-Stream-Recource deklarieren.
     */
    public cmdMusic() {
        AudioSourceManagers.registerRemoteSources(MANAGER);
    }

    /**
     * Erstellt einen Audioplayer und fügt diesen in die PLAYERS-Map ein.
     *
     * @param g Guild
     * @return AudioPlayer
     */
    public static AudioPlayer createPlayer(Guild g) {
        player = MANAGER.createPlayer();
        TrackManager m = new TrackManager(player);
        player.addListener(m);
        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(player));
        PLAYERS.put(g, new AbstractMap.SimpleEntry<>(player, m));
        return player;
    }

    /**
     * Returnt, ob die Guild einen Eintrag in der PLAYERS-Map hat.
     *
     * @param g Guild
     * @return Boolean
     */
    public static boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }

    /**
     * Returnt den momentanen Player der Guild aus der PLAYERS-Map,
     * oder erstellt einen neuen Player für die Guild.
     *
     * @param g Guild
     * @return AudioPlayer
     */
    public static AudioPlayer getPlayer(Guild g) {
        if (hasPlayer(g))
            return PLAYERS.get(g).getKey();
        else
            return createPlayer(g);
    }

    /**
     * Returnt den momentanen TrackManager der Guild aus der PLAYERS-Map.
     *
     * @param g Guild
     * @return TrackManager
     */
    public static TrackManager getManager(Guild g) {
        return PLAYERS.get(g).getValue();
    }

    /**
     * Returnt, ob die Guild einen Player hat oder ob der momentane Player
     * gerade einen Track spielt.
     *
     * @param g Guild
     * @return Boolean
     */
    public static boolean isIdle(Guild g) {
        return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
    }

    /**
     * Läd aus der URL oder dem Search String einen Track oder eine Playlist
     * in die Queue.
     *
     * @param identifier URL oder Search String
     * @param author     Member, der den Track / die Playlist eingereiht hat
     * @param msg        Message des Contents
     */


    private void loadTrack(String identifier, Member author, Message msg) {
        Guild guild = author.getGuild();
        getPlayer(guild);

        MANAGER.setFrameBufferDuration(10000);
        //Todo: Fix this override
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, author);
                player.setVolume(20);
                updateInfoMessage();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (msg.getContentDisplay().contains("spotify") && msg.getContentDisplay().contains("playlist") && msg.getContentDisplay().contains("http") == true) {
                    getManager(guild).queue(playlist.getTracks().get(0), author);
                    player.setVolume(20);
                } else if (msg.getContentDisplay().contains("spotify") && msg.getContentDisplay().contains("track") && msg.getContentDisplay().contains("http") == true) {
                    getManager(guild).queue(playlist.getTracks().get(0), author);
                    updateInfoMessage();
                    player.setVolume(20);
                } else if (msg.getContentDisplay().contains(".play") && msg.getContentDisplay().contains("http") == false) {
                    getManager(guild).queue(playlist.getTracks().get(0), author);
                    player.setVolume(20);
                    updateInfoMessage();
                } else {
                    for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                        System.out.println("...");
                        getManager(guild).queue(playlist.getTracks().get(i), author);
                    }
                    player.setVolume(20);
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

    /**
     * Stoppt den momentanen Track, worauf der nächste Track gespielt wird.
     */
    public static void skip() {
        player.stopTrack();
    }

    /**
     * Erzeugt aus dem Timestamp in Millisekunden ein hh:mm:ss - Zeitformat.
     */
    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    /**
     * Returnt aus der AudioInfo eines Tracks die Informationen als String.
     */
    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    /**
     * Sendet eine Embed-Message in der Farbe Rot mit eingegebenen Content.
     */
    private void sendErrorMsg(MessageReceivedEvent event, String content) {
        System.out.println("sendErrorMsg");
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
            System.out.println("[INFO] Command wurde nicht ausgeführt");
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
                System.out.println("queue shown");
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
                    System.out.println("Passed http check");
                    //Youtube
                    if (input.contains("youtube")) {
                        System.out.println("Youtube");
                        input = "ytsearch: " + input;
                        loadTrack(input, event.getMember(), event.getMessage());
                        //Spotify
                    } else if (input.contains("spotify")) {
                        System.out.print("Spotify, ");
                        //Playlist???
                        if (input.contains("playlist")) {
                            System.out.println("Playlist");
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
                                    System.out.print("Waiting for Queue...");
                                    waiting = 1;
                                }
                            }
                            System.out.println("FINISHED");
                            updateInfoMessage();
                            //Track
                        } else if (input.contains("track")) {
                            System.out.println("Track");
                            String[] test = input.split("/");
                            String ID = test[test.length - 1].substring(0, test[test.length - 1].indexOf("?"));
                            getTrackInfo Track = new getTrackInfo(ID);
                            System.out.println(Track.getTrackName());
                            loadTrack("ytsearch: " + Track.getTrackName(), event.getMember(), event.getMessage());
                        }

                    }
                }

            } else {
                sendErrorMsg(event, "Please enter a valid source!_test");
                return;
            }

        } else if (event.getMessage().getContentDisplay().toLowerCase().contains(".play")) {
            System.out.println("Youtube search: ");
            String input = "ytsearch: ";
            for (int i = 0; i < args.length; i++) {
                input += (args[i] + " ");
            }
            System.out.print(input);
            loadTrack(input, event.getMember(), event.getMessage());
        } else {
            switch (args[0].toLowerCase()) {

                case "stop":
                case "delete":
                    if (isIdle(guild)) return;
                    getManager(guild).purgeQueue();
                    skip();
                    guild.getAudioManager().closeAudioConnection();
                    break;

                case "now":
                case "info":
                    if (isIdle(guild)) return;
                    sendInfoMessage();
                    break;
                case "skip":
                    skip();
                    break;
            }
        }
    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {
        System.out.println("[DONE]");
    }

    @Override
    public String help() {
        return null;
    }

    public static void updateInfoMessage() {
        eb = new EmbedBuilder();
        eb.setColor(Color.blue)
                .setDescription("Aktueller Track")
                .addField("Title", TrackManager.queue.peek().getTrack().getInfo().title, false)
                //.addField("Duration", "`[ " + getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()) + " ]`", false)
                .addField("by", TrackManager.queue.peek().getTrack().getInfo().author, false);
        List<Message> messages = guild.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).getHistory().retrievePast(20).complete();
        System.out.println(guild.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).getHistory().getRetrievedHistory().size());
        Message msg = guild.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).editMessageById(messages.get(messages.size() - 1).getId(), eb.build()).complete();
        msg.addReaction(STATIC.EmoteforPause).complete();
        msg.addReaction(STATIC.EmoteforStop).complete();
        msg.addReaction(STATIC.EmoteforSkip).complete();
        msg.addReaction(STATIC.EmoteforShuffle).complete();
        msg.addReaction(STATIC.Emoteforlower).complete();
        msg.addReaction(STATIC.Emoteforhigher).complete();

    }

    public static void sendInfoMessage() {
        eb = new EmbedBuilder();
        eb.setColor(Color.blue)
                .setDescription("Aktueller Track")
                .addField("Title", TrackManager.queue.peek().getTrack().getInfo().title, false)
                //.addField("Duration", "`[ " + getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()) + " ]`", false)
                .addField("by", TrackManager.queue.peek().getTrack().getInfo().author, false);
        List<Message> messages = guild.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).getHistory().retrievePast(20).complete();
        System.out.println(guild.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).getHistory().getRetrievedHistory().size());

        Message msg = guild.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).sendMessage(eb.build()).complete();
        msg.addReaction(STATIC.EmoteforPause).complete();
        msg.addReaction(STATIC.EmoteforStop).complete();
        msg.addReaction(STATIC.EmoteforSkip).complete();
        msg.addReaction(STATIC.EmoteforShuffle).complete();
        msg.addReaction(STATIC.Emoteforlower).complete();
        msg.addReaction(STATIC.Emoteforhigher).complete();
    }
}