//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.common.MinecraftForge
 */
package cascade.manager;

import cascade.Cascade;
import cascade.features.Feature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class PlayerManager
extends Feature {
    public void load() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        int size = PlayerManager.mc.world.playerEntities.size();
        for (int i = 0; i < size; ++i) {
            EntityPlayer p = (EntityPlayer)PlayerManager.mc.world.playerEntities.get(i);
            if (!this.isValid(p, range)) continue;
            if (target == null) {
                target = p;
                continue;
            }
            if (!(PlayerManager.mc.player.getDistanceSqToEntity((Entity)p) < PlayerManager.mc.player.getDistanceSqToEntity((Entity)target))) continue;
            target = p;
        }
        return target;
    }

    boolean isValid(EntityPlayer en, double range) {
        return en != PlayerManager.mc.player && !Cascade.friendManager.isFriend(en) && (double)PlayerManager.mc.player.getDistanceToEntity((Entity)en) <= range && en.isEntityAlive();
    }

    public EntityPlayer getClosest(double range) {
        EntityPlayer target = null;
        for (EntityPlayer p : PlayerManager.mc.world.playerEntities) {
            if (p == PlayerManager.mc.player || Cascade.friendManager.isFriend(p) || (double)PlayerManager.mc.player.getDistanceToEntity((Entity)p) > range || !p.isEntityAlive()) continue;
            target = p;
        }
        return target;
    }
}

