package audioCore;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.cmdMusic;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import util.STATIC;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import static commands.cmdMusic.*;

/**
 * Created by zekro on 18.06.2017 / 11:30
 * supremeBot.audioCore
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class TrackManager extends AudioEventAdapter {

    private final AudioPlayer PLAYER;
    public static Queue<AudioInfo> queue;


    /**
     * Erstellt eine Instanz der Klasse TrackManager.
     *
     * @param player
     */
    public TrackManager(AudioPlayer player) {
        this.PLAYER = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * Reiht den übergebenen Track in die Queue ein.
     *
     * @param track  AudioTrack
     * @param author Member, der den Track eingereiht hat
     */
    public void queue(AudioTrack track, Member author) {
        AudioInfo info = new AudioInfo(track, author);
        queue.add(info);

        if (PLAYER.getPlayingTrack() == null) {
            PLAYER.playTrack(track);
        }
    }

    /**
     * Returnt die momentane Queue als LinkedHashSet.
     *
     * @return Queue
     */
    public Set<AudioInfo> getQueue() {
        return new LinkedHashSet<>(queue);
    }

    /**
     * Returnt AudioInfo des Tracks aus der Queue.
     *
     * @param track AudioTrack
     * @return AudioInfo
     */
    public AudioInfo getInfo(AudioTrack track) {
        return queue.stream()
                .filter(info -> info.getTrack().equals(track))
                .findFirst().orElse(null);
    }

    /**
     * Leert die gesammte Queue.
     */
    public void purgeQueue() {
        queue.clear();
    }

    /**
     * Shufflet die momentane Queue.
     */
    public void shuffleQueue() {
        List<AudioInfo> cQueue = new ArrayList<>(getQueue());
        AudioInfo current = cQueue.get(0);
        cQueue.remove(0);
        Collections.shuffle(cQueue);
        cQueue.add(0, current);
        purgeQueue();
        queue.addAll(cQueue);
    }

    /**
     * PLAYER EVENT: TRACK STARTET
     * Wenn Einreiher nicht im VoiceChannel ist, wird der Player gestoppt.
     * Sonst connectet der Bot in den Voice Channel des Einreihers.
     *
     * @param player AudioPlayer
     * @param track  AudioTrack
     */
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        AudioInfo info = queue.element();
        VoiceChannel vChan = info.getAuthor().getVoiceState().getChannel();

        if (vChan == null)
            player.stopTrack();
        else
            info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);
    }

    /**
     * PLAYER EVENT: TRACK ENDE
     * Wenn die Queue zuende ist, verlässt der Bot den Audio Channel.
     * Sonst wird der nächste Track in der Queue wiedergegeben.
     *
     * @param player
     * @param track
     * @param endReason
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(queue.poll() != null) {
            Guild g = queue.poll().getAuthor().getGuild();
            if (queue.isEmpty())
                g.getAudioManager().closeAudioConnection();
            else{
                eb = new EmbedBuilder();
                eb.setColor(Color.blue)
                        .setDescription("Aktueller Track")
                        .addField("Title", TrackManager.queue.peek().getTrack().getInfo().title, false)
                        //.addField("Duration", "`[ " + getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()) + " ]`", false)
                        .addField("by", TrackManager.queue.peek().getTrack().getInfo().author, false);
                List<Message> messages = g.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).getHistory().retrievePast(20).complete();
                Message msg = g.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).editMessageById(messages.get(messages.size() - 1).getId(), eb.build()).complete();
                msg.addReaction(STATIC.EmoteforPause).complete();
                msg.addReaction(STATIC.EmoteforStop).complete();
                msg.addReaction(STATIC.EmoteforSkip).complete();
                msg.addReaction(STATIC.EmoteforShuffle).complete();
                System.out.println(TrackManager.queue.peek().getTrack().getInfo().title);
                player.playTrack(queue.element().getTrack());
            }
        }
    }
}