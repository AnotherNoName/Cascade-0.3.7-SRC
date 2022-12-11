//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 */
package cascade.util.player;

import cascade.Cascade;
import cascade.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TargetUtil
implements Util {
    public static EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        int size = TargetUtil.mc.world.playerEntities.size();
        for (int i = 0; i < size; ++i) {
            EntityPlayer p = (EntityPlayer)TargetUtil.mc.world.playerEntities.get(i);
            if (TargetUtil.isntValid(p, range)) continue;
            if (target == null) {
                target = p;
                continue;
            }
            if (!(TargetUtil.mc.player.getDistanceSqToEntity((Entity)p) < TargetUtil.mc.player.getDistanceSqToEntity((Entity)target))) continue;
            target = p;
        }
        return target;
    }

    static boolean isntValid(EntityPlayer en, double range) {
        return Cascade.friendManager.isFriend(en) || (double)TargetUtil.mc.player.getDistanceToEntity((Entity)en) > range || en == TargetUtil.mc.player || en.getHealth() <= 0.0f || en.isDead;
    }
}

