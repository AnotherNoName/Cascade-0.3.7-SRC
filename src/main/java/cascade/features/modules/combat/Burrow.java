//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package cascade.features.modules.combat;

import cascade.Cascade;
import cascade.features.modules.Module;
import cascade.features.modules.player.Freecam;
import cascade.features.setting.Setting;
import cascade.util.player.AttackUtil;
import cascade.util.player.BlockUtil;
import cascade.util.player.InventoryUtil;
import cascade.util.player.ItemUtil;
import cascade.util.player.PlayerUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Burrow
extends Module {
    Setting<Boolean> breakCrystals = this.register(new Setting<Boolean>("BreakCrystals", false));
    Setting<Boolean> cooldown = this.register(new Setting<Object>("Cooldown", Boolean.valueOf(true), v -> this.breakCrystals.getValue()));
    Setting<Boolean> antiWeakness = this.register(new Setting<Object>("AntiWeakness", Boolean.valueOf(false), v -> this.breakCrystals.getValue()));
    Setting<Boolean> bypass = this.register(new Setting<Boolean>("Bypass", false));
    Setting<Block> prefer = this.register(new Setting<Block>("Prefer", Block.EChest));
    Setting<Double> offset = this.register(new Setting<Double>("Offset", 3.0, -9.0, 9.0));
    double[] offsets = new double[]{0.41999998688698, 0.7531999805212, 1.00133597911214, 1.16610926093821};
    BlockPos startPos = null;

    public Burrow() {
        super("Burrow", Module.Category.COMBAT, "");
    }

    @Override
    public void onDisable() {
        this.startPos = null;
    }

    @Override
    public void onEnable() {
        this.startPos = Burrow.mc.player.getPosition();
    }

    @Override
    public void onUpdate() {
        if (Burrow.fullNullCheck()) {
            return;
        }
        int oglSlot = Burrow.mc.player.inventory.currentItem;
        int ecSlot = InventoryUtil.getItemFromHotbar(Item.getItemFromBlock((net.minecraft.block.Block)Blocks.ENDER_CHEST));
        int obbySlot = InventoryUtil.getItemFromHotbar(Item.getItemFromBlock((net.minecraft.block.Block)Blocks.OBSIDIAN));
        if (ecSlot == -1 && obbySlot == -1) {
            this.disable();
            return;
        }
        if (!this.canPlaceBlock(this.startPos)) {
            return;
        }
        if (this.prefer.getValue() == Block.EChest) {
            if (ecSlot != -1) {
                ItemUtil.silentSwap(ecSlot);
            } else if (obbySlot != -1) {
                ItemUtil.silentSwap(obbySlot);
            }
        }
        if (this.prefer.getValue() == Block.Obsidian) {
            if (Burrow.mc.world.getBlockState(this.startPos.down()).getBlock() == Blocks.ENDER_CHEST) {
                InventoryUtil.packetSwap(ecSlot);
            } else if (obbySlot != -1) {
                ItemUtil.silentSwap(obbySlot);
            } else if (ecSlot != -1) {
                InventoryUtil.packetSwap(ecSlot);
            }
        }
        PlayerUtil.startSneaking();
        for (double pos : this.offsets) {
            mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX + (this.bypass.getValue() != false ? pos : 0.0), Burrow.mc.player.posY + pos, Burrow.mc.player.posZ + (this.bypass.getValue() != false ? pos : 0.0), true));
        }
        BlockUtil.placeBlock(this.startPos, true, false);
        ItemUtil.silentSwapRecover(oglSlot);
        this.doLagBack();
        this.disable();
    }

    void doLagBack() {
        int y = 2;
        int offset = 2;
        while ((double)y < (double)Burrow.mc.world.getHeight() - Burrow.mc.player.posY) {
            IBlockState scanState2;
            IBlockState scanState1 = Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ).up(y));
            if (scanState1.getBlock() == Blocks.AIR && (scanState2 = Burrow.mc.world.getBlockState(new BlockPos(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ).up(y + 1))).getBlock() == Blocks.AIR) {
                offset = y;
                break;
            }
            ++y;
        }
        mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(Burrow.mc.player.posX + (this.bypass.getValue() != false ? 1.16610926093821 : 0.0), Burrow.mc.player.posY + (this.bypass.getValue() != false ? 1.16610926093821 : (double)offset), Burrow.mc.player.posZ + (this.bypass.getValue() != false ? 1.16610926093821 : 0.0), false));
        PlayerUtil.stopSneaking();
    }

    boolean canPlaceBlock(BlockPos pos) {
        if (!Burrow.mc.world.getBlockState(pos).getMaterial().isReplaceable() || AttackUtil.isInterceptedByOther(pos) || this.startPos.getY() > 255 || !Burrow.mc.player.onGround || PlayerUtil.isClipping() || Freecam.getInstance().isEnabled() || Burrow.mc.player.isInWeb || PlayerUtil.isInLiquid() || Burrow.mc.world.checkBlockCollision(Burrow.mc.player.boundingBox.addCoord(0.0, 1.0, 0.0))) {
            return false;
        }
        if (AttackUtil.isInterceptedByCrystal(this.startPos)) {
            if (!this.breakCrystals.getValue().booleanValue()) {
                return false;
            }
            EntityEnderCrystal crystal = null;
            for (Entity entity : Burrow.mc.world.loadedEntityList) {
                if (entity == null || (double)Burrow.mc.player.getDistanceToEntity(entity) > 1.75 || !(entity instanceof EntityEnderCrystal) || entity.isDead) continue;
                crystal = (EntityEnderCrystal)entity;
            }
            if (crystal != null && (!this.cooldown.getValue().booleanValue() || this.cooldown.getValue().booleanValue() && !Cascade.swapManager.hasSwapped())) {
                int awSlot;
                int previousSlot = Burrow.mc.player.inventory.currentItem;
                boolean awSwitched = false;
                if (this.antiWeakness.getValue().booleanValue() && ItemUtil.shouldAntiWeakness() && (awSlot = this.getAwSlot()) != -1) {
                    ItemUtil.silentSwap(awSlot);
                    awSwitched = true;
                }
                mc.getConnection().sendPacket((Packet)new CPacketUseEntity((Entity)crystal));
                mc.getConnection().sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                if (this.antiWeakness.getValue().booleanValue() && ItemUtil.shouldAntiWeakness() && awSwitched) {
                    ItemUtil.silentSwapRecover(previousSlot);
                }
                return true;
            }
        }
        return false;
    }

    int getAwSlot() {
        int slot = this.getHotbarItemSlot(Items.DIAMOND_SWORD);
        if (slot == -1) {
            slot = this.getHotbarItemSlot(Items.DIAMOND_PICKAXE);
        }
        if (slot == -1) {
            slot = this.getHotbarItemSlot(Items.DIAMOND_AXE);
        }
        return slot;
    }

    int getHotbarItemSlot(Item item) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (!Burrow.mc.player.inventory.getStackInSlot(i).getItem().equals(item)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    static enum Block {
        EChest,
        Obsidian;

    }
}

