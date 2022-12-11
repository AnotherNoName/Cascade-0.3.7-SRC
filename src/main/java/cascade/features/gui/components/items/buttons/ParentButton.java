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
import cascade.features.gui.components.items.buttons.Button;
import cascade.features.modules.core.ClickGui;
import cascade.features.setting.ParentSetting;
import cascade.features.setting.Setting;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class ParentButton
extends Button {
    public ParentSetting parentSetting;
    float currSize;

    public ParentButton(ParentSetting parentSetting) {
        super(parentSetting.getName());
        this.parentSetting = parentSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x + 1.0f, this.y, this.x + (float)this.width + 6.0f, this.y + (float)this.height, this.isHovering(mouseX, mouseY) ? Cascade.colorManager.getColorWithAlpha(Cascade.moduleManager.getModuleByClass(ClickGui.class).c.getValue().getAlpha()) : Cascade.colorManager.getColorWithAlpha(Cascade.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()));
        RenderUtil.drawOutlineRect(this.x + 1.0f, this.y, this.x + (float)this.width + 6.0f, this.y + (float)this.height, new Color(1), 1.0f);
        ParentButton.mc.fontRendererObj.drawStringWithShadow(this.parentSetting.getName(), this.x + 3.0f, this.y + (float)this.height / 2.0f - (float)ParentButton.mc.fontRendererObj.FONT_HEIGHT / 2.0f + 1.0f, -1);
        if (this.parentSetting.isOpened()) {
            if (this.currSize > 0.0f) {
                this.currSize -= 0.1f;
            }
            int i = 0;
            for (Setting setting : this.parentSetting.getChildren()) {
                if (setting.isVisible()) {
                    i += 15;
                }
                if (setting.getValue() instanceof Color && setting.isOpen) {
                    i += 110;
                }
                if (!(setting.getValue() instanceof Enum) || !setting.isOpen) continue;
                i += setting.getValue().getClass().getEnumConstants().length * 15;
            }
            RenderUtil.drawOutlineRect(this.x + 1.0f, this.y + 1.0f, this.x + (float)this.width + 6.0f, this.y + (float)this.height + (float)i, new Color(Cascade.colorManager.getColorWithAlpha(Cascade.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue())), 2.0f);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovering(mouseX, mouseY) && mouseButton == 1) {
            this.parentSetting.setOpened((Boolean)this.parentSetting.getValue() == false);
            mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
        }
    }
}

