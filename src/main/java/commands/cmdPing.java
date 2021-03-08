package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;
import util.checkingSetup;

public class cmdPing implements command {


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (event.getTextChannel().getName().equals(STATIC.NameofControlChannel)) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        event.getChannel().sendMessage("Pong").queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
