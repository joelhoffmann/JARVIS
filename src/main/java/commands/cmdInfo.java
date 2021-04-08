package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class cmdInfo implements command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        if(event.getTextChannel().getName().contains("Allgemein")){
            if(event.getGuild().getCategoriesByName("Jarvis", false).size() == 0){
                eb.setTitle("Info");
                eb.setDescription("Nutze den Befehl '.setup', damit Jarvis alles erstellen kann, was er zum Funktionieren brauch!");
                eb.setColor(Color.red);
                event.getChannel().sendMessage(eb.build()).queue();
            }
            else {

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
