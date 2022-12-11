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
import cascade.features.gui.CascadeGui;
import cascade.features.gui.components.items.buttons.Button;
import cascade.features.modules.core.ClickGui;
import cascade.features.setting.Setting;
import cascade.util.render.ColorUtil;
import cascade.util.render.RenderUtil;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class BooleanButton
extends Button {
    Setting setting;

    public BooleanButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, !this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515);
        float left = this.x + (float)this.width - 4.0f;
        float top = this.y + 4.0f;
        float right = this.x + (float)this.width + 5.0f;
        float bottom = this.y + (float)this.height - 3.0f;
        RenderUtil.drawRect(left, top, right, bottom, this.getState() ? ColorUtil.toRGBA(ClickGui.getInstance().boolEnabledBox.getValue()) : ColorUtil.toRGBA(ClickGui.getInstance().boolDisabledBox.getValue()));
        RenderUtil.drawOutlineRect(left, top, right, bottom, this.getState() ? ClickGui.getInstance().boolEnabledOut.getValue() : ClickGui.getInstance().boolDisabledOut.getValue(), 0.1f);
        RenderUtil.drawGradient(left, top, right, bottom + 0.1f, ColorUtil.toRGBA(0, 0, 0, 0), ColorUtil.toRGBA(40, 40, 40, 99));
        Cascade.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 1.7f - (float)CascadeGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.setting.setValue((Boolean)this.setting.getValue() == false);
    }

    @Override
    public boolean getState() {
        return (Boolean)this.setting.getValue();
    }
}

