package core;

import commands.*;
import listeners.commandListener;
import listeners.readyListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import util.SECRETS;

import javax.security.auth.login.LoginException;

public class Main {
    public static JDABuilder builder;

    public static void main(String[] args) {
        builder = JDABuilder.createDefault(SECRETS.Token);
        builder.setToken(SECRETS.Token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        addListener();
        addCommand();
        try {
            builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static void addListener() {
        builder.addEventListeners(new readyListener());
        builder.addEventListeners(new commandListener());

    }

    public static void addCommand() {
        commandHandler.commands.put("ping", new cmdPing());
        commandHandler.commands.put("voice", new cmdVoiceChannelCreate());
        commandHandler.commands.put("delete", new cmdVoiceChannelDelete());
        commandHandler.commands.put("join", new cmdAnmelden());
        commandHandler.commands.put("kick", new cmdKick());
        commandHandler.commands.put("ban", new cmdBan());
        commandHandler.commands.put("music", new cmdMusic());
    }
}
