package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import util.STATIC;

import java.util.Arrays;

public class cmdKick implements command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (Arrays.stream(STATIC.PERMStoKick).anyMatch(event.getMember().getRoles().toString()::contains)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentDisplay().split(" ");
        System.out.println(split[1]);
        String usertoKick = event.getGuild().getMembersByName("Pinky", false).get(0).toString();
        event.getGuild().kick(usertoKick).reason("Jarvis hat dich aus Gründen gekicket...");
        System.out.print("Input: ");
        for(int i = 0; i< split.length; i++){
            System.out.println(split[i]);
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
