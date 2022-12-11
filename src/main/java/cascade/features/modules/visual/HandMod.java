//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 */
package cascade.features.modules.visual;

import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import java.awt.Color;

public class HandMod
extends Module {
    Setting<Page> page = this.register(new Setting<Page>("Page", Page.Misc));
    public Setting<Float> x = this.register(new Setting<Object>("X", Float.valueOf(0.0f), Float.valueOf(-10.0f), Float.valueOf(10.0f), v -> this.page.getValue() == Page.ViewModel));
    public Setting<Float> y = this.register(new Setting<Object>("Y", Float.valueOf(0.0f), Float.valueOf(-10.0f), Float.valueOf(10.0f), v -> this.page.getValue() == Page.ViewModel));
    public Setting<Float> z = this.register(new Setting<Object>("Z", Float.valueOf(0.0f), Float.valueOf(-10.0f), Float.valueOf(10.0f), v -> this.page.getValue() == Page.ViewModel));
    public Setting<Float> sizeX = this.register(new Setting<Object>("SizeX", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), v -> this.page.getValue() == Page.ViewModel));
    public Setting<Float> sizeY = this.register(new Setting<Object>("SizeY", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), v -> this.page.getValue() == Page.ViewModel));
    public Setting<Float> sizeZ = this.register(new Setting<Object>("SizeZ", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), v -> this.page.getValue() == Page.ViewModel));
    public Setting<Boolean> cancel = this.register(new Setting<Boolean>("Cancel", false));
    public Setting<Color> fill = this.register(new Setting<Color>("Fill", new Color(120, 0, 255, 20)));
    public Setting<Color> outline = this.register(new Setting<Color>("Outline", new Color(144, 0, 255, 255)));
    Setting<Boolean> instantSwap = this.register(new Setting<Object>("InstantSwap", Boolean.valueOf(true), v -> this.page.getValue() == Page.Misc));
    public Setting<Boolean> noSway = this.register(new Setting<Object>("NoSway", Boolean.valueOf(true), v -> this.page.getValue() == Page.Misc));
    public Setting<Swing> swing = this.register(new Setting<Object>("Swing", (Object)Swing.Mainhand, v -> this.page.getValue() == Page.Misc));
    public Setting<Boolean> swingSpeed = this.register(new Setting<Object>("SwingSpeed", Boolean.valueOf(false), v -> this.page.getValue() == Page.Misc));
    public Setting<Integer> factor = this.register(new Setting<Object>("Factor", Integer.valueOf(16), Integer.valueOf(1), Integer.valueOf(50), v -> this.page.getValue() == Page.Misc && this.swingSpeed.getValue() != false));
    public Setting<Color> c = this.register(new Setting<Object>("ItemColor", new Color(1.0f, 1.0f, 1.0f, 1.0f), v -> this.page.getValue() == Page.Misc));
    static HandMod INSTANCE = new HandMod();

    public HandMod() {
        super("HandMod", Module.Category.VISUAL, "View model changer");
        INSTANCE = this;
    }

    public static HandMod getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HandMod();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (HandMod.fullNullCheck()) {
            return;
        }
        if (this.instantSwap.getValue().booleanValue() && (double)HandMod.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            HandMod.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            HandMod.mc.entityRenderer.itemRenderer.itemStackMainHand = HandMod.mc.player.getHeldItemMainhand();
        }
    }

    public static enum Swing {
        Mainhand,
        Offhand,
        Packet;

    }

    static enum Page {
        ViewModel,
        Chams,
        Misc;

    }
}

