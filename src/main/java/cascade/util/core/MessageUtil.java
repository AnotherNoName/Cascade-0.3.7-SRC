//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 */
package cascade.util.core;

import cascade.Cascade;
import cascade.features.Feature;
import cascade.features.command.Command;
import cascade.util.Util;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class MessageUtil
implements Util {
    public static void sendMessage(String message, Boolean client, Boolean bold) {
        MessageUtil.sendSilentMessage((client != false ? Cascade.chatManager.getClientMessage() : "") + (bold != false ? ChatFormatting.BOLD : "") + " " + message);
    }

    public static void sendMessage(String message) {
        MessageUtil.sendSilentMessage(Cascade.chatManager.getClientMessage() + " " + message);
    }

    public static void sendRemovableMessage(String message, int id) {
        MessageUtil.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)new TextComponentString(message), id);
    }

    public static void sendSilentMessage(String message) {
        if (Feature.nullCheck()) {
            return;
        }
        MessageUtil.mc.player.addChatMessage((ITextComponent)new Command.ChatMessage(message));
    }

    public static void getColor() {
    }
}

