package core;

import commands.command;

import java.util.HashMap;

public class commandHandler {

    public static commandParser parse = new commandParser();
    public static HashMap<String, command> commands = new HashMap<>();

    public static void handleCommand(commandParser.commandContainer cmd) {
        System.out.println("------------------------------------------------");
        System.out.println("[RAW] " + cmd.raw);
        //System.out.println(cmd.beheaded);
        //System.out.println(cmd.splitBeheaded);
        //System.out.println(cmd.invoke);
        //System.out.println(cmd.args);
        if (commands.containsKey(cmd.invoke)) {
            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);
            if (!safe) {
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
        } else {
            System.out.println("Du hast den Token vergessen/ flaschen befehl verwendet");
        }
        System.out.println("------------------------------------------------");
    }
}
