package core;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.Static;

import java.util.Arrays;
import net.dv8tion.jda.api.entities.*;


public class permsCore {

    public static boolean check (MessageReceivedEvent event){
        //false = event
        //true = no event

        if(Arrays.stream(Static.PERMS).anyMatch(event.getMember().getRoles().toString()::contains)){
            return false;
        }else {
            event.getTextChannel().sendMessage("you have not hte permissions to do that");
            System.out.println("No");
            return true;
        }

    }

}
