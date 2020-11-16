package comments;

import core.commandHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.entities.CategoryImpl;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;


import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import util.Variable;

public class cmdVoiceChannel implements command {

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
        name_voice = "Allgemein";
        name_rolle = event.getAuthor().getName() + "´s Rolle";
        name_kategorie = event.getAuthor().getName();

        Guild guild = event.getGuild();
        //Rolle erstellen
        guild.createRole().setName(name_rolle).complete();
        event.getGuild().addRoleToMember(event.getAuthor().getId(), event.getGuild().getRolesByName(name_rolle,false).get(0)).queue();
        //Channel erstellen
        guild.createCategory(name_kategorie)
                .addRolePermissionOverride(event.getGuild().getRolesByName(name_rolle,false).get(0).getIdLong(), EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL), null)
                .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL))
                .complete();
        guild.createVoiceChannel(name_voice, event.getGuild().getCategoriesByName(name_kategorie, false).get(0))
                .addRolePermissionOverride(event.getGuild().getRolesByName(name_rolle,false).get(0).getIdLong(), EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL), null)
                .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL))
                .queue();
        guild.createTextChannel(name_channel, event.getGuild().getCategoriesByName(name_kategorie, false).get(0))
                .addRolePermissionOverride(event.getGuild().getRolesByName(name_rolle,false).get(0).getIdLong(), EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL), null)
                .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL))
                .queue();

        //DirectMessage
        event.getAuthor().openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage("Hey " + event.getAuthor().getName() + "!\n" + "Ich habe dir deinen eigenen Sprach- und Textkanal erstellt\nDie Kanäle gibt es aber nicht für immer !!! Nach 24h inaktivität werden beide Kanäle wieder gelöscht\nDenk dran: Kein Backup, kein Mittleid\nViel Spaß mit deinem eigen Teil des Servers").queue();
        });



    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        System.out.println(event.getGuild().getRolesByName(name_rolle,false).get(0).getId());
        System.out.println("Done");
    }

    @Override
    public String help() {
        return null;
    }
}
