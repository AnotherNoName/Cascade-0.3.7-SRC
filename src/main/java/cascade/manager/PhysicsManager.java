//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.manager;

import cascade.event.events.DisconnectEvent;
import cascade.event.events.GameLoopEvent;
import cascade.event.events.WorldClientEvent;
import cascade.features.Feature;
import cascade.util.misc.Timer;
import cascade.util.player.PhysicsUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PhysicsManager
extends Feature {
    Timer timer = new Timer();
    boolean blocking;
    int delay;
    int times;

    @SubscribeEvent
    public void invoke(GameLoopEvent e) {
        if (PhysicsManager.mc.player == null) {
            this.times = 0;
            return;
        }
        if (this.times > 0 && this.timer.passedMs(this.delay)) {
            this.blocking = true;
            while (this.times > 0) {
                this.invokePhysics();
                if (this.delay != 0) break;
                --this.times;
            }
            this.blocking = false;
            this.timer.reset();
        }
    }

    @SubscribeEvent
    public void invoke(DisconnectEvent event) {
        this.times = 0;
    }

    @SubscribeEvent
    public void invoke(WorldClientEvent.Load event) {
        this.times = 0;
    }

    public void invokePhysics(int times, int delay) {
        if (!this.blocking) {
            this.times = times;
            this.delay = delay;
        }
    }

    public void invokePhysics() {
        PhysicsUtil.runPhysicsTick();
    }
}

