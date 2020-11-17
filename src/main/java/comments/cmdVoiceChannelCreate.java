package comments;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.EnumSet;

public class cmdVoiceChannelCreate implements command {

    public static String name_channel;
    public static String name_voice;
    public static String name_rolle;
    public static String name_kategorie;

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

        if (event.getGuild().getVoiceChannelsByName(name_voice, false).size() == 0) {
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

            //DirectMessage
            event.getAuthor().openPrivateChannel().queue((channel) ->
            {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor("Jarvis");
                eb.setTitle("Hey " + event.getAuthor().getName() + "!\n");
                eb.setDescription("Ich habe dir deinen eigenen Sprach- und Textkanal erstellt. " +
                        "\nDie Kanäle gibt es aber nicht für immer !!! ⚠ " +
                        "\nNach 24h Inaktivität werden beide Kanäle wieder gelöscht." +
                        "\nDenk dran: Kein Backup, kein Mittleid." +
                        "\nViel Spaß mit deinem eigen Teil des Servers");
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
