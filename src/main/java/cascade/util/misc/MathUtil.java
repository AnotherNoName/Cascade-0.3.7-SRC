//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package cascade.util.misc;

import cascade.util.Util;
import cascade.util.entity.EntityUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MathUtil
implements Util {
    private static final Random random = new Random();

    public static Vec3d getInterpolatedRenderPos(Entity entity, float ticks) {
        return MathUtil.interpolateEntity(entity, ticks).subtract(Minecraft.getMinecraft().getRenderManager().renderPosX, Minecraft.getMinecraft().getRenderManager().renderPosY, Minecraft.getMinecraft().getRenderManager().renderPosZ);
    }

    public static Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)time);
    }

    public static int getRandom(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    public static double getRandom(double min, double max) {
        return MathHelper.clamp((double)(min + random.nextDouble() * max), (double)min, (double)max);
    }

    public static float getRandom(float min, float max) {
        return MathHelper.clamp((float)(min + random.nextFloat() * max), (float)min, (float)max);
    }

    public static int clamp(int num, int min, int max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static double clamp(double num, double min, double max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float sin(float value) {
        return MathHelper.sin((float)value);
    }

    public static float cos(float value) {
        return MathHelper.cos((float)value);
    }

    public static float wrapDegrees(float value) {
        return MathHelper.wrapDegrees((float)value);
    }

    public static double wrapDegrees(double value) {
        return MathHelper.wrapDegrees((double)value);
    }

    public static Vec3d roundVec(Vec3d vec3d, int places) {
        return new Vec3d(MathUtil.round(vec3d.xCoord, places), MathUtil.round(vec3d.yCoord, places), MathUtil.round(vec3d.zCoord, places));
    }

    public static double square(double input) {
        return input * input;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.doubleValue();
    }

    public static float wrap(float valI) {
        float val = valI % 360.0f;
        if (val >= 180.0f) {
            val -= 360.0f;
        }
        if (val < -180.0f) {
            val += 360.0f;
        }
        return val;
    }

    public static Vec3d direction(float yaw) {
        return new Vec3d(Math.cos(MathUtil.degToRad(yaw + 90.0f)), 0.0, Math.sin(MathUtil.degToRad(yaw + 90.0f)));
    }

    public static float round(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.floatValue();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean descending) {
        LinkedList<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        if (descending) {
            list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        } else {
            list.sort(Map.Entry.comparingByValue());
        }
        LinkedHashMap result = new LinkedHashMap();
        for (Map.Entry entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static String getTimeOfDay() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(11);
        if (timeOfDay < 12) {
            return "Good Morning ";
        }
        if (timeOfDay < 16) {
            return "Good Afternoon ";
        }
        if (timeOfDay < 21) {
            return "Good Evening ";
        }
        return "Good Night ";
    }

    public static double radToDeg(double rad) {
        return rad * (double)57.29578f;
    }

    public static double degToRad(double deg) {
        return deg * 0.01745329238474369;
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round(val * one) / one;
    }

    public static double[] directionSpeed(double speed) {
        float forward = MathUtil.mc.player.movementInput.field_192832_b;
        float side = MathUtil.mc.player.movementInput.moveStrafe;
        float yaw = MathUtil.mc.player.prevRotationYaw + (MathUtil.mc.player.rotationYaw - MathUtil.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static List<Vec3d> getBlockBlocks(Entity entity) {
        ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        double y = entity.posY;
        double minX = MathUtil.round(bb.minX, 0);
        double minZ = MathUtil.round(bb.minZ, 0);
        double maxX = MathUtil.round(bb.maxX, 0);
        double maxZ = MathUtil.round(bb.maxZ, 0);
        if (minX != maxX) {
            vec3ds.add(new Vec3d(minX, y, minZ));
            vec3ds.add(new Vec3d(maxX, y, minZ));
            if (minZ != maxZ) {
                vec3ds.add(new Vec3d(minX, y, maxZ));
                vec3ds.add(new Vec3d(maxX, y, maxZ));
                return vec3ds;
            }
        } else if (minZ != maxZ) {
            vec3ds.add(new Vec3d(minX, y, minZ));
            vec3ds.add(new Vec3d(minX, y, maxZ));
            return vec3ds;
        }
        vec3ds.add(entity.getPositionVector());
        return vec3ds;
    }

    public static boolean areVec3dsAligned(Vec3d vec3d1, Vec3d vec3d2) {
        return MathUtil.areVec3dsAlignedRetarded(vec3d1, vec3d2);
    }

    public static boolean areVec3dsAlignedRetarded(Vec3d vec3d1, Vec3d vec3d2) {
        BlockPos pos1 = new BlockPos(vec3d1);
        BlockPos pos2 = new BlockPos(vec3d2.xCoord, vec3d1.yCoord, vec3d2.zCoord);
        return pos1.equals((Object)pos2);
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.xCoord - from.xCoord;
        double difY = (to.yCoord - from.yCoord) * -1.0;
        double difZ = to.zCoord - from.zCoord;
        double dist = MathHelper.sqrt((double)(difX * difX + difZ * difZ));
        return new float[]{(float)MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)), (float)MathHelper.wrapDegrees((double)Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreNoBox, boolean returnLastUncollidableBlock, boolean ignoreWebs) {
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
            IBlockState state = MathUtil.mc.world.getBlockState(pos);
            Block block = state.getBlock();
            if (!(ignoreNoBox && state.getCollisionBoundingBox((IBlockAccess)MathUtil.mc.world, pos) == Block.NULL_AABB || !block.canCollideCheck(state, stopOnLiquid) || ignoreWebs && block instanceof BlockWeb || (raytraceresult = state.collisionRayTrace((World)MathUtil.mc.world, pos, vec31, vec32)) == null)) {
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
                IBlockState state2 = MathUtil.mc.world.getBlockState(pos);
                Block block2 = state2.getBlock();
                if (ignoreNoBox && state2.getMaterial() != Material.PORTAL && state2.getCollisionBoundingBox((IBlockAccess)MathUtil.mc.world, pos) == Block.NULL_AABB) continue;
                if (!(!block2.canCollideCheck(state2, stopOnLiquid) || ignoreWebs && block2 instanceof BlockWeb)) {
                    RayTraceResult raytraceresult3 = state2.collisionRayTrace((World)MathUtil.mc.world, pos, vec31, vec32);
                    if (raytraceresult3 == null) continue;
                    return raytraceresult3;
                }
                raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, pos);
            }
            return returnLastUncollidableBlock ? raytraceresult2 : null;
        }
        return null;
    }

    public static double calculateXOffset(AxisAlignedBB other, double OffsetX, AxisAlignedBB this1) {
        if (other.maxY > this1.minY && other.minY < this1.maxY && other.maxZ > this1.minZ && other.minZ < this1.maxZ) {
            double d2;
            if (OffsetX > 0.0 && other.maxX <= this1.minX) {
                double d1 = this1.minX - 0.3 - other.maxX;
                if (d1 < OffsetX) {
                    OffsetX = d1;
                }
            } else if (OffsetX < 0.0 && other.minX >= this1.maxX && (d2 = this1.maxX + 0.3 - other.minX) > OffsetX) {
                OffsetX = d2;
            }
        }
        return OffsetX;
    }

    public static double calculateZOffset(AxisAlignedBB other, double OffsetZ, AxisAlignedBB this1) {
        if (other.maxX > this1.minX && other.minX < this1.maxX && other.maxY > this1.minY && other.minY < this1.maxY) {
            double d2;
            if (OffsetZ > 0.0 && other.maxZ <= this1.minZ) {
                double d1 = this1.minZ - 0.3 - other.maxZ;
                if (d1 < OffsetZ) {
                    OffsetZ = d1;
                }
            } else if (OffsetZ < 0.0 && other.minZ >= this1.maxZ && (d2 = this1.maxZ + 0.3 - other.minZ) > OffsetZ) {
                OffsetZ = d2;
            }
        }
        return OffsetZ;
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                float f2;
                float f;
                int y = sphere ? cy - (int)r : cy;
                while (!((float)y >= (f = (f2 = sphere ? (float)cy + r : (float)(cy + h))))) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < (double)(r * r) && (!hollow || dist >= (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static EntityPlayer placeValue(double x, double y, double z, EntityPlayer entity) {
        List list1 = MathUtil.mc.world.getCollisionBoxes((Entity)entity, entity.getEntityBoundingBox().addCoord(x, y, z));
        if (y != 0.0) {
            int l = list1.size();
            for (int k = 0; k < l; ++k) {
                y = ((AxisAlignedBB)list1.get(k)).calculateYOffset(entity.getEntityBoundingBox(), y);
            }
            if (y != 0.0) {
                entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(0.0, y, 0.0));
            }
        }
        if (x != 0.0) {
            int l2 = list1.size();
            for (int j5 = 0; j5 < l2; ++j5) {
                x = MathUtil.calculateXOffset(entity.getEntityBoundingBox(), x, (AxisAlignedBB)list1.get(j5));
            }
            if (x != 0.0) {
                entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(x, 0.0, 0.0));
            }
        }
        if (z != 0.0) {
            int i6 = list1.size();
            for (int k2 = 0; k2 < i6; ++k2) {
                z = MathUtil.calculateZOffset(entity.getEntityBoundingBox(), z, (AxisAlignedBB)list1.get(k2));
            }
            if (z != 0.0) {
                entity.setEntityBoundingBox(entity.getEntityBoundingBox().offset(0.0, 0.0, z));
            }
        }
        return entity;
    }

    public static Entity getPredictedPosition(Entity entity, double x) {
        if (x == 0.0) {
            return entity;
        }
        EntityPlayer e = null;
        double mX = entity.posX - entity.lastTickPosX;
        double mY = entity.posY - entity.lastTickPosY;
        double mZ = entity.posZ - entity.lastTickPosZ;
        boolean shouldPredict = false;
        boolean shouldStrafe = false;
        double motion = Math.sqrt(Math.pow(mX, 2.0) + Math.pow(mZ, 2.0) + Math.pow(mY, 2.0));
        if (motion > 0.1) {
            shouldPredict = true;
        }
        if (!shouldPredict) {
            return entity;
        }
        if (motion > 0.31) {
            shouldStrafe = true;
        }
        int i = 0;
        while ((double)i < x) {
            if (e == null) {
                if (EntityUtil.isOnGround(0.0, 0.0, 0.0, entity)) {
                    mY = shouldStrafe ? 0.4 : -0.07840015258789;
                } else {
                    mY -= 0.08;
                    mY *= (double)0.98f;
                }
                e = MathUtil.placeValue(mX, mY, mZ, (EntityPlayer)entity);
            } else {
                if (EntityUtil.isOnGround(0.0, 0.0, 0.0, e)) {
                    mY = shouldStrafe ? 0.4 : -0.07840015258789;
                } else {
                    mY -= 0.08;
                    mY *= (double)0.98f;
                }
                e = MathUtil.placeValue(mX, mY, mZ, e);
            }
            ++i;
        }
        return e;
    }
}

