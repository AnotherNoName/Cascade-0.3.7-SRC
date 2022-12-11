//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package cascade.util.player;

import cascade.util.Util;
import cascade.util.misc.MathUtil;
import cascade.util.player.BlockUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class CrystalUtil
implements Util {
    public static boolean isBreakValid(Entity en, float range, float wallRange) {
        if (!(en instanceof EntityEnderCrystal)) {
            return false;
        }
        if (en.isDead) {
            return false;
        }
        if (en.getDistanceToEntity((Entity)CrystalUtil.mc.player) > range) {
            return false;
        }
        return CrystalUtil.mc.player.canEntityBeSeen(en) || !(en.getDistanceToEntity((Entity)CrystalUtil.mc.player) > wallRange);
    }

    public static boolean isCrystalBreakValid(Entity en, BlockPos pos, float range, float wallRange) {
        if (!(en instanceof EntityEnderCrystal) || en.isDead) {
            return false;
        }
        if (!en.getEntityBoundingBox().intersectsWith(new AxisAlignedBB(pos))) {
            return false;
        }
        if (en.getDistanceToEntity((Entity)CrystalUtil.mc.player) > range) {
            return false;
        }
        return CrystalUtil.mc.player.canEntityBeSeen(en) || !(en.getDistanceToEntity((Entity)CrystalUtil.mc.player) > wallRange);
    }

    public static List<BlockPos> getSphere(float radius, boolean ignoreAir) {
        ArrayList<BlockPos> sphere = new ArrayList<BlockPos>();
        BlockPos pos = new BlockPos(CrystalUtil.mc.player.getPositionVector());
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
                        if (!ignoreAir || CrystalUtil.mc.world.getBlockState(position).getBlock() != Blocks.AIR) {
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

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean holePlacement, double wallRange) {
        if (CrystalUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && CrystalUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
            return false;
        }
        if (!BlockUtil.rayTracePlaceCheck(blockPos) && CrystalUtil.mc.player.getDistanceSq(blockPos) > MathUtil.square(wallRange)) {
            return false;
        }
        BlockPos boost = blockPos.add(0, 1, 0);
        return CrystalUtil.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && CrystalUtil.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() == Blocks.AIR && CrystalUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double)boost.getX(), (double)boost.getY(), (double)boost.getZ(), (double)(boost.getX() + 1), (double)(boost.getY() + (holePlacement ? 2 : 1)), (double)(boost.getZ() + 1)), e -> !(e instanceof EntityEnderCrystal)).size() == 0;
    }

    public static boolean isArmorLow(EntityPlayer player, int durability) {
        for (int i = 0; i < 4; ++i) {
            if (!(CrystalUtil.getDamageInPercent((ItemStack)player.inventory.armorInventory.get(i)) < (float)durability)) continue;
            return true;
        }
        return false;
    }

    public static float getDamageInPercent(ItemStack stack) {
        float green = ((float)stack.getMaxDamage() - (float)stack.getItemDamage()) / (float)stack.getMaxDamage();
        float red = 1.0f - green;
        return 100 - (int)(red * 100.0f);
    }

    public static float getHealth(EntityLivingBase player) {
        return player.getHealth() + player.getAbsorptionAmount();
    }

    public static float getDamageMultiplied(float damage) {
        int diff = CrystalUtil.mc.world.getDifficulty().getDifficultyId();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double)doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = CrystalUtil.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception exception) {
            // empty catch block
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * (double)doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            try {
                finald = CrystalUtil.getBlastReduction((EntityLivingBase)entity, CrystalUtil.getDamageMultiplied(damage), new Explosion((World)CrystalUtil.mc.world, null, posX, posY, posZ, 6.0f, false, true));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return (float)finald;
    }

    static float getBlockDensity(Vec3d vec, AxisAlignedBB bb) {
        double d0 = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
        double d2 = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
        double d3 = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
        double d4 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        double d5 = (1.0 - Math.floor(1.0 / d3) * d3) / 2.0;
        if (d0 >= 0.0 && d2 >= 0.0 && d3 >= 0.0) {
            int j2 = 0;
            int k2 = 0;
            for (float f = 0.0f; f <= 1.0f; f += (float)d0) {
                for (float f2 = 0.0f; f2 <= 1.0f; f2 += (float)d2) {
                    for (float f3 = 0.0f; f3 <= 1.0f; f3 += (float)d3) {
                        double d6 = bb.minX + (bb.maxX - bb.minX) * (double)f;
                        double d7 = bb.minY + (bb.maxY - bb.minY) * (double)f2;
                        double d8 = bb.minZ + (bb.maxZ - bb.minZ) * (double)f3;
                        if (CrystalUtil.rayTraceBlocks(new Vec3d(d6 + d4, d7, d8 + d5), vec, false, false, false, true) == null) {
                            ++j2;
                        }
                        ++k2;
                    }
                }
            }
            return (float)j2 / (float)k2;
        }
        return 0.0f;
    }

    static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            DamageSource ds = DamageSource.causeExplosionDamage((Explosion)explosion);
            damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)ep.getTotalArmorValue(), (float)((float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage((Iterable)ep.getArmorInventoryList(), (DamageSource)ds);
            }
            catch (Exception exception) {
                // empty catch block
            }
            float f = MathHelper.clamp((float)k, (float)0.0f, (float)20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)entity.getTotalArmorValue(), (float)((float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
        return damage;
    }

    static RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreNoBox, boolean returnLastUncollidableBlock, boolean ignoreWebs) {
        if (Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord)) {
            return null;
        }
        if (!(Double.isNaN(vec32.xCoord) || Double.isNaN(vec32.yCoord) || Double.isNaN(vec32.zCoord))) {
            RayTraceResult raytraceresult;
            int x1 = MathHelper.floor((double)vec31.xCoord);
            int y1 = MathHelper.floor((double)vec31.yCoord);
            int z1 = MathHelper.floor((double)vec31.zCoord);
            int x2 = MathHelper.floor((double)vec32.xCoord);
            int y2 = MathHelper.floor((double)vec32.yCoord);
            int z2 = MathHelper.floor((double)vec32.zCoord);
            BlockPos pos = new BlockPos(x1, y1, z1);
            IBlockState state = CrystalUtil.mc.world.getBlockState(pos);
            Block block = state.getBlock();
            if (!(ignoreNoBox && state.getCollisionBoundingBox((IBlockAccess)CrystalUtil.mc.world, pos) == Block.NULL_AABB || !block.canCollideCheck(state, stopOnLiquid) || ignoreWebs && block instanceof BlockWeb || (raytraceresult = state.collisionRayTrace((World)CrystalUtil.mc.world, pos, vec31, vec32)) == null)) {
                return raytraceresult;
            }
            RayTraceResult raytraceresult2 = null;
            int k1 = 200;
            while (k1-- >= 0) {
                EnumFacing enumfacing;
                if (Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord)) {
                    return null;
                }
                if (x1 == x2 && y1 == y2 && z1 == z2) {
                    return returnLastUncollidableBlock ? raytraceresult2 : null;
                }
                boolean flag2 = true;
                boolean flag3 = true;
                boolean flag4 = true;
                double d0 = 999.0;
                double d2 = 999.0;
                double d3 = 999.0;
                if (x2 > x1) {
                    d0 = (double)x1 + 1.0;
                } else if (x2 < x1) {
                    d0 = (double)x1 + 0.0;
                } else {
                    flag2 = false;
                }
                if (y2 > y1) {
                    d2 = (double)y1 + 1.0;
                } else if (y2 < y1) {
                    d2 = (double)y1 + 0.0;
                } else {
                    flag3 = false;
                }
                if (z2 > z1) {
                    d3 = (double)z1 + 1.0;
                } else if (z2 < z1) {
                    d3 = (double)z1 + 0.0;
                } else {
                    flag4 = false;
                }
                double d4 = 999.0;
                double d5 = 999.0;
                double d6 = 999.0;
                double d7 = vec32.xCoord - vec31.xCoord;
                double d8 = vec32.yCoord - vec31.yCoord;
                double d9 = vec32.zCoord - vec31.zCoord;
                if (flag2) {
                    d4 = (d0 - vec31.xCoord) / d7;
                }
                if (flag3) {
                    d5 = (d2 - vec31.yCoord) / d8;
                }
                if (flag4) {
                    d6 = (d3 - vec31.zCoord) / d9;
                }
                if (d4 == -0.0) {
                    d4 = -1.0E-4;
                }
                if (d5 == -0.0) {
                    d5 = -1.0E-4;
                }
                if (d6 == -0.0) {
                    d6 = -1.0E-4;
                }
                if (d4 < d5 && d4 < d6) {
                    enumfacing = x2 > x1 ? EnumFacing.WEST : EnumFacing.EAST;
                    vec31 = new Vec3d(d0, vec31.yCoord + d8 * d4, vec31.zCoord + d9 * d4);
                } else if (d5 < d6) {
                    enumfacing = y2 > y1 ? EnumFacing.DOWN : EnumFacing.UP;
                    vec31 = new Vec3d(vec31.xCoord + d7 * d5, d2, vec31.zCoord + d9 * d5);
                } else {
                    enumfacing = z2 > z1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                    vec31 = new Vec3d(vec31.xCoord + d7 * d6, vec31.yCoord + d8 * d6, d3);
                }
                x1 = MathHelper.floor((double)vec31.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                y1 = MathHelper.floor((double)vec31.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
                z1 = MathHelper.floor((double)vec31.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                pos = new BlockPos(x1, y1, z1);
                IBlockState state2 = CrystalUtil.mc.world.getBlockState(pos);
                Block block2 = state2.getBlock();
                if (ignoreNoBox && state2.getMaterial() != Material.PORTAL && state2.getCollisionBoundingBox((IBlockAccess)CrystalUtil.mc.world, pos) == Block.NULL_AABB) continue;
                if (!(!block2.canCollideCheck(state2, stopOnLiquid) || ignoreWebs && block2 instanceof BlockWeb)) {
                    RayTraceResult raytraceresult3 = state2.collisionRayTrace((World)CrystalUtil.mc.world, pos, vec31, vec32);
                    if (raytraceresult3 == null) continue;
                    return raytraceresult3;
                }
                raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, pos);
            }
            return returnLastUncollidableBlock ? raytraceresult2 : null;
        }
        return null;
    }

    boolean doSmartRaytrace(Vec3d startPos, AxisAlignedBB endBB, Vec3d playerPos, double wallRange, int hitCount) {
        double centerZ;
        double centerY;
        double centerX;
        Vec3d vec3d;
        boolean allow = false;
        int hits = 0;
        for (Vec3d pos : this.getSmartRaytraceVertex(endBB)) {
            RayTraceResult result = CrystalUtil.mc.world.rayTraceBlocks(startPos, pos);
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) continue;
            ++hits;
        }
        if (hits >= hitCount) {
            allow = true;
        }
        if (!allow && playerPos.distanceTo(vec3d = new Vec3d(endBB.minX + (centerX = (endBB.maxX - endBB.minX) / 2.0), endBB.minY + (centerY = (endBB.maxY - endBB.minY) / 2.0), endBB.minZ + (centerZ = (endBB.maxZ - endBB.minZ) / 2.0))) <= wallRange) {
            allow = true;
        }
        return allow;
    }

    Vec3d[] getSmartRaytraceVertex(AxisAlignedBB boundingBox) {
        double centerX = (boundingBox.maxX - boundingBox.minX) / 2.0;
        double centerY = (boundingBox.maxY - boundingBox.minY) / 2.0;
        double centerZ = (boundingBox.maxZ - boundingBox.minZ) / 2.0;
        return new Vec3d[]{new Vec3d(boundingBox.minX + centerX, boundingBox.minY + centerY, boundingBox.minZ + centerZ), new Vec3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ), new Vec3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ), new Vec3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ), new Vec3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ), new Vec3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ), new Vec3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ), new Vec3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ), new Vec3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ)};
    }
}

