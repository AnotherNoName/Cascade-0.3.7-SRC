//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.MathHelper
 */
package cascade.util.misc;

import cascade.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class CalcUtil
implements Util {
    public static double getDistance(double posX, double posY, double posZ) {
        double x = CalcUtil.mc.player.posX - posX;
        double y = CalcUtil.mc.player.posY - posY;
        double z = CalcUtil.mc.player.posY - posZ;
        return MathHelper.sqrt((double)(x * x + y * y + z * z));
    }

    public static double getDistance(Entity entity) {
        double x = CalcUtil.mc.player.posX - entity.posX;
        double y = CalcUtil.mc.player.posY - entity.posY;
        double z = CalcUtil.mc.player.posY - entity.posZ;
        return MathHelper.sqrt((double)(x * x + y * y + z * z));
    }

    public static double getDistanceFromPos2Pos(double x, double y, double z, double x2, double y2, double z2) {
        double posX = x - x2;
        double posY = y - y2;
        double posZ = z - z2;
        return MathHelper.sqrt((double)(posX * posX + posY * posY + posZ * posZ));
    }

    public static double getDistanceFromPos2Entity(Entity entity, double x, double y, double z) {
        double posX = x - entity.posX;
        double posY = y - entity.posY;
        double posZ = z - entity.posZ;
        return MathHelper.sqrt((double)(posX * posX + posY * posY + posZ * posZ));
    }
}

