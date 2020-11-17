package comments;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.EnumSet;

import static comments.cmdVoiceChannelCreate.name_voice;
import static comments.cmdVoiceChannelCreate.name_channel;
import static comments.cmdVoiceChannelCreate.name_rolle;
import static comments.cmdVoiceChannelCreate.name_kategorie;

public class cmdVoiceChannelDelete implements command{

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        name_channel = "chat";
        name_voice = "Allgemein_User";
        name_rolle = event.getAuthor().getName() + "´s Rolle";
        name_kategorie = event.getAuthor().getName();

        /* Für spätet
        String[] split = event.getMessage().getContentDisplay().split(" ");

        System.out.print("Input: ");
        for(int i = 0; i< split.length; i++){
            System.out.println(split[i]);
        }
        */

        if (event.getGuild().getRolesByName(name_rolle, false).size() == 1) {
            Guild guild = event.getGuild();
            guild.getVoiceChannelsByName(name_voice, false).get(0).delete().reason("reason").complete();
            guild.getTextChannelsByName(name_channel, false).get(0).delete().reason("reason").complete();
            guild.getCategoriesByName(name_kategorie, false).get(0).delete().reason("reason").complete();
            guild.getRolesByName(name_rolle, false).get(0).delete().reason("reason").complete();
            //DirectMessage
            event.getAuthor().openPrivateChannel().queue((channel) ->
            {
                channel.sendMessage("gelöscht").queue();
            });
        } else {
            //DirectMessage
            event.getAuthor().openPrivateChannel().queue((channel) ->
            {
                channel.sendMessage("Gibt nix zu löschen").queue();
            });
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
