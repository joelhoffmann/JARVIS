package listeners;


import commands.cmdMusic;
import core.commandHandler;
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
    private static Logger LOGGER = LoggerFactory.getLogger(commandListener.class);

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
                LOGGER.info(event.getMessage().getContentDisplay());
                event.getMessage().delete().queue();
            }
            commandHandler.handleCommand(commandHandler.parse.parser(event.getMessage().getContentDisplay(), event));
        }
    }

    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        LOGGER.info(event.getReaction().getReactionEmote().getEmoji());
        if (event.getUser().isBot()) return;
        //Musik
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforSkip)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            cmdMusic musicController = new cmdMusic();
            musicController.skip();
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforStop)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            cmdMusic musicController = new cmdMusic();
            musicController.stop();
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.EmoteforShuffle)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            cmdMusic musicController = new cmdMusic();
            musicController.shuffle();
        }
        if(event.getReactionEmote().getEmoji().contains(STATIC.EmoteforPause)){
            event.getReaction().removeReaction(event.getUser()).queue();
            cmdMusic musicController = new cmdMusic();
            musicController.pause();
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.Emoteforlower)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            cmdMusic musicController = new cmdMusic();
            musicController.vol_lower();
        }
        if (event.getReactionEmote().getEmoji().contains(STATIC.Emoteforhigher)) {
            event.getReaction().removeReaction(event.getUser()).queue();
            cmdMusic musicController = new cmdMusic();
            musicController.vol_higher();
        }
    }
}
