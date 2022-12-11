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
import cascade.features.gui.components.Component;
import cascade.features.gui.components.items.Item;
import cascade.features.modules.core.ClickGui;
import cascade.util.render.ColorUtil;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class Button
extends Item {
    boolean state;

    public Button(String name) {
        super(name);
        this.height = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Color guiEnabledBox = new Color(ClickGui.getInstance().moduleEnabledBox.getValue().getRed(), ClickGui.getInstance().moduleEnabledBox.getValue().getGreen(), ClickGui.getInstance().moduleEnabledBox.getValue().getBlue(), ClickGui.getInstance().moduleEnabledBox.getValue().getAlpha());
        Color guiDisabledBox = new Color(ClickGui.getInstance().moduleDisabledBox.getValue().getRed(), ClickGui.getInstance().moduleDisabledBox.getValue().getGreen(), ClickGui.getInstance().moduleDisabledBox.getValue().getBlue(), ClickGui.getInstance().moduleDisabledBox.getValue().getAlpha());
        Color guiEnabledText = new Color(ClickGui.getInstance().moduleEnabledText.getValue().getRed(), ClickGui.getInstance().moduleEnabledText.getValue().getGreen(), ClickGui.getInstance().moduleEnabledText.getValue().getBlue(), ClickGui.getInstance().moduleEnabledText.getValue().getAlpha());
        Color guiDisabledText = new Color(ClickGui.getInstance().moduleDisabledText.getValue().getRed(), ClickGui.getInstance().moduleDisabledText.getValue().getGreen(), ClickGui.getInstance().moduleDisabledText.getValue().getBlue(), ClickGui.getInstance().moduleDisabledText.getValue().getAlpha());
        Color textColor = new Color(-5592406);
        Color boxColor = new Color(-1);
        if (this.getState()) {
            textColor = guiEnabledText;
            boxColor = guiEnabledBox;
        } else {
            textColor = guiDisabledText;
            boxColor = guiDisabledBox;
        }
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height, ColorUtil.toRGBA(boxColor));
        RenderUtil.drawGradient(this.x, this.y, this.x + (float)this.width, this.y + (float)this.height, ColorUtil.toRGBA(0, 0, 0, 0), this.getState() ? ColorUtil.toRGBA(ClickGui.getInstance().gradientEnabled.getValue().getRed(), ClickGui.getInstance().gradientEnabled.getValue().getGreen(), ClickGui.getInstance().gradientEnabled.getValue().getBlue(), ClickGui.getInstance().gradientEnabled.getValue().getAlpha()) : ColorUtil.toRGBA(ClickGui.getInstance().gradientDisabled.getValue().getRed(), ClickGui.getInstance().gradientDisabled.getValue().getGreen(), ClickGui.getInstance().gradientDisabled.getValue().getBlue(), ClickGui.getInstance().gradientDisabled.getValue().getAlpha()));
        Cascade.textManager.drawStringWithShadow(this.getName(), this.x + (this.isHovering(mouseX, mouseY) ? 3.3f : 2.3f), this.y - 2.0f - (float)CascadeGui.getClickGui().getTextOffset(), ColorUtil.toRGBA(textColor));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.onMouseClick();
        }
    }

    public void onMouseClick() {
        this.state = !this.state;
        this.toggle();
        mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
    }

    public void toggle() {
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        for (Component component : CascadeGui.getClickGui().getComponents()) {
            if (!component.drag) continue;
            return false;
        }
        return (float)mouseX >= this.getX() && (float)mouseX <= this.getX() + (float)this.getWidth() && (float)mouseY >= this.getY() && (float)mouseY <= this.getY() + (float)this.height;
    }
}

