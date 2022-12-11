//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.movement;

import cascade.Cascade;
import cascade.event.events.MoveEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.entity.EntityUtil;
import cascade.util.player.MovementUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AirStrafe
extends Module {
    Setting<Boolean> noLiquid = this.register(new Setting<Boolean>("NoLiquid", true));
    Setting<Boolean> step = this.register(new Setting<Boolean>("Step", true));
    Setting<Float> height = this.register(new Setting<Object>("Height", Float.valueOf(2.0f), Float.valueOf(1.0f), Float.valueOf(2.0f), v -> this.step.getValue()));
    boolean stepFlag;

    public AirStrafe() {
        super("AirStrafe", Module.Category.MOVEMENT, "");
    }

    @Override
    public void onToggle() {
        if (AirStrafe.fullNullCheck()) {
            return;
        }
        if (this.stepFlag) {
            AirStrafe.mc.player.stepHeight = 0.6f;
        }
        this.stepFlag = false;
    }

    @SubscribeEvent
    public void onMove(MoveEvent e) {
        if (this.isDisabled() || AirStrafe.mc.player.isElytraFlying()) {
            return;
        }
        if (this.noLiquid.getValue().booleanValue() && EntityUtil.isInLiquid() || Cascade.packetManager.getCaughtPPS() || AirStrafe.mc.player.capabilities.isFlying) {
            return;
        }
        if (Cascade.moduleManager.isModuleEnabled("HoleSnap") || Cascade.moduleManager.isModuleEnabled("Freecam") || Cascade.moduleManager.isModuleEnabled("YPort") || Cascade.moduleManager.isModuleEnabled("Strafe")) {
            return;
        }
        if (this.step.getValue().booleanValue()) {
            MovementUtil.step(this.height.getValue().floatValue());
            this.stepFlag = true;
        }
        MovementUtil.strafe(e, MovementUtil.getSpeed());
    }
}

