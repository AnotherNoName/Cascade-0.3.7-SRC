//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 */
package cascade.features.modules.hud;

import cascade.Cascade;
import cascade.event.events.Render2DEvent;
import cascade.features.modules.Module;
import cascade.features.modules.combat.CascadeAura;
import cascade.features.modules.hud.HUDManager;
import cascade.features.setting.Setting;
import cascade.util.entity.EntityUtil;
import cascade.util.misc.CalcUtil;
import cascade.util.player.InventoryUtil;
import cascade.util.player.TargetUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PvpInfo
extends Module {
    Setting<String> text = this.register(new Setting<String>("Text", "Cascade"));
    Setting<Integer> height = this.register(new Setting<Integer>("Height", 2, -10, 10));
    Setting<Boolean> totems = this.register(new Setting<Boolean>("Totems", true));
    Setting<Boolean> ping = this.register(new Setting<Boolean>("Ping", true));
    Setting<Boolean> targetDistance = this.register(new Setting<Boolean>("TargetDistance", false));
    Setting<Boolean> targetHoleCheck = this.register(new Setting<Boolean>("TargetHoleCheck", false));
    Setting<Boolean> playersCount = this.register(new Setting<Boolean>("PlayersCount", true));
    Setting<Boolean> enemiesCount = this.register(new Setting<Boolean>("EnemiesCount", false));
    Setting<Boolean> friendsCount = this.register(new Setting<Boolean>("FriendsCount", false));

    public PvpInfo() {
        super("PvpInfo", Module.Category.HUD, "");
    }

    @Override
    public void onRender2D(Render2DEvent e) {
        if (PvpInfo.fullNullCheck()) {
            return;
        }
        int height = this.renderer.scaledHeight;
        int i = HUDManager.getInstance().i;
        int pig = 0;
        this.renderer.drawString(this.text.getValue(), 2.0f, height / 2 - (i += 10) + this.height.getValue() * 10 + pig, HUDManager.INSTANCE.getColor(), true);
        pig += 10;
        if (this.totems.getValue().booleanValue()) {
            int totems = 0;
            for (ItemStack is : PvpInfo.mc.player.inventory.mainInventory) {
                if (is.getItem() != Items.field_190929_cY) continue;
                totems += is.func_190916_E();
            }
            if (InventoryUtil.heldItem(Items.field_190929_cY, InventoryUtil.Hand.Off)) {
                ++totems;
            }
            this.renderer.drawString(totems == 0 ? ChatFormatting.RED + "" + totems : ChatFormatting.GREEN + "" + totems, 2.0f, height / 2 - i + this.height.getValue() * 10 + pig, HUDManager.INSTANCE.getColor(), true);
            pig += 10;
        }
        if (this.ping.getValue().booleanValue()) {
            int ping = Cascade.serverManager.getPing();
            String pingString = null;
            if (ping <= 50) {
                pingString = ChatFormatting.GREEN + "" + ping;
            }
            if (ping > 50 && ping <= 100) {
                pingString = ChatFormatting.YELLOW + "" + ping;
            }
            if (ping > 100) {
                pingString = ChatFormatting.RED + "" + ping;
            }
            this.renderer.drawString(pingString, 2.0f, height / 2 - i + this.height.getValue() * 10 + pig, HUDManager.INSTANCE.getColor(), true);
            pig += 10;
        }
        if (this.targetDistance.getValue().booleanValue()) {
            EntityPlayer target = TargetUtil.getTarget(256.0);
            double distance = target == null ? 0.0 : (double)Math.round(CalcUtil.getDistance((Entity)target));
            ChatFormatting color = ChatFormatting.RED;
            color = distance < (double)CascadeAura.getInstance().range.getValue().floatValue() ? ChatFormatting.GREEN : (distance < 12.0 ? ChatFormatting.YELLOW : ChatFormatting.RED);
            this.renderer.drawString(color + String.valueOf(distance), 2.0f, height / 2 - i + this.height.getValue() * 10 + pig, HUDManager.INSTANCE.getColor(), true);
            pig += 10;
        }
        if (this.targetHoleCheck.getValue().booleanValue()) {
            EntityPlayer target = TargetUtil.getTarget(300.0);
            String string = ChatFormatting.GREEN + "Unsafe";
            if (target != null && EntityUtil.isSafe((Entity)target)) {
                string = ChatFormatting.RED + "Safe";
            }
            this.renderer.drawString(string, 2.0f, height / 2 - i + this.height.getValue() * 10 + pig, HUDManager.INSTANCE.getColor(), true);
            pig += 10;
        }
    }
}

