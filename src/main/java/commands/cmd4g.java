package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

import java.awt.*;

public class cmd4g implements command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setColor(Color.red);
        eb.setTitle("Vier gewinnt");
        eb.addField("Spielfeld", "|   |   |   |   |   |   |   |", false);

        Message msg = event.getChannel().sendMessage(eb.build()).complete();
        msg.addReaction(STATIC.EmoteforMalte).complete();
        msg.addReaction(STATIC.EmoteforNXZAS8CA).complete();
        msg.addReaction(STATIC.EmoteforHoffi).complete();
        msg.addReaction(STATIC.Emoteforready).complete();
        eb.clear();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
