//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.misc;

import cascade.event.events.PacketEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.player.MovementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSuffocation
extends Module {
    Setting<Integer> resyncType = this.register(new Setting<Integer>("ResyncType(dev)", 1, 1, 3));

    public NoSuffocation() {
        super("NoSuffocation", Module.Category.MISC, "");
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        if (NoSuffocation.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (e.getPacket() instanceof CPacketPlayer && this.checkCollisionBox()) {
            e.setCanceled(true);
            MovementUtil.setMotion(0.0, 0.0, 0.0);
            NoSuffocation.mc.player.setVelocity(0.0, 0.0, 0.0);
        }
    }

    boolean checkCollisionBox() {
        if (!NoSuffocation.mc.world.getCollisionBoxes((Entity)NoSuffocation.mc.player, NoSuffocation.mc.player.getEntityBoundingBox().addCoord(0.0, 0.0, 0.0)).isEmpty()) {
            return true;
        }
        return !NoSuffocation.mc.world.getCollisionBoxes((Entity)NoSuffocation.mc.player, NoSuffocation.mc.player.getEntityBoundingBox().offset(0.0, 2.0, 0.0).func_191195_a(0.0, 1.99, 0.0)).isEmpty();
    }
}

