package commands.test;

import commands.command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;

import static commands.test.cmdVoiceChannelCreate.name_voice;
import static commands.test.cmdVoiceChannelCreate.name_channel;
import static commands.test.cmdVoiceChannelCreate.name_rolle;
import static commands.test.cmdVoiceChannelCreate.name_kategorie;
import static commands.test.cmdVoiceChannelCreate.name_command;

public class cmdVoiceChannelDelete implements command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (event.getChannel().getId().equals(STATIC.IDofControlChannel)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        name_channel = "chat";
        name_voice = "voice";
        name_command = "bot";
        name_rolle = event.getAuthor().getName() + "´s Rolle";
        name_kategorie = event.getAuthor().getName();

        /* Für spätet
        String[] split = event.getMessage().getContentDisplay().split(" ");

        System.out.print("Input: ");
        for(int i = 0; i< split.length; i++){
            System.out.println(split[i]);
        }
        */
        if (event.getGuild().getRolesByName(name_rolle, false).size() >= 1) {
            for (int i = 0; i < event.getGuild().getTextChannelsByName(name_channel, false).size(); i++) {
                String role = event.getGuild().getRolesByName(name_rolle, false).get(0).getId();
                for (int j = 0; j < event.getGuild().getTextChannelsByName(name_channel, false).get(i).getPermissionOverrides().size(); j++) {
                    String test = event.getGuild().getTextChannelsByName(name_channel, false).get(i).getPermissionOverrides().get(j).toString();
                    if (test.contains(role)) {
                        event.getGuild().getVoiceChannelsByName(name_voice, false).get(i).delete().reason("reason").complete();
                        event.getGuild().getTextChannelsByName(name_channel, false).get(i).delete().reason("reason").complete();
                        event.getGuild().getTextChannelsByName(name_command, false).get(i).delete().reason("reason").complete();
                        event.getGuild().getCategoriesByName(name_kategorie, false).get(0).delete().reason("reason").complete();
                        for (int k = 0; k < event.getGuild().getRolesByName(name_rolle, false).size(); k++) {
                            event.getGuild().getRolesByName(name_rolle, false).get(k).delete().reason("reason").complete();
                        }
                    }
                }
            }
            event.getAuthor().openPrivateChannel().queue((channel) ->
            {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor("Jarvis");
                eb.setTitle("Hey !\n");
                eb.setDescription("Ich habe deinen eigenen Bereich wieder gelöscht" +
                        "\nbei Bedarf kannst du dir einen neuen erstellen mit dem Befehl '.create'." +
                        "\nAber bitte denk dran:" +
                        "\nkein Spam ist cool. Die Server danken dir.");
                eb.setColor(Color.red);
                channel.sendMessage(eb.build()).queue();
            });
        } else {

            EmbedBuilder eb = new EmbedBuilder();
            eb.setDescription("⚠" + " Ich konnte nichts löschen");
            eb.setColor(Color.red);
            event.getTextChannel().sendMessage(eb.build()).queue();

        }
    }


    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        System.out.println("Done");
    }

    @Override
    public String help() {
        return null;
    }
}
