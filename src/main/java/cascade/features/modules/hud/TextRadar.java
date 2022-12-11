//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 */
package cascade.features.modules.hud;

import cascade.Cascade;
import cascade.event.events.Render2DEvent;
import cascade.features.modules.Module;
import cascade.features.modules.hud.HUDManager;
import cascade.features.setting.Setting;
import cascade.util.misc.MathUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TextRadar
extends Module {
    Setting<Boolean> friends = this.register(new Setting<Boolean>("Friends", true));
    Setting<Boolean> enemies = this.register(new Setting<Boolean>("Enemies", true));
    Setting<Integer> limit = this.register(new Setting<Integer>("MaxEntites", 9, 3, 35));
    Setting<Integer> x = this.register(new Setting<Integer>("x", 10, 0, 50));
    Setting<Integer> y = this.register(new Setting<Integer>("y", 20, 0, 50));

    public TextRadar() {
        super("TextRadar", Module.Category.HUD, "");
    }

    @Override
    public void onRender2D(Render2DEvent e) {
        if (TextRadar.fullNullCheck()) {
            return;
        }
        int y = 0;
        for (EntityPlayer p : TextRadar.mc.world.playerEntities) {
            if (p == TextRadar.mc.player || Cascade.friendManager.isFriend(p.getName()) && !this.friends.getValue().booleanValue() || !Cascade.friendManager.isFriend(p.getName()) && !this.enemies.getValue().booleanValue()) continue;
            String s = MathUtil.round(TextRadar.mc.player.getDistanceToEntity((Entity)p), 1) + " " + p.getName() + " " + MathUtil.round(p.getHealth(), 1);
            this.renderer.drawString((Cascade.friendManager.isFriend(p.getName()) ? ChatFormatting.AQUA : "") + s, this.x.getValue().intValue(), y + this.y.getValue(), HUDManager.getInstance().getColor(), true);
            y = (int)((float)y + 10.0f);
        }
    }
}

