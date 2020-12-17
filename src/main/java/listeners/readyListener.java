package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import util.SECRETS;
import util.STATIC;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.awt.*;

public class readyListener extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        JDABuilder.createLight(SECRETS.Token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new readyListener())
                .build();
    }

    public void onReady(ReadyEvent event) {
        if (event.getJDA().getCategoriesByName("Jarvis", false).size() == 0) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Info!");
            eb.addField("Setup", "Dieser Befehl erstellt alle Kanäle/Rollen auf dem Server, die der Bot braucht.", false);
            eb.setColor(Color.red);
            event.getJDA().getTextChannels().get(0).sendMessage(eb.build()).queue();
            System.out.println("Jarvis wants to setup");
        } else {
            STATIC.IDofMusicControlChannel = event.getJDA().getTextChannelsByName("music_control", false).get(0).getId();
            STATIC.IDofWelcomeChannel = event.getJDA().getTextChannelsByName("welcome", false).get(0).getId();
            STATIC.IDofControlChannel = event.getJDA().getTextChannelsByName("jarvis_control", false).get(0).getId();
            event.getJDA().getTextChannelsByName("jarvis_control", false).get(0).sendMessage("-Online-\n" +
                    "ID of music-control channel:  " + STATIC.IDofMusicControlChannel + "\n" +
                    "ID of welcome channel:           " + STATIC.IDofWelcomeChannel + "\n" +
                    "ID of jarvis.control channel:    " + STATIC.IDofControlChannel + "\n" +
                    "-Status Ende -").queue();
            System.out.println("Jarvis is ready");
        }
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
