//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 */
package cascade.features.modules.movement;

import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.entity.EntityUtil;
import cascade.util.player.MovementUtil;

public class Step
extends Module {
    Setting<Float> height = this.register(new Setting<Float>("Height", Float.valueOf(2.0f), Float.valueOf(1.0f), Float.valueOf(2.0f)));
    Setting<Boolean> noLiquid = this.register(new Setting<Boolean>("NoLiquid", true));

    public Step() {
        super("Step", Module.Category.MOVEMENT, "Allows you to step up blocks");
    }

    @Override
    public void onToggle() {
        if (Step.mc.player != null) {
            Step.mc.player.stepHeight = 0.6f;
        }
    }

    @Override
    public void onUpdate() {
        if (Step.fullNullCheck()) {
            return;
        }
        if (this.noLiquid.getValue().booleanValue() && EntityUtil.isInLiquid()) {
            Step.mc.player.stepHeight = 0.6f;
            return;
        }
        MovementUtil.step(this.height.getValue().floatValue());
    }
}

