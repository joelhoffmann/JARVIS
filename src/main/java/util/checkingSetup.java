package util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class checkingSetup {

    public static String text;

    public checkingSetup (){
        List<String> doings = new ArrayList<>();
    }
    public static boolean checkMissing(Guild g){
        text = "";
        List<String> doings = new ArrayList<>();
        if (g.getCategoriesByName(STATIC.NameOfCategorie, false).size() == 0) {
            doings.add("missing category");
        }
        if (g.getRolesByName(STATIC.NameofWelcomeRole, false).size() == 0) {
            doings.add("missing welcome role");
        }
        if (g.getRolesByName(STATIC.NameofJarvisControlRole, false).size() == 0) {
            doings.add("missing bot-control role");
        }
        if (g.getRolesByName(STATIC.NameofMusicControlRole, false).size() == 0) {
            doings.add("missing music-control role");
        }
        if (g.getTextChannelsByName(STATIC.NameofWelcomeChannel, false).size() == 0) {
            doings.add("missing welcome chat");
        }
        if (g.getTextChannelsByName(STATIC.NameofControlChannel, false).size() == 0) {
            doings.add("missing bot-control channel");
        }
        if (g.getTextChannelsByName(STATIC.NameofMusicControlChannel, false).size() == 0) {
            doings.add("missing music-control channel");
        }
        if(doings.size() > 0){
            for(int i = 0; i < doings.size(); i++){
                text += "\n" + doings.get(i);
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.blue)
                    .setTitle("Achtung!!!")
                    .setDescription("Es sind nich alle nötigen Dinge auf deinem Server installiert, damit Jravis funktioniert." +
                            "\nMissing Things:" +
                            text +
                            "\nMöchtest du, dass ich es fixe?");
            Message msg = g.getTextChannelsByName(STATIC.NameofControlChannel, false).get(0).sendMessage(eb.build()).complete();
            msg.addReaction(STATIC.Emoteforready).complete();
            return true;
        }
        else {
            return false;
        }
    }
}
