package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

public class cmdPing implements command {


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (event.getTextChannel().getName().equals("jarvis_control")) {
            return false;
        } else {
            System.out.println("[INFO] Command Ping wurde nicht ausgeführt");
            return true;
        }

    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        event.getChannel().sendMessage("Pong").queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        System.out.println("[INFO] Command Ping wurde ausgeführt");
    }

    @Override
    public String help() {
        return null;
    }
}
