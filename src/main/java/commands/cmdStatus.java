package commands;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

public class cmdStatus implements command {
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
        if(args[0].contains("online")){ //Online
            event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
        } else if(args[0].contains("unsichtbar")){ // Unsichtbar
            event.getJDA().getPresence().setStatus(OnlineStatus.INVISIBLE);
        } else if(args[0].contains("abwesend")){ // Abwesend
            event.getJDA().getPresence().setStatus(OnlineStatus.IDLE);
        } else if(args[0].contains("bns")){ //Bitte nicht Stören
            event.getJDA().getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
