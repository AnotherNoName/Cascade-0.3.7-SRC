//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.misc;

import cascade.Cascade;
import cascade.event.events.PacketEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.CalcUtil;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoReply
extends Module {
    Setting<Boolean> coords = this.register(new Setting<Boolean>("CoordReply", true));

    public AutoReply() {
        super("AutoReply", Module.Category.MISC, "autp reply rbh");
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        SPacketChat p;
        String unormatted;
        if (AutoReply.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (e.getPacket() instanceof SPacketChat && ((unormatted = (p = (SPacketChat)e.getPacket()).getChatComponent().getUnformattedText()).contains("says: ") || unormatted.contains("whispers: "))) {
            String ign = unormatted.split(" ")[0];
            if (AutoReply.mc.player.getName() == ign || !Cascade.friendManager.isFriend(ign) || CalcUtil.getDistance(0.0, AutoReply.mc.player.posY, 0.0) > 5000.0) {
                return;
            }
            String msg = unormatted.toLowerCase();
            if (this.coords.getValue().booleanValue() && (msg.contains("cord") || msg.contains("coord") || msg.contains("coords") || msg.contains("cords") || msg.contains("wya") || msg.contains("where are you") || msg.contains("where r u") || msg.contains("where ru"))) {
                if (msg.contains("discord") || msg.contains("record")) {
                    return;
                }
                int x = (int)AutoReply.mc.player.posX;
                int z = (int)AutoReply.mc.player.posZ;
                AutoReply.mc.player.sendChatMessage("/msg " + ign + " " + x + "x " + z + "z");
            }
        }
    }
}

