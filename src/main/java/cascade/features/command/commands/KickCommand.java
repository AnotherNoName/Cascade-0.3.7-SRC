//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 */
package cascade.features.command.commands;

import cascade.features.command.Command;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class KickCommand
extends Command {
    public KickCommand() {
        super("kick", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        if (KickCommand.fullNullCheck()) {
            return;
        }
        for (int i = 0; i < 20; ++i) {
            mc.getConnection().sendPacket((Packet)new CPacketPlayer.PositionRotation(Double.NaN, Double.NEGATIVE_INFINITY, Double.NaN, KickCommand.mc.player.eyeHeight, KickCommand.mc.player.renderArmPitch, false));
        }
    }
}

