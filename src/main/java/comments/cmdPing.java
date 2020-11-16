package comments;

import core.permsCore;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class cmdPing implements command {


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {

            return false;

    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(permsCore.check(event)){
            return;
        }
        event.getTextChannel().sendMessage("Pong").queue();
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
