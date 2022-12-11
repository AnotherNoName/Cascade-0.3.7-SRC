//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityShulkerBox
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package cascade.util.player;

import cascade.util.Util;
import cascade.util.entity.EntityUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PlayerUtil
implements Util {
    static List<Block> pistons = Arrays.asList(Blocks.PISTON, Blocks.PISTON_EXTENSION, Blocks.PISTON_HEAD, Blocks.STICKY_PISTON);

    public static boolean isElytraEquipped() {
        return PlayerUtil.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA;
    }

    public static boolean isBoxColliding() {
        return PlayerUtil.mc.world.getCollisionBoxes((Entity)PlayerUtil.mc.player, PlayerUtil.mc.player.getEntityBoundingBox().offset(0.0, 0.21, 0.0)).size() > 0;
    }

    public static boolean isClipping() {
        return !PlayerUtil.mc.world.getCollisionBoxes((Entity)PlayerUtil.mc.player, PlayerUtil.mc.player.getEntityBoundingBox()).isEmpty();
    }

    public static boolean isOffset() {
        Vec3d center = EntityUtil.getCenter(PlayerUtil.mc.player.posX, PlayerUtil.mc.player.posY, PlayerUtil.mc.player.posZ);
        return PlayerUtil.mc.player.getDistance(center.xCoord, center.yCoord, center.zCoord) > 0.2;
    }

    public static boolean isPushable(double x, double y, double z) {
        Block temp = PlayerUtil.mc.world.getBlockState(new BlockPos(x, y += 1.0, z)).getBlock();
        if (temp == Blocks.PISTON_HEAD || temp == Blocks.PISTON_EXTENSION) {
            return true;
        }
        for (TileEntity entity : PlayerUtil.mc.world.loadedTileEntityList) {
            AxisAlignedBB axisAlignedBB;
            TileEntityShulkerBox tileEntityShulkerBox;
            if (!(entity instanceof TileEntityShulkerBox)) continue;
            TileEntityShulkerBox tempShulker = (TileEntityShulkerBox)entity;
            if (!(tileEntityShulkerBox.func_190585_a(mc.getRenderPartialTicks()) > 0.0f)) continue;
            AxisAlignedBB tempAxis = tempShulker.getRenderBoundingBox();
            if (!(axisAlignedBB.minY <= y && tempAxis.maxY >= y && (double)((int)tempAxis.minX) <= x && tempAxis.maxX >= x) && (!((double)((int)tempAxis.minZ) <= z) || !(tempAxis.maxZ >= z))) continue;
            return true;
        }
        return false;
    }

    public static boolean isPushable() {
        ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
        offsets.add(PlayerUtil.mc.player.getPositionVector().addVector(1.0, 1.0, 0.0));
        offsets.add(PlayerUtil.mc.player.getPositionVector().addVector(0.0, 1.0, 1.0));
        offsets.add(PlayerUtil.mc.player.getPositionVector().addVector(1.0, 1.0, 0.0));
        offsets.add(PlayerUtil.mc.player.getPositionVector().addVector(0.0, 1.0, -1.0));
        for (Vec3d vec3d : offsets) {
            if (PlayerUtil.mc.world.getBlockState(new BlockPos(vec3d)).getBlock() != pistons) continue;
            return true;
        }
        return false;
    }

    public static boolean isBurrow() {
        Block block = PlayerUtil.mc.world.getBlockState(new BlockPos(PlayerUtil.mc.player.getPositionVector().addVector(0.0, 0.2, 0.0))).getBlock();
        return block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST;
    }

    public static boolean isBurrowed() {
        Block block = PlayerUtil.mc.world.getBlockState(new BlockPos(PlayerUtil.mc.player.getPositionVector().addVector(0.0, 0.2, 0.0))).getBlock();
        return block != Blocks.AIR;
    }

    public static boolean isChestBelow() {
        return !PlayerUtil.isBurrow() && EntityUtil.isOnChest((Entity)PlayerUtil.mc.player);
    }

    public static boolean isInLiquid() {
        return PlayerUtil.mc.player.isInLava() || PlayerUtil.mc.player.isInWater();
    }

    public static boolean isInLiquidF() {
        if (PlayerUtil.mc.player.fallDistance >= 3.0f) {
            return false;
        }
        boolean inLiquid = false;
        AxisAlignedBB bb = PlayerUtil.mc.player.getRidingEntity() != null ? PlayerUtil.mc.player.getRidingEntity().getEntityBoundingBox() : PlayerUtil.mc.player.getEntityBoundingBox();
        int y = (int)bb.minY;
        for (int x = MathHelper.floor((double)bb.minX); x < MathHelper.floor((double)bb.maxX) + 1; ++x) {
            for (int z = MathHelper.floor((double)bb.minZ); z < MathHelper.floor((double)bb.maxZ) + 1; ++z) {
                Block block = PlayerUtil.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block instanceof BlockAir) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                inLiquid = true;
            }
        }
        return inLiquid;
    }

    public static boolean inLiquid() {
        return PlayerUtil.inLiquid(MathHelper.floor((double)(PlayerUtil.mc.player.getEntityBoundingBox().minY + 0.01)));
    }

    public static boolean inLiquid(boolean feet) {
        return PlayerUtil.inLiquid(MathHelper.floor((double)(PlayerUtil.mc.player.getEntityBoundingBox().minY - (feet ? 0.03 : 0.2))));
    }

    private static boolean inLiquid(int y) {
        return PlayerUtil.findState(BlockLiquid.class, y) != null;
    }

    private static IBlockState findState(Class<? extends Block> block, int y) {
        int startX = MathHelper.floor((double)PlayerUtil.mc.player.getEntityBoundingBox().minX);
        int startZ = MathHelper.floor((double)PlayerUtil.mc.player.getEntityBoundingBox().minZ);
        int endX = MathHelper.ceil((double)PlayerUtil.mc.player.getEntityBoundingBox().maxX);
        int endZ = MathHelper.ceil((double)PlayerUtil.mc.player.getEntityBoundingBox().maxZ);
        for (int x = startX; x < endX; ++x) {
            for (int z = startZ; z < endZ; ++z) {
                IBlockState s = PlayerUtil.mc.world.getBlockState(new BlockPos(x, y, z));
                if (!block.isInstance(s.getBlock())) continue;
                return s;
            }
        }
        return null;
    }

    public static boolean isAbove(BlockPos pos) {
        return PlayerUtil.mc.player.getEntityBoundingBox().minY >= (double)pos.getY();
    }

    public static boolean isMovementBlocked() {
        IBlockState state = PlayerUtil.findState(Block.class, MathHelper.floor((double)(PlayerUtil.mc.player.getEntityBoundingBox().minY - 0.01)));
        return state != null && state.getMaterial().blocksMovement();
    }

    public static boolean isAboveLiquid() {
        if (PlayerUtil.mc.player != null) {
            double n = PlayerUtil.mc.player.posY + 0.01;
            for (int i = MathHelper.floor((double)PlayerUtil.mc.player.posX); i < MathHelper.ceil((double)PlayerUtil.mc.player.posX); ++i) {
                for (int j = MathHelper.floor((double)PlayerUtil.mc.player.posZ); j < MathHelper.ceil((double)PlayerUtil.mc.player.posZ); ++j) {
                    if (!(EntityUtil.mc.world.getBlockState(new BlockPos(i, (int)n, j)).getBlock() instanceof BlockLiquid)) continue;
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean checkForLiquid(boolean b) {
        if (PlayerUtil.mc.player != null) {
            double posY = PlayerUtil.mc.player.posY;
            double n = b ? 0.03 : (PlayerUtil.mc.player instanceof EntityPlayer ? 0.2 : 0.5);
            double n2 = posY - n;
            for (int i = MathHelper.floor((double)PlayerUtil.mc.player.posX); i < MathHelper.ceil((double)PlayerUtil.mc.player.posX); ++i) {
                for (int j = MathHelper.floor((double)PlayerUtil.mc.player.posZ); j < MathHelper.ceil((double)PlayerUtil.mc.player.posZ); ++j) {
                    if (!(EntityUtil.mc.world.getBlockState(new BlockPos(i, MathHelper.floor((double)n2), j)).getBlock() instanceof BlockLiquid)) continue;
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isAboveBlock(BlockPos blockPos) {
        return PlayerUtil.mc.player.posY >= (double)blockPos.getY();
    }

    public static void startSneaking() {
        mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)PlayerUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
    }

    public static void stopSneaking() {
        mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)PlayerUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
    }
}

