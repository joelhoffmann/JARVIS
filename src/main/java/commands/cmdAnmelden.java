package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.JoinMessageContent;
import util.STATIC;

import java.awt.*;

public class cmdAnmelden implements command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (event.getTextChannel().getName().equals(STATIC.NameofControlChannel)) {
            return false;
        } else {
            System.out.println("[INFO] Command Ping wurde nicht ausgeführt");
            return true;
        }
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        JoinMessageContent test = new JoinMessageContent();
        test.sendmessage(event);
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        System.out.println("[INFO] Command join wurde ausgeführt");
    }

    @Override
    public String help() {
        return null;
    }
}
