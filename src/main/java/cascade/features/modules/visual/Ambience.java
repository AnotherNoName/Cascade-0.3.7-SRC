//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.EntityViewRenderEvent$FogColors
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.visual;

import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import java.awt.Color;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Ambience
extends Module {
    Setting<Boolean> fullBright = this.register(new Setting<Boolean>("FullBright", true));
    public Setting<Color> ambienceC = this.register(new Setting<Color>("AmbienceColor", new Color(-1)));
    Setting<Boolean> sky = this.register(new Setting<Boolean>("Sky", false));
    Setting<Color> skyC = this.register(new Setting<Object>("SkyColor", new Color(-1), v -> this.sky.getValue()));
    float originalBrightness;
    static Ambience INSTANCE;

    public Ambience() {
        super("Ambience", Module.Category.VISUAL, "Ambience tweakzs");
        INSTANCE = this;
    }

    public static Ambience getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Ambience();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (Ambience.fullNullCheck()) {
            return;
        }
        if (this.fullBright.getValue().booleanValue() && Ambience.mc.gameSettings.gammaSetting != 42069.0f) {
            Ambience.mc.gameSettings.gammaSetting = 42069.0f;
        }
    }

    @Override
    public void onEnable() {
        if (Ambience.fullNullCheck()) {
            return;
        }
        this.originalBrightness = Ambience.mc.gameSettings.gammaSetting;
        if (this.fullBright.getValue().booleanValue()) {
            Ambience.mc.gameSettings.gammaSetting = 42069.0f;
        }
    }

    @Override
    public void onDisable() {
        if (Ambience.fullNullCheck()) {
            return;
        }
        Ambience.mc.gameSettings.gammaSetting = this.originalBrightness;
    }

    @SubscribeEvent
    public void onSkyColor(EntityViewRenderEvent.FogColors e) {
        if (this.sky.getValue().booleanValue() && this.isEnabled() && e != null) {
            e.setRed((float)this.skyC.getValue().getRed() / 255.0f);
            e.setGreen((float)this.skyC.getValue().getGreen() / 255.0f);
            e.setBlue((float)this.skyC.getValue().getBlue() / 255.0f);
        }
    }
}

