package util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JoinMessageContent {
    public static List<String> member;

    public JoinMessageContent(){
        member = new ArrayList<>();
        member.add("Malte: 🐺");
        member.add("Simon: 🐻");
        member.add("Joel: 🦒");
    }
    public void sendmessage (MessageReceivedEvent event){
        List<String> emoji = new ArrayList<>();
        for(int i = 0; i < member.size(); i++){
            emoji.add(member.get(i).substring(member.get(i).length() - 2));
        }
        String text = "";
        for(int i = 0; i < member.size(); i++){
            text += "\n" + member.get(i) ;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setTitle("Wilkommen auf brain.exe!");
        embedBuilder.setDescription("Fühl dich wie zu Hause." +
                "\nWegen wem bist du hier ?" +
                "\nKlicke doch einfach auf das passende Emoji unter dieser Nachricht und ich ereldige alles weitere." +
                text +
                "\nWenn du fertig bist, klicke auf den Haken!");
        Message msg = event.getGuild().getTextChannelsByName(STATIC.NameofWelcomeChannel, false).get(0).sendMessage(embedBuilder.build()).complete();
        for(int i = 0; i < emoji.size(); i++){
            msg.addReaction(emoji.get(i)).complete();
        }
        msg.addReaction(STATIC.Emoteforready).complete();
        embedBuilder.clear();
    }
}
