package commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

import java.util.EnumSet;

public class cmdSetup implements command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        /*Todo:
           Channel: welcome channel (Variable machen in den utils) + rolle welcome
                    jarvis-control + rolle botcontrol
                    music-control + rolle music_control
         */
        if(event.getGuild().getCategoriesByName("Jarvis", false).size() == 0){
            event.getGuild().createCategory("Jarvis").complete();
            event.getGuild().createRole().setName("welcome").complete();
            event.getGuild().createTextChannel("welcome", event.getGuild().getCategoriesByName("Jarvis", false).get(0))
                    .addRolePermissionOverride(event.getGuild().getRolesByName("welcome", false).get(0).getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .complete();
            event.getGuild().createRole().setName("bot_control").complete();
            event.getGuild().createTextChannel("jarvis_control", event.getGuild().getCategoriesByName("Jarvis", false).get(0))
                    .addRolePermissionOverride(event.getGuild().getRolesByName("bot_control", false).get(0).getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .complete();
            event.getGuild().createRole().setName("music_control").complete();
            event.getGuild().createTextChannel("music_control", event.getGuild().getCategoriesByName("Jarvis", false).get(0))
                    .addRolePermissionOverride(event.getGuild().getRolesByName("music_control", false).get(0).getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .complete();

            STATIC.IDofMusicControlChannel = event.getGuild().getTextChannelsByName("music_control", false).get(0).getId();
            STATIC.IDofWelcomeChannel = event.getGuild().getTextChannelsByName("welcome", false).get(0).getId();
            STATIC.IDofControlChannel = event.getGuild().getTextChannelsByName("jarvis_control", false).get(0).getId();
            event.getJDA().getTextChannelsByName("jarvis_control", false).get(0).sendMessage("-Online-\n" +
                    "ID of music-control channel:  " + STATIC.IDofMusicControlChannel + "\n" +
                    "ID of welcome channel:           " + STATIC.IDofWelcomeChannel + "\n" +
                    "ID of jarvis.control channel:    " + STATIC.IDofControlChannel + "\n" +
                    "-Status Ende -").queue();
            event.getGuild().getTextChannelsByName("jarvis_control", false).get(0).sendMessage("-Jarvis is ready-").queue();
            System.out.println("Jarvis is ready");

        }else {
            event.getGuild().getTextChannelsByName("jarvis_control", false).get(0).sendMessage("-Jarvis is already good to go-").queue();
            System.out.println("Jarvis is already good to go");
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
