package listeners;


import commands.cmdMusic;
import core.commandHandler;
import core.commandParser;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SECRETS;
import util.STATIC;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;


public class commandListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(commandListener.class);

    public void main(String[] args) throws LoginException {
        JDABuilder.createLight(SECRETS.Token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new readyListener())
                .build();
    }

    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getMessage().getContentDisplay().startsWith(STATIC.prefix) && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            if (event.getAuthor().isBot()) {
                return;
            } else {
                LOGGER.info("command: " + event.getMessage().getContentDisplay());
                event.getMessage().delete().queue();
            }
            commandHandler.handleCommand(commandParser.parser(event.getMessage().getContentDisplay(), event));
        }
    }

    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        LOGGER.info(event.getReaction().getReactionEmote().getEmoji());
        cmdMusic musicController = new cmdMusic();
        if (event.getUser().isBot()) return;
        //Musik
        else if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforSkip)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            musicController.skip();
        }
        else if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforStop)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            musicController.stop();
        }
        else if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforShuffle)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            musicController.shuffle();
        }
        else if(event.getReactionEmote().getEmoji().contains(STATIC.EmoteforPause)){
            event.getReaction().removeReaction(event.getUser()).queue();
            musicController.pause();
        }
        else if (event.getReactionEmote().getEmoji().contains(STATIC.Emoteforlower)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            musicController.vol_lower();
        }
        else if (event.getReactionEmote().getEmoji().contains(STATIC.Emoteforhigher)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            musicController.vol_higher();
        }
        else if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforDelete)){
            event.getChannel().deleteMessageById(event.getMessageId()).queue();
        }
    }
}
