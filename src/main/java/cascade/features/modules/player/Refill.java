//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 */
package cascade.features.modules.player;

import cascade.features.command.Command;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.Timer;
import java.util.ArrayList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Refill
extends Module {
    Setting<Integer> threshold = this.register(new Setting<Integer>("Threshold", 10, 0, 64));
    Setting<Double> delay = this.register(new Setting<Double>("Delay", 0.0, 0.0, 1.0));
    Setting<Boolean> debug = this.register(new Setting<Boolean>("Debug", false));
    ArrayList<Item> hotbar = new ArrayList();
    Timer timer = new Timer();

    public Refill() {
        super("Refill", Module.Category.PLAYER, "");
    }

    @Override
    public void onEnable() {
        if (Refill.fullNullCheck()) {
            return;
        }
        this.hotbar.clear();
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = Refill.mc.player.inventory.getStackInSlot(i);
            if (!stack.func_190926_b() && !this.hotbar.contains(stack.getItem())) {
                this.hotbar.add(stack.getItem());
                continue;
            }
            this.hotbar.add(Items.field_190931_a);
        }
    }

    @Override
    public void onUpdate() {
        if (Refill.mc.currentScreen != null || Refill.fullNullCheck()) {
            return;
        }
        if (!this.timer.passedMs((long)(this.delay.getValue() * 1000.0))) {
            return;
        }
        if (!Refill.mc.world.getEntitiesWithinAABB(EntityItem.class, Refill.mc.player.getEntityBoundingBox()).isEmpty()) {
            if (this.debug.getValue().booleanValue()) {
                Command.sendMessage("@BB=true");
            }
            return;
        }
        for (int i = 0; i < 9; ++i) {
            if (!this.doRefill(i)) continue;
            if (this.debug.getValue().booleanValue()) {
                Command.sendMessage("@Refilling");
            }
            this.timer.reset();
            return;
        }
    }

    boolean doRefill(int slot) {
        ItemStack stack = Refill.mc.player.inventory.getStackInSlot(slot);
        if (stack.func_190926_b() || stack.getItem() == Items.field_190931_a) {
            return false;
        }
        if (!stack.isStackable()) {
            return false;
        }
        if (stack.func_190916_E() >= stack.getMaxStackSize()) {
            return false;
        }
        if ((stack.getItem().equals(Items.GOLDEN_APPLE) || stack.getItem().equals(Items.EXPERIENCE_BOTTLE)) && stack.func_190916_E() >= this.threshold.getValue()) {
            return false;
        }
        for (int i = 9; i < 36; ++i) {
            ItemStack item = Refill.mc.player.inventory.getStackInSlot(i);
            if (item.func_190926_b() || !this.CanItemBeMergedWith(stack, item)) continue;
            Refill.mc.playerController.windowClick(Refill.mc.player.inventoryContainer.windowId, i, 0, ClickType.QUICK_MOVE, (EntityPlayer)Refill.mc.player);
            Refill.mc.playerController.updateController();
            return true;
        }
        return false;
    }

    boolean CanItemBeMergedWith(ItemStack source, ItemStack target) {
        return source.getItem() == target.getItem() && target.getDisplayName().equals(target.getDisplayName());
    }
}

