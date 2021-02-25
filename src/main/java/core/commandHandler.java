package core;

import commands.command;

import java.util.HashMap;

public class commandHandler {

    public static commandParser parse = new commandParser();
    public static HashMap<String, command> commands = new HashMap<>();

    public static void handleCommand(commandParser.commandContainer cmd) {
        if (commands.containsKey(cmd.invoke)) {
            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);
            if (!safe) {
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
        } else {
            System.out.println("Du hast den Token vergessen/ flaschen befehl verwendet");
        }
    }
}
