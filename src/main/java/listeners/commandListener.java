package listeners;

import audioCore.TrackManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import core.commandHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import util.SECRETS;
import util.STATIC;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static commands.cmdMusic.*;

import java.awt.Color;
import java.util.function.Predicate;


public class commandListener extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        JDABuilder.createLight(SECRETS.Token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new readyListener())
                .build();
    }

    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        System.out.println(event.getMessage());
        if (event.getMessage().getContentDisplay().startsWith(STATIC.prefix) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()) {
            if (event.getAuthor().isBot()) {
                return;
            } else {
                event.getMessage().delete().queue();
                //event.getTextChannel().editMessageById(event.getTextChannel().getLatestMessageId(), "test").queue();
            }
            commandHandler.handleCommand(commandHandler.parse.parser(event.getMessage().getContentDisplay(), event));

        } else {
            if (event.getAuthor().isBot()) {
                if (event.getMessage().getContentDisplay().contains("Ich spiele Musik")) {
                    AudioTrack track = getPlayer(guild).getPlayingTrack();
                    AudioTrackInfo info = track.getInfo();

                    eb = new EmbedBuilder();
                    eb.setColor(Color.blue)
                            .setDescription("Aktueller Track")
                            .addField("Title", info.title, false)
                            //.addField("Duration", "`[ " + getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()) + " ]`", false)
                            .addField("by", info.author, false);

                    Message msg = event.getGuild().getTextChannelById(STATIC.IDofMusicControlChannel).sendMessage(eb.build()).complete();
                    msg.addReaction(STATIC.EmoteforBack).complete();
                    msg.addReaction(STATIC.EmoteforStop).complete();
                    msg.addReaction(STATIC.EmoteforSkip).complete();
                    msg.addReaction(STATIC.EmoteforShuffle).complete();
                }
            }
        }

    }

    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        if (event.getUser().isBot()) return;
        //Rollenzuweisung
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforHoffi)) {
            System.out.println("emote true");
            String[] vergl = {"Hoffis"};
            if (Arrays.stream(vergl).noneMatch(event.getMember().getRoles().toString()::contains)) {
                event.getGuild().addRoleToMember(event.getUser().getId(), event.getGuild().getRolesByName("Hoffis", false).get(0)).queue();
                event.getChannel().sendMessage("Du hast die Rolle von Joel erhalten").queue();
            } else {
                event.getChannel().sendMessage("Du hast die Rolle schon").queue();
            }
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforNXZAS8CA)) {
            System.out.println("emote true");
            String[] vergl = {"Simons"};
            if (Arrays.stream(vergl).noneMatch(event.getMember().getRoles().toString()::contains)) {
                event.getGuild().addRoleToMember(event.getUser().getId(), event.getGuild().getRolesByName("Simons", false).get(0)).queue();
                event.getChannel().sendMessage("Du hast die Rolle von Simon erhalten").queue();
            } else {
                event.getChannel().sendMessage("Du hast die Rolle schon").queue();
            }
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforMalte)) {
            System.out.println("emote true");
            String[] vergl = {"Maltes"};
            if (Arrays.stream(vergl).noneMatch(event.getMember().getRoles().toString()::contains)) {
                event.getGuild().addRoleToMember(event.getUser().getId(), event.getGuild().getRolesByName("Maltes", false).get(0)).queue();
                event.getChannel().sendMessage("Du hast die Rolle von Malte erhalten").queue();
            } else {
                event.getChannel().sendMessage("Du hast die Rolle schon").queue();
            }
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.Emoteforready)) {
            System.out.println("emote true");

            List<Message> messages = event.getChannel().getHistory().retrievePast(50).complete();
            //System.out.println(messages.get(0));
            if (messages.size() > 1) {
                for (int i = 0; i < (messages.size() - 1); i++) {
                    String t = messages.get(i).toString();
                    String requiredString = t.substring(t.indexOf("(") + 1, t.indexOf(")"));
                    System.out.println(requiredString);
                    event.getChannel().deleteMessageById(requiredString).complete();
                }
            }

            event.getReaction().removeReaction(event.getUser()).queue();
            event.getGuild().removeRoleFromMember(event.getUser().getIdLong(), event.getGuild().getRolesByName("welcome", false).get(0)).queue();
        }
        //Musik
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforSkip)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            for (int i = 1; i == 1; i--) {
                skip(guild);
            }
            AudioTrack track = getPlayer(guild).getPlayingTrack();
            AudioTrackInfo info = track.getInfo();
            eb = new EmbedBuilder();
            eb.setColor(Color.blue)
                    .setDescription("Aktueller Track")
                    .addField("Title", info.title, false)
                    .addField("by", info.author, false);
            event.getChannel().editMessageById(event.getMessageId(), eb.build()).queue();
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforStop)) {
            TrackManager.queue.clear();
            guild.getAudioManager().closeAudioConnection();
            List<Message> messages = event.getChannel().getHistory().retrievePast(50).complete();
            for (int i = 0; i < messages.size(); i++) {
                String t = messages.get(i).toString();
                String requiredString = t.substring(t.indexOf("(") + 1, t.indexOf(")"));
                System.out.println(requiredString);
                event.getChannel().deleteMessageById(requiredString).complete();
            }
        }

    }


}
