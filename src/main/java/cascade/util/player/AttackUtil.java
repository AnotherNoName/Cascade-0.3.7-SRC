//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.effect.EntityLightningBolt
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.entity.projectile.EntityPotion
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package cascade.util.player;

import cascade.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class AttackUtil
implements Util {
    public static boolean isInterceptedByCrystal(BlockPos pos) {
        for (Entity entity : AttackUtil.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || !new AxisAlignedBB(pos).intersectsWith(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    public static boolean isBlockedByCrystal(BlockPos pos) {
        for (Entity entity : AttackUtil.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || !new AxisAlignedBB(pos).intersectsWith(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    public static boolean isInterceptedByOther(BlockPos pos) {
        for (Entity entity : AttackUtil.mc.world.loadedEntityList) {
            if (entity instanceof EntityItem || !new AxisAlignedBB(pos).intersectsWith(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    public static boolean isInterceptedByOtherTest(BlockPos pos) {
        for (Entity e : AttackUtil.mc.world.loadedEntityList) {
            if (e instanceof EntityEnderCrystal || e instanceof EntityItem || e instanceof EntityExpBottle || e instanceof EntityXPOrb || e instanceof EntityPlayer || e instanceof EntityArrow || e instanceof EntityEnderPearl || e instanceof EntityPotion || e instanceof EntityLightningBolt || !new AxisAlignedBB(pos).intersectsWith(e.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    public static boolean isInterceptedByOtherNew(BlockPos pos) {
        for (Entity entity : AttackUtil.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
            if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
            return true;
        }
        return false;
    }
}

