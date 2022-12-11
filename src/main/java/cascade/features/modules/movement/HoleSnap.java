//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.movement;

import cascade.Cascade;
import cascade.event.events.PacketEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.mixin.mixins.accessor.ITimer;
import cascade.util.entity.EntityUtil;
import cascade.util.misc.Timer;
import cascade.util.player.HoleUtil;
import cascade.util.player.MovementUtil;
import cascade.util.player.RotationUtil;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HoleSnap
extends Module {
    public Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(4.5f), Float.valueOf(0.1f), Float.valueOf(12.0f)));
    Setting<Float> factor = this.register(new Setting<Float>("Factor", Float.valueOf(2.5f), Float.valueOf(1.0f), Float.valueOf(15.0f)));
    Setting<Boolean> step = this.register(new Setting<Boolean>("Step", true));
    Timer timer = new Timer();
    HoleUtil.Hole holes;

    public HoleSnap() {
        super("HoleSnap", Module.Category.MOVEMENT, "drags u to the nearest hole");
    }

    @Override
    public void onEnable() {
        if (HoleSnap.fullNullCheck()) {
            return;
        }
        this.timer.reset();
        this.holes = null;
    }

    @Override
    public void onDisable() {
        if (HoleSnap.fullNullCheck()) {
            return;
        }
        this.timer.reset();
        this.holes = null;
        if (this.step.getValue().booleanValue()) {
            HoleSnap.mc.player.stepHeight = 0.6f;
        }
        if (((ITimer)HoleSnap.mc.timer).getTickLength() != 50.0f) {
            Cascade.timerManager.reset();
        }
    }

    @Override
    public void onUpdate() {
        if (HoleSnap.fullNullCheck()) {
            return;
        }
        if (EntityUtil.isInLiquid()) {
            this.disable();
            return;
        }
        this.holes = RotationUtil.getTargetHoleVec3D(this.range.getValue().floatValue());
        if (this.holes == null || HoleUtil.isObbyHole(RotationUtil.getPlayerPos()) || HoleUtil.isBedrockHoles(RotationUtil.getPlayerPos())) {
            this.disable();
            return;
        }
        if (this.timer.passedMs(500L) && MovementUtil.anyMovementKeys()) {
            this.disable();
            return;
        }
        if (HoleSnap.mc.world.getBlockState(this.holes.pos1).getBlock() == Blocks.AIR) {
            if (this.step.getValue().booleanValue()) {
                MovementUtil.step(2.0f);
            }
        } else {
            this.disable();
            return;
        }
        Vec3d playerPos = HoleSnap.mc.player.getPositionVector();
        Vec3d targetPos = new Vec3d((double)this.holes.pos1.getX() + 0.5, HoleSnap.mc.player.posY, (double)this.holes.pos1.getZ() + 0.5);
        double yawRad = Math.toRadians(RotationUtil.getRotationTo((Vec3d)playerPos, (Vec3d)targetPos).x);
        double dist = playerPos.distanceTo(targetPos);
        double speed = HoleSnap.mc.player.onGround ? -Math.min(0.2805, dist / 2.0) : -MovementUtil.getSpeed() + 0.02;
        Cascade.timerManager.set(this.factor.getValue().floatValue());
        HoleSnap.mc.player.motionX = -Math.sin(yawRad) * speed;
        HoleSnap.mc.player.motionZ = Math.cos(yawRad) * speed;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        if (e.getPacket() instanceof SPacketPlayerPosLook && this.isEnabled()) {
            this.disable();
            return;
        }
    }
}

