/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.common.MinecraftForge
 */
package cascade.manager;

import cascade.features.Feature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class Simulation
extends Feature {
    EntityPlayer target = null;
    boolean tooClose = false;

    public void load() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    public boolean isInDanger() {
        return this.tooClose;
    }
}

