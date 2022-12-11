//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.MobEffects
 *  net.minecraft.util.MovementInput
 *  net.minecraft.util.math.BlockPos
 */
package cascade.util.player;

import cascade.event.events.MoveEvent;
import cascade.util.Util;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;

public class MovementUtil
implements Util {
    public static boolean isMoving() {
        return (double)MovementUtil.mc.player.field_191988_bg != 0.0 || (double)MovementUtil.mc.player.moveStrafing != 0.0;
    }

    public static boolean anyMovementKeys() {
        return MovementUtil.mc.player.movementInput.forwardKeyDown || MovementUtil.mc.player.movementInput.backKeyDown || MovementUtil.mc.player.movementInput.leftKeyDown || MovementUtil.mc.player.movementInput.rightKeyDown || MovementUtil.mc.player.movementInput.jump || MovementUtil.mc.player.movementInput.sneak;
    }

    public static void setMoveSpeed(double speed) {
        double forward = MovementUtil.mc.player.movementInput.field_192832_b;
        double strafe = MovementUtil.mc.player.movementInput.moveStrafe;
        float yaw = MovementUtil.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            MovementUtil.mc.player.motionX = 0.0;
            MovementUtil.mc.player.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            MovementUtil.mc.player.motionX = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
            MovementUtil.mc.player.motionZ = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
        }
    }

    public static void strafe(MoveEvent event, double speed) {
        if (MovementUtil.isMoving()) {
            double[] strafe = MovementUtil.strafe(speed);
            event.setX(strafe[0]);
            event.setZ(strafe[1]);
        } else {
            event.setX(0.0);
            event.setZ(0.0);
        }
    }

    public static double[] strafe(double speed) {
        return MovementUtil.strafe((Entity)MovementUtil.mc.player, speed);
    }

    public static double[] strafe(Entity entity, double speed) {
        return MovementUtil.strafe(entity, MovementUtil.mc.player.movementInput, speed);
    }

    public static double[] strafe(Entity entity, MovementInput movementInput, double speed) {
        float moveForward = movementInput.field_192832_b;
        float moveStrafe = movementInput.moveStrafe;
        float rotationYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double)moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double)moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double)moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double)moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    public static MovementInput inverse(Entity entity, double speed) {
        MovementInput input = new MovementInput();
        input.sneak = entity.isSneaking();
        block0: for (float d = -1.0f; d <= 1.0f; d += 1.0f) {
            for (float e = -1.0f; e <= 1.0f; e += 1.0f) {
                MovementInput dummyInput = new MovementInput();
                dummyInput.field_192832_b = d;
                dummyInput.moveStrafe = e;
                dummyInput.sneak = entity.isSneaking();
                double[] moveVec = MovementUtil.strafe(entity, dummyInput, speed);
                if (entity.isSneaking()) {
                    moveVec[0] = moveVec[0] * (double)0.3f;
                    moveVec[1] = moveVec[1] * (double)0.3f;
                }
                double targetMotionX = moveVec[0];
                double targetMotionZ = moveVec[1];
                if (!(targetMotionX < 0.0 ? entity.motionX <= targetMotionX : entity.motionX >= targetMotionX) || !(targetMotionZ < 0.0 ? entity.motionZ <= targetMotionZ : entity.motionZ >= targetMotionZ)) continue;
                input.field_192832_b = d;
                input.moveStrafe = e;
                continue block0;
            }
        }
        return input;
    }

    public static double getDistanceXZ() {
        double xDist = MovementUtil.mc.player.posX - MovementUtil.mc.player.prevPosX;
        double zDist = MovementUtil.mc.player.posZ - MovementUtil.mc.player.prevPosZ;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public static double getDistanceXYZ() {
        double xDist = MovementUtil.mc.player.posX - MovementUtil.mc.player.prevPosX;
        double yDist = MovementUtil.mc.player.posY - MovementUtil.mc.player.prevPosY;
        double zDist = MovementUtil.mc.player.posZ - MovementUtil.mc.player.prevPosZ;
        return Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
    }

    public static double getSpeed() {
        return MovementUtil.getSpeed(false);
    }

    public static double getSpeed(boolean slowness) {
        int amplifier;
        double defaultSpeed = 0.2873;
        if (MovementUtil.mc.player.isPotionActive(MobEffects.SPEED)) {
            amplifier = Objects.requireNonNull(MovementUtil.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            defaultSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (slowness && MovementUtil.mc.player.isPotionActive(MobEffects.SLOWNESS)) {
            amplifier = Objects.requireNonNull(MovementUtil.mc.player.getActivePotionEffect(MobEffects.SLOWNESS)).getAmplifier();
            defaultSpeed /= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return defaultSpeed;
    }

    public static double getJumpSpeed() {
        double defaultSpeed = 0.0;
        if (MovementUtil.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
            int amplifier = MovementUtil.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier();
            defaultSpeed += (double)(amplifier + 1) * 0.1;
        }
        return defaultSpeed;
    }

    public static boolean isInMovementDirection(double x, double y, double z) {
        if (MovementUtil.mc.player.motionX != 0.0 || MovementUtil.mc.player.motionZ != 0.0) {
            BlockPos movingPos = new BlockPos((Entity)MovementUtil.mc.player).add(MovementUtil.mc.player.motionX * 10000.0, 0.0, MovementUtil.mc.player.motionZ * 10000.0);
            BlockPos antiPos = new BlockPos((Entity)MovementUtil.mc.player).add(MovementUtil.mc.player.motionX * -10000.0, 0.0, MovementUtil.mc.player.motionY * -10000.0);
            return movingPos.distanceSq(x, y, z) < antiPos.distanceSq(x, y, z);
        }
        return true;
    }

    public static boolean isWasdPressed() {
        return MovementUtil.mc.gameSettings.keyBindForward.isKeyDown() || MovementUtil.mc.gameSettings.keyBindBack.isKeyDown() || MovementUtil.mc.gameSettings.keyBindLeft.isKeyDown() || MovementUtil.mc.gameSettings.keyBindRight.isKeyDown();
    }

    public static void step(float height) {
        if (MovementUtil.mc.player.isOnLadder()) {
            return;
        }
        MovementUtil.mc.player.stepHeight = height;
    }

    public static void setMotion(double x, double y, double z) {
        if (MovementUtil.mc.player != null) {
            if (MovementUtil.mc.player.isRiding()) {
                MovementUtil.mc.player.ridingEntity.motionX = x;
                MovementUtil.mc.player.ridingEntity.motionY = y;
                MovementUtil.mc.player.ridingEntity.motionZ = x;
            } else {
                MovementUtil.mc.player.motionX = x;
                MovementUtil.mc.player.motionY = y;
                MovementUtil.mc.player.motionZ = z;
            }
        }
    }

    public static double calcEffects(double speed) {
        int amplifier;
        double i = speed;
        if (MovementUtil.mc.player.isPotionActive(MobEffects.SPEED)) {
            amplifier = MovementUtil.mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            i *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (MovementUtil.mc.player.isPotionActive(MobEffects.SLOWNESS)) {
            amplifier = MovementUtil.mc.player.getActivePotionEffect(MobEffects.SLOWNESS).getAmplifier();
            i /= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return i;
    }
}

