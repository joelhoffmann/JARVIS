package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

public class cmdPing implements command {


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return !event.getTextChannel().getName().equals(STATIC.NameofControlChannel);
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        event.getChannel().sendMessage("Pong").queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
