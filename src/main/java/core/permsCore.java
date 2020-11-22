package core;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.STATIC;

import java.util.Arrays;


public class permsCore {

    public static boolean check (MessageReceivedEvent event){
        //false = event
        //true = no event

        if(Arrays.stream(STATIC.PERMS).anyMatch(event.getMember().getRoles().toString()::contains)){
            return false;
        }else {
            event.getTextChannel().sendMessage("you have not hte permissions to do that");
            System.out.println("No");
            return true;
        }

    }

}
