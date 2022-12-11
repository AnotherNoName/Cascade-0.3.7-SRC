//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.misc;

import cascade.event.events.PacketEvent;
import cascade.features.command.Command;
import cascade.features.modules.Module;
import cascade.features.modules.movement.Velocity;
import cascade.features.setting.Setting;
import cascade.util.misc.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Debug
extends Module {
    Setting<Boolean> plrCr = this.register(new Setting<Boolean>("PlrCr", false));
    Setting<Boolean> explosion = this.register(new Setting<Boolean>("Explosion", false));
    Setting<Boolean> eVelocity = this.register(new Setting<Boolean>("eVelocity", false));
    static Debug INSTANCE;

    public Debug() {
        super("Debug", Module.Category.MISC, "");
        INSTANCE = this;
    }

    public static Debug getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Debug();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (Debug.fullNullCheck()) {
            return;
        }
        if (this.plrCr.getValue().booleanValue()) {
            Entity crystal = null;
            for (Entity en : Debug.mc.world.loadedEntityList) {
                if (en instanceof EntityEnderCrystal) continue;
            }
            Command.sendMessage("@" + MathUtil.round(Debug.mc.player.getDistanceToEntity(crystal), 2) + ", " + Debug.mc.player.canEntityBeSeen(crystal));
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        if (Debug.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (e.getPacket() instanceof SPacketExplosion && this.explosion.getValue().booleanValue()) {
            Command.sendMessage("@" + MathUtil.round(Velocity.getEX(), 2) + ", " + MathUtil.round(Velocity.getEY(), 2) + ", " + MathUtil.round(Velocity.getEZ(), 2));
        }
        if (e.getPacket() instanceof SPacketEntityVelocity && this.eVelocity.getValue().booleanValue()) {
            Command.sendMessage("@@" + MathUtil.round(Velocity.getVX(), 2) + ", " + MathUtil.round(Velocity.getVY(), 2) + ", " + MathUtil.round(Velocity.getVZ(), 2));
        }
    }
}

