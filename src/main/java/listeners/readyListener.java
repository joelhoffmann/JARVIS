package listeners;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SECRETS;
import util.STATIC;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class readyListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(readyListener.class);

    public static void main(String[] args) throws LoginException {
        JDABuilder.createLight(SECRETS.Token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new readyListener())
                .build();
    }

    public void onReady(ReadyEvent event) {
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd.MM - HH:mm ");
            Date currentTime = new Date();
            LOGGER.info("going online:" + formatter.format(currentTime));
            event.getJDA().getTextChannelsByName(STATIC.NameofControlChannel, false).get(0).sendMessage("-Online-\n" +
                    formatter.format(currentTime) +
                    "\n-Status Ende -").queue();
            LOGGER.info("Jarvis is ready");
    }

    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        if(event.getGuild().getRoleById("827485243627929610") != null){
            event.getGuild().addRoleToMember(event.getMember(), Objects.requireNonNull(event.getGuild().getRoleById("827485243627929610"))).complete();
        }
        System.out.println("test................................");
        event.getUser().openPrivateChannel().queue((channel) ->
                channel.sendMessage("Hey " + event.getUser().getName() + "!" +
                        "\nCool das du auf meinen Server gekommen bist!" +
                        "\nIch bin Jarvis, der allwissende Bot auf dem Server 'brain.exe'" +
                        "\nViel Spaß auf dem Server!!!").queue());
    }
}
