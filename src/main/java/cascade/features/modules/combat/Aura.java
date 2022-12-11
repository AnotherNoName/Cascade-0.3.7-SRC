//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumHand
 */
package cascade.features.modules.combat;

import cascade.features.command.Command;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.Timer;
import cascade.util.player.InventoryUtil;
import cascade.util.player.TargetUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

public class Aura
extends Module {
    Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(6.0f)));
    Setting<Float> wallRange = this.register(new Setting<Float>("WallRange", Float.valueOf(3.0f), Float.valueOf(0.1f), Float.valueOf(6.0f)));
    Setting<Boolean> swordOnly = this.register(new Setting<Boolean>("SwordOnly", true));
    Setting<Boolean> debug = this.register(new Setting<Boolean>("Debug", false));
    Timer timer = new Timer();
    EntityPlayer target;
    static Aura INSTANCE;

    public Aura() {
        super("Aura", Module.Category.COMBAT, "");
        INSTANCE = this;
    }

    public static Aura getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Aura();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (Aura.fullNullCheck()) {
            return;
        }
        if (this.swordOnly.getValue().booleanValue() && !this.holdingWeapon()) {
            return;
        }
        this.target = TargetUtil.getTarget(this.range.getValue().floatValue());
        if (this.target == null) {
            if (this.debug.getValue().booleanValue()) {
                Command.sendMessage("@Target is null");
            }
            return;
        }
        if (!Aura.mc.player.canEntityBeSeen((Entity)this.target) && Aura.mc.player.getDistanceToEntity((Entity)this.target) > this.wallRange.getValue().floatValue()) {
            if (this.debug.getValue().booleanValue()) {
                Command.sendMessage("@Target cant be seen");
            }
            return;
        }
        if (this.timer.passedMs(this.getCooldownByWeapon((EntityPlayer)Aura.mc.player))) {
            if (this.debug.getValue().booleanValue()) {
                Command.sendMessage("@Delay");
            }
            mc.getConnection().sendPacket((Packet)new CPacketUseEntity((Entity)this.target));
            mc.getConnection().sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            this.timer.reset();
        }
    }

    boolean holdingWeapon() {
        return InventoryUtil.heldItem(Items.DIAMOND_SWORD, InventoryUtil.Hand.Main) || InventoryUtil.heldItem(Items.DIAMOND_AXE, InventoryUtil.Hand.Main);
    }

    int getCooldownByWeapon(EntityPlayer player) {
        Item item = player.getHeldItemMainhand().getItem();
        if (item instanceof ItemSword) {
            return 600;
        }
        if (item == Items.DIAMOND_AXE) {
            return 1000;
        }
        return 250;
    }
}

