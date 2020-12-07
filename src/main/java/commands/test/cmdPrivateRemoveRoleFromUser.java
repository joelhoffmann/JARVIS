package commands.test;

import commands.command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class cmdPrivateRemoveRoleFromUser implements command {
    public static String name_rolle;
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        name_rolle = event.getAuthor().getName() + "´s Rolle";
        if(event.getGuild().getRolesByName(name_rolle, false).size() > 0){
            return false;

        }else {
            return true;
        }
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String name = args[0];
        name = name.substring(1);
        event.getGuild().removeRoleFromMember(event.getGuild().getMembersByName(name, false).get(0), event.getGuild().getRolesByName(name_rolle, false).get(0)).queue();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
