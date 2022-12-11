/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package cascade.manager;

import cascade.event.events.PacketEvent;
import cascade.features.Feature;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SwapManager
extends Feature {
    boolean hasSwapped = false;
    int ticks = 0;

    public void load() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent e) {
        if (SwapManager.fullNullCheck()) {
            return;
        }
        if (this.hasSwapped && this.ticks++ >= 10) {
            this.hasSwapped = false;
            this.ticks = 0;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        if (SwapManager.fullNullCheck()) {
            return;
        }
        if (e.getPacket() instanceof CPacketHeldItemChange) {
            this.hasSwapped = true;
        }
    }

    public boolean hasSwapped() {
        return this.hasSwapped;
    }
}

