//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.movement;

import cascade.Cascade;
import cascade.event.events.MoveEvent;
import cascade.event.events.PacketEvent;
import cascade.event.events.UpdateWalkingPlayerEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.entity.EntityUtil;
import cascade.util.misc.MathUtil;
import cascade.util.player.HoleUtil;
import cascade.util.player.MovementUtil;
import cascade.util.player.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LongJump
extends Module {
    Setting<Boolean> noLiquid = this.register(new Setting<Boolean>("NoLiquid", true));
    Setting<Boolean> step = this.register(new Setting<Boolean>("Step", true));
    Setting<Float> height = this.register(new Setting<Object>("Height", Float.valueOf(2.0f), Float.valueOf(1.0f), Float.valueOf(2.0f), v -> this.step.getValue()));
    Setting<Boolean> always = this.register(new Setting<Boolean>("Always", false));
    Setting<Float> factor = this.register(new Setting<Float>("Factor", Float.valueOf(5.0f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
    Setting<Jump> jump = this.register(new Setting<Jump>("Jump", Jump.Full));
    Setting<Boolean> lagDisable = this.register(new Setting<Boolean>("LagDisable", true));
    boolean stepFlag;
    int groundTicks;
    double distance;
    double speed;
    int airTicks;
    int stage;

    public LongJump() {
        super("LongJump", Module.Category.MOVEMENT, "Long jump dude");
    }

    @Override
    public void onEnable() {
        if (LongJump.mc.player != null) {
            this.distance = MovementUtil.getDistanceXZ();
            this.speed = MovementUtil.getSpeed();
        }
        this.groundTicks = 0;
        this.airTicks = 0;
        this.stage = 0;
    }

    @Override
    public void onDisable() {
        if (LongJump.fullNullCheck()) {
            return;
        }
        if (this.stepFlag) {
            LongJump.mc.player.stepHeight = 0.6f;
        }
        this.stepFlag = false;
    }

    @SubscribeEvent
    public void onMove(MoveEvent e) {
        if (this.isDisabled() || e.isCanceled()) {
            return;
        }
        if (this.noLiquid.getValue().booleanValue() && EntityUtil.isInLiquid()) {
            return;
        }
        if (Cascade.packetManager.getCaughtPPS()) {
            return;
        }
        if (this.always.getValue().booleanValue() && !Cascade.packetManager.isValid()) {
            return;
        }
        if (this.step.getValue().booleanValue()) {
            MovementUtil.step(this.height.getValue().floatValue());
            this.stepFlag = true;
        }
        if (HoleUtil.isInHole(LongJump.mc.player.getPosition()) || PlayerUtil.isBurrowed()) {
            return;
        }
        if (LongJump.mc.player.moveStrafing <= 0.0f && LongJump.mc.player.field_191988_bg <= 0.0f) {
            this.stage = 1;
        }
        if (MathUtil.round(LongJump.mc.player.posY - (double)((int)LongJump.mc.player.posY), 3) == MathUtil.round(0.943, 3)) {
            LongJump.mc.player.motionY -= 0.03;
            e.setY(e.getY() - 0.03);
        }
        if (this.stage == 1 && MovementUtil.isMoving()) {
            this.stage = 2;
            this.speed = (double)this.factor.getValue().floatValue() * MovementUtil.getSpeed() - 0.01;
        } else if (this.stage == 2) {
            this.stage = 3;
            if (!EntityUtil.isInLiquid() && !LongJump.mc.player.isInWeb && LongJump.mc.player.onGround) {
                if (this.jump.getValue() == Jump.Full) {
                    LongJump.mc.player.motionY = 0.424 + MovementUtil.getJumpSpeed();
                }
                if (this.jump.getValue() == Jump.Full || this.jump.getValue() == Jump.Low) {
                    e.setY(0.424 + MovementUtil.getJumpSpeed());
                }
                if (this.jump.getValue() == Jump.Bypass) {
                    LongJump.mc.player.onGround = false;
                }
            }
            this.speed *= 2.149802;
        } else if (this.stage == 3) {
            this.stage = 4;
            double difference = 0.66 * (this.distance - MovementUtil.getSpeed());
            this.speed = this.distance - difference;
        } else {
            if (LongJump.mc.world.getCollisionBoxes((Entity)LongJump.mc.player, LongJump.mc.player.getEntityBoundingBox().offset(0.0, LongJump.mc.player.motionY, 0.0)).size() > 0 || LongJump.mc.player.isCollidedVertically) {
                this.stage = 1;
            }
            this.speed = this.distance - this.distance / 159.0;
        }
        this.speed = Math.max(this.speed, MovementUtil.getSpeed());
        MovementUtil.strafe(e, this.speed);
        float moveForward = LongJump.mc.player.movementInput.field_192832_b;
        float moveStrafe = LongJump.mc.player.movementInput.moveStrafe;
        float rotationYaw = LongJump.mc.player.rotationYaw;
        if (moveForward == 0.0f && moveStrafe == 0.0f) {
            e.setX(0.0);
            e.setZ(0.0);
        } else if (moveForward != 0.0f) {
            if (moveStrafe >= 1.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? -45 : 45);
                moveStrafe = 0.0f;
            } else if (moveStrafe <= -1.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? 45 : -45);
                moveStrafe = 0.0f;
            }
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double cos = Math.cos(Math.toRadians(rotationYaw + 90.0f));
        double sin = Math.sin(Math.toRadians(rotationYaw + 90.0f));
        e.setX((double)moveForward * this.speed * cos + (double)moveStrafe * this.speed * sin);
        e.setZ((double)moveForward * this.speed * sin - (double)moveStrafe * this.speed * cos);
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent e) {
        if (this.isDisabled()) {
            return;
        }
        if (e.getStage() == 0) {
            this.distance = MovementUtil.getDistanceXZ();
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        if (this.isDisabled()) {
            return;
        }
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            this.groundTicks = 0;
            this.speed = 0.0;
            this.airTicks = 0;
            this.stage = 0;
            if (this.lagDisable.getValue().booleanValue()) {
                this.disable();
                return;
            }
        }
    }

    static enum Jump {
        Full,
        Low,
        Bypass,
        None;

    }
}

