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


import java.text.SimpleDateFormat;
import java.util.Date;


public class readyListener extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        SECRETS secrets = new SECRETS();
        JDABuilder.createLight(secrets.getToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
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

        } else {
            //TODO: needs to be done in slf4j
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy.MM.dd - HH:mm:ss ");
            Date currentTime = new Date();
            System.out.println("going online:" + formatter.format(currentTime));

            event.getJDA().getTextChannelsByName(STATIC.NameofControlChannel, false).get(0).sendMessage("-Online-\n" +
                    formatter.format(currentTime) +
                    "\n-Status Ende -").queue();
            System.out.println("Jarvis is ready");
        }
        String out = "\nThis bot is running on following servers: \n";
        for (Guild g : event.getJDA().getGuilds()) {
            out += g.getName() + " (" + g.getId() + ") \n";
        }
        System.out.println(out);
    }

    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        System.out.println("test................................");
        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("827485243627929610")).complete();
        event.getUser().openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage("Hey " + event.getUser().getName() + "!" +
                    "\nCool das du auf meinen Server gekommen bist!" +
                    "\nIch bin Jarvis, der allwissende Bot auf dem Server 'brain.exe'" +
                    "\nViel Spaß auf dem Server!!!").queue();
        });
    }



}
