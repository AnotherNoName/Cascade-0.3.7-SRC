/*
 * Decompiled with CFR 0.152.
 */
package cascade.features.modules.visual;

import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import java.awt.Color;

public class GlintMod
extends Module {
    public Setting<Color> c = this.register(new Setting<Color>("Color", new Color(-1)));
    public static GlintMod INSTANCE;

    public GlintMod() {
        super("GlintMod", Module.Category.VISUAL, "Changes enchantment glint color");
        INSTANCE = this;
    }

    public static GlintMod getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GlintMod();
        }
        return INSTANCE;
    }
}

