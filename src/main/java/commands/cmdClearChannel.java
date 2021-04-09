package commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

import java.util.List;
import java.util.Objects;

public class cmdClearChannel implements command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        for(int i = 0; i < Objects.requireNonNull(event.getMember()).getRoles().size(); i++){
            String input = event.getMember().getRoles().get(i).toString();
            if(input.contains(STATIC.NameofJarvisControlRole)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args.length == 0){
            List<Message> messages = event.getChannel().getHistory().retrievePast(50).complete();
            if (messages.size() > 1) {
                for (Message message : messages) {
                    String t = message.toString();
                    String requiredString = t.substring(t.indexOf("(") + 1, t.indexOf(")"));
                    event.getChannel().deleteMessageById(requiredString).queue();
                }
            }
        }else{
            int input = Integer.parseInt(args[0]);
            List<Message> messages = event.getChannel().getHistory().retrievePast(input).complete();
            if (messages.size() > 0) {
                for (Message message : messages) {
                    String t = message.toString();
                    String requiredString = t.substring(t.indexOf("(") + 1, t.indexOf(")"));
                    event.getChannel().deleteMessageById(requiredString).complete();
                }
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
