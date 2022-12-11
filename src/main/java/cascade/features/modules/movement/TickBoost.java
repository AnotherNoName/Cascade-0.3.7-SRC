//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.Vec3d
 */
package cascade.features.modules.movement;

import cascade.Cascade;
import cascade.features.modules.Module;
import cascade.features.modules.player.Freecam;
import cascade.features.setting.Setting;
import cascade.util.entity.EntityUtil;
import cascade.util.misc.MathUtil;
import cascade.util.misc.Timer;
import cascade.util.player.MovementUtil;
import cascade.util.player.PhysicsUtil;
import cascade.util.player.PlayerUtil;
import cascade.util.player.RotationUtil;
import cascade.util.player.TargetUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class TickBoost
extends Module {
    Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.Always));
    Setting<Double> minHealth = this.register(new Setting<Object>("MinHealth", Double.valueOf(19.0), Double.valueOf(0.1), Double.valueOf(20.0), v -> this.mode.getValue() == Mode.Health));
    Setting<Double> range = this.register(new Setting<Object>("Range", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(18.0f), v -> this.mode.getValue() == Mode.Test));
    Setting<Integer> factor = this.register(new Setting<Integer>("factor", 4, 1, 16));
    Setting<Boolean> cooldown = this.register(new Setting<Boolean>("Cooldown", true));
    Timer doubleTapTimer = new Timer();
    Timer cooldownTimer = new Timer();
    int runs = 0;

    public TickBoost() {
        super("TickBoost", Module.Category.MOVEMENT, "");
    }

    @Override
    public String getDisplayInfo() {
        if (this.cooldownTimer != null) {
            return this.cooldownTimer.passedMs(4500L) ? ChatFormatting.GREEN + "" + this.cooldown.getValue() : ChatFormatting.RED + "" + MathUtil.round((double)this.cooldownTimer.getPassedTimeMs() / 1000.0, 1);
        }
        return null;
    }

    @Override
    public void onUpdate() {
        if (TickBoost.fullNullCheck() || this.shouldReturn()) {
            return;
        }
        if (Cascade.timerManager.isFlagged() || Cascade.packetManager.getCaughtPPS()) {
            this.cooldownTimer.reset();
        }
        if (this.mode.getValue() == Mode.Health && (double)EntityUtil.getHealth((Entity)TickBoost.mc.player) > this.minHealth.getValue()) {
            return;
        }
        if (this.cooldownTimer.passedMs(4500L) && MovementUtil.isMoving()) {
            if (this.mode.getValue() == Mode.Test) {
                Vec3d playerPos = TickBoost.mc.player.getPositionVector();
                Vec3d targetPos = new Vec3d(TargetUtil.getTarget((double)this.range.getValue().doubleValue()).posX, TickBoost.mc.player.posY, TargetUtil.getTarget((double)this.range.getValue().doubleValue()).posZ);
                double yawRad = Math.toRadians(RotationUtil.getRotationTo((Vec3d)playerPos, (Vec3d)targetPos).x);
                double dist = playerPos.distanceTo(targetPos);
                double speed = TickBoost.mc.player.onGround ? -Math.min(0.2805, dist / 2.0) : -MovementUtil.getSpeed() + 0.02;
                TickBoost.mc.player.motionX = -Math.sin(yawRad) * speed;
                TickBoost.mc.player.motionZ = Math.cos(yawRad) * speed;
            }
            MovementUtil.strafe(MovementUtil.getSpeed());
            if (this.runs < this.factor.getValue()) {
                ++this.runs;
                PhysicsUtil.runPhysicsTick();
            } else {
                this.runs = 0;
            }
            this.cooldownTimer.reset();
        }
    }

    @Override
    public void onToggle() {
        this.doubleTapTimer.reset();
        this.cooldownTimer.reset();
        this.runs = 0;
    }

    boolean shouldReturn() {
        return PlayerUtil.isInLiquid() || EntityUtil.isInHole((Entity)TickBoost.mc.player) || PlayerUtil.isClipping() || Cascade.serverManager.isServerNotResponding(1050) || Freecam.getInstance().isEnabled() || Cascade.moduleManager.isModuleEnabled("PacketFly");
    }

    static enum Mode {
        Health,
        Always,
        Test;

    }
}

