/*
 * Decompiled with CFR 0.152.
 */
package cascade.features.modules.hud;

import cascade.Cascade;
import cascade.event.events.Render2DEvent;
import cascade.features.modules.Module;
import cascade.features.modules.hud.HUDManager;
import cascade.features.setting.Setting;
import cascade.util.core.TextUtil;

public class Ping
extends Module {
    Setting<Boolean> ms = this.register(new Setting<Boolean>("Ms", false));
    Setting<Boolean> colon = this.register(new Setting<Boolean>("Colon", true));
    Setting<Boolean> syncHUD = this.register(new Setting<Boolean>("SyncHUD", true));
    Setting<Integer> x = this.register(new Setting<Object>("X", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1000), v -> this.syncHUD.getValue() == false));
    Setting<Integer> y = this.register(new Setting<Object>("Y", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1000), v -> this.syncHUD.getValue() == false));

    public Ping() {
        super("Ping", Module.Category.HUD, "");
    }

    @Override
    public void onRender2D(Render2DEvent e) {
        if (Ping.fullNullCheck()) {
            return;
        }
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int i = HUDManager.getInstance().i;
        String start = this.colon.getValue().booleanValue() ? (this.ms.getValue().booleanValue() ? "Ms: " : "Ping: ") : (this.ms.getValue() != false ? "Ms " : "Ping ");
        String ping = start + TextUtil.coloredString(String.valueOf(Cascade.serverManager.getPing()), HUDManager.getInstance().infoColor.getValue());
        i += 10;
        this.renderer.drawString(ping, this.syncHUD.getValue() != false ? (float)(width - this.renderer.getStringWidth(ping) - 2) : (float)this.x.getValue().intValue() + 0.0f, this.syncHUD.getValue() != false ? (float)(HUDManager.getInstance().renderingUp.getValue() != false ? height - 2 - i : 2 + i++ * 10) : (float)this.y.getValue().intValue() + 0.0f, HUDManager.getInstance().getColor(), true);
    }
}

