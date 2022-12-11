//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.projectile.EntityFishHook
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.movement;

import cascade.event.events.PacketEvent;
import cascade.event.events.PushEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.mixin.mixins.accessor.ISPacketEntityVelocity;
import cascade.mixin.mixins.accessor.ISPacketExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity
extends Module {
    public Setting<Boolean> knockBack = this.register(new Setting<Boolean>("KnockBack", true));
    public Setting<Boolean> noPush = this.register(new Setting<Boolean>("NoPush", true));
    public Setting<Boolean> bobbers = this.register(new Setting<Boolean>("Bobbers", true));
    public Setting<Boolean> water = this.register(new Setting<Boolean>("Water", false));
    public Setting<Boolean> blocks = this.register(new Setting<Boolean>("Blocks", false));
    static Velocity INSTANCE;
    static double eX;
    static double eY;
    static double eZ;
    static double vX;
    static double vY;
    static double vZ;

    public Velocity() {
        super("Velocity", Module.Category.MOVEMENT, "Player Tweaks");
        INSTANCE = this;
    }

    public static Velocity getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Velocity();
        }
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        if (this.isDisabled()) {
            return;
        }
        if (e.getStage() == 0 && Velocity.mc.player != null) {
            Entity entity;
            SPacketEntityStatus packet;
            if (this.knockBack.getValue().booleanValue()) {
                SPacketEntityVelocity p;
                if (e.getPacket() instanceof SPacketEntityVelocity && (p = (SPacketEntityVelocity)e.getPacket()).getEntityID() == Velocity.mc.player.entityId) {
                    vX = ((ISPacketEntityVelocity)p).getX();
                    vY = ((ISPacketEntityVelocity)p).getY();
                    vZ = ((ISPacketEntityVelocity)p).getZ();
                    e.setCanceled(true);
                }
                if (e.getPacket() instanceof SPacketExplosion) {
                    p = (SPacketExplosion)e.getPacket();
                    eX = ((ISPacketExplosion)p).getX();
                    eY = ((ISPacketExplosion)p).getY();
                    eZ = ((ISPacketExplosion)p).getZ();
                    e.setCanceled(true);
                }
            }
            if (e.getPacket() instanceof SPacketEntityStatus && this.bobbers.getValue().booleanValue() && (packet = (SPacketEntityStatus)e.getPacket()).getOpCode() == 31 && (entity = packet.getEntity((World)Velocity.mc.world)) instanceof EntityFishHook) {
                EntityFishHook fishHook = (EntityFishHook)entity;
                if (fishHook.caughtEntity == Velocity.mc.player) {
                    e.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent e) {
        if (this.isDisabled()) {
            return;
        }
        if (e.getStage() == 0 && this.noPush.getValue().booleanValue() && e.entity == Velocity.mc.player) {
            e.setCanceled(true);
        } else if (e.getStage() == 1 && this.blocks.getValue().booleanValue()) {
            e.setCanceled(true);
        } else if (e.getStage() == 2 && this.water.getValue().booleanValue() && Velocity.mc.player != null && Velocity.mc.player == e.entity) {
            e.setCanceled(true);
        }
    }

    public static double getEX() {
        return eX;
    }

    public static double getEY() {
        return eY;
    }

    public static double getEZ() {
        return eZ;
    }

    public static double getVX() {
        return vX;
    }

    public static double getVY() {
        return vY;
    }

    public static double getVZ() {
        return vZ;
    }

    static {
        eX = 0.0;
        eY = 0.0;
        eZ = 0.0;
        vX = 0.0;
        vY = 0.0;
        vZ = 0.0;
    }
}

