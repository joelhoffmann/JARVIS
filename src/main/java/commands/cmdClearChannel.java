package commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class cmdClearChannel implements command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        for(int i = 0; i < event.getMember().getRoles().size(); i++){
            String input = event.getMember().getRoles().get(i).toString();
            if(input.contains("bot_control")){
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
                for (int i = 0; i < messages.size(); i++) {
                    String t = messages.get(i).toString();
                    String requiredString = t.substring(t.indexOf("(") + 1, t.indexOf(")"));
                    event.getChannel().deleteMessageById(requiredString).queue();
                }
            }
        }else{
            int input = Integer.parseInt(args[0]);
            List<Message> messages = event.getChannel().getHistory().retrievePast(input).complete();
            if (messages.size() > 0) {
                for (int i = 0; i < messages.size(); i++) {
                    String t = messages.get(i).toString();
                    String requiredString = t.substring(t.indexOf("(") + 1, t.indexOf(")"));
                    event.getChannel().deleteMessageById(requiredString).complete();
                }
            }
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
