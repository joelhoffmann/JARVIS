package listeners;


import commands.cmdMusic;
import core.commandHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import util.SECRETS;
import util.STATIC;
import util.checkingSetup;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;
import java.awt.Color;


public class commandListener extends ListenerAdapter {

    public void main(String[] args) throws LoginException {
        SECRETS secrets = new SECRETS();
        JDABuilder.createLight(secrets.getToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new readyListener())
                .build();
    }

    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getMessage().getContentDisplay().startsWith(STATIC.prefix) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()) {
            if (event.getAuthor().isBot()) {
                return;
            } else {
                event.getMessage().delete().queue();
            }
            commandHandler.handleCommand(commandHandler.parse.parser(event.getMessage().getContentDisplay(), event));
        }
    }

    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        if (event.getUser().isBot()) return;
        //Rollenzuweisung
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforHoffi)) {
            String[] vergl = {"Hoffis"};
            if (Arrays.stream(vergl).noneMatch(event.getMember().getRoles().toString()::contains)) {
                event.getGuild().addRoleToMember(event.getUser().getId(), event.getGuild().getRolesByName("Hoffis", false).get(0)).queue();
                event.getChannel().sendMessage("Du hast die Rolle von Joel erhalten").queue();
            } else {
                event.getChannel().sendMessage("Du hast die Rolle schon").queue();
            }
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforNXZAS8CA)) {
            String[] vergl = {"Simons"};
            if (Arrays.stream(vergl).noneMatch(event.getMember().getRoles().toString()::contains)) {
                event.getGuild().addRoleToMember(event.getUser().getId(), event.getGuild().getRolesByName("Simons", false).get(0)).queue();
                event.getChannel().sendMessage("Du hast die Rolle von Simon erhalten").queue();
            } else {
                event.getChannel().sendMessage("Du hast die Rolle schon").queue();
            }
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforMalte)) {
            String[] vergl = {"Maltes"};
            if (Arrays.stream(vergl).noneMatch(event.getMember().getRoles().toString()::contains)) {
                event.getGuild().addRoleToMember(event.getUser().getId(), event.getGuild().getRolesByName("Maltes", false).get(0)).queue();
                event.getChannel().sendMessage("Du hast die Rolle von Malte erhalten").queue();
            } else {
                event.getChannel().sendMessage("Du hast die Rolle schon").queue();
            }
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.Emoteforready)) {
            if(checkingSetup.text.length() == 0){
                List<Message> messages = event.getChannel().getHistory().retrievePast(50).complete();
                if (messages.size() > 1) {
                    for (int i = 0; i < (messages.size() - 1); i++) {
                        String t = messages.get(i).toString();
                        String requiredString = t.substring(t.indexOf("(") + 1, t.indexOf(")"));
                        event.getChannel().deleteMessageById(requiredString).queue();
                    }
                }
                event.getGuild().removeRoleFromMember(event.getUser().getIdLong(), event.getGuild().getRolesByName(STATIC.NameofWelcomeRole, false).get(0)).queue();
            }else{
                event.getReaction().removeReaction(event.getUser()).queue();
                Guild g = event.getGuild();
                cmdSetup.makeSetup(g);
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.blue)
                        .setTitle("Alles Klar!")
                        .setDescription("Es ist alles wieder in Ordnung." +
                                "Zum Löschen dieser Nachricht, klicke einfach auf das Kreuz.");
                Message msg = event.getGuild().getTextChannelsByName(STATIC.NameofControlChannel, false).get(0).editMessageById(g.getTextChannelsByName(STATIC.NameofControlChannel, false).get(0).getLatestMessageId(), eb.build()).complete();
                msg.addReaction(STATIC.Emotefordelete).complete();
            }
        }
        if(event.getReactionEmote().getEmoji().contains(STATIC.Emotefordelete)){
            event.getGuild().getTextChannelsByName(STATIC.NameofControlChannel, false).get(0).deleteMessageById(event.getGuild().getTextChannelsByName(STATIC.NameofControlChannel, false).get(0).getLatestMessageId()).queue();
        }
        //Musik
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforSkip)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            cmdMusic musicControler = new cmdMusic();
            musicControler.skip();
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforStop)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            cmdMusic musicControler = new cmdMusic();
            musicControler.stop();

        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforShuffle)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            cmdMusic musicControler = new cmdMusic();
            musicControler.shuffle();

        }
        if(event.getReactionEmote().getEmoji().contains(STATIC.EmoteforPause)){
            event.getReaction().removeReaction(event.getUser()).queue();
            cmdMusic musicControler = new cmdMusic();
            musicControler.pause();
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.Emoteforlower)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            cmdMusic musicControler = new cmdMusic();
            musicControler.vol_lower();
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.Emoteforhigher)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            cmdMusic musicControler = new cmdMusic();
            musicControler.vol_higher();
        }
    }
}
