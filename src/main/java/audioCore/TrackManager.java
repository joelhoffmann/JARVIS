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


public class TrackManager extends AudioEventAdapter {

    private final AudioPlayer PLAYER;
    public static Queue<AudioInfo> queue;

    public TrackManager(AudioPlayer player) {
        this.PLAYER = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track, Member author) {
        AudioInfo info = new AudioInfo(track, author);
        queue.add(info);

        if (PLAYER.getPlayingTrack() == null) {
            PLAYER.playTrack(track);
        }
    }

    public Set<AudioInfo> getQueue() {
        return new LinkedHashSet<>(queue);
    }

    public AudioInfo getInfo(AudioTrack track) {
        return queue.stream()
                .filter(info -> info.getTrack().equals(track))
                .findFirst().orElse(null);
    }

    public void purgeQueue() {
        queue.clear();
    }

    public void shuffleQueue() {
        List<AudioInfo> cQueue = new ArrayList<>(getQueue());
        AudioInfo current = cQueue.get(0);
        cQueue.remove(0);
        Collections.shuffle(cQueue);
        cQueue.add(0, current);
        purgeQueue();
        queue.addAll(cQueue);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        AudioInfo info = queue.element();
        VoiceChannel vChan = info.getAuthor().getVoiceState().getChannel();

        if (vChan == null)
            player.stopTrack();
        else
            info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(queue.poll() != null) {
            if (queue.isEmpty()){
                guild.getAudioManager().closeAudioConnection();
            }
            else{
                cmdMusic Message = new cmdMusic();
                Message.updateInfoMessage();
                player.playTrack(queue.element().getTrack());
            }
        }
    }
}