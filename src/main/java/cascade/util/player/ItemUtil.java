//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package cascade.util.player;

import cascade.mixin.mixins.accessor.IPlayerControllerMP;
import cascade.util.Util;
import net.minecraft.block.Block;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class ItemUtil
implements Util {
    public static void silentSwap(int slot) {
        if (slot != -1) {
            mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(slot));
        }
    }

    public static void silentSwapRecover(int slot) {
        mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(slot));
        ItemUtil.mc.playerController.updateController();
    }

    public static void bypassSwap(int slot) {
        if (slot != -1) {
            ItemUtil.mc.playerController.pickItem(slot);
        }
    }

    public static void syncItem(boolean newMode) {
        if (newMode) {
            ItemUtil.mc.playerController.updateController();
        } else {
            ((IPlayerControllerMP)ItemUtil.mc.playerController).syncItem();
        }
    }

    public static void normalSwap(int slot) {
        if (slot != -1 && ItemUtil.mc.player.inventory.currentItem != slot) {
            ItemUtil.mc.player.inventory.currentItem = slot;
            ItemUtil.mc.playerController.updateController();
        }
    }

    public static int getItemFromHotbar(Item item) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = ItemUtil.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() != item) continue;
            slot = i;
        }
        return slot;
    }

    public static int getBlockFromHotbar(Block block) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (ItemUtil.mc.player.inventory.getStackInSlot(i).getItem() != Item.getItemFromBlock((Block)block)) continue;
            slot = i;
        }
        return slot;
    }

    public static boolean shouldAntiWeakness() {
        return ItemUtil.mc.player.isPotionActive(MobEffects.WEAKNESS) && !(ItemUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && !(ItemUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) && !(ItemUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe);
    }
}

