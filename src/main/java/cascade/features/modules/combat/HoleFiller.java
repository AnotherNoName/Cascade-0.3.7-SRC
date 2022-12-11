//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package cascade.features.modules.combat;

import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.Timer;
import cascade.util.player.BlockUtil;
import cascade.util.player.InventoryUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class HoleFiller
extends Module {
    Setting<Integer> bpt = this.register(new Setting<Integer>("BPT", 8, 1, 25));
    Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 0, 0, 250));
    Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(6.0f)));
    Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", true));
    Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotation", false));
    Setting<Integer> toggle = this.register(new Setting<Integer>("AutoDisable", 10, 0, 250));
    Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    ArrayList<BlockPos> holes = new ArrayList();
    Timer retryTimer = new Timer();
    Timer offTimer = new Timer();
    Timer timer = new Timer();
    int placements = 0;
    int trie;

    public HoleFiller() {
        super("HoleFiller", Module.Category.COMBAT, "Fills holes around ur target");
    }

    @Override
    public void onEnable() {
        this.offTimer.reset();
        this.trie = 0;
    }

    @Override
    public void onDisable() {
        this.retries.clear();
    }

    @Override
    public void onUpdate() {
        if (!HoleFiller.fullNullCheck()) {
            mc.addScheduledTask(() -> this.doHoleFill());
        }
    }

    void doHoleFill() {
        if (this.check()) {
            return;
        }
        this.holes = new ArrayList();
        Iterable blocks = BlockPos.getAllInBox((BlockPos)HoleFiller.mc.player.getPosition().add((double)(-this.range.getValue().floatValue()), (double)(-this.range.getValue().floatValue()), (double)(-this.range.getValue().floatValue())), (BlockPos)HoleFiller.mc.player.getPosition().add((double)this.range.getValue().floatValue(), (double)this.range.getValue().floatValue(), (double)this.range.getValue().floatValue()));
        for (BlockPos pos : blocks) {
            boolean solidNeighbours;
            if (HoleFiller.mc.world.getBlockState(pos).getMaterial().blocksMovement() || HoleFiller.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial().blocksMovement()) continue;
            boolean bl = HoleFiller.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK | HoleFiller.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN && HoleFiller.mc.world.getBlockState(pos.add(0, 0, 0)).getMaterial() == Material.AIR && HoleFiller.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR && HoleFiller.mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR ? true : (solidNeighbours = false);
            if (!solidNeighbours) continue;
            this.holes.add(pos);
        }
        if (!this.holes.isEmpty()) {
            this.holes.forEach(this::placeBlock);
            if (this.toggle.getValue() != 0 && this.offTimer.passedMs(this.toggle.getValue().intValue())) {
                this.disable();
                return;
            }
        }
    }

    void placeBlock(BlockPos pos) {
        for (Entity entity : HoleFiller.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityLivingBase)) continue;
            return;
        }
        if (this.placements < this.bpt.getValue()) {
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.disable();
                return;
            }
            int originalSlot = HoleFiller.mc.player.inventory.currentItem;
            InventoryUtil.packetSwap(obbySlot != -1 ? obbySlot : eChestSot);
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), HoleFiller.mc.player.isSneaking(), true);
            InventoryUtil.packetSwap(originalSlot);
            this.timer.reset();
            ++this.placements;
        }
    }

    boolean check() {
        if (HoleFiller.fullNullCheck()) {
            return true;
        }
        this.placements = 0;
        if (this.retryTimer.passedMs(250L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        return !this.timer.passedMs((long)((double)this.delay.getValue().intValue() * 10.0));
    }
}

