package commands;

import core.permsCore;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import util.STATIC;

public class cmdPing implements command {


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
        
        if(permsCore.check(event)){
            return;
        }
        event.getTextChannel().sendMessage("Pong").complete();


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
