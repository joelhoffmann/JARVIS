package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;

public class cmdAnmelden implements command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(event.getChannel().getId().equals(STATIC.IDofControlChannel)){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        /*event.getAuthor().openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage("Hey " + event.getAuthor().getName() + "!" +
                    "\nCool das du auf meinen Server gekommen bist!" +
                    "\nIch bin Jarvis und kontrolliere hier alles." +
                    "\nSchau doch einfach mal in den Willkommenschannel auf dem Server!").queue();
        });

         */
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setTitle("Wilkommen auf brain.exe!");
        embedBuilder.setDescription("Fühl dich wie zu Hause." +
                "\nWegen wem bist du hier ?" +
                "\nKlicke doch einfach auf das passende Emoji unter dieser Nachricht und ich ereldige alles weitere." +
                "\n Für Malte " + STATIC.EmoteforMalte +
                "\n Für Simon " + STATIC.EmoteforNXZAS8CA +
                "\n Für Hoffi " + STATIC.EmoteforHoffi +
                "\nWenn du fertig bist, klicke auf den Haken!");

        Message msg = event.getGuild().getTextChannelById(STATIC.IDofWelcomeChannel).sendMessage(embedBuilder.build()).complete();
        msg.addReaction(STATIC.EmoteforMalte).complete();
        msg.addReaction(STATIC.EmoteforNXZAS8CA).complete();
        msg.addReaction(STATIC.EmoteforHoffi).complete();
        msg.addReaction(STATIC.Emoteforready).complete();
        embedBuilder.clear();
        event.getGuild().addRoleToMember(event.getMember().getId(), event.getGuild().getRolesByName("welcome", false).get(0)).queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
