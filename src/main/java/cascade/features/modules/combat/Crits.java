//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.combat;

import cascade.event.events.PacketEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.entity.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Crits
extends Module {
    Setting<Integer> packets = this.register(new Setting<Integer>("Packets", 1, 1, 5));

    public Crits() {
        super("Crits", Module.Category.COMBAT, "Scores criticals for you");
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        if (Crits.fullNullCheck() || this.isDisabled() || Crits.mc.player.isSpectator()) {
            return;
        }
        if (!(e.getPacket() instanceof CPacketUseEntity)) {
            return;
        }
        CPacketUseEntity p = (CPacketUseEntity)e.getPacket();
        if (!e.isCanceled() && p.getEntityFromWorld((World)Crits.mc.world) instanceof EntityLivingBase && p.getAction() == CPacketUseEntity.Action.ATTACK) {
            if (!Crits.mc.player.onGround || EntityUtil.isInLiquid()) {
                return;
            }
            switch (this.packets.getValue()) {
                case 1: {
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY + (double)0.1f, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY, Crits.mc.player.posZ, false));
                    break;
                }
                case 2: {
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY + 0.0625101, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY + 1.1E-5, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY, Crits.mc.player.posZ, false));
                    break;
                }
                case 3: {
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY + 0.0625101, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY + 0.0125, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY, Crits.mc.player.posZ, false));
                    break;
                }
                case 4: {
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY + 0.05, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY + 0.03, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY, Crits.mc.player.posZ, false));
                    break;
                }
                case 5: {
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY + 0.1625, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY + 4.0E-6, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY + 1.0E-6, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Crits.mc.player.posX, Crits.mc.player.posY, Crits.mc.player.posZ, false));
                    Crits.mc.player.connection.sendPacket((Packet)new CPacketPlayer());
                    Crits.mc.player.onCriticalHit(p.getEntityFromWorld((World)Crits.mc.world));
                }
            }
        }
    }
}

