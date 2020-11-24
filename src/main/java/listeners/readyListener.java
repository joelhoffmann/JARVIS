package listeners;

import core.commandHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import util.SECRETS;
import util.STATIC;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class readyListener extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        JDABuilder.createLight(SECRETS.Token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new readyListener())
                .build();
    }

    public void onReady(ReadyEvent event) {
        String out = "\nThis bot is running on following servers: \n";
        for (Guild g : event.getJDA().getGuilds()) {
            out += g.getName() + " (" + g.getId() + ") \n";
        }
        System.out.println(out);
    }

    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        event.getGuild().addRoleToMember(event.getUser().getId(), event.getGuild().getRolesByName("welcome", false).get(0)).queue();
        event.getUser().openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage("Hey " + event.getUser().getName() + "!" +
                    "\nCool das du auf meinen Server gekommen bist!" +
                    "\nIch bin Jarvis und kontrolliere hier alles." +
                    "\nSchau doch einfach mal in den Willkommenschannel auf dem Server!").queue();
        });

    }


}
