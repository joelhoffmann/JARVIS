package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class cmdKick implements command {
    //TODO: class is not in use and could be deleted.
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
