package core;

import comments.cmdPing;
import comments.cmdVoiceChannel;
import listeners.commandListener;
import listeners.readyListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import util.SECRETS;

import javax.security.auth.login.LoginException;

public class Main {
    public static JDABuilder builder;
    public static void main(String[] args) {
        builder = JDABuilder.createDefault(SECRETS.Token);


        builder.setToken(SECRETS.Token);

        //builder.setActivity(Activity.watching("yx"));

        builder.setStatus(OnlineStatus.ONLINE);

        builder.addEventListeners(new readyListener());
        builder.addEventListeners(new commandListener());

        addCommand();
        try {
            builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }


    }
    public static void addCommand(){
        commandHandler.commands.put("ping", new cmdPing());
        commandHandler.commands.put("voice", new cmdVoiceChannel());
    }
}
