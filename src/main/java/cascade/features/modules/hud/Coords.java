//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.util.math.MathHelper
 */
package cascade.features.modules.hud;

import cascade.event.events.Render2DEvent;
import cascade.features.modules.Module;
import cascade.features.modules.hud.HUDManager;
import cascade.features.setting.Setting;
import cascade.util.render.ColorUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.math.MathHelper;

public class Coords
extends Module {
    Setting<Boolean> coords = this.register(new Setting<Boolean>("Coords", true));
    Setting<Boolean> otherDimension = this.register(new Setting<Boolean>("OtherDimension", false));
    Setting<Boolean> direction = this.register(new Setting<Boolean>("Direction", false));

    public Coords() {
        super("Coords", Module.Category.HUD, "");
    }

    @Override
    public void onRender2D(Render2DEvent e) {
        if (Coords.fullNullCheck()) {
            return;
        }
        int height = this.renderer.scaledHeight;
        int i = HUDManager.getInstance().i;
        i += 10;
        boolean chatGui = Coords.mc.currentScreen instanceof GuiChat;
        if (this.direction.getValue().booleanValue()) {
            this.renderer.drawString(this.getDirection(), 2.0f, height - i - (this.coords.getValue() != false ? 11 : 0) - (chatGui ? 13 : 0), HUDManager.getInstance().getColor(), true);
        }
        if (this.coords.getValue().booleanValue()) {
            int x = (int)Coords.mc.player.posX;
            int y = (int)Coords.mc.player.posY;
            int z = (int)Coords.mc.player.posZ;
            int hX = (int)(Coords.mc.player.posX * (this.isNether() ? 8.0 : 0.125));
            int hZ = (int)(Coords.mc.player.posZ * (this.isNether() ? 8.0 : 0.125));
            String coordinates = "" + x + ChatFormatting.DARK_GRAY + ", " + ChatFormatting.RESET + y + ChatFormatting.DARK_GRAY + ", " + ChatFormatting.RESET + z + ChatFormatting.DARK_GRAY + (this.otherDimension.getValue() != false ? " [" + ChatFormatting.RESET + hX + ChatFormatting.DARK_GRAY + ", " + ChatFormatting.RESET + hZ + ChatFormatting.DARK_GRAY + "]" : "");
            this.renderer.drawString(coordinates, 2.0f, height - i - (chatGui ? 13 : 0), ColorUtil.toRGBA(new Color(HUDManager.getInstance().getColor())), true);
        }
    }

    String getDirection() {
        int dirnumber = MathHelper.floor((double)((double)(Coords.mc.player.rotationYaw * 4.0f / 360.0f) + 0.5)) & 3;
        if (dirnumber == 0) {
            return ChatFormatting.RESET + "South " + ChatFormatting.DARK_GRAY + "(" + ChatFormatting.RESET + "+Z" + ChatFormatting.DARK_GRAY + ")";
        }
        if (dirnumber == 1) {
            return ChatFormatting.RESET + "West " + ChatFormatting.DARK_GRAY + "(" + ChatFormatting.RESET + "-X" + ChatFormatting.DARK_GRAY + ")";
        }
        if (dirnumber == 2) {
            return ChatFormatting.RESET + "North " + ChatFormatting.DARK_GRAY + "(" + ChatFormatting.RESET + "-Z" + ChatFormatting.DARK_GRAY + ")";
        }
        if (dirnumber == 3) {
            return ChatFormatting.RESET + "East " + ChatFormatting.DARK_GRAY + "(" + ChatFormatting.RESET + "+X" + ChatFormatting.DARK_GRAY + ")";
        }
        return "Loading...";
    }

    boolean isNether() {
        return Coords.mc.world.getBiome(Coords.mc.player.getPosition()).getBiomeName().equals("Hell");
    }
}

