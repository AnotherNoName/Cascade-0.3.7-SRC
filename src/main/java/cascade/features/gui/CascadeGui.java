//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  org.lwjgl.input.Mouse
 */
package cascade.features.gui;

import cascade.Cascade;
import cascade.features.Feature;
import cascade.features.gui.components.Component;
import cascade.features.gui.components.items.Item;
import cascade.features.gui.components.items.buttons.ModuleButton;
import cascade.features.modules.Module;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class CascadeGui
extends GuiScreen {
    private static CascadeGui INSTANCE = new CascadeGui();
    private final ArrayList<Component> components = new ArrayList();

    public CascadeGui() {
        this.setInstance();
        this.load();
    }

    public static CascadeGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CascadeGui();
        }
        return INSTANCE;
    }

    public static CascadeGui getClickGui() {
        return CascadeGui.getInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onGuiClosed() {
        if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    void load() {
        int x = -106;
        for (final Module.Category category : Cascade.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 110, 4, true){

                @Override
                public void setupItems() {
                    counter1 = new int[]{1};
                    Cascade.moduleManager.getModulesByCategory(category).forEach(module -> this.addButton(new ModuleButton((Module)module)));
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    public void updateModule(Module module) {
        for (Component component : this.components) {
            for (Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton)item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
}

