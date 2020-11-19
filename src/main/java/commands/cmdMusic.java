package commands;

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
import util.Static;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zekro on 18.06.2017 / 11:47
 * supremeBot.commands
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class cmdMusic implements command {


    private static final int PLAYLIST_LIMIT = 1000;
    private static Guild guild;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();

    public static boolean isWorking = false;


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
    private AudioPlayer createPlayer(Guild g) {
        AudioPlayer player = MANAGER.createPlayer();
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
    private boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }

    /**
     * Returnt den momentanen Player der Guild aus der PLAYERS-Map,
     * oder erstellt einen neuen Player für die Guild.
     *
     * @param g Guild
     * @return AudioPlayer
     */
    private AudioPlayer getPlayer(Guild g) {
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
    private TrackManager getManager(Guild g) {
        return PLAYERS.get(g).getValue();
    }

    /**
     * Returnt, ob die Guild einen Player hat oder ob der momentane Player
     * gerade einen Track spielt.
     *
     * @param g Guild
     * @return Boolean
     */
    private boolean isIdle(Guild g) {
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

        MANAGER.setFrameBufferDuration(5000);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                    getManager(guild).queue(playlist.getTracks().get(i), author);
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
     *
     * @param g Guild
     */
    private void skip(Guild g) {
        getPlayer(g).stopTrack();
    }

    /**
     * Erzeugt aus dem Timestamp in Millisekunden ein hh:mm:ss - Zeitformat.
     *
     * @param milis Timestamp
     * @return Zeitformat
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
     *
     * @param info AudioInfo
     * @return Informationen als String
     */
    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    /**
     * Sendet eine Embed-Message in der Farbe Rot mit eingegebenen Content.
     *
     * @param event   MessageReceivedEvent
     * @param content Error Message Content
     */
    private void sendErrorMsg(MessageReceivedEvent event, String content) {
        System.out.println("sendErrorMsg");
        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription(content)
                        .build()
        ).queue();
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        //778708291135864832
        if (event.getTextChannel().getId().equals(Static.IDofMusicControlChannel)) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        guild = event.getGuild();

        if (args.length < 1) {
            sendErrorMsg(event, help());
            return;
        } else if (args[0].toLowerCase().startsWith("http")) {
            System.out.println("--------------");
            System.out.println(args[0]);
            System.out.println(args.length);
            System.out.println("--------------");
            if (args.length == 1) {
                String input = args[0];

                if (!(input.startsWith("http://") || input.startsWith("https://")))
                    input = "ytsearch: " + input;

                loadTrack(input, event.getMember(), event.getMessage());
            } else {
                sendErrorMsg(event, "Please enter a valid source!_test");
                return;

            }

//



        } else {
            switch (args[0].toLowerCase()) {

                case "play":
                case "p":

                    if (args.length < 2) {
                        sendErrorMsg(event, "Please enter a valid source!");
                        return;
                    }

                    System.out.println("--------------");
                    System.out.println(Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1));
                    System.out.println("--------------");
                    String input2 = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                    if (!(input2.startsWith("http://") || input2.startsWith("https://")))
                        input2 = "ytsearch: " + input2;

                    loadTrack(input2, event.getMember(), event.getMessage());

                    break;


                case "skip":
                case "s":

                    if (isIdle(guild)) return;
                    for (int i = (args.length > 1 ? Integer.parseInt(args[1]) : 1); i == 1; i--) {
                        skip(guild);
                    }

                    break;


                case "stop":
                case "delete":

                    if (isIdle(guild)) return;
                    getManager(guild).purgeQueue();
                    skip(guild);
                    guild.getAudioManager().closeAudioConnection();

                    List<Message> messages = event.getTextChannel().getHistory().retrievePast(50).complete();
                    for(int i = 0; i < messages.size(); i++){
                        String t = messages.get(i).toString();
                        String requiredString = t.substring(t.indexOf("(") + 1, t.indexOf(")"));
                        System.out.println(requiredString);
                        event.getTextChannel().deleteMessageById(requiredString).complete();
                    }

                    break;

                case "shuffle":

                    if (isIdle(guild)) return;
                    getManager(guild).shuffleQueue();

                    break;


                case "now":
                case "info":

                    if (isIdle(guild)) return;

                    AudioTrack track = getPlayer(guild).getPlayingTrack();
                    AudioTrackInfo info = track.getInfo();

                    event.getTextChannel().sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.blue)
                                    .setDescription("**CURRENT TRACK INFO:**")
                                    .addField("Title", info.title, false)
                                    .addField("Duration", "`[ " + getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()) + " ]`", false)
                                    .addField("Author", info.author, false)
                                    .build()
                    ).queue();

                    break;


                case "queue":

                    if (isIdle(guild)) return;

                    int sideNumb = args.length > 1 ? Integer.parseInt(args[1]) : 1;

                    List<String> tracks = new ArrayList<>();
                    List<String> trackSublist;

                    getManager(guild).getQueue().forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

                    if (tracks.size() > 20)
                        trackSublist = tracks.subList((sideNumb - 1) * 20, (sideNumb - 1) * 20 + 20);
                    else
                        trackSublist = tracks;

                    String out = trackSublist.stream().collect(Collectors.joining("\n"));
                    int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;
                    System.out.println("queue shown");
                    event.getTextChannel().sendMessage(
                            new EmbedBuilder()
                                    .setDescription(
                                            "**CURRENT QUEUE:**\n" +
                                                    "*[" + getManager(guild).getQueue().stream() + " Tracks | Side " + sideNumb + " / " + sideNumbAll + "]*" +
                                                    out
                                    )
                                    .build()
                    ).queue();


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
}