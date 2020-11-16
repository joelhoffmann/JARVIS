package core;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import util.Static;

import java.util.ArrayList;

public class commandParser {

    public static commandContainer parser (String raw, MessageReceivedEvent event){
        String beheaded = raw.replaceFirst(Static.prefix, "");
        String[] splitbeheaded = beheaded.split(" ");
        String invoke = splitbeheaded[0];
        ArrayList<String> split = new ArrayList<>();

        for(String s : splitbeheaded){
            split.add(s);
        }

        String[] args = new String[split.size() -1 ];
        split.subList(1, split.size()).toArray(args);
        return new commandContainer(raw, beheaded, splitbeheaded, invoke, args, event);
    }

    public static class commandContainer {

        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final MessageReceivedEvent event;

        public commandContainer(String rw, String beheaded, String[] splitBeheaded, String invoke, String[] args, MessageReceivedEvent event) {
            this.raw = rw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = event;
        }

    }
}
