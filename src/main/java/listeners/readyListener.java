package listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import util.SECRETS;

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

        for (Guild g : event.getJDA().getGuilds()) {
            g.getTextChannels().get(0).sendMessage(
                    "Hey guys! Im ONLINE!"
            ).queue();
        }

    }

    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        if(event.getUser().isBot()) return;
        //Rollenzuweisung
        if(event.getReactionEmote().getEmoji().contains("👺")){
            System.out.println("emote true");
            String[] test = {"Hoffis"};
            if(Arrays.stream(test).noneMatch(event.getMember().getRoles().toString()::contains)){
                event.getGuild().addRoleToMember(event.getUser().getId(),  event.getGuild().getRolesByName("Hoffis", false).get(0)).queue();
                event.getChannel().deleteMessageById(event.getMessageId()).queueAfter(5, TimeUnit.SECONDS);
            }
            else {
                event.getChannel().sendMessage("Du hast die Rolle schon").queue();
            }
        }

    }

    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setTitle("Verify yourself!");
        event.getGuild().getTextChannelById("778314557134471179").sendMessage(embedBuilder.build()).complete().addReaction("👺").complete();
        embedBuilder.clear();
    }


}
