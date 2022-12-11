//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package cascade.features.modules.combat;

import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.player.AttackUtil;
import cascade.util.player.BlockUtil;
import cascade.util.player.ItemUtil;
import cascade.util.player.TargetUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class HoleFillR
extends Module {
    Setting<Float> placeRange = this.register(new Setting<Float>("PlaceRange", Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(6.0f)));
    Setting<Float> selfRange = this.register(new Setting<Float>("SelfRange", Float.valueOf(2.0f), Float.valueOf(0.1f), Float.valueOf(3.0f)));
    Setting<Float> targetFillRange = this.register(new Setting<Float>("TargetFillRange", Float.valueOf(3.0f), Float.valueOf(1.0f), Float.valueOf(6.0f)));
    Setting<Boolean> breakCrystals = this.register(new Setting<Boolean>("BreakCrystals", false));
    Setting<Float> breakRange = this.register(new Setting<Object>("BreakRange", Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), v -> this.breakCrystals.getValue()));
    Setting<Float> wallRange = this.register(new Setting<Object>("WallRange", Float.valueOf(3.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), v -> this.breakCrystals.getValue()));
    BlockPos[] surroundOffset = BlockUtil.toBlockPos(new Vec3d[]{new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, -1.0, 0.0)});
    EntityPlayer target;
    int placedBlocks;

    public HoleFillR() {
        super("HoleFill", Module.Category.COMBAT, "");
    }

    @Override
    public void onToggle() {
        if (HoleFillR.fullNullCheck()) {
            return;
        }
        this.placedBlocks = 0;
        this.target = null;
    }

    @Override
    public void onUpdate() {
        if (HoleFillR.fullNullCheck()) {
            return;
        }
        this.target = TargetUtil.getTarget(7.0);
        if (this.target == null) {
            return;
        }
        List<BlockPos> holes = this.calcHoles();
        if (holes.size() == 0) {
            return;
        }
        this.placedBlocks = 0;
        for (BlockPos pos : holes) {
            if (!this.canPlaceBlock(pos) || this.placedBlocks > 20) continue;
            this.placeBlock(pos);
            ++this.placedBlocks;
        }
    }

    void placeBlock(BlockPos pos) {
        int oldSlot = HoleFillR.mc.player.inventory.currentItem;
        int obbySlot = ItemUtil.getBlockFromHotbar(Blocks.OBSIDIAN);
        if (obbySlot == -1) {
            this.disable();
            return;
        }
        ItemUtil.silentSwap(obbySlot);
        BlockUtil.placeBlock5(pos);
        ItemUtil.silentSwapRecover(oldSlot);
    }

    boolean canPlaceBlock(BlockPos pos) {
        boolean allow = true;
        if (!HoleFillR.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            allow = false;
        }
        if (AttackUtil.isInterceptedByOther(pos)) {
            allow = false;
        }
        if (AttackUtil.isInterceptedByCrystal(pos)) {
            if (!this.breakCrystals.getValue().booleanValue()) {
                allow = false;
            }
            EntityEnderCrystal c = null;
            for (Entity en : HoleFillR.mc.world.loadedEntityList) {
                if (en == null || !(en instanceof EntityEnderCrystal) || en.isDead || HoleFillR.mc.player.getDistanceToEntity(en) > this.breakRange.getValue().floatValue() || !HoleFillR.mc.player.canEntityBeSeen(en) && HoleFillR.mc.player.getDistanceToEntity(en) > this.wallRange.getValue().floatValue()) continue;
                c = (EntityEnderCrystal)en;
            }
            if (c != null) {
                mc.getConnection().sendPacket((Packet)new CPacketUseEntity(c));
                mc.getConnection().sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                allow = true;
            }
        }
        return allow;
    }

    List<BlockPos> calcHoles() {
        ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        List<BlockPos> sphere = this.getSphere(this.placeRange.getValue().floatValue(), false);
        int size = sphere.size();
        for (int i = 0; i < size; ++i) {
            BlockPos pos = sphere.get(i);
            if (!HoleFillR.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) || !HoleFillR.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) || !HoleFillR.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) continue;
            boolean isSafe = true;
            for (BlockPos offset : this.surroundOffset) {
                Block b = HoleFillR.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
                if (b == Blocks.BEDROCK || b == Blocks.OBSIDIAN || b == Blocks.ENDER_CHEST) continue;
                isSafe = false;
            }
            if (!isSafe || this.target == null || !(this.target.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getX()) <= (double)this.targetFillRange.getValue().floatValue()) || !(HoleFillR.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) > (double)this.selfRange.getValue().floatValue())) continue;
            safeSpots.add(pos);
        }
        return safeSpots;
    }

    List<BlockPos> getSphere(float radius, boolean ignoreAir) {
        ArrayList<BlockPos> sphere = new ArrayList<BlockPos>();
        BlockPos pos = new BlockPos(HoleFillR.mc.player.getPositionVector());
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        int radiuss = (int)radius;
        int x = posX - radiuss;
        while ((float)x <= (float)posX + radius) {
            int z = posZ - radiuss;
            while ((float)z <= (float)posZ + radius) {
                int y = posY - radiuss;
                while ((float)y < (float)posY + radius) {
                    if ((float)((posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y)) < radius * radius) {
                        BlockPos position = new BlockPos(x, y, z);
                        if (!ignoreAir || HoleFillR.mc.world.getBlockState(position).getBlock() != Blocks.AIR) {
                            sphere.add(position);
                        }
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return sphere;
    }
}

