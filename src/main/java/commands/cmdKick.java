package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class cmdKick implements command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        System.out.println("Getting kicked");
        //event.getGuild().getMembersByName("Pinky", false).get(0).kick().queue();
        //event.getGuild().getMembersByName("Pinky", false).get(0).kick().queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
