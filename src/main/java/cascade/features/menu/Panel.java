//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 */
package cascade.features.menu;

import cascade.features.menu.PanelUtil;
import cascade.features.menu.Quad;
import net.minecraft.client.gui.GuiScreen;

public class Panel
extends GuiScreen {
    static Panel instance = new Panel();
    boolean drag;

    public Panel() {
        instance = this;
        this.reload();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        PanelUtil.renderBox(new Quad(10.0f, 10.0f, 400.0f, 380.0f));
    }

    void reload() {
    }

    public static Panel getInstance() {
        return instance;
    }
}

