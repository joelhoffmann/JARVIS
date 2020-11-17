package comments;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class cmdAnmelden implements command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage("Hey " + event.getAuthor() +  "!" +
                    "\nCool das du auf meinen Server gekommen bist!" +
                    "\nIch bin Jarvis und kontrolliere hier alles" +
                    "\n Schau doch einfach mal in den Willkommenschannel auf dem Server!").queue();
        });
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.red);
        embedBuilder.setTitle("Verify yourself!");
        event.getGuild().getTextChannelById("778314557134471179").sendMessage(embedBuilder.build()).complete().addReaction("👺").complete();
        embedBuilder.clear();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
