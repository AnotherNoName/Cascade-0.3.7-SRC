//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package cascade.features.gui.components.items.buttons;

import cascade.Cascade;
import cascade.features.gui.CascadeGui;
import cascade.features.gui.components.items.buttons.Button;
import cascade.features.modules.core.ClickGui;
import cascade.features.setting.Setting;
import cascade.util.render.ColorUtil;
import cascade.util.render.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;

public class EnumButton
extends Button {
    public Setting setting;

    public EnumButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, !this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515);
        Cascade.textManager.drawStringWithShadow(this.setting.getName() + " " + ChatFormatting.GRAY + (this.setting.currentEnumName().equalsIgnoreCase("ABC") ? "ABC" : this.setting.currentEnumName()), this.x + 2.3f, this.y - 1.7f - (float)CascadeGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        int y = (int)this.y;
        if (this.setting.isOpen) {
            for (Object obber : this.setting.getValue().getClass().getEnumConstants()) {
                if ((float)mouseX > this.x && (float)mouseX < this.x + (float)this.width && mouseY > (y += this.height) && mouseY < y + this.height) {
                    RenderUtil.drawRect(this.x + 2.0f, y, this.x + (float)this.width + 7.0f, y + this.height, new Color(1677721601, true).getRGB());
                }
                Cascade.textManager.drawStringWithShadow((this.setting.getValueAsString().equals(obber.toString()) ? ChatFormatting.WHITE : ChatFormatting.GRAY) + obber.toString(), this.x + 4.0f, (float)y + (float)this.height / 2.0f - (float)EnumButton.mc.fontRendererObj.FONT_HEIGHT / 2.0f, -1);
            }
            RenderUtil.drawOutlineRect(this.x + 3.0f, this.y + (float)this.height - 1.0f, this.x + (float)this.width + 5.0f, y + this.height - 2, new Color(ColorUtil.toRGBA(ClickGui.getInstance().enumOutline.getValue())), 1.0f);
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int y = (int)this.y;
        if (this.setting.isOpen) {
            for (Object obber : this.setting.getValue().getClass().getEnumConstants()) {
                if (!((float)mouseX > this.x) || !((float)mouseX < this.x + (float)this.width) || mouseY <= (y += this.height) || mouseY >= y + this.height || mouseButton != 0) continue;
                this.setting.setEnumValue(String.valueOf(obber));
            }
        }
        if (this.isHovering(mouseX, mouseY) && mouseButton == 1) {
            this.setting.isOpen = !this.setting.isOpen;
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public boolean getState() {
        return true;
    }
}

