package listeners;

import com.mysql.cj.core.exceptions.StatementIsClosedException;
import core.commandHandler;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import util.SECRETS;
import util.Static;
import util.Variable;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class commandListener extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        JDABuilder.createLight(SECRETS.Token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new readyListener())
                .build();
    }
    public void onMessageReceived(@Nonnull MessageReceivedEvent event){
        System.out.println(event.getMessage().getContentDisplay());

        if(event.getMessage().getContentDisplay().startsWith(Static.prefix) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()){
            commandHandler.handleCommand(commandHandler.parse.parser(event.getMessage().getContentDisplay(), event));
        }
    }


}
