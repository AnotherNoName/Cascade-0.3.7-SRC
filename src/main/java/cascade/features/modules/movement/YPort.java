//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 */
package cascade.features.modules.movement;

import cascade.Cascade;
import cascade.features.modules.Module;
import cascade.features.modules.movement.Strafe;
import cascade.features.setting.Setting;
import cascade.util.misc.Timer;
import cascade.util.player.MovementUtil;
import cascade.util.player.PlayerUtil;

public class YPort
extends Module {
    Setting<Boolean> step = this.register(new Setting<Boolean>("Step", true));
    Setting<Integer> delayUp = this.register(new Setting<Integer>("DelayUp", 1, 1, 500));
    Setting<Integer> delayDown = this.register(new Setting<Integer>("DelayDown", 1, 1, 500));
    Setting<Double> height = this.register(new Setting<Double>("MaxHeight", 3.0, 1.0, 5.0));
    Timer downTimer = new Timer();
    Timer upTimer = new Timer();
    static YPort INSTANCE;
    boolean stepFlag;

    public YPort() {
        super("YPort", Module.Category.MOVEMENT, "");
        INSTANCE = this;
    }

    public static YPort getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new YPort();
        }
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        if (YPort.fullNullCheck()) {
            return;
        }
        if (this.stepFlag) {
            YPort.mc.player.stepHeight = 0.6f;
        }
        this.stepFlag = false;
    }

    @Override
    public void onUpdate() {
        if (YPort.fullNullCheck()) {
            return;
        }
        if (MovementUtil.isMoving() && !PlayerUtil.inLiquid() && Strafe.getInstance().isDisabled()) {
            if (Cascade.packetManager.getCaughtPPS()) {
                return;
            }
            if (this.step.getValue().booleanValue()) {
                MovementUtil.step(2.0f);
                this.stepFlag = true;
            }
            if (YPort.mc.player.onGround) {
                if (this.upTimer.passedMs(this.delayUp.getValue().intValue())) {
                    if (!YPort.mc.player.isCollidedHorizontally) {
                        YPort.mc.player.jump();
                        this.upTimer.reset();
                    }
                    MovementUtil.setMoveSpeed(MovementUtil.getSpeed());
                }
            } else if ((double)YPort.mc.player.fallDistance <= this.height.getValue() && this.downTimer.passedMs(this.delayDown.getValue().intValue())) {
                YPort.mc.player.motionY -= 3.0;
                this.downTimer.reset();
            }
        }
    }
}

