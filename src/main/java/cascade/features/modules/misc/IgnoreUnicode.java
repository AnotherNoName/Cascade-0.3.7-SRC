//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.misc;

import cascade.event.events.PacketEvent;
import cascade.features.command.Command;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class IgnoreUnicode
extends Module {
    Setting<Integer> threshold = this.register(new Setting<Integer>("Threshold", 420, 10, 550));

    public IgnoreUnicode() {
        super("IgnoreUnicode", Module.Category.MISC, "");
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        String chatMessage;
        if (IgnoreUnicode.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (e.getPacket() instanceof SPacketChat && (chatMessage = ((SPacketChat)e.getPacket()).getChatComponent().getUnformattedText()).getBytes().length > this.threshold.getValue()) {
            e.setCanceled(true);
            Command.sendMessage("Ignored unicode message(" + chatMessage.getBytes().length + ")");
        }
    }
}

