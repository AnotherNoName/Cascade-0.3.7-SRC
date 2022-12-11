//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.core;

import cascade.Cascade;
import cascade.event.events.ClientEvent;
import cascade.event.events.Render2DEvent;
import cascade.features.gui.CascadeGui;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui
extends Module {
    Setting<Page> page = this.register(new Setting<Page>("Page", Page.General));
    public Setting<Color> c = this.register(new Setting<Object>("Color", new Color(-5840085), v -> this.page.getValue() == Page.General));
    public Setting<Color> background = this.register(new Setting<Object>("BackgroundColor", new Color(0x1A1A1A), v -> this.page.getValue() == Page.General));
    public Setting<Boolean> descriptions = this.register(new Setting<Object>("Descriptions", Boolean.valueOf(true), v -> this.page.getValue() == Page.General));
    Setting<Boolean> gradient = this.register(new Setting<Object>("Gradient", Boolean.valueOf(false), v -> this.page.getValue() == Page.General));
    Setting<Color> gradientTop = this.register(new Setting<Object>("Top", new Color(447144747, true), v -> this.page.getValue() == Page.General && this.gradient.getValue() != false));
    Setting<Color> gradientBottom = this.register(new Setting<Object>("Bottom", new Color(447144747, true), v -> this.page.getValue() == Page.General && this.gradient.getValue() != false));
    public Setting<Boolean> syncSize = this.register(new Setting<Object>("SyncSize", Boolean.valueOf(false), v -> this.page.getValue() == Page.Categories));
    public Setting<Color> categoryGradient = this.register(new Setting<Object>("CategoryGradient", new Color(0x181919, true), v -> this.page.getValue() == Page.Categories));
    public Setting<Color> categoryOutlineColor = this.register(new Setting<Object>("OutlineColor", new Color(10937131), v -> this.page.getValue() == Page.Categories));
    public Setting<Color> categoryBoxColor = this.register(new Setting<Object>("BoxColor", new Color(0x101010), v -> this.page.getValue() == Page.Categories));
    public Setting<Boolean> moduleOutline = this.register(new Setting<Object>("ModOutline", Boolean.valueOf(true), v -> this.page.getValue() == Page.Modules));
    public Setting<Color> moduleOutlineColor = this.register(new Setting<Object>("ModOutlineColor", new Color(0x2D2D2D), v -> this.page.getValue() == Page.Modules && this.moduleOutline.getValue() != false));
    public Setting<Color> moduleEnabledBox = this.register(new Setting<Object>("EnabledBox", new Color(10937131), v -> this.page.getValue() == Page.Modules));
    public Setting<Color> moduleDisabledBox = this.register(new Setting<Object>("DisabledBox", new Color(0x101010), v -> this.page.getValue() == Page.Modules));
    public Setting<Color> moduleEnabledText = this.register(new Setting<Object>("EnabledText", new Color(0xFFFFFF), v -> this.page.getValue() == Page.Modules));
    public Setting<Color> moduleDisabledText = this.register(new Setting<Object>("DisabledText", new Color(11779493), v -> this.page.getValue() == Page.Modules));
    public Setting<Color> gradientEnabled = this.register(new Setting<Object>("GradientEnabled", new Color(0x181919, true), v -> this.page.getValue() == Page.Modules));
    public Setting<Color> gradientDisabled = this.register(new Setting<Object>("GradientDisabled", new Color(0x181919, true), v -> this.page.getValue() == Page.Modules));
    public Setting<Integer> hoverAlpha = this.register(new Setting<Object>("HoverAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Page.Modules));
    public Setting<Color> boolEnabledBox = this.register(new Setting<Object>("BoolEnabledBox", new Color(10937131), v -> this.page.getValue() == Page.Booleans));
    public Setting<Color> boolDisabledBox = this.register(new Setting<Object>("BoolDisabledBox", new Color(0x101010), v -> this.page.getValue() == Page.Booleans));
    public Setting<Color> boolEnabledOut = this.register(new Setting<Object>("BoolEnabledOut", new Color(0x2E2E2E), v -> this.page.getValue() == Page.Booleans));
    public Setting<Color> boolDisabledOut = this.register(new Setting<Object>("BoolDisabledOut", new Color(0x2E2E2E), v -> this.page.getValue() == Page.Booleans));
    private static ClickGui INSTANCE = new ClickGui();
    public Setting<Color> enumOutline = this.register(new Setting<Object>("EnumOutline", new Color(0x333333), v -> this.page.getValue() == Page.Enums));

    public ClickGui() {
        super("ClickGui", Module.Category.CORE, "Client's Click GUI");
        this.setInstance();
        this.setBind(208);
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            Cascade.colorManager.setColor(this.c.getValue().getRed(), this.c.getValue().getGreen(), this.c.getValue().getBlue(), this.c.getValue().getAlpha());
        }
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        ScaledResolution resolution = new ScaledResolution(mc);
        if (ClickGui.mc.currentScreen instanceof CascadeGui && this.gradient.getValue().booleanValue()) {
            RenderUtil.drawGradient(0.0, 0.0, resolution.getScaledWidth(), resolution.getScaledHeight(), new Color(0, 0, 0, 0).getRGB(), this.gradientBottom.getValue().getRGB());
            RenderUtil.drawGradient(0.0, 0.0, resolution.getScaledWidth(), resolution.getScaledHeight(), this.gradientTop.getValue().getRGB(), new Color(0, 0, 0, 0).getRGB());
        }
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen((GuiScreen)CascadeGui.getClickGui());
    }

    @Override
    public void onDisable() {
        if (ClickGui.mc.player != null) {
            ClickGui.mc.player.closeScreen();
        }
    }

    @Override
    public void onLoad() {
        Cascade.colorManager.setColor(this.c.getValue().getRed(), this.c.getValue().getGreen(), this.c.getValue().getBlue(), this.c.getValue().getAlpha());
    }

    @Override
    public void onUpdate() {
        if (!ClickGui.fullNullCheck() && !(ClickGui.mc.currentScreen instanceof CascadeGui)) {
            this.disable();
        }
    }

    static enum Page {
        General,
        Categories,
        Modules,
        Booleans,
        Enums;

    }
}

