/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package cascade.features.modules.hud;

import cascade.event.events.Render2DEvent;
import cascade.features.modules.Module;
import cascade.features.modules.hud.HUDManager;
import cascade.features.setting.Setting;
import com.mojang.realmsclient.gui.ChatFormatting;

public class Watermark
extends Module {
    Setting<String> text = this.register(new Setting<String>("Text", "Cascade"));
    Setting<Appender> appender = this.register(new Setting<Appender>("Appender", Appender.Alpha));
    Setting<Boolean> gray = this.register(new Setting<Object>("Gray", Boolean.valueOf(true), v -> this.appender.getValue() != Appender.None));

    public Watermark() {
        super("Watermark", Module.Category.HUD, "");
    }

    @Override
    public void onRender2D(Render2DEvent e) {
        if (Watermark.fullNullCheck()) {
            return;
        }
        String vers = null;
        switch (this.appender.getValue()) {
            case None: {
                vers = "";
                break;
            }
            case Version: {
                vers = " v0.3.7";
                break;
            }
            case Build: {
                vers = " b0.3.7";
                break;
            }
            case Beta: {
                vers = " 0.3.7-beta";
                break;
            }
            case Alpha: {
                vers = " 0.3.7-alpha";
            }
        }
        String watermark = this.text.getValue() + (this.gray.getValue() != false ? ChatFormatting.DARK_GRAY : "") + vers;
        this.renderer.drawString(watermark, 2.0f, 1.0f, HUDManager.getInstance().getColor(), true);
    }

    static enum Appender {
        None,
        Version,
        Build,
        Beta,
        Alpha;

    }
}

