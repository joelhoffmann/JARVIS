package comments;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.entities.CategoryImpl;

import java.util.concurrent.TimeUnit;


import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class cmdVoiceChannel implements command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String name = event.getAuthor().getName() + "´s Channel";
                Guild guild = event.getGuild();
        guild.createVoiceChannel(name, event.getGuild().getCategoryById("693164328500854786"))
                .queue();

        event.getAuthor().openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage("Ja hier, blubedi blabbedi").queue();
        });


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        System.out.println("Done");
    }

    @Override
    public String help() {
        return null;
    }
}
