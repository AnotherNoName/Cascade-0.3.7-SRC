/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.Display
 */
package cascade.features.modules.core;

import cascade.Cascade;
import cascade.event.events.ClientEvent;
import cascade.features.command.Command;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.core.TextUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;

public class Manager
extends Module {
    Setting<Page> page = this.register(new Setting<Page>("Page", Page.Client));
    public Setting<String> name = this.register(new Setting<Object>("Name", "Cascade", v -> this.page.getValue() == Page.Client));
    Setting<String> prefix = this.register(new Setting<Object>("Prefix", ".", v -> this.page.getValue() == Page.Client));
    public Setting<String> title = this.register(new Setting<Object>("Title", "Cascade", v -> this.page.getValue() == Page.Client));
    public Setting<Integer> unfocusedFps = this.register(new Setting<Object>("UnfocusedFPS", Integer.valueOf(24), Integer.valueOf(1), Integer.valueOf(120), v -> this.page.getValue() == Page.Client));
    public Setting<TextUtil.Color> textMain = this.register(new Setting<Object>("TextMain", (Object)TextUtil.Color.DARK_GRAY, v -> this.page.getValue() == Page.Client));
    public Setting<TextUtil.Color> textAccent = this.register(new Setting<Object>("TextAccent", (Object)TextUtil.Color.GREEN, v -> this.page.getValue() == Page.Client));
    Setting<Double> explosionTime = this.register(new Setting<Object>("Explosion", Double.valueOf(2.5), Double.valueOf(1.0), Double.valueOf(10.0), v -> this.page.getValue() == Page.Timings));
    Setting<Double> lagBackTime = this.register(new Setting<Object>("LagBack", Double.valueOf(2.5), Double.valueOf(1.0), Double.valueOf(10.0), v -> this.page.getValue() == Page.Timings));
    Setting<Double> knockbackTime = this.register(new Setting<Object>("Knockback", Double.valueOf(1.0), Double.valueOf(0.1), Double.valueOf(8.0), v -> this.page.getValue() == Page.Timings));
    Setting<Double> timerTime = this.register(new Setting<Object>("Timer", Double.valueOf(2.5), Double.valueOf(0.1), Double.valueOf(8.0), v -> this.page.getValue() == Page.Timings));
    public Setting<Boolean> enabledS = this.register(new Setting<Object>("Enabled", Boolean.valueOf(false), v -> this.page.getValue() == Page.Simulation));
    public Setting<Boolean> debug = this.register(new Setting<Object>("Debug", Boolean.valueOf(false), v -> this.page.getValue() == Page.Simulation && this.enabledS.getValue() != false));
    public Setting<Double> scanRange = this.register(new Setting<Object>("ScanRange", Float.valueOf(30.0f), Float.valueOf(1.0f), Float.valueOf(150.0f), v -> this.page.getValue() == Page.Simulation && this.enabledS.getValue() != false));
    public Setting<Double> dangerRange = this.register(new Setting<Object>("DangerRange", Float.valueOf(22.5f), Float.valueOf(1.0f), Float.valueOf(60.0f), v -> this.page.getValue() == Page.Simulation && this.enabledS.getValue() != false));
    static Manager INSTANCE = new Manager();

    public Manager() {
        super("Manager", Module.Category.CORE, "");
        INSTANCE = this;
    }

    public static Manager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Manager();
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent e) {
        if (e.getStage() == 2 && e.getSetting().getFeature().equals(this)) {
            if (e.getSetting() == this.prefix) {
                Cascade.chatManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + Cascade.chatManager.getPrefix(), true, false);
            }
            if (e.getSetting() == this.name) {
                Cascade.chatManager.setClientMessage(this.getCommandMessage());
            }
        }
    }

    @Override
    public void onLoad() {
        Cascade.chatManager.setClientMessage(this.getCommandMessage());
        Cascade.chatManager.setPrefix(this.prefix.getValue());
    }

    String getCommandMessage() {
        return TextUtil.coloredString("[", this.textMain.getValue()) + TextUtil.coloredString(this.name.getValue(), this.textAccent.getValue()) + TextUtil.coloredString("]", this.textMain.getValue());
    }

    @Override
    public void onUpdate() {
        Display.setTitle((String)this.title.getValueAsString());
    }

    public double getExplosion() {
        return this.explosionTime.getValue() * 100.0;
    }

    public double getLagBack() {
        return this.lagBackTime.getValue() * 100.0;
    }

    public double getKnockback() {
        return this.knockbackTime.getValue() * 1000.0;
    }

    public double getTimer() {
        return this.timerTime.getValue() * 1000.0;
    }

    static enum Page {
        Client,
        Timings,
        Simulation;

    }
}

