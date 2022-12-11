//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package cascade.manager;

import cascade.features.Feature;
import cascade.features.modules.core.Manager;
import cascade.mixin.mixins.accessor.ITimer;
import cascade.util.misc.Timer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TimerManager
extends Feature {
    float timer = 1.0f;
    boolean flagged = false;
    Timer flagTimer = new Timer();

    public void load() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent e) {
        if (TimerManager.fullNullCheck()) {
            return;
        }
        if (this.flagged && this.flagTimer.passedMs((long)Manager.getInstance().getTimer())) {
            this.flagged = false;
        }
        if (TimerManager.mc.timer.field_194149_e != 50.0f) {
            this.flagged = true;
            this.flagTimer.reset();
        }
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        this.timer = 1.0f;
        ((ITimer)TimerManager.mc.timer).setTickLength(50.0f);
    }

    public void set(float timer) {
        if (timer > 0.0f) {
            ((ITimer)TimerManager.mc.timer).setTickLength(50.0f / timer);
        }
    }

    public float getTimer() {
        return this.timer;
    }

    public boolean isFlagged() {
        return this.flagged;
    }

    @Override
    public void reset() {
        this.timer = 1.0f;
        ((ITimer)TimerManager.mc.timer).setTickLength(50.0f);
    }
}

