package commands.test;

import commands.command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;
import java.util.EnumSet;

public class cmdVoiceChannelCreate implements command {

    public static String name_channel;
    public static String name_voice;
    public static String name_rolle;
    public static String name_kategorie;
    public static String name_command;

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

        if (event.getGuild().getRolesByName(name_rolle, false ).size() == 0) {
            Guild guild = event.getGuild();
            //Rolle erstellen
            guild.createRole().setName(name_rolle).complete();
            event.getGuild().addRoleToMember(event.getAuthor().getId(), event.getGuild().getRolesByName(name_rolle, false).get(0)).queue();
            //Channel erstellen
            guild.createCategory(name_kategorie)
                    .addRolePermissionOverride(event.getGuild().getRolesByName(name_rolle, false).get(0).getIdLong(), EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL))
                    .complete();
            guild.createVoiceChannel(name_voice, event.getGuild().getCategoriesByName(name_kategorie, false).get(0))
                    .addRolePermissionOverride(event.getGuild().getRolesByName(name_rolle, false).get(0).getIdLong(), EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL))
                    .queue();
            guild.createTextChannel(name_channel, event.getGuild().getCategoriesByName(name_kategorie, false).get(0))
                    .addRolePermissionOverride(event.getGuild().getRolesByName(name_rolle, false).get(0).getIdLong(), EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL))
                    .queue();
            guild.createTextChannel(name_command, event.getGuild().getCategoriesByName(name_kategorie, false).get(0))
                    .addRolePermissionOverride(event.getGuild().getRolesByName(name_rolle, false).get(0).getIdLong(), EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL))
                    .queue();

            //DirectMessage
            event.getAuthor().openPrivateChannel().queue((channel) ->
            {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor("Jarvis");
                eb.setTitle("Hey " + event.getAuthor().getName() + "!\n");
                eb.setDescription("Ich habe dir deinen eigenen Sprach- und Textkanal erstellt. " +
                        "\nDie Kanäle gibt es aber nicht für immer !!! ⚠ " +
                        "\nNach langer Inaktivität werden beide Kanäle wieder gelöscht." +
                        "\nAlso denk dran: Kein Backup, kein Mittleid." +
                        "\nViel Spaß mit deinem eigenen Teil des Servers!");
                eb.setColor(Color.red);
                channel.sendMessage(eb.build()).queue();
            });
        } else {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setDescription("⚠" + " Du hast schon deinen eigenen Berreich!!!");
            eb.setColor(Color.red);
            event.getTextChannel().sendMessage(eb.build()).queue();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        //System.out.println(event.getGuild().getRolesByName(name_rolle,false).get(0).getId());
        System.out.println("Done");
    }

    @Override
    public String help() {
        return null;
    }
}
