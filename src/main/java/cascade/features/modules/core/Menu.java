//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 */
package cascade.features.modules.core;

import cascade.features.menu.Panel;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.render.ColorUtil;
import java.awt.Color;
import net.minecraft.client.gui.GuiScreen;

public class Menu
extends Module {
    Setting<Color> color = this.register(new Setting<Color>("Color", new Color(141, 238, 35, 255)));
    public Setting<Integer> opacity = this.register(new Setting<Integer>("Opacity", 255, 0, 255));
    Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", false));
    Setting<Integer> hue = this.register(new Setting<Integer>("Hue", 193, 1, 255));
    Setting<Integer> saturation = this.register(new Setting<Integer>("Saturation", 185, 1, 255));
    Setting<Integer> brightness = this.register(new Setting<Integer>("Brightness", 255, 1, 255));
    static Menu instance = new Menu();

    public Menu() {
        super("Menu", Module.Category.CORE, "");
        instance = this;
        this.setBind(205);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen((GuiScreen)Panel.getInstance());
    }

    @Override
    public void onDisable() {
        if (Menu.mc.player != null) {
            Menu.mc.player.closeScreen();
        }
    }

    public Color getColor() {
        Color rainbow = ColorUtil.rainbow(this.hue.getValue(), this.saturation.getValue().intValue(), this.brightness.getValue().intValue());
        Color color = this.rainbow.getValue() != false ? rainbow : this.color.getValue();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static Menu getInstance() {
        return instance;
    }
}

