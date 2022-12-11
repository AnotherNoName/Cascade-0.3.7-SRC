//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 */
package cascade.features.modules.visual;

import cascade.event.events.Render3DEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.render.ColorUtil;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class BlockHighlight
extends Module {
    Setting<RenderMode> renderMode = this.register(new Setting<RenderMode>("RenderMode", RenderMode.Mono));
    Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.Outline));
    Setting<Float> width = this.register(new Setting<Float>("Width", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    Setting<Color> c = this.register(new Setting<Object>("Color", new Color(-1), v -> this.renderMode.getValue() != RenderMode.Rainbow));
    Setting<Color> cTwo = this.register(new Setting<Object>("Color2", new Color(-1), v -> this.renderMode.getValue() == RenderMode.Pulse));
    Setting<Boolean> rainbow = this.register(new Setting<Object>("Rainbow", Boolean.valueOf(false), v -> this.renderMode.getValue() == RenderMode.Rainbow));
    Setting<Integer> hue = this.register(new Setting<Object>("Hue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.renderMode.getValue() == RenderMode.Rainbow && this.rainbow.getValue() != false));
    Setting<Float> saturation = this.register(new Setting<Object>("Saturation", Float.valueOf(255.0f), Float.valueOf(0.0f), Float.valueOf(255.0f), v -> this.renderMode.getValue() == RenderMode.Rainbow && this.rainbow.getValue() != false));
    Setting<Float> brightness = this.register(new Setting<Object>("Brightness", Float.valueOf(255.0f), Float.valueOf(0.0f), Float.valueOf(255.0f), v -> this.renderMode.getValue() == RenderMode.Rainbow && this.rainbow.getValue() != false));
    static ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    public BlockHighlight() {
        super("BlockHighlight", Module.Category.VISUAL, "");
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockPos = ray.getBlockPos();
            Color color = new Color(-1);
            switch (this.renderMode.getValue()) {
                case Mono: {
                    color = new Color(this.c.getValue().getRed(), this.c.getValue().getGreen(), this.c.getValue().getBlue(), this.c.getValue().getAlpha());
                    break;
                }
                case Rainbow: {
                    color = ColorUtil.rainbow(this.hue.getValue(), this.saturation.getValue().floatValue(), this.brightness.getValue().floatValue());
                    break;
                }
            }
            RenderUtil.drawBoxESP(blockPos, color, false, color, this.width.getValue().floatValue(), this.mode.getValue() != Mode.Fill, this.mode.getValue() != Mode.Outline, this.c.getValue().getAlpha(), false);
        }
    }

    static enum Mode {
        Fill,
        Outline,
        Both;

    }

    static enum RenderMode {
        Mono,
        Rainbow,
        Pulse;

    }
}

