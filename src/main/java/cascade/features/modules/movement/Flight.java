//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.movement;

import cascade.event.events.PacketEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.Timer;
import cascade.util.player.MovementUtil;
import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Flight
extends Module {
    Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.Const));
    Setting<ForceGround> forceGround = this.register(new Setting<ForceGround>("ForceGround", ForceGround.None));
    Setting<Double> hSpeed = this.register(new Setting<Object>("HSpeed", Double.valueOf(1.0), Double.valueOf(0.2), Double.valueOf(10.0), v -> this.mode.getValue() == Mode.Normal));
    Setting<Float> vSpeed = this.register(new Setting<Object>("VSpeed", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(1.0f), v -> this.mode.getValue() == Mode.Normal));
    Setting<Integer> packets = this.register(new Setting<Object>("Packets", Integer.valueOf(15), Integer.valueOf(2), Integer.valueOf(32), v -> this.mode.getValue() == Mode.Normal));
    Setting<Color> c = this.register(new Setting<Object>("Color", new Color(120, 0, 255, 205), v -> this.mode.getValue() == Mode.Normal));
    Timer timer = new Timer();
    int flightTicks;
    double x;
    double y;
    double z;

    public Flight() {
        super("Flight", Module.Category.MOVEMENT, "M");
    }

    @Override
    public void onToggle() {
        Flight.mc.player.capabilities.isFlying = false;
        Flight.mc.player.noClip = false;
        this.flightTicks = 0;
    }

    @Override
    public void onUpdate() {
        block8: {
            block7: {
                if (Flight.fullNullCheck()) {
                    return;
                }
                if (this.mode.getValue() != Mode.Const) break block7;
                if (!Flight.mc.world.getCollisionBoxes((Entity)Flight.mc.player, Flight.mc.player.getEntityBoundingBox().offset(0.0, -0.1, 0.0)).isEmpty()) break block8;
                Flight.mc.player.motionY = 0.0;
                if (!this.forceGround.getValue().equals((Object)ForceGround.None)) {
                    boolean bl = Flight.mc.player.onGround = this.forceGround.getValue().equals((Object)ForceGround.True);
                }
                if (this.flightTicks > 40) {
                    Flight.mc.player.posY -= 0.032;
                    this.flightTicks = 0;
                } else {
                    ++this.flightTicks;
                }
                if (Flight.mc.player.ticksExisted % 3 == 0) break block8;
                Flight.mc.player.setPosition(Flight.mc.player.posX, Flight.mc.player.posY += 1.0E-9, Flight.mc.player.posZ);
                break block8;
            }
            Flight.mc.player.capabilities.isFlying = true;
            Flight.mc.player.noClip = true;
            if (this.timer.passedMs(2550L)) {
                this.timer.reset();
                return;
            }
            if (this.timer.passedMs(2500L)) {
                return;
            }
            double[] dir = MovementUtil.strafe(this.hSpeed.getValue());
            Flight.mc.player.capabilities.setFlySpeed(this.vSpeed.getValue().floatValue() / 10.0f);
            MovementUtil.setMotion(dir[1], Flight.mc.player.motionY, dir[0]);
            for (int i = 0; i < this.packets.getValue(); ++i) {
                mc.getConnection().sendPacket((Packet)new CPacketPlayer.PositionRotation(Flight.mc.player.posX, Flight.mc.player.posY, Flight.mc.player.posZ, Flight.mc.player.rotationYaw, Flight.mc.player.rotationPitch, false));
                mc.getConnection().sendPacket((Packet)new CPacketPlayer.PositionRotation(Flight.mc.player.prevPosX, Flight.mc.player.prevPosY + 0.05, Flight.mc.player.prevPosZ, Flight.mc.player.rotationYaw, Flight.mc.player.rotationPitch, true));
                mc.getConnection().sendPacket((Packet)new CPacketPlayer.PositionRotation(Flight.mc.player.posX, Flight.mc.player.posY, Flight.mc.player.posZ, Flight.mc.player.rotationYaw, Flight.mc.player.rotationPitch, false));
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        if (Flight.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (e.getPacket() instanceof SPacketPlayerPosLook && this.mode.getValue() == Mode.Normal) {
            if (this.timer.passedMs(2500L)) {
                return;
            }
            this.x = ((SPacketPlayerPosLook)e.getPacket()).x;
            this.y = ((SPacketPlayerPosLook)e.getPacket()).z;
            this.z = ((SPacketPlayerPosLook)e.getPacket()).y;
            ((SPacketPlayerPosLook)e.getPacket()).y = Flight.mc.player.posY;
            ((SPacketPlayerPosLook)e.getPacket()).x = Flight.mc.player.posX;
            ((SPacketPlayerPosLook)e.getPacket()).z = Flight.mc.player.posZ;
            ((SPacketPlayerPosLook)e.getPacket()).yaw = Flight.mc.player.rotationYaw;
            ((SPacketPlayerPosLook)e.getPacket()).pitch = Flight.mc.player.rotationPitch;
            mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(Flight.mc.player.posX, Flight.mc.player.posY, Flight.mc.player.posZ, false));
            mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(Flight.mc.player.prevPosX, Flight.mc.player.prevPosY + 0.05, Flight.mc.player.prevPosZ, true));
            mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(Flight.mc.player.posX, Flight.mc.player.posY, Flight.mc.player.posZ, false));
        }
    }

    static enum ForceGround {
        None,
        True,
        False;

    }

    static enum Mode {
        Const,
        Normal;

    }
}

