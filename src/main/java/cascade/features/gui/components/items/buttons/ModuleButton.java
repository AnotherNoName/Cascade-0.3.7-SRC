//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.SoundEvent
 */
package cascade.features.gui.components.items.buttons;

import cascade.Cascade;
import cascade.features.gui.components.Component;
import cascade.features.gui.components.items.Item;
import cascade.features.gui.components.items.buttons.BindButton;
import cascade.features.gui.components.items.buttons.BooleanButton;
import cascade.features.gui.components.items.buttons.Button;
import cascade.features.gui.components.items.buttons.ColorButton;
import cascade.features.gui.components.items.buttons.EnumButton;
import cascade.features.gui.components.items.buttons.ParentButton;
import cascade.features.gui.components.items.buttons.Slider;
import cascade.features.gui.components.items.buttons.StringButton;
import cascade.features.modules.Module;
import cascade.features.modules.core.ClickGui;
import cascade.features.setting.Bind;
import cascade.features.setting.ParentSetting;
import cascade.features.setting.Setting;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class ModuleButton
extends Button {
    private final Module module;
    private List<Item> items = new ArrayList<Item>();
    private boolean subOpen;
    private float _y;
    float currSize;

    public ModuleButton(Module module) {
        super(module.getName());
        this.module = module;
        this.initSettings();
    }

    public void initSettings() {
        ArrayList<Item> newItems = new ArrayList<Item>();
        if (!this.module.getSettings().isEmpty()) {
            this.module.getSettings().forEach(setting -> {
                if (setting instanceof ParentSetting) {
                    newItems.add(new ParentButton((ParentSetting)setting));
                    return;
                }
                if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
                    newItems.add(new BooleanButton((Setting)setting));
                }
                if (setting.getValue() instanceof Bind && !setting.getName().equalsIgnoreCase("Keybind") && !this.module.getName().equalsIgnoreCase("Hud")) {
                    newItems.add(new BindButton((Setting)setting));
                }
                if ((setting.getValue() instanceof String || setting.getValue() instanceof Character) && !setting.getName().equalsIgnoreCase("displayName")) {
                    newItems.add(new StringButton((Setting)setting));
                }
                if (setting.getValue() instanceof Color) {
                    newItems.add(new ColorButton((Setting)setting));
                }
                if (setting.isNumberSetting() && setting.hasRestriction()) {
                    newItems.add(new Slider((Setting)setting));
                    return;
                }
                if (!setting.isEnumSetting()) {
                    return;
                }
                newItems.add(new EnumButton((Setting)setting));
            });
        }
        newItems.add(new BindButton(this.module.getSettingByName("Keybind")));
        this.items = newItems;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.isHovering(mouseX, mouseY) && ClickGui.getInstance().isEnabled() && ClickGui.getInstance().descriptions.getValue().booleanValue()) {
            RenderUtil.drawOutlinedRoundedRectangle(mouseX + 10, mouseY, mouseX + 10 + this.renderer.getStringWidth(this.module.getDescription()), mouseY + 10, 1.0f, 0.0f, 0.0f, 0.0f, 100.0f, 1.0f);
            this.renderer.drawStringWithShadow(this.module.getDescription(), mouseX + 10, mouseY, -1);
        }
        if (this.subOpen && this.currSize > 0.0f) {
            this.currSize -= 0.1f;
        }
        if (!this.items.isEmpty()) {
            if (ClickGui.getInstance().moduleOutline.getValue().booleanValue()) {
                RenderUtil.drawOutlineRect(this.x, this.y + (float)this.height, this.x + (float)this.width, this.y, ClickGui.getInstance().moduleOutlineColor.getValue(), 0.1f);
            }
            if (this.subOpen) {
                float height = 1.0f;
                for (Item item : this.items) {
                    Component.counter1[0] = Component.counter1[0] + 1;
                    if (!item.isHidden()) {
                        item.setLocation(this.x + 1.0f, this.y + (height += 15.0f));
                        item.setHeight(15);
                        item.setWidth(this.width - 9);
                        item.drawScreen(mouseX, mouseY, partialTicks);
                        this._y = height;
                        if (item instanceof ColorButton && ((ColorButton)item).setting.isOpen) {
                            height += 110.0f;
                        }
                        if (item instanceof EnumButton && ((EnumButton)item).setting.isOpen) {
                            height += (float)(((EnumButton)item).setting.getValue().getClass().getEnumConstants().length * 15);
                        }
                    }
                    item.update();
                }
                if (this.module.isEnabled()) {
                    RenderUtil.drawOutlineRect(this.x, this.y + 1.0f, this.x + (float)this.width, this.y + (this._y + 16.0f), this.getState() ? Cascade.colorManager.getColorWithAlphaColor(Cascade.moduleManager.getModuleByClass(ClickGui.class).c.getValue().getAlpha()) : ClickGui.getInstance().moduleOutlineColor.getValue(), 2.0f);
                } else {
                    RenderUtil.drawOutlineRect(this.x, this.y, this.x + (float)this.width, this.y + (this._y + 16.0f), ClickGui.getInstance().moduleOutlineColor.getValue(), 1.0f);
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
            }
            if (this.subOpen) {
                for (Item item : this.items) {
                    if (item.isHidden()) continue;
                    item.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        super.onKeyTyped(typedChar, keyCode);
        if (!this.items.isEmpty() && this.subOpen) {
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                item.onKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public int getHeight() {
        if (this.subOpen) {
            int height = 14;
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                height = item instanceof ColorButton && ((ColorButton)item).setting.isOpen ? (height += 125) : (height += item.getHeight() + 1);
                if (!(item instanceof EnumButton) || !((EnumButton)item).setting.isOpen) continue;
                height += ((EnumButton)item).setting.getValue().getClass().getEnumConstants().length * 15;
            }
            return height + 2;
        }
        return 14;
    }

    public Module getModule() {
        return this.module;
    }

    @Override
    public void toggle() {
        this.module.toggle();
    }

    @Override
    public boolean getState() {
        return this.module.isEnabled();
    }
}

