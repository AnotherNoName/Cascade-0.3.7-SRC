//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 */
package cascade.features.modules.movement;

import cascade.Cascade;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.player.PlayerUtil;

public class FastFall
extends Module {
    Setting<Double> speed = this.register(new Setting<Double>("Speed", 10.0, 0.1, 10.0));
    Setting<Double> height = this.register(new Setting<Double>("Height", 5.0, 1.0, 10.0));

    public FastFall() {
        super("FastFall", Module.Category.MOVEMENT, "");
    }

    @Override
    public void onUpdate() {
        if (FastFall.fullNullCheck() || this.check()) {
            return;
        }
        FastFall.mc.player.motionY = -this.speed.getValue().doubleValue();
    }

    boolean check() {
        return FastFall.mc.gameSettings.keyBindJump.isKeyDown() || FastFall.mc.gameSettings.keyBindSneak.isKeyDown() || Cascade.packetManager.getCaughtPPS() || FastFall.mc.player.noClip || !FastFall.mc.player.onGround || PlayerUtil.isInLiquid() || FastFall.mc.player.capabilities.isFlying || FastFall.mc.player.isElytraFlying() || FastFall.mc.player.isOnLadder();
    }
}

