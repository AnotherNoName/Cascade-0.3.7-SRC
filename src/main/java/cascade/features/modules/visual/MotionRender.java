//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 */
package cascade.features.modules.visual;

import cascade.event.events.Render3DEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;

public class MotionRender
extends Module {
    Setting<Integer> ticks = this.register(new Setting<Integer>("Ticks", 3, 0, 20));

    public MotionRender() {
        super("MotionRender", Module.Category.VISUAL, "");
    }

    @Override
    public void onRender3D(Render3DEvent e) {
        if (MotionRender.mc.renderViewEntity != null) {
            double motionX = MotionRender.mc.player.posX - MotionRender.mc.player.lastTickPosX;
            double motionY = MotionRender.mc.player.posY - MotionRender.mc.player.lastTickPosY;
            double motionZ = MotionRender.mc.player.posZ - MotionRender.mc.player.lastTickPosZ;
            double d = Math.sqrt(Math.pow(motionX, 2.0) + Math.pow(motionZ, 2.0) + Math.pow(motionY, 2.0));
        }
    }
}

