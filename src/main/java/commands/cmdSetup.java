package commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;
import util.checkingSetup;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class cmdSetup implements command {
    //TODO: needs also a rework
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        makeSetup(event.getGuild());
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        System.out.println("[Done]");
    }

    @Override
    public String help() {
        return null;
    }

    public static void makeSetup(Guild g) {
        List<String> doings = new ArrayList<>();
        //Categorie erstellen------------------------------------------------------------------------------
        if (g.getCategoriesByName(STATIC.NameOfCategorie, false).size() == 0) {
            g.createCategory(STATIC.NameOfCategorie).complete();
            doings.add("creating category");
        }
        //Welcome role und channel------------------------------------------------------------------------------
        if (g.getRolesByName(STATIC.NameofWelcomeRole, false).size() == 0) {
            g.createRole().setName(STATIC.NameofWelcomeRole).setColor(Color.GRAY).complete();
            doings.add("creating welcome role");
            if (g.getTextChannelsByName(STATIC.NameofWelcomeChannel, false).size() > 0) {
                g.getTextChannelsByName(STATIC.NameofWelcomeChannel, false).get(0).createPermissionOverride(g.getRolesByName(STATIC.NameofWelcomeRole, false).get(0)).setAllow(Permission.VIEW_CHANNEL).complete();
                g.getTextChannelsByName(STATIC.NameofWelcomeChannel, false).get(0).createPermissionOverride(g.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).complete();
                doings.add("creating welcome chat");
            }
        }
        if(g.getTextChannelsByName(STATIC.NameofWelcomeChannel, false).size() == 0){
            g.createTextChannel(STATIC.NameofWelcomeChannel, g.getCategoriesByName(STATIC.NameOfCategorie, false).get(0))
                    .addRolePermissionOverride(g.getRolesByName(STATIC.NameofWelcomeRole, false).get(0).getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(g.getPublicRole().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .complete();
            doings.add("creating welcome chat");
        }
        //Jarvis role und channel------------------------------------------------------------------------------
        if (g.getRolesByName(STATIC.NameofJarvisControlRole, false).size() == 0) {
            g.createRole().setName(STATIC.NameofJarvisControlRole).setColor(Color.magenta).complete();
            doings.add("creating bot-control role");
            if (g.getTextChannelsByName(STATIC.NameofControlChannel, false).size() > 0) {
                g.getTextChannelsByName(STATIC.NameofControlChannel, false).get(0).createPermissionOverride(g.getRolesByName(STATIC.NameofJarvisControlRole, false).get(0)).setAllow(Permission.VIEW_CHANNEL).complete();
                g.getTextChannelsByName(STATIC.NameofControlChannel, false).get(0).createPermissionOverride(g.getPublicRole()).setDeny(Permission.VIEW_CHANNEL).complete();
                doings.add("creating welcome chat");
            }
        }
        if (g.getTextChannelsByName(STATIC.NameofControlChannel, false).size() == 0) {
            g.createTextChannel(STATIC.NameofControlChannel, g.getCategoriesByName(STATIC.NameOfCategorie, false).get(0))
                    .addRolePermissionOverride(g.getRolesByName(STATIC.NameofJarvisControlRole, false).get(0).getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(g.getPublicRole().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .complete();
            doings.add("creating bot-control channel");
        }
        //Musik role und channel------------------------------------------------------------------------------
        if (g.getRolesByName(STATIC.NameofMusicControlRole, false).size() == 0) {
            g.createRole().setName(STATIC.NameofMusicControlRole).setColor(Color.cyan).complete();
            doings.add("creating music-control role");
            if (g.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).size() > 0) {
                g.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).createPermissionOverride(g.getRolesByName(STATIC.NameofJarvisControlRole, false).get(0)).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION).complete();
                g.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).get(0).createPermissionOverride(g.getPublicRole()).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION).complete();
                doings.add("creating welcome chat");
            }
        }
        if (g.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).size() == 0) {
            g.createTextChannel(STATIC.NameofMusicControlChannel, g.getCategoriesByName(STATIC.NameOfCategorie, false).get(0))
                    .addRolePermissionOverride(g.getRolesByName(STATIC.NameofMusicControlRole, false).get(0).getIdLong(), EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION), null)
                    .addRolePermissionOverride(g.getPublicRole().getIdLong(), null, EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION))
                    .complete();
            doings.add("creating music-control channel");
        }
        //Ending + Output ------------------------------------------------------------------------------
        if (doings.size() == 0) {
            g.getTextChannelsByName(STATIC.NameofControlChannel, false).get(0).sendMessage("-Jarvis is already good to go-").queue();
            }
        if (doings.size() > 0) {
            String text = "";
            for (int i = 0; i < doings.size(); i++) {
                text += ("\n" + doings.get(i));
            }
            g.getJDA().getTextChannelsByName(STATIC.NameofControlChannel, false).get(0).sendMessage("-Online-\n" +
                    "doings:" + text).queue();
            g.getTextChannelsByName(STATIC.NameofControlChannel, false).get(0).sendMessage("-Jarvis is ready-").queue();
            }
    }
}
