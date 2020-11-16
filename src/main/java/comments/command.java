package comments;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface command {

    boolean called(String[] args, MessageReceivedEvent event);
    void action(String[] args, MessageReceivedEvent event);
    void executed(boolean success, MessageReceivedEvent event);
    String help();

}