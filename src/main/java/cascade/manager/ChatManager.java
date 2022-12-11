/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package cascade.manager;

import cascade.features.Feature;
import cascade.features.command.Command;
import cascade.features.command.commands.BindCommand;
import cascade.features.command.commands.ConfigCommand;
import cascade.features.command.commands.FriendCommand;
import cascade.features.command.commands.KickCommand;
import cascade.features.command.commands.ModuleCommand;
import cascade.features.modules.core.Manager;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChatManager
extends Feature {
    ArrayList<Command> com = new ArrayList();
    String clientMessage;
    String prefix;

    public ChatManager() {
        super("Command");
        this.clientMessage = Manager.getInstance().name.getValue();
        this.prefix = ".";
        this.com.add(new BindCommand());
        this.com.add(new ConfigCommand());
        this.com.add(new FriendCommand());
        this.com.add(new KickCommand());
        this.com.add(new ModuleCommand());
    }

    public static String[] removeElement(String[] input, int indexToDelete) {
        LinkedList<String> result = new LinkedList<String>();
        for (int i = 0; i < input.length; ++i) {
            if (i == indexToDelete) continue;
            result.add(input[i]);
        }
        return result.toArray(input);
    }

    static String strip(String str, String key) {
        if (str.startsWith(key) && str.endsWith(key)) {
            return str.substring(key.length(), str.length() - key.length());
        }
        return str;
    }

    public void executeCommand(String command) {
        String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String name = parts[0].substring(1);
        String[] args = ChatManager.removeElement(parts, 0);
        for (int i = 0; i < args.length; ++i) {
            if (args[i] == null) continue;
            args[i] = ChatManager.strip(args[i], "\"");
        }
        for (Command c : this.com) {
            if (!c.getName().equalsIgnoreCase(name)) continue;
            c.execute(parts);
            return;
        }
    }

    public Command getCommandByName(String name) {
        for (Command command : this.com) {
            if (!command.getName().equals(name)) continue;
            return command;
        }
        return null;
    }

    public ArrayList<Command> getCommands() {
        return this.com;
    }

    public String getClientMessage() {
        return this.clientMessage + ChatFormatting.BOLD;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

