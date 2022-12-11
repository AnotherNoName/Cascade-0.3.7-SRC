//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.combat;

import cascade.Cascade;
import cascade.event.events.PacketEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.entity.EntityUtil;
import cascade.util.player.InventoryUtil;
import cascade.util.player.TargetUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Offhand
extends Module {
    Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.Crystal));
    Setting<Boolean> closeGUI = this.register(new Setting<Boolean>("CloseGUI", false));
    Setting<Float> totemHealth = this.register(new Setting<Object>("Health", Float.valueOf(17.5f), Float.valueOf(0.1f), Float.valueOf(36.0f), v -> this.mode.getValue() != Mode.Totem));
    Setting<Float> totemHoleHealth = this.register(new Setting<Object>("HoleHealth", Float.valueOf(8.0f), Float.valueOf(0.1f), Float.valueOf(36.0f), v -> this.mode.getValue() != Mode.Totem));
    Setting<Float> fallDistance = this.register(new Setting<Object>("FallDistance", Float.valueOf(40.0f), Float.valueOf(0.1f), Float.valueOf(90.0f), v -> this.mode.getValue() != Mode.Totem));
    Setting<Boolean> totemOnLag = this.register(new Setting<Object>("TotemOnLag", Boolean.valueOf(false), v -> this.mode.getValue() != Mode.Totem));
    Setting<Integer> lagTime = this.register(new Setting<Object>("LagTime", Integer.valueOf(1000), Integer.valueOf(500), Integer.valueOf(2000), v -> this.mode.getValue() != Mode.Totem && this.totemOnLag.getValue() != false));
    Setting<Boolean> totemElytra = this.register(new Setting<Object>("TotemElytra", Boolean.valueOf(true), v -> this.mode.getValue() != Mode.Totem));
    Setting<Boolean> gapSword = this.register(new Setting<Object>("GapSword", Boolean.valueOf(true), v -> this.mode.getValue() != Mode.Gapple));
    Setting<Boolean> gapPickaxe = this.register(new Setting<Object>("GapPickaxe", Boolean.valueOf(true), v -> this.mode.getValue() != Mode.Gapple));
    Setting<Boolean> noWaste = this.register(new Setting<Object>("NoWaste", Boolean.valueOf(true), v -> this.mode.getValue() != Mode.Gapple && (this.gapPickaxe.getValue() != false || this.gapSword.getValue() != false)));
    Setting<Boolean> forceGap = this.register(new Setting<Object>("ForceGap", Boolean.valueOf(true), v -> this.mode.getValue() != Mode.Gapple && (this.gapPickaxe.getValue() != false || this.gapSword.getValue() != false)));
    EntityPlayer nearestEnemy;

    public Offhand() {
        super("Offhand", Module.Category.COMBAT, "");
    }

    @Override
    public String getDisplayInfo() {
        return String.valueOf(this.getSize(this.mode.getValue() == Mode.Totem ? Items.field_190929_cY : (this.mode.getValue() == Mode.Crystal ? Items.END_CRYSTAL : Items.GOLDEN_APPLE)));
    }

    @Override
    public void onUpdate() {
        if (Offhand.fullNullCheck()) {
            return;
        }
        Item i = this.getItemType();
        if (Offhand.mc.player.getHeldItemOffhand().getItem() != i) {
            if (Offhand.mc.currentScreen instanceof GuiContainer && this.closeGUI.getValue().booleanValue()) {
                Offhand.mc.player.closeScreen();
            }
            if (Offhand.mc.currentScreen instanceof GuiContainer && !this.closeGUI.getValue().booleanValue() || Offhand.mc.currentScreen instanceof GuiInventory) {
                return;
            }
            int item = this.GetItemSlot(i);
            if (item != -1) {
                Offhand.mc.playerController.windowClick(Offhand.mc.player.inventoryContainer.windowId, item, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
                Offhand.mc.playerController.windowClick(Offhand.mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
                Offhand.mc.playerController.windowClick(Offhand.mc.player.inventoryContainer.windowId, item, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.player);
                Offhand.mc.playerController.updateController();
            }
        }
        this.nearestEnemy = TargetUtil.getTarget(32.0);
        if (this.nearestEnemy != null) {
            // empty if block
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        if (Offhand.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (this.noWaste.getValue().booleanValue() && this.mode.getValue() != Mode.Gapple && e.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && (this.gapSword.getValue().booleanValue() && InventoryUtil.heldItem(Items.DIAMOND_SWORD, InventoryUtil.Hand.Main) || this.gapPickaxe.getValue().booleanValue() && InventoryUtil.heldItem(Items.DIAMOND_PICKAXE, InventoryUtil.Hand.Main)) && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            CPacketPlayerTryUseItemOnBlock p = (CPacketPlayerTryUseItemOnBlock)e.getPacket();
            if (p.hand == EnumHand.OFF_HAND) {
                e.setCanceled(true);
            }
        }
    }

    Item getItemType() {
        switch (this.mode.getValue()) {
            case Totem: {
                if (this.shouldGapSword()) {
                    return Items.GOLDEN_APPLE;
                }
                if (this.shouldGapPickaxe()) {
                    return Items.GOLDEN_APPLE;
                }
                return Items.field_190929_cY;
            }
            case Crystal: {
                if (this.shouldSwitchHP() && this.forceGap.getValue().booleanValue() && !this.shouldGapPickaxe() && !this.shouldGapSword()) {
                    return Items.field_190929_cY;
                }
                if (this.shouldSwitchHoleHP() && this.forceGap.getValue().booleanValue() && !this.shouldGapPickaxe() && !this.shouldGapSword()) {
                    return Items.field_190929_cY;
                }
                if (this.shouldSwitchFall() && this.forceGap.getValue().booleanValue() && !this.shouldGapPickaxe() && !this.shouldGapSword()) {
                    return Items.field_190929_cY;
                }
                if (this.shouldSwitchLag()) {
                    return Items.field_190929_cY;
                }
                if (this.shouldSwitchElytra() && this.forceGap.getValue().booleanValue() && !this.shouldGapPickaxe() && !this.shouldGapSword()) {
                    return Items.field_190929_cY;
                }
                if (this.shouldGapSword()) {
                    return Items.GOLDEN_APPLE;
                }
                if (this.shouldGapPickaxe()) {
                    return Items.GOLDEN_APPLE;
                }
                return Items.END_CRYSTAL;
            }
            case Gapple: {
                if (this.shouldSwitchHP()) {
                    return Items.field_190929_cY;
                }
                if (this.shouldSwitchHoleHP()) {
                    return Items.field_190929_cY;
                }
                if (this.shouldSwitchFall()) {
                    return Items.field_190929_cY;
                }
                if (this.shouldSwitchLag()) {
                    return Items.field_190929_cY;
                }
                if (this.shouldSwitchElytra()) {
                    return Items.field_190929_cY;
                }
                return Items.GOLDEN_APPLE;
            }
        }
        return Items.field_190929_cY;
    }

    boolean shouldSwitchHP() {
        return EntityUtil.getHealth((Entity)Offhand.mc.player) < this.totemHealth.getValue().floatValue() && !EntityUtil.isPlayerSafe((EntityPlayer)Offhand.mc.player);
    }

    boolean shouldSwitchHoleHP() {
        return EntityUtil.getHealth((Entity)Offhand.mc.player) < this.totemHoleHealth.getValue().floatValue() && EntityUtil.isPlayerSafe((EntityPlayer)Offhand.mc.player) && Math.abs(Offhand.mc.player.motionY) > 0.01;
    }

    boolean shouldGapSword() {
        return this.gapSword.getValue() != false && InventoryUtil.heldItem(Items.DIAMOND_SWORD, InventoryUtil.Hand.Main) && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown();
    }

    boolean shouldGapPickaxe() {
        return this.gapPickaxe.getValue() != false && InventoryUtil.heldItem(Items.DIAMOND_PICKAXE, InventoryUtil.Hand.Main) && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown();
    }

    boolean shouldSwitchFall() {
        return Offhand.mc.player.motionY < 0.0 && Offhand.mc.player.fallDistance > this.fallDistance.getValue().floatValue() && !Offhand.mc.player.isElytraFlying();
    }

    boolean shouldSwitchLag() {
        return this.totemOnLag.getValue() != false && Cascade.serverManager.isServerNotResponding(this.lagTime.getValue());
    }

    boolean shouldSwitchElytra() {
        return this.totemElytra.getValue() != false && Offhand.mc.player.isElytraFlying();
    }

    boolean priorityCheck() {
        return false;
    }

    int GetItemSlot(Item item) {
        if (Offhand.mc.player == null) {
            return 0;
        }
        for (int i = 0; i < Offhand.mc.player.inventoryContainer.getInventory().size(); ++i) {
            ItemStack s;
            if (i == 0 || i == 5 || i == 6 || i == 7 || i == 8 || (s = (ItemStack)Offhand.mc.player.inventoryContainer.getInventory().get(i)).func_190926_b() || s.getItem() != item) continue;
            return i;
        }
        return -1;
    }

    int getSize(Item item) {
        int amt = 0;
        for (int i = 45; i < 0; ++i) {
            if (Offhand.mc.player.inventory.getStackInSlot(i).getItem() != item) continue;
            amt += Offhand.mc.player.inventory.getStackInSlot(i).func_190916_E();
        }
        return amt;
    }

    int getStackSize() {
        int amt = 0;
        switch (this.mode.getValue()) {
            case Totem: {
                for (int i = 45; i > 0; --i) {
                    if (Offhand.mc.player.inventory.getStackInSlot(i).getItem() != Items.field_190929_cY) continue;
                    amt += Offhand.mc.player.inventory.getStackInSlot(i).func_190916_E();
                }
                break;
            }
            case Crystal: {
                for (int i = 45; i > 0; --i) {
                    if (Offhand.mc.player.inventory.getStackInSlot(i).getItem() != Items.END_CRYSTAL) continue;
                    amt += Offhand.mc.player.inventory.getStackInSlot(i).func_190916_E();
                }
                break;
            }
            case Gapple: {
                for (int i = 45; i > 0; --i) {
                    if (Offhand.mc.player.inventory.getStackInSlot(i).getItem() != Items.GOLDEN_APPLE) continue;
                    amt += Offhand.mc.player.inventory.getStackInSlot(i).func_190916_E();
                }
                break;
            }
        }
        return amt;
    }

    static enum Priority {
        Gap,
        EGap;

    }

    static enum Mode {
        Totem,
        Crystal,
        Gapple;

    }
}

