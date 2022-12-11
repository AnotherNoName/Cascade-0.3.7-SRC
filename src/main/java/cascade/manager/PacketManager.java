//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package cascade.manager;

import cascade.event.events.PacketEvent;
import cascade.features.Feature;
import cascade.features.modules.core.Manager;
import cascade.util.misc.Timer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PacketManager
extends Feature {
    SPacketExplosion pExplosion = null;
    RayTraceResult raytrace = null;
    Timer timerExplosion = new Timer();
    boolean caughtPExplosion = false;
    SPacketPlayerPosLook pPlayerPosLook = null;
    Timer timerPlayerPosLook = new Timer();
    boolean caughtPlayerPosLook = false;
    SPacketEntityStatus pEntityStatus = null;
    Timer timerEntityStatus = new Timer();
    boolean caughtEntityStatus = false;
    boolean liquidFlag = false;
    boolean groundFlag;
    Timer liquidTimer = new Timer();
    Timer groundTimer;

    public void load() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPacketReceive(PacketEvent.Receive e) {
        if (PacketManager.fullNullCheck() || e.getPacket() == null) {
            return;
        }
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            this.pPlayerPosLook = (SPacketPlayerPosLook)e.getPacket();
            this.timerPlayerPosLook.reset();
            this.caughtPlayerPosLook = true;
        }
        if (e.getPacket() instanceof SPacketExplosion) {
            this.pExplosion = (SPacketExplosion)e.getPacket();
            try {
                this.raytrace = PacketManager.mc.world.rayTraceBlocks(PacketManager.mc.player.getPositionVector(), new Vec3d(this.pExplosion.getX(), this.pExplosion.getY(), this.pExplosion.getZ()), false, false, false);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            this.timerExplosion.reset();
            this.caughtPExplosion = true;
        }
        if (e.getPacket() instanceof SPacketEntityStatus) {
            this.pEntityStatus = (SPacketEntityStatus)e.getPacket();
            this.timerEntityStatus.reset();
            this.caughtEntityStatus = true;
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent e) {
        if (PacketManager.fullNullCheck()) {
            return;
        }
        if (this.timerExplosion.passedMs((long)Manager.getInstance().getExplosion())) {
            this.raytrace = null;
            this.pExplosion = null;
            this.caughtPExplosion = false;
        }
        if (this.timerPlayerPosLook.passedMs((long)Manager.getInstance().getLagBack())) {
            this.pPlayerPosLook = null;
            this.caughtPlayerPosLook = false;
        }
        if (this.timerEntityStatus.passedMs((long)Manager.getInstance().getKnockback())) {
            this.pEntityStatus = null;
            this.caughtEntityStatus = false;
        }
    }

    public SPacketPlayerPosLook getPacketPPS() {
        return this.pPlayerPosLook;
    }

    public Timer getTimerPPS() {
        return this.timerPlayerPosLook;
    }

    public boolean getCaughtPPS() {
        return this.caughtPlayerPosLook;
    }

    public SPacketExplosion getPacketE() {
        return this.pExplosion;
    }

    public RayTraceResult getRaytrace() {
        return this.raytrace;
    }

    public Timer getTimerE() {
        return this.timerExplosion;
    }

    public boolean getCaughtE() {
        return this.caughtPExplosion;
    }

    public boolean raytraceCheck() {
        try {
            return PacketManager.mc.world.rayTraceBlocks(new Vec3d(PacketManager.mc.player.posX, PacketManager.mc.player.posY, PacketManager.mc.player.posZ), new Vec3d(this.pExplosion.getX(), this.pExplosion.getY(), this.pExplosion.getZ()), false, true, false) == null;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean isValid() {
        try {
            if (PacketManager.mc.player != null && this.caughtPExplosion && this.pExplosion.getStrength() == 6.0f && PacketManager.mc.player.getDistance(this.pExplosion.getX(), this.pExplosion.getY(), this.pExplosion.getZ()) <= 8.0 && this.raytraceCheck()) {
                return true;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public SPacketEntityStatus getPacketES() {
        return this.pEntityStatus;
    }

    public Timer getTimerES() {
        return this.timerEntityStatus;
    }

    public boolean getCaughtES() {
        return this.caughtEntityStatus;
    }

    public boolean isValidEntityStatus() {
        try {
            return !PacketManager.fullNullCheck() && this.caughtEntityStatus && this.pEntityStatus.getOpCode() == 2 && this.pEntityStatus.getEntity((World)PacketManager.mc.world).getEntityId() == PacketManager.mc.player.getEntityId();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean getLiquid() {
        return this.liquidFlag;
    }

    public boolean getGround() {
        return this.groundFlag;
    }
}

