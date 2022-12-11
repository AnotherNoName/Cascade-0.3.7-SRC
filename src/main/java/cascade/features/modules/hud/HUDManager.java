//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 */
package cascade.features.modules.hud;

import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.core.TextUtil;
import cascade.util.render.ColorUtil;
import java.awt.Color;
import net.minecraft.client.gui.GuiChat;

public class HUDManager
extends Module {
    public Setting<Boolean> renderingUp = this.register(new Setting<Boolean>("RenderingUp", false));
    public Setting<TextUtil.Color> infoColor = this.register(new Setting<TextUtil.Color>("InfoColor", TextUtil.Color.DARK_GRAY));
    public Setting<Color> c = this.register(new Setting<Color>("Color", new Color(120, 0, 255, 255)));
    public int i;
    static HUDManager INSTANCE;

    public HUDManager() {
        super("HUDManager", Module.Category.HUD, "");
        this.i = HUDManager.mc.currentScreen instanceof GuiChat && this.renderingUp.getValue() != false ? 13 : (this.renderingUp.getValue() != false ? -2 : 0);
        INSTANCE = this;
    }

    public static HUDManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HUDManager();
        }
        return INSTANCE;
    }

    public int getColor() {
        return ColorUtil.toRGBA(new Color(this.c.getValue().getRed(), this.c.getValue().getGreen(), this.c.getValue().getBlue(), this.c.getValue().getAlpha()));
    }

    public int getRed() {
        return this.c.getValue().getRed();
    }

    public int getGreen() {
        return this.c.getValue().getGreen();
    }

    public int getBlue() {
        return this.c.getValue().getBlue();
    }

    public int getAlpha() {
        return this.c.getValue().getAlpha();
    }
}

